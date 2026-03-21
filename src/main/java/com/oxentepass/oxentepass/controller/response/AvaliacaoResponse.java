package com.oxentepass.oxentepass.controller.response;

import com.oxentepass.oxentepass.entity.Avaliacao;

public record AvaliacaoResponse(
    long id,
    int nota,
    String comentario,
    Long usuarioId,
    String nomeUsuario
) {
    public static AvaliacaoResponse paraDTO(Avaliacao avaliacao) {
        return new AvaliacaoResponse(
            avaliacao.getId(),
            avaliacao.getNota(),
            avaliacao.getComentario(),
            avaliacao.getUsuarioId(),
            avaliacao.getNomeUsuario()
        );
    }
}
