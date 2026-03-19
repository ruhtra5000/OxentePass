package com.oxentepass.oxentepass.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oxentepass.oxentepass.entity.Organizador;
import com.oxentepass.oxentepass.entity.Usuario;
import com.oxentepass.oxentepass.exceptions.NaoAutenticadoException;
import com.oxentepass.oxentepass.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthSessionServiceImpl {

    public static final String SESSION_USER_ID = "usuarioId";

    @Autowired
    private UsuarioService usuarioService;

    public Usuario obterUsuarioAutenticado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SESSION_USER_ID) == null) {
            throw new NaoAutenticadoException("Nenhum usuario autenticado na sessao atual.");
        }

        long usuarioId = (long) session.getAttribute(SESSION_USER_ID);

        return usuarioService.buscarUsuarioPorId(usuarioId);
    }

    public void autenticarSessao(HttpServletRequest request, Usuario usuario) {
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_USER_ID, usuario.getId());
    }

    public void invalidarSessao(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }

    public boolean usuarioAutenticadoEhOrganizador(HttpServletRequest request) {
        return obterUsuarioAutenticado(request) instanceof Organizador;
    }
}
