package com.oxentepass.oxentepass.controller.request;

import java.math.BigDecimal;

import com.oxentepass.oxentepass.entity.Ingresso;

import jakarta.validation.constraints.NotBlank;

public record IngressoRequest(

    @NotBlank(message = "O campo \"tipoIngresso\" é obrigatório.")
    String tipoIngresso,
    @NotBlank(message = "O campo \"valorBase\" é obrigatório.")
    BigDecimal valorBase,
    @NotBlank(message = "O campo \"quantidadeDisponivel\" é obrigatório.") 
    int quantidadeDisponivel,
    @NotBlank(message = "O campo \"temMeiaEntrada\" é obrigatório.")
    boolean temMeiaEntrada

) {

    public Ingresso paraEntidade() {

    Ingresso ingresso = new Ingresso();

        ingresso.setTipo(tipoIngresso);
        ingresso.setValorBase(valorBase);
        ingresso.setQuantidadeDisponivel(quantidadeDisponivel);
        ingresso.setTemMeiaEntrada(temMeiaEntrada);
        return ingresso;

    }
}
    

