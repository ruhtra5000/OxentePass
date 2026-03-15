package com.oxentepass.oxentepass.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.entity.StatusPagamento;
import com.oxentepass.oxentepass.entity.Venda;
import com.oxentepass.oxentepass.repository.PagamentoRepository;
import com.oxentepass.oxentepass.repository.VendaRepository;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author Victor Cauã
 * Controller para manipular webhook do Mercado Pago
 */

@RestController
@RequestMapping("/webhook/mercadopago")
public class MercadoPagoWebhookController {

    // Repositórios
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    // Métodos
    
    // Recebe notificações do Mercado Pago sobre mudanças no status dos pagamentos
    @Operation(summary = "Receber Notificações do Mercado Pago", description = "Recebe notificações do Mercado Pago sobre mudanças no status dos pagamentos")
    @PostMapping
    public void receber(@RequestBody Map<String, Object> payload) {
        try {
            if (!"payment".equals(payload.get("type"))) return;

            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            Long paymentId = Long.valueOf(data.get("id").toString());

            PaymentClient client = new PaymentClient();
            Payment payment = client.get(paymentId);

            Pagamento pagamento = pagamentoRepository
                .findByMercadoPagoId(payment.getId().toString())
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

            if (pagamento.getStatus() != StatusPagamento.PENDENTE) return;

            switch (payment.getStatus()) {
                case "approved" -> {
                    pagamento.setStatus(StatusPagamento.CONFIRMADO);
                    pagamento.setDataPagamento(java.time.LocalDateTime.now());

                    Venda venda = pagamento.getVenda();
                    venda.setStatus(com.oxentepass.oxentepass.entity.StatusVenda.PAGA);

                    pagamentoRepository.save(pagamento);
                    vendaRepository.save(venda);
                }
                case "rejected" -> {
                    pagamento.setStatus(StatusPagamento.RECUSADO);
                    pagamentoRepository.save(pagamento);
                }
                case "cancelled" -> {
                    pagamento.setStatus(StatusPagamento.CANCELADO);
                    pagamentoRepository.save(pagamento);
                }
            }

        } catch (Exception e) {
            System.err.println("Erro no Webhook Mercado Pago: " + e.getMessage());
        }
    }

}