package com.oxentepass.oxentepass.controller.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxentepass.oxentepass.entity.Cidade;
import com.oxentepass.oxentepass.entity.Endereco;
import com.oxentepass.oxentepass.entity.Evento;
import com.oxentepass.oxentepass.entity.EventoComposto;
import com.oxentepass.oxentepass.entity.EventoSimples;
import com.oxentepass.oxentepass.entity.Organizador;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventoRequest(
    @NotBlank(message = "O campo \"nome\" é obrigatório.")
    String nome,

    @NotBlank(message = "O campo \"descricao\" é obrigatório.")
    String descricao,

    @NotNull(message = "O evento deve contar com um organizador.")
    long idOrganizador,

    @NotNull(message = "O evento deve contar com uma cidade sede.")
    long idCidade,
    
    @FutureOrPresent(message = "O campo \"dataHoraInicio\" deve estar no presente ou futuro.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dataHoraInicio,

    @Future(message = "O campo \"dataHoraFim\" deve estar no futuro.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dataHoraFim,

    @NotNull(message = "O evento deve contar com um endereço.")
    Endereco endereco,

    String classificacao,
    String emailContato,
    String telefoneContato
) {
    public Evento paraEntidade(boolean ehSimples) {

        Evento evento;

        if(ehSimples)
            evento = new EventoSimples();
        else
            evento = new EventoComposto();

        Organizador organizador = new Organizador();
        organizador.setId(idOrganizador);

        Cidade cidade = new Cidade();
        cidade.setId(idCidade);

        evento.setNome(nome);
        evento.setDescricao(descricao);
        evento.setOrganizador(organizador);
        evento.setCidade(cidade);
        evento.setDataHoraInicio(dataHoraInicio);
        evento.setDataHoraFim(dataHoraFim);
        evento.setClassificacao(classificacao);
        evento.setEmailContato(emailContato);
        evento.setTelefoneContato(telefoneContato);
        evento.setEndereco(endereco);
        evento.setAltura(1);
        return evento;
    }
} 