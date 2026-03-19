package com.oxentepass.oxentepass.service;

import org.springframework.stereotype.Service;
import com.oxentepass.oxentepass.entity.Usuario;
import jakarta.servlet.http.HttpServletRequest;

@Service
public interface AuthSessionService {

    public Usuario obterUsuarioAutenticado(HttpServletRequest request);
    public void autenticarSessao(HttpServletRequest request, Usuario usuario);
    public void invalidarSessao(HttpServletRequest request);
    public boolean usuarioAutenticadoEhOrganizador(HttpServletRequest request);
    
}
