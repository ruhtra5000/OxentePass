package com.oxentepass.oxentepass.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.oxentepass.oxentepass.service.IngressoService;
import com.querydsl.core.types.Predicate;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.oxentepass.oxentepass.entity.Ingresso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Victor Cauã
 * Controller para manipular Ingresso, através de IngressoService
 */

@RestController
@RequestMapping("/ingresso")
public class IngressoController {

    // Service de ingresso
    @Autowired
    private IngressoService ingressoService;
    
    // As operações de criar e deletar ingresso
    // estão em EventoController, para que não sejam
    // criados ingressos desvinculados de eventos

    // Listar todos os ingressos
    @Operation(summary = "Listar Ingressos", description = "Lista todos os Ingressos cadastrados")
    @GetMapping("/listar")
    public ResponseEntity<Page<Ingresso>> listarTodosIngressos (Pageable pageable) {
        return new ResponseEntity<Page<Ingresso>>(
            ingressoService.listarTodosIngressos(pageable), 
            HttpStatus.OK
        );
    }

    // Filtrar ingressos
    @Operation(summary = "Filtrar Ingressos", description = "Filtra Ingressos com base em critérios dinâmicos")
    @GetMapping("/filtrar")
    public ResponseEntity<Page<Ingresso>> filtrarIngressos (Predicate predicate, Pageable pageable) {
        return new ResponseEntity<Page<Ingresso>>(
            ingressoService.filtrarIngressos(predicate, pageable), 
            HttpStatus.OK
        );
    }

    // Buscar ingresso por ID
    @Operation(summary = "Buscar Ingresso por ID", description = "Busca um Ingresso específico pelo seu ID")
    @GetMapping("/buscar/{idIngresso}")
    public ResponseEntity<Ingresso> buscarIngressoPorId (@PathVariable Long idIngresso) {
        return new ResponseEntity<Ingresso>(
            ingressoService.buscarIngressoPorId(idIngresso), 
            HttpStatus.OK
        );
    }

    // Quantidade de ingressos disponíveis para um evento
    @Operation(summary = "Quantidade de Ingressos Disponíveis", description = "Retorna a quantidade de Ingressos disponíveis para um evento específico")
    @GetMapping("/disponivel/{idEvento}")
    public ResponseEntity<Page<Ingresso>> quantidadeIngressosDisponiveis (@PathVariable Long idEvento, Pageable pageable) {
        return new ResponseEntity<Page<Ingresso>>(
            ingressoService.ingressosDisponiveis(idEvento, pageable), 
            HttpStatus.OK
        );
    }
}