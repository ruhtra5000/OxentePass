package com.oxentepass.oxentepass.service.implementation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.payment.Payment;
import com.oxentepass.oxentepass.entity.MetodoPagamento;
import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.entity.StatusPagamento;
import com.oxentepass.oxentepass.entity.Venda;
import com.oxentepass.oxentepass.repository.PagamentoRepository;
import com.oxentepass.oxentepass.repository.VendaRepository;
import com.oxentepass.oxentepass.service.MercadoPagoService;

@Service
public class MercadoPagoServiceImpl implements MercadoPagoService {

    // Configurações do MercadoPago
    @Value("${mercadopago.access.token}")
    private String accessToken;

    // Repositórios
    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    // Métodos

    // Paga uma venda utilizando PIX
    @Override
    public Pagamento pagarComPix(Long idVenda) {
        try {
            Venda venda = vendaRepository.findById(idVenda).orElseThrow(() -> new RuntimeException("Venda não encontrada"));

            Pagamento pagamento = criarPagamentoSeNecessario(venda, MetodoPagamento.PIX);

            PaymentClient client = new PaymentClient();

            PaymentCreateRequest request = PaymentCreateRequest.builder()
                .transactionAmount(venda.getValorTotal())
                .description("Pagamento PIX - Venda #" + venda.getId())
                .paymentMethodId("pix")
                .payer(PaymentPayerRequest.builder().email(venda.getUsuario().getEmail()).firstName(venda.getUsuario().getNome()).identification(IdentificationRequest.builder().type("CPF").number(venda.getUsuario().getCpf()).build()).build())
                .build();
                
            Payment payment = client.create(request);

            pagamento.setMercadoPagoId(payment.getId().toString());
            
            if (payment.getPointOfInteraction() != null && payment.getPointOfInteraction().getTransactionData() != null) {
                
                pagamento.setQrCode(payment.getPointOfInteraction().getTransactionData().getQrCode());
                pagamento.setQrCodeBase64(payment.getPointOfInteraction().getTransactionData().getQrCodeBase64());
            }

            return pagamentoRepository.save(pagamento);

        } catch (MPApiException e) {
            throw new RuntimeException("Erro na API do Mercado Pago: " + e.getApiResponse().getContent());
        } catch (Exception e) {
            throw new RuntimeException("Erro interno ao gerar PIX: " + e.getMessage());
        }
    }

    // Paga uma venda utilizando cartão de crédito
    @Override
    public Pagamento pagarComCartao(Long idVenda, String tokenCartao) {
        try {
            Venda venda = vendaRepository.findById(idVenda).orElseThrow(() -> new RuntimeException("Venda não encontrada"));

            Pagamento pagamento = criarPagamentoSeNecessario(venda, MetodoPagamento.CARTAO_CREDITO);

            PaymentClient client = new PaymentClient();

            PaymentCreateRequest request = PaymentCreateRequest.builder()
                .transactionAmount(venda.getValorTotal())
                .token(tokenCartao)
                .description("Venda #" + venda.getId())
                .installments(1)
                .paymentMethodId("visa")
                .payer(PaymentPayerRequest.builder().email(venda.getUsuario().getEmail()).firstName(venda.getUsuario().getNome()).identification(IdentificationRequest.builder().type("CPF").number(venda.getUsuario().getCpf()).build()).build())
                .build();

            Payment payment = client.create(request);
            pagamento.setMercadoPagoId(payment.getId().toString());
            return pagamentoRepository.save(pagamento);
            
        } catch (MPApiException e) {
            System.err.println("ERRO MERCADO PAGO CARTÃO: " + e.getApiResponse().getContent());
            throw new RuntimeException("Erro na API de Cartão: " + e.getApiResponse().getContent());
        } catch (Exception e) {
            throw new RuntimeException("Erro interno: " + e.getMessage());
        }
    }

    // Método auxiliar para criar um pagamento se ele não existir
    private Pagamento criarPagamentoSeNecessario(Venda venda, MetodoPagamento metodo) {
        if (venda.getPagamento() != null) {
            return venda.getPagamento();
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(venda.getValorTotal());
        pagamento.setMetodo(metodo);
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setVenda(venda);

        venda.setPagamento(pagamento);
        return pagamentoRepository.save(pagamento);
    }
}