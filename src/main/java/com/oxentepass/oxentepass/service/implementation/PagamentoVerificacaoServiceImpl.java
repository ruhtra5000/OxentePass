package com.oxentepass.oxentepass.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.entity.StatusPagamento;
import com.oxentepass.oxentepass.entity.StatusVenda;
import com.oxentepass.oxentepass.repository.PagamentoRepository;
import com.oxentepass.oxentepass.repository.VendaRepository;
import com.oxentepass.oxentepass.service.PagamentoVerificacaoService;

@Service
public class PagamentoVerificacaoServiceImpl implements PagamentoVerificacaoService {

    // Repositórios
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    // Clientes externos
    private final PaymentClient paymentClient = new PaymentClient();

    // Métodos

    // Verifica pagamentos pendentes e atualiza seus status conforme necessário
    @Override
    public void verificarPagamentosPendentes() {

        List<Pagamento> pendentes = pagamentoRepository.findByStatus(StatusPagamento.PENDENTE);

        for (Pagamento pagamento : pendentes) {
            try {
                verificarPagamento(pagamento);
            } catch (Exception e) {
                System.err.println("Erro ao verificar pagamento " + pagamento.getId() + ": " + e.getMessage());
            }
        }
    }

    // Verifica o status de um pagamento específico no MercadoPago e atualiza o status local
    private void verificarPagamento(Pagamento pagamento) throws Exception {

        if (pagamento.getMercadoPagoId() == null) return;

        Payment payment = paymentClient.get(Long.valueOf(pagamento.getMercadoPagoId()));

        // Se o pagamento não estiver mais pendente, atualize o status localmente
        if (pagamento.getStatus() != StatusPagamento.PENDENTE) return;

        switch (payment.getStatus()) {

            case "approved" -> {
                pagamento.setStatus(StatusPagamento.CONFIRMADO);
                pagamento.setDataPagamento(LocalDateTime.now());

                pagamento.getVenda().setStatus(StatusVenda.PAGA);

                pagamentoRepository.save(pagamento);
                vendaRepository.save(pagamento.getVenda());

                System.out.println(
                    "Pagamento CONFIRMADO (MP): " + pagamento.getId()
                );
            }

            case "rejected" -> {
                pagamento.setStatus(StatusPagamento.RECUSADO);
                pagamentoRepository.save(pagamento);
            }

            case "cancelled" -> {
                pagamento.setStatus(StatusPagamento.CANCELADO);
                pagamentoRepository.save(pagamento);
            }

            default -> {
            }
        }
    }
}
