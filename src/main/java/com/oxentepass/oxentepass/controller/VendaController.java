package com.oxentepass.oxentepass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oxentepass.oxentepass.controller.request.VendaRequest;
import com.oxentepass.oxentepass.controller.response.VendaResponse;
import com.oxentepass.oxentepass.entity.IngressoVenda;
import com.oxentepass.oxentepass.service.VendaService;
import com.querydsl.core.types.Predicate;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

/**
 * @author Victor Cauã
 * Controller para manipular Venda, através de VendaService
 */

@RestController
@RequestMapping("/venda")

public class VendaController {

    // Service de venda
    @Autowired VendaService vendaService;

    // Endpoints

    // Criar uma nova venda
    @Operation(summary = "Criar Venda", description = "Cria uma nova Venda")
    @PostMapping("/criar")
    public ResponseEntity<String> criarVenda(@RequestBody @Valid VendaRequest dto) {
        vendaService.criarVenda(dto.paraEntidade());

        return new ResponseEntity<String>(
            "Venda criada com sucesso!", 
            HttpStatus.CREATED
        );
    }

    // Finalizar uma venda
    @Operation(summary = "Finalizar Venda", description = "Finaliza uma Venda existente")
    @PostMapping("/finalizar/{id}")
    public ResponseEntity<String> finalizarVenda(@PathVariable long id) {
        vendaService.finalizarVenda(id);

        return new ResponseEntity<String>(
            "Venda finalizada com sucesso!", 
            HttpStatus.OK
        );
    }

    // Listar todas as vendas
    @Operation(summary = "Listar Vendas", description = "Retorna todas as Vendas com paginação")
    @GetMapping("/listar")
    public ResponseEntity<Page<VendaResponse>> listarTodasVendas(Pageable pageable) {
        
        return ResponseEntity.ok(
            vendaService.listarTodasVendas(pageable).map(VendaResponse::paraDTO)
        );
    }  
    
    // Filtrar vendas com QueryDSL
    @Operation(summary = "Filtrar Vendas", description = "Filtra as Vendas com QueryDSL e paginação")
    @GetMapping("/filtrar")
    public ResponseEntity<Page<VendaResponse>> filtrarVendas(Predicate predicate, Pageable pageable){

        return ResponseEntity.ok(
            vendaService.filtrarVendas(predicate, pageable).map(VendaResponse::paraDTO)
        );
    }

    // Cancelar uma venda
    @PostMapping("/cancelar/{id}")
    @Operation(summary = "Cancelar Venda", description = "Cancela uma Venda existente")
    public ResponseEntity<String> cancelarVenda(@PathVariable long id) {
        vendaService.cancelarVenda(id);

        return new ResponseEntity<String>(
            "Venda cancelada com sucesso!", 
            HttpStatus.OK
        );
    }

    // Adicionar ingresso à venda
    @Operation(summary = "Adicionar Ingresso à Venda", description = "Adiciona um Ingresso à Venda existente")
    @PutMapping("/adicionaringresso/{id}")
    public ResponseEntity<VendaResponse> adicionarIngresso(@PathVariable Long id, @RequestBody IngressoVenda ingressoVenda) {
        
        return ResponseEntity.ok(
            VendaResponse.paraDTO(vendaService.adicionarIngresso(ingressoVenda, id))
        );
    }

    // Remover ingresso da venda
    @Operation(summary = "Remover Ingresso da Venda", description = "Remove um Ingresso da Venda existente")
    @PutMapping("/removeringresso/{id}/{idIngressoVenda}")
    public ResponseEntity<VendaResponse> removerIngresso(@PathVariable long idIngressoVenda, @PathVariable Long id) {
    
        return ResponseEntity.ok(
            VendaResponse.paraDTO(vendaService.removerIngresso(idIngressoVenda, id))
        );
    }
    
    // Buscar venda por ID
    @Operation(summary = "Buscar Venda por ID", description = "Retorna uma Venda com o ID especificado")
    @GetMapping("/buscar/{id}")
    public ResponseEntity<VendaResponse> buscarVendaPorId(@PathVariable long id) {
        
        return ResponseEntity.ok(
            VendaResponse.paraDTO(vendaService.buscarVendaPorId(id))
        );
    }

    // Buscar vendas por usuário
    @Operation(summary = "Buscar Vendas por Usuário", description = "Retorna todas as Vendas de um Usuário com paginação e filtro QueryDSL")
    @GetMapping("/buscar/usuario/{idUsuario}")
    public ResponseEntity<Page<VendaResponse>> buscarVendaPorUsuario(@PathVariable Long idUsuario, Predicate predicate, Pageable pageable) {

        return ResponseEntity.ok(
            vendaService.buscarVendaPorUsuario(idUsuario, predicate, pageable).map(VendaResponse::paraDTO)
        );
    }
}
