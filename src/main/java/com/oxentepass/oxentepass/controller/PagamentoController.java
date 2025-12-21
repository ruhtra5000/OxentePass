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
import com.oxentepass.oxentepass.entity.Ingresso;
import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.service.PagamentoService;
import com.querydsl.core.types.Predicate;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping("/criar")
    public ResponseEntity<String> criarPagamento(@RequestBody @Valid PagamentoRequest dto) {
        pagamentoService.criarPagamento(dto.paraEntidade());

        return new ResponseEntity<String>(
            "Pagamento criada com sucesso!", 
            HttpStatus.CREATED
        );
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<Pagamento>> listarTodosPagamentos (Pageable pageable) {
        return new ResponseEntity<Page<Pagamento>>(
            pagamentoService.listarTodosPagamentos(pageable), 
            HttpStatus.OK
        );
    }

    @GetMapping("/filtrar")
    public ResponseEntity<Page<Pagamento>> filtrarPagamentos (Predicate predicate, Pageable pageable) {
        return new ResponseEntity<Page<Pagamento>>(
            pagamentoService.filtrarPagamentos(predicate, pageable), 
            HttpStatus.OK
        );
    }
    
    @PutMapping("/confirmar/{id}/")
    public ResponseEntity<Pagamento> confirmarPagamento(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.confirmarPagamento(id));
    }

    @PutMapping("/cancelar/{id}/")
    public ResponseEntity<Pagamento> cancelarPagamento(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.cancelarPagamento(id));
    }

    @PutMapping("/estornar/{id}/")
    public ResponseEntity<Pagamento> estornarPagamento(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.estornarPagamento(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.buscarPagamentoPorId(id));
    }
    
}
