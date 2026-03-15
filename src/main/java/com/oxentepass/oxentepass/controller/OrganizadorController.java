package com.oxentepass.oxentepass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oxentepass.oxentepass.controller.request.OrganizadorRequest;
import com.oxentepass.oxentepass.entity.Organizador;
import com.oxentepass.oxentepass.service.OrganizadorService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * @author Guilherme Paes
 * Controller para manipular Organizador, através de OrganizadorService
 */

@RestController
@RequestMapping("/organizador")
public class OrganizadorController {

    @Autowired
    private OrganizadorService service;

    @Operation(summary = "Promover Usuário a Organizador", description = "Promove um Usuário comum a Organizador")
    @PostMapping("/promover")
    public ResponseEntity<String> promoverUsuario(@RequestBody @Valid OrganizadorRequest dto) {
        service.promoverUsuario(dto);
        return new ResponseEntity<String>("Usuário promovido a Organizador com sucesso!", HttpStatus.CREATED);
    }

    @Operation(summary = "Editar Organizador", description = "Edita os dados do Organizador com id especificado")
    @PutMapping
    public ResponseEntity<String> editarOrganizador(@RequestBody @Valid OrganizadorRequest dados) {
        service.editarOrganizador(dados.usuarioId(), dados);
        
        return new ResponseEntity<String>(
            "Organizador com id " + dados.usuarioId() + " atualizado com sucesso!",
            HttpStatus.OK
        );
    }

    @Operation(summary = "Listar Organizadores", description = "Retorna os Organizadores cadastrados")
    @GetMapping
    public ResponseEntity<Page<Organizador>> listarOrganizadores(Pageable pageable) {
        return new ResponseEntity<>(service.listarOrganizadores(pageable), HttpStatus.OK);
    }
}
