package com.oxentepass.oxentepass.controller.request;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UsuarioLoginRequest(
        @NotBlank @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos numéricos") @CPF String cpf,
        @NotBlank String senha) {
}
