package com.oxentepass.oxentepass.controller.request;

import org.hibernate.validator.constraints.br.CPF;

import com.oxentepass.oxentepass.entity.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UsuarioRequest(
        @NotBlank(message = "O campo \"nome\" é obrigatorio") String nome,

        @NotBlank(message = "O campo \"cpf\" é obrigatorio") @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos numéricos") @CPF String cpf,

        @NotBlank(message = "O campo \"email\" é obrigatorio") @Email String email,

        @NotBlank(message = "O campo \"senha\" é obrigatorio") String senha) {

    public Usuario paraEntidade() {
        Usuario usuario = new Usuario();

        String cpfLimpo = cpf.replaceAll("\\D", "");

        usuario.setNome(nome);
        usuario.setCpf(cpfLimpo);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        return usuario;
    }
}
