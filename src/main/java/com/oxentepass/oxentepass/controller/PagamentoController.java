package com.oxentepass.oxentepass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.oxentepass.oxentepass.controller.request.PagamentoRequest;
import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.service.MercadoPagoService;
import com.oxentepass.oxentepass.service.PagamentoService;
import com.querydsl.core.types.Predicate;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * @author Victor Cauã
 * Controller para manipular Pagamento, através de PagamentoService
 */

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    // Service de pagamento
    @Autowired
    private PagamentoService pagamentoService;

    // Service do Mercado Pago
    @Autowired
    private MercadoPagoService mercadoPagoService;

    // Endpoints

    // Criar um novo pagamento
    @Operation(summary = "Criar Pagamento", description = "Cria um novo Pagamento")
    @PostMapping("/criar")
    public ResponseEntity<String> criarPagamento(@RequestBody @Valid PagamentoRequest dto) {
        pagamentoService.criarPagamento(dto.paraEntidade());

        return new ResponseEntity<String>(
            "Pagamento criada com sucesso!", 
            HttpStatus.CREATED
        );
    }

    // Listar todos os pagamentos
    @Operation(summary = "Listar Pagamentos", description = "Lista todos os Pagamentos cadastrados")
    @GetMapping("/listar")
    public ResponseEntity<Page<Pagamento>> listarTodosPagamentos (Pageable pageable) {
        return new ResponseEntity<Page<Pagamento>>(
            pagamentoService.listarTodosPagamentos(pageable), 
            HttpStatus.OK
        );
    }

    // Filtrar pagamentos
    @Operation(summary = "Filtrar Pagamentos", description = "Filtra os Pagamentos cadastrados de acordo com os parâmetros fornecidos")
    @GetMapping("/filtrar")
    public ResponseEntity<Page<Pagamento>> filtrarPagamentos (Predicate predicate, Pageable pageable) {
        return new ResponseEntity<Page<Pagamento>>(
            pagamentoService.filtrarPagamentos(predicate, pageable), 
            HttpStatus.OK
        );
    }
    
    // Endpoints para alterar o estado do pagamento

    // Confirmar pagamento
    @Operation(summary = "Confirmar Pagamento", description = "Confirma um Pagamento existente")
    @PutMapping("/confirmar/{id}/")
    public ResponseEntity<Pagamento> confirmarPagamento(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.confirmarPagamento(id));
    }

    // Cancelar pagamento
    @Operation(summary = "Cancelar Pagamento", description = "Cancela um Pagamento existente")
    @PutMapping("/cancelar/{id}/")
    public ResponseEntity<Pagamento> cancelarPagamento(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.cancelarPagamento(id));
    }

    // Estornar pagamento
    @Operation(summary = "Estornar Pagamento", description = "Estorna um Pagamento existente")
    @PutMapping("/estornar/{id}/")
    public ResponseEntity<Pagamento> estornarPagamento(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.estornarPagamento(id));
    }

    // Buscar pagamento por ID
    @Operation(summary = "Buscar Pagamento por ID", description = "Busca um Pagamento existente pelo seu ID")
    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.buscarPagamentoPorId(id));
    }

    // Endpoints de integração com o Mercado Pago

    // Pagar com PIX
    @Operation(summary = "Pagar com PIX", description = "Realiza o pagamento de uma Venda utilizando PIX através do Mercado Pago")
    @PostMapping("/pix/{idVenda}")
    public Pagamento pagarPix(@PathVariable Long idVenda) {
        return mercadoPagoService.pagarComPix(idVenda);
    }

    // Pagar com cartão
    @Operation(summary = "Pagar com Cartão", description = "Realiza o pagamento de uma Venda utilizando Cartão de Crédito através do Mercado Pago")
    @PostMapping("/cartao/{idVenda}")
    public Pagamento pagarCartao(
        @PathVariable Long idVenda,
        @RequestBody String tokenCartao
    ) {
        return mercadoPagoService.pagarComCartao(idVenda, tokenCartao);
    }
}


