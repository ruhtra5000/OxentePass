package com.oxentepass.oxentepass.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oxentepass.oxentepass.controller.response.EventoResponse;
import com.oxentepass.oxentepass.entity.Avaliacao;
import com.oxentepass.oxentepass.entity.Evento;
import com.oxentepass.oxentepass.entity.Ingresso;
import com.oxentepass.oxentepass.entity.PontoVenda;
import com.oxentepass.oxentepass.service.outrasInterfaces.TagManipulacao;
import com.querydsl.core.types.Predicate;

public interface EventoService extends TagManipulacao {
    // Operações Básicas
    public void criarEvento(Evento evento);
    public Page<EventoResponse> listarEventos(Pageable pageable);
    public Page<EventoResponse> listarEventosFiltro (Predicate predicate, Pageable pageable);
    public void editarEvento(Long idEvento, Evento evento);
    public void deletarEvento(long idEvento);
    // Tags -> TagManipulacao
    // Ingressos
    public void adicionarIngresso(long idEvento, Ingresso ingresso);
    public void removerIngresso(long idEvento, long idIngresso);
    // Pontos de venda
    public void adicionarPontoVendaExistente(long idEvento, long idPontoVenda);    // Adição de PontoVenda existente
    public void adicionarPontoVendaNovo(long idEvento, PontoVenda pontoVenda);// Criação de novo PontoVenda 
    public void removerPontoVenda(long idEvento, long idPontoVenda);
    // Avaliações
    public void adicionarAvaliacao(long idEvento, Avaliacao avaliacao);
    public void removerAvaliacao(long idEvento, long idAvaliacao);
    // Sub-Eventos
    public void criarSubevento(long idEvento, Evento subevento);
    public Page<EventoResponse> listarSubeventos(long idEvento, Pageable pageable);
    public void removerSubevento(long idEvento, long idSubevento);
}
