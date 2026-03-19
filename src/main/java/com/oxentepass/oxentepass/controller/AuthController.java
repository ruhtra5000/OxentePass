package com.oxentepass.oxentepass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oxentepass.oxentepass.controller.response.AuthResponse;
import com.oxentepass.oxentepass.entity.Usuario;
import com.oxentepass.oxentepass.service.implementation.AuthSessionServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthSessionServiceImpl authSessionService;

    @Operation(summary = "Usuario autenticado", description = "Retorna os dados basicos do usuario autenticado na sessao atual")
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> buscarUsuarioAutenticado(HttpServletRequest request) {
        Usuario usuario = authSessionService.obterUsuarioAutenticado(request);

        return new ResponseEntity<AuthResponse>(AuthResponse.paraDTO(usuario), HttpStatus.OK);
    }

    @Operation(summary = "Logout", description = "Encerra a sessao autenticada atual")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authSessionService.invalidarSessao(request);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
