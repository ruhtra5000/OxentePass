package com.oxentepass.oxentepass.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioEdicaoRequest(
    @NotBlank(message = "O campo \"nome\" é obrigatorio")
    String nome,

    @NotBlank(message = "O campo \"email\" é obrigatorio")
    @Email
    String email
) {
}
