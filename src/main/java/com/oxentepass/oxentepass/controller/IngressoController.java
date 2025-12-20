package com.oxentepass.oxentepass.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.oxentepass.oxentepass.service.IngressoService;
import com.querydsl.core.types.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.oxentepass.oxentepass.entity.Ingresso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/ingresso")
public class IngressoController {

    @Autowired
    private IngressoService ingressoService;
    
    //Operações Básicas

    @GetMapping("/listar")
    public ResponseEntity<Page<Ingresso>> listarTodosIngressos (Pageable pageable) {
        return new ResponseEntity<Page<Ingresso>>(
            ingressoService.listarTodosIngressos(pageable), 
            HttpStatus.OK
        );
    }

    @GetMapping("/filtrar")
    public ResponseEntity<Page<Ingresso>> filtrarIngressos (Predicate predicate, Pageable pageable) {
        return new ResponseEntity<Page<Ingresso>>(
            ingressoService.filtrarIngressos(predicate, pageable), 
            HttpStatus.OK
        );
    }

    @GetMapping("/buscar/{idIngresso}")
    public ResponseEntity<Ingresso> buscarIngressoPorId (@PathVariable Long idIngresso) {
        return new ResponseEntity<Ingresso>(
            ingressoService.buscarIngressoPorId(idIngresso), 
            HttpStatus.OK
        );
    }

    @GetMapping("/buscar/tipo/{tipoIngresso}")
    public ResponseEntity<Ingresso> buscarIngressoPorTipo (@PathVariable String tipoIngresso) {
        return new ResponseEntity<Ingresso>(
            ingressoService.buscarIngressPorTipo(tipoIngresso), 
            HttpStatus.OK
        );
    }

    @GetMapping("/disponivel/{idEvento}")
    public ResponseEntity<Page<Ingresso>> quantidadeIngressosDisponiveis (@PathVariable Long idEvento, Pageable pageable) {
        return new ResponseEntity<Page<Ingresso>>(
            ingressoService.ingressosDisponiveis(idEvento, pageable), 
            HttpStatus.OK
        );
    }
}