package com.oxentepass.oxentepass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oxentepass.oxentepass.controller.request.UsuarioLoginRequest;
import com.oxentepass.oxentepass.controller.request.UsuarioRequest;
import com.oxentepass.oxentepass.entity.Usuario;
import com.oxentepass.oxentepass.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * @author Guilherme Paes
 * Controller para manipular Usuário, através de UsuarioService
 */

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Operation(summary = "Cadastrar Usuário", description = "Cadastra um novo Usuário")
    @PostMapping
    public ResponseEntity<String> cadastrarUsuario(@RequestBody @Valid UsuarioRequest dto) {

        service.cadastrarUsuario(dto.paraEntidade());

        return new ResponseEntity<String>("Usuário " + dto.nome() + " criado com sucesso!", HttpStatus.CREATED);
    }

    @Operation(summary = "Listar Usuários", description = "Retorna os Usuários cadastrados")
    @GetMapping
    public ResponseEntity<Page<Usuario>> listarUsuarios(Pageable pageable) {
        return new ResponseEntity<Page<Usuario>>(service.listarUsuarios(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Editar Usuário", description = "Edita os dados do Usuário com id especificado")
    @PutMapping("/{id}")
    public ResponseEntity<String> editarUsuario(@PathVariable long id, @RequestBody @Valid UsuarioRequest dto) {

        service.editarUsuario(id, dto.paraEntidade());

        return new ResponseEntity<String>("Usuário " + dto.nome() + " editado com sucesso!", HttpStatus.OK);

    }

    @Operation(summary = "Deletar Usuário", description = "Deleta o Usuário com id especificado")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable long id) {

        service.deletarUsuario(id);

        return new ResponseEntity<String>("Usuário com id " + id + " deletado com sucesso!", HttpStatus.OK);

    }

    @Operation(summary = "Login de Usuário", description = "Autentica um Usuário com CPF e senha")
    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody @Valid UsuarioLoginRequest dto) {

        service.loginUsuario(dto.cpf(), dto.senha());

        return new ResponseEntity<String>("O usuário com CPF " + dto.cpf() + " está autenticado!", HttpStatus.OK);
    }
}