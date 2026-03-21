package com.oxentepass.oxentepass.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.querydsl.core.types.Predicate;

import com.oxentepass.oxentepass.controller.request.UsuarioEdicaoRequest;
import com.oxentepass.oxentepass.entity.Usuario;

public interface UsuarioService {

    public void cadastrarUsuario(Usuario usuario);

    public Usuario loginUsuario(String cpf, String senha);

    public Usuario buscarUsuarioPorId(long id);

    public void editarUsuario(long id, Usuario dados);
    public void editarUsuarioParcial(long id, UsuarioEdicaoRequest dados);

    public void deletarUsuario(long id);

    public Page<Usuario> listarUsuarios(Pageable pageable);

    public Page<Usuario> listarUsuariosFiltro(Predicate predicate, Pageable pageable);
}
