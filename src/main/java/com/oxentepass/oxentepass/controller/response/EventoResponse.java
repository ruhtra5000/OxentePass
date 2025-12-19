package com.oxentepass.oxentepass.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import com.oxentepass.oxentepass.entity.Avaliacao;
import com.oxentepass.oxentepass.entity.Cidade;
import com.oxentepass.oxentepass.entity.Endereco;
import com.oxentepass.oxentepass.entity.Evento;
import com.oxentepass.oxentepass.entity.Ingresso;
import com.oxentepass.oxentepass.entity.PontoVenda;
import com.oxentepass.oxentepass.entity.Tag;

public record EventoResponse(
    long id,
    String nome,
    String descricao,
    UsuarioResponse organizador,
    Cidade cidade,
    List<Tag> tags,
    List<Ingresso> ingressos,
    LocalDateTime dataHoraInicio,
    LocalDateTime dataHoraFim,
    String classificacao,
    String emailContato,
    String telefoneContato,
    Endereco endereco,
    List<PontoVenda> pontosVenda,
    List<Avaliacao> avaliacoes,
    double mediaAvaliacao
) {
    public static EventoResponse paraDTO(Evento evento) {
        return new EventoResponse (
            evento.getId(), evento.getNome(), evento.getDescricao(), 
            UsuarioResponse.paraDTO(evento.getOrganizador()),
            evento.getCidade(), evento.getTags(), 
            evento.getIngressos(), evento.getDataHoraInicio(), 
            evento.getDataHoraFim(), evento.getClassificacao(), 
            evento.getEmailContato(), evento.getTelefoneContato(), 
            evento.getEndereco(), evento.getPontosVenda(), 
            evento.getAvaliacoes(), evento.getMediaAvaliacao()
        );
    }
} 
