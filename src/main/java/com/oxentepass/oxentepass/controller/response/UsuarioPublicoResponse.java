package com.oxentepass.oxentepass.controller.response;

import com.oxentepass.oxentepass.entity.Usuario;

public record UsuarioPublicoResponse(
    long id,
    String nome
) {
    public static UsuarioPublicoResponse paraDTO(Usuario usuario) {
        return new UsuarioPublicoResponse(
            usuario.getId(),
            usuario.getNome()
        );
    }
}
