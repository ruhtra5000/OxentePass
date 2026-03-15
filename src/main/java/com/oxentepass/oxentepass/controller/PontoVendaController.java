package com.oxentepass.oxentepass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oxentepass.oxentepass.controller.request.PontoVendaRequest;
import com.oxentepass.oxentepass.entity.PontoVenda;
import com.oxentepass.oxentepass.service.PontoVendaService;
import com.querydsl.core.types.Predicate;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Guilherme Paes
 * Controller para manipular Ponto de Venda, através de PontoVendaService
 */

@RestController
@RequestMapping("/pontovenda")
public class PontoVendaController {

    @Autowired
    PontoVendaService service;

    @Operation(summary = "Cadastrar Ponto de Venda", description = "Cadastra um novo Ponto de Venda")
    @PostMapping
    public ResponseEntity<String> cadastrarPontoVenda(@RequestBody @Valid PontoVendaRequest dto) {
        service.cadastrarPontoVenda(dto.paraEntidade());

        return new ResponseEntity<String>(
                "Ponto de Venda " + dto.nome() + " criado com sucesso",
                HttpStatus.CREATED);
    }

    @Operation(summary = "Editar Ponto de Venda", description = "Edita os dados do Ponto de Venda com id especificado")
    @PutMapping("/{id}")
    public ResponseEntity<String> editarPontoVenda(@PathVariable long id, @RequestBody PontoVendaRequest dto) {
        service.editarPontoVenda(id, dto.paraEntidade());

        return new ResponseEntity<String>(
                "Ponto de Venda " + dto.nome() + " atualizado com sucesso",
                HttpStatus.OK);
    }

    @Operation(summary = "Deletar Ponto de Venda", description = "Deleta o Ponto de Venda com id especificado")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPontoVenda(@PathVariable long id) {
        service.deletarPontoVenda(id);

        return new ResponseEntity<String>(
                "Ponto de Venda com id " + id + " deletado com sucesso",
                HttpStatus.OK);
    }

    @Operation(summary = "Listar Pontos de Venda", description = "Retorna os Pontos de Venda cadastrados")
    @GetMapping
    public ResponseEntity<Page<PontoVenda>> listarPontoVendas(Pageable pageable) {
        return new ResponseEntity<Page<PontoVenda>>(
                service.listarPontoVendas(pageable),
                HttpStatus.OK);
    }

    @Operation(summary = "Listar Pontos de Venda com Filtro", description = "Retorna os Pontos de Venda cadastrados conforme filtros aplicados")
    @GetMapping("/filtro")
    public ResponseEntity<Page<PontoVenda>> listaPontoVendasFiltro(Predicate predicate, Pageable pageable) {
        return new ResponseEntity<Page<PontoVenda>>(
                service.listarPontoVendasFiltro(predicate, pageable),
                HttpStatus.OK);
    }

}