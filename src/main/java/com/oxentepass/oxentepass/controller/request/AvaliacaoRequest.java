package com.oxentepass.oxentepass.controller.request;

import com.oxentepass.oxentepass.entity.Avaliacao;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AvaliacaoRequest(
        String comentario,
        @NotNull(message = "Nota não pode ser nula")
        @Min(value = 1, message = "Nota deve ser no mínimo 1")
        @Max(value = 5, message = "Nota deve ser no máximo 5")
        Integer nota
) {
    public Avaliacao paraEntidade() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setComentario(comentario);
        avaliacao.setNota(nota);

        return avaliacao;
    }
}
