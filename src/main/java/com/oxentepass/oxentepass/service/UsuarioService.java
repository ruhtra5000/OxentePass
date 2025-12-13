package com.oxentepass.oxentepass.service;

import com.oxentepass.oxentepass.entity.Usuario;

public interface UsuarioService {

    public void cadastrarUsuario(Usuario usuario);

    public void autenticarUsuario(String email, String senha);

    public void atualizarDadosUsuario(Long id, Usuario dados);

}
