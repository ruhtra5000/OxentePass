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
import com.oxentepass.oxentepass.entity.IngressoVenda;
import com.oxentepass.oxentepass.entity.Venda;
import com.oxentepass.oxentepass.service.VendaService;

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
    public ResponseEntity<Page<Venda>> listarTodasVendas(Pageable pageable) {
        return new ResponseEntity<Page<Venda>>(
            vendaService.listarTodasVendas(pageable), 
            HttpStatus.OK
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
    public ResponseEntity<Venda> adicionarIngresso(@PathVariable Long id, @RequestBody IngressoVenda ingressoVenda) {
        Venda vendaAtualizada = vendaService.adicionarIngresso(ingressoVenda, id);
        
        return ResponseEntity.ok(vendaAtualizada);
    }

    @PutMapping("/removeringresso/{id}")
    public ResponseEntity<Venda> removerIngresso(@RequestBody @Valid VendaRequest dto, @PathVariable long id) {
        Venda vendaAtualizada = vendaService.removerIngresso(dto.getIngressos().get(0), id);
        
        return new ResponseEntity<Venda>(
            vendaAtualizada, 
            HttpStatus.OK
        );
    }
    
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Venda> buscarVendaPorId(@PathVariable long id) {
        return new ResponseEntity<Venda>(
            vendaService.buscarVendaPorId(id), 
            HttpStatus.OK
        );
    }

    @GetMapping("/buscar/usuario/{idUsuario}")
    public ResponseEntity<Page<Venda>> buscarVendaPorUsuario(@PathVariable Long idUsuario, Pageable pageable) {
        return new ResponseEntity<Page<Venda>>(
            vendaService.buscarVendaPorUsuario(idUsuario, pageable), 
            HttpStatus.OK
        );
    }
}
