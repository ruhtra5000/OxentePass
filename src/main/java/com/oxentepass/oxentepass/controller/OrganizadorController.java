package com.oxentepass.oxentepass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oxentepass.oxentepass.controller.request.OrganizadorEdicaoRequest;
import com.oxentepass.oxentepass.controller.request.OrganizadorRequest;
import com.oxentepass.oxentepass.controller.response.OrganizadorResponse;
import com.oxentepass.oxentepass.entity.Organizador;
import com.oxentepass.oxentepass.service.AuthSessionService;
import com.oxentepass.oxentepass.service.AutorizacaoService;
import com.oxentepass.oxentepass.service.OrganizadorService;
import com.querydsl.core.types.Predicate;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * @author Guilherme Paes
 *         Controller para manipular Organizador, através de OrganizadorService
 */

@RestController
@RequestMapping("/organizador")
public class OrganizadorController {

    @Autowired
    private OrganizadorService service;
    @Autowired
    private AutorizacaoService autorizacaoService;
    @Autowired
    private AuthSessionService authSessionService;

    @Operation(summary = "Promover Usuário a Organizador", description = "Promove um Usuário comum a Organizador")
    @PostMapping("/promover")
    public ResponseEntity<String> promoverUsuario(@RequestBody @Valid OrganizadorRequest dto, HttpServletRequest request) {
        autorizacaoService.exigirMesmoUsuario(request, dto.usuarioId());

        service.promoverUsuario(dto);
        return new ResponseEntity<String>("Usuario promovido a Organizador com sucesso!", HttpStatus.CREATED);
    }

    @Operation(summary = "Editar Organizador", description = "Edita os dados do Organizador com id especificado")
    @PutMapping
    public ResponseEntity<String> editarOrganizador(@RequestBody @Valid OrganizadorRequest dados, HttpServletRequest request) {
        autorizacaoService.exigirMesmoUsuario(request, dados.usuarioId());

        service.editarOrganizador(dados.usuarioId(), dados);

        return new ResponseEntity<String>(
                "Organizador com id " + dados.usuarioId() + " atualizado com sucesso!",
                HttpStatus.OK);
    }

    @Operation(summary = "Editar parcialmente Organizador", description = "Edita apenas telefone e biografia do organizador autenticado")
    @PatchMapping("/me")
    public ResponseEntity<String> editarMeuPerfilOrganizador(@RequestBody @Valid OrganizadorEdicaoRequest dados, HttpServletRequest request) {
        autorizacaoService.exigirUsuarioOrganizador(request);

        long usuarioId = authSessionService.obterUsuarioAutenticado(request).getId();
        service.editarOrganizadorParcial(usuarioId, dados);

        return new ResponseEntity<String>(
                "Organizador com id " + usuarioId + " atualizado com sucesso!",
                HttpStatus.OK);
    }

    @Operation(summary = "Meu perfil de organizador", description = "Retorna os dados do organizador autenticado")
    @GetMapping("/me")
    public ResponseEntity<OrganizadorResponse> buscarMeuPerfilOrganizador(HttpServletRequest request) {
        autorizacaoService.exigirUsuarioOrganizador(request);

        long usuarioId = authSessionService.obterUsuarioAutenticado(request).getId();
        Organizador organizador = service.buscarOrganizadorPorId(usuarioId);

        return new ResponseEntity<OrganizadorResponse>(OrganizadorResponse.paraDTO(organizador), HttpStatus.OK);
    }

    @Operation(summary = "Listar Organizadores", description = "Retorna os Organizadores cadastrados")
    @GetMapping
    public ResponseEntity<Page<Organizador>> listarOrganizadores(Pageable pageable) {
        return new ResponseEntity<>(service.listarOrganizadores(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Listar Organizadores com Filtro", description = "Retorna os Organizadores cadastrados que satisfazem o filtro")
    @GetMapping("/filtro")
    public ResponseEntity<Page<Organizador>> listarOrganizadoresFiltro(
            @QuerydslPredicate(root = Organizador.class) Predicate predicate,
            Pageable pageable) {
        return new ResponseEntity<Page<Organizador>>(
                service.listarOrganizadoresFiltro(predicate, pageable),
                HttpStatus.OK);
    }
}
