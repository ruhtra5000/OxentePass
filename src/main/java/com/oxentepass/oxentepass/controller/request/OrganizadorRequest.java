package com.oxentepass.oxentepass.controller.request;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OrganizadorRequest(
    @NotNull(message = "O ID do usuário é obrigatório")
    Long usuarioId,
    
    @CNPJ
    @Pattern(
        regexp = "^(\\d{14}|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2})$",
        message = "CNPJ inválido (use 14 dígitos ou o formato 00.000.000/0001-00)"
    ) 
    String cnpj,
    String telefone,
    String biografia) {
}
