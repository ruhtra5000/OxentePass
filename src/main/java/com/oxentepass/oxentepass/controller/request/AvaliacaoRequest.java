package com.oxentepass.oxentepass.controller.request;

import com.oxentepass.oxentepass.entity.Avaliacao;

import jakarta.validation.constraints.NotNull;

public record AvaliacaoRequest(
        String comentario,
        @NotNull(message = "Nota não pode ser nula")
        Integer nota
) {
    public Avaliacao paraEntidade() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setComentario(comentario);
        avaliacao.setNota(nota);

        return avaliacao;
    }
}
