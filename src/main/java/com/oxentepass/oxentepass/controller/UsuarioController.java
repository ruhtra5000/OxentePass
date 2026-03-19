package com.oxentepass.oxentepass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;

import com.querydsl.core.types.Predicate;
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
import com.oxentepass.oxentepass.controller.response.AuthResponse;
import com.oxentepass.oxentepass.controller.response.UsuarioPublicoResponse;
import com.oxentepass.oxentepass.controller.response.UsuarioResponse;
import com.oxentepass.oxentepass.entity.Usuario;
import com.oxentepass.oxentepass.service.UsuarioService;
import com.oxentepass.oxentepass.service.implementation.AuthSessionServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    private AuthSessionServiceImpl authSessionService;

    @Operation(summary = "Cadastrar Usuário", description = "Cadastra um novo Usuário")
    @PostMapping
    public ResponseEntity<String> cadastrarUsuario(@RequestBody @Valid UsuarioRequest dto) {

        service.cadastrarUsuario(dto.paraEntidade());

        return new ResponseEntity<String>("Usuário " + dto.nome() + " criado com sucesso!", HttpStatus.CREATED);
    }

    @Operation(summary = "Listar Usuários", description = "Retorna os perfis públicos dos Usuários cadastrados")
    @GetMapping
    public ResponseEntity<Page<UsuarioPublicoResponse>> listarUsuarios(Pageable pageable) {
        return new ResponseEntity<Page<UsuarioPublicoResponse>>(
            service.listarUsuarios(pageable).map(UsuarioPublicoResponse::paraDTO),
            HttpStatus.OK
        );
    }

    @Operation(summary = "Busca Usuário com filtro", description = "Busca os perfis públicos dos Usuários cadastrados com filtro e paginação")
    @GetMapping("/filtro")
    public ResponseEntity<Page<UsuarioPublicoResponse>> listarUsuariosFiltro(@QuerydslPredicate(root = Usuario.class) Predicate predicate, Pageable pageable) {
        return new ResponseEntity<Page<UsuarioPublicoResponse>>(
            service.listarUsuariosFiltro(predicate, pageable).map(UsuarioPublicoResponse::paraDTO),
            HttpStatus.OK
        );
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
    public ResponseEntity<AuthResponse> loginUsuario(
            @RequestBody @Valid UsuarioLoginRequest dto,
            HttpServletRequest request) {

        Usuario usuario = service.loginUsuario(dto.cpf(), dto.senha());
        authSessionService.autenticarSessao(request, usuario);

        return new ResponseEntity<AuthResponse>(AuthResponse.paraDTO(usuario), HttpStatus.OK);
    }

    @Operation(summary = "Perfil do usuário autenticado", description = "Retorna os dados completos do usuário autenticado na sessão atual")
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> buscarMeuPerfil(HttpServletRequest request) {
        Usuario usuario = authSessionService.obterUsuarioAutenticado(request);

        return new ResponseEntity<UsuarioResponse>(UsuarioResponse.paraDTO(usuario), HttpStatus.OK);
    }

    @Operation(summary = "Perfil público do usuário", description = "Retorna somente os dados públicos do usuário informado")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPublicoResponse> buscarPerfilPublico(@PathVariable long id) {
        Usuario usuario = service.buscarUsuarioPorId(id);

        return new ResponseEntity<UsuarioPublicoResponse>(UsuarioPublicoResponse.paraDTO(usuario), HttpStatus.OK);
    }
}
