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

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/pontovenda")
public class PontoVendaController {

    @Autowired
    PontoVendaService service;

    @PostMapping
    public ResponseEntity<String> cadastrarPontoVenda(@RequestBody @Valid PontoVendaRequest dto) {
        service.cadastrarPontoVenda(dto.paraEntidade());

        return new ResponseEntity<String>(
                "Ponto de Venda " + dto.nome() + " criado com sucesso",
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editarPontoVenda(@PathVariable long id, @RequestBody PontoVendaRequest dto) {
        service.editarPontoVenda(id, dto.paraEntidade());

        return new ResponseEntity<String>(
                "Ponto de Venda " + dto.nome() + " atualizado com sucesso",
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPontoVenda(@PathVariable long id) {
        service.deletarPontoVenda(id);

        return new ResponseEntity<String>(
                "Ponto de Venda com id " + id + " deletado com sucesso",
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<PontoVenda>> listarPontoVendas(Pageable pageable) {
        return new ResponseEntity<Page<PontoVenda>>(
                service.listarPontoVendas(pageable),
                HttpStatus.OK);
    }

    @GetMapping("/filtro")
    public ResponseEntity<Page<PontoVenda>> listaPontoVendasFiltro(Predicate predicate, Pageable pageable) {
        return new ResponseEntity<Page<PontoVenda>>(
                service.listarPontoVendasFiltro(predicate, pageable),
                HttpStatus.OK);
    }

}