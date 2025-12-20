package com.oxentepass.oxentepass.controller.request;

import com.oxentepass.oxentepass.entity.Endereco;
import com.oxentepass.oxentepass.entity.PontoVenda;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PontoVendaRequest(
        @NotBlank(message = "O campo 'nome' é obrigatório") String nome,
        @NotBlank(message = "O campo 'detalhes' é obrigatório") String detalhes,
        @NotBlank(message = "O campo 'cep' é obrigatório") @Pattern(regexp = "^(\\d{8}|\\d{5}-\\d{3})$", message = "O campo 'cep' deve conter exatamente 8 dígitos numéricos") String cep,
        @NotBlank(message = "O campo 'bairro' é obrigatório") String bairro,
        @NotBlank(message = "O campo 'rua' é obrigatório") String rua,
        @NotNull(message = "O campo 'numero' é obrigatório") Integer numero) {

    public PontoVenda paraEntidade() {

        String cepLimpo = cep.replaceAll("\\D", "");

        PontoVenda pontoVenda = new PontoVenda();
        Endereco endereco = new Endereco(cepLimpo, bairro, rua, numero);

        pontoVenda.setNome(nome);
        pontoVenda.setDetalhes(detalhes);
        pontoVenda.setEndereco(endereco);

        return pontoVenda;
    }
}