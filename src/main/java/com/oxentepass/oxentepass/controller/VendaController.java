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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/venda")

public class VendaController {

    @Autowired VendaService vendaService;

    @PostMapping("/criar")
    public ResponseEntity<String> criarVenda(@RequestBody @Valid VendaRequest dto) {
        vendaService.criarVenda(dto.paraEntidade());

        return new ResponseEntity<String>(
            "Venda criada com sucesso!", 
            HttpStatus.CREATED
        );
    }

    @PostMapping("/finalizar/{id}")
    public ResponseEntity<String> finalizarVenda(@PathVariable long id) {
        vendaService.finalizarVenda(id);

        return new ResponseEntity<String>(
            "Venda finalizada com sucesso!", 
            HttpStatus.OK
        );
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<VendaResponse>> listarTodasVendas(Pageable pageable) {
        
        return ResponseEntity.ok(
            vendaService.listarTodasVendas(pageable).map(VendaResponse::paraDTO)
        );
    }  
    
    @GetMapping("/filtrar")
    public ResponseEntity<Page<VendaResponse>> filtrarVendas(Predicate predicate, Pageable pageable){

        return ResponseEntity.ok(
            vendaService.filtrarVendas(predicate, pageable).map(VendaResponse::paraDTO)
        );
    }

    @PostMapping("/cancelar/{id}")
    public ResponseEntity<String> cancelarVenda(@PathVariable long id) {
        vendaService.cancelarVenda(id);

        return new ResponseEntity<String>(
            "Venda cancelada com sucesso!", 
            HttpStatus.OK
        );
    }

    @PutMapping("/adicionaringresso/{id}")
    public ResponseEntity<VendaResponse> adicionarIngresso(@PathVariable Long id, @RequestBody IngressoVenda ingressoVenda) {
        
        return ResponseEntity.ok(
            VendaResponse.paraDTO(vendaService.adicionarIngresso(ingressoVenda, id))
        );
    }

    @PutMapping("/removeringresso/{id}/{idIngressoVenda}")
    public ResponseEntity<VendaResponse> removerIngresso(@PathVariable long idIngressoVenda, @PathVariable Long id) {
    
        return ResponseEntity.ok(
            VendaResponse.paraDTO(vendaService.removerIngresso(idIngressoVenda, id))
        );
    }
    
    @GetMapping("/buscar/{id}")
    public ResponseEntity<VendaResponse> buscarVendaPorId(@PathVariable long id) {
        
        return ResponseEntity.ok(
            VendaResponse.paraDTO(vendaService.buscarVendaPorId(id))
        );
    }

    @GetMapping("/buscar/usuario/{idUsuario}")
    public ResponseEntity<Page<VendaResponse>> buscarVendaPorUsuario(@PathVariable Long idUsuario, Predicate predicate, Pageable pageable) {

        return ResponseEntity.ok(
            vendaService.buscarVendaPorUsuario(idUsuario, predicate, pageable).map(VendaResponse::paraDTO)
        );
    }
}
