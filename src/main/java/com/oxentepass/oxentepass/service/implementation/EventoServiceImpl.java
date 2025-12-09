package com.oxentepass.oxentepass.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oxentepass.oxentepass.controller.response.EventoResponse;
import com.oxentepass.oxentepass.entity.Avaliacao;
import com.oxentepass.oxentepass.entity.Evento;
import com.oxentepass.oxentepass.entity.EventoComposto;
import com.oxentepass.oxentepass.entity.Ingresso;
import com.oxentepass.oxentepass.entity.PontoVenda;
import com.oxentepass.oxentepass.entity.Tag;
import com.oxentepass.oxentepass.exceptions.EventoInvalidoException;
import com.oxentepass.oxentepass.exceptions.PontoVendaInvalidoException;
import com.oxentepass.oxentepass.exceptions.TagInvalidaException;
import com.oxentepass.oxentepass.repository.EventoRepository;
import com.oxentepass.oxentepass.repository.PontoVendaRepository;
import com.oxentepass.oxentepass.repository.TagRepository;
import com.oxentepass.oxentepass.service.EventoService;
import com.oxentepass.oxentepass.service.TagService;
import com.querydsl.core.types.Predicate;

@Service
public class EventoServiceImpl implements EventoService {
    //Repositórios
    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PontoVendaRepository pontoVendaRepository;

    //Outros services
    @Autowired
    private TagService tagService;

    //Métodos Auxiliares
    private Evento buscarEventoId(long id) {
        Optional<Evento> eventoBusca = eventoRepository.findById(id);

        if (eventoBusca.isEmpty())
            throw new EventoInvalidoException("O evento com id " + id + " não existe.");

        return eventoBusca.get();
    }

    private Page<EventoResponse> paraDTOPage(Page<Evento> eventos) {
        List<EventoResponse> dtos = eventos.getContent()
            .stream()
            .map(EventoResponse::paraDTO)
            .toList();

        return new PageImpl<>(
            dtos, 
            eventos.getPageable(), 
            eventos.getTotalElements()
        );
    }

    // Checagem de data para eventos
    private boolean checagemDataEvento(Evento evento) {
        return evento.getDataHoraInicio().isAfter(evento.getDataHoraFim());
    }

    // Checagem de data para sub-eventos (devem estar dentro do período de ocorrência do evento pai)
    private boolean checagemDataSubevento(Evento evento, Evento subevento) {
        return (subevento.getDataHoraInicio().isBefore(evento.getDataHoraInicio()) || subevento.getDataHoraFim().isAfter(evento.getDataHoraFim()));
    }

    //Métodos da Interface

    // Operações Básicas
    @Override
    public void criarEvento(Evento evento) {
        if(checagemDataEvento(evento))
            throw new EventoInvalidoException("A data e hora de início do evento deve ser anterior a de fim.");

        eventoRepository.save(evento);
    }

    @Override
    public Page<EventoResponse> listarEventos(Pageable pageable) {
        return this.paraDTOPage(
            eventoRepository.findAll(pageable)
        );
    }

    @Override
    public Page<EventoResponse> listarEventosFiltro(Predicate predicate, Pageable pageable) {
        return this.paraDTOPage(
            eventoRepository.findAll(predicate, pageable)
        );
    }

    @Override
    public void editarEvento(Long idEvento, Evento evento) {
        if(checagemDataEvento(evento))
            throw new EventoInvalidoException("A data e hora de início do evento deve ser anterior a de fim.");

        if (eventoRepository.isSubevento(idEvento)) {
            Evento eventoPai = eventoRepository.findEventoPaiBySubeventoId(idEvento).get();
            
            if(checagemDataSubevento(eventoPai, evento))
                throw new EventoInvalidoException("O horário do sub-evento precisa estar dentro do horário do evento principal.");
        }

        Evento eventoEdicao = buscarEventoId(idEvento);

        // Atributos modificados pela "edição padrão"
        eventoEdicao.setNome(evento.getNome());                         // Nome
        eventoEdicao.setDescricao(evento.getDescricao());               // Descricao
        eventoEdicao.setOrganizador(evento.getOrganizador());           // Organizador
        eventoEdicao.setDataHoraInicio(evento.getDataHoraInicio());     // DataHoraInicio
        eventoEdicao.setDataHoraFim(evento.getDataHoraFim());           // DataHoraFim
        eventoEdicao.setClassificacao(evento.getClassificacao());       // Classificacao
        eventoEdicao.setEmailContato(evento.getEmailContato());         // EmailContato
        eventoEdicao.setTelefoneContato(evento.getTelefoneContato());   // TelefoneContato
        eventoEdicao.setEndereco(evento.getEndereco());                 // Endereco

        eventoRepository.save(eventoEdicao);
    }

    @Override
    public void deletarEvento(long idEvento) {
        Evento evento = buscarEventoId(idEvento);
        eventoRepository.delete(evento); // Pensar se soft delete faz sentido para evento
    }

    // Tags
    @Override
    public void adicionarTagExistente(long idEvento, long idTag) {
        Evento evento = buscarEventoId(idEvento);

        Optional<Tag> tagBusca = tagRepository.findById(idTag);
        if (tagBusca.isEmpty())
            throw new TagInvalidaException("A tag informada não existe.");

        Tag tag = tagBusca.get();

        evento.addTag(tag);
        eventoRepository.save(evento);
    }

    @Override
    @Transactional 
    public void adicionarTagNova(long idEvento, Tag tag) {
        Evento evento = buscarEventoId(idEvento);

        tagService.criarTag(tag);

        evento.addTag(tag);
        eventoRepository.save(evento);
    }

    @Override
    public void removerTag(long idEvento, long idTag) {
        Evento evento = buscarEventoId(idEvento);

        Optional<Tag> tagBusca = tagRepository.findById(idTag);
        if (tagBusca.isEmpty())
            throw new TagInvalidaException("A tag informada não existe.");

        Tag tag = tagBusca.get();

        evento.removerTag(tag);
        eventoRepository.save(evento);
    }

    // Ingressos
    @Override
    public void adicionarIngresso(long idEvento, Ingresso ingresso) { // Precisa do Service de Ingresso
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adicionarIngresso'");
    }

    @Override
    public void removerIngresso(long idEvento, long idIngresso) { // Precisa do Service de Ingresso (caso for deletar o ingresso)
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removerIngresso'");
    }

    // Pontos de Venda
    @Override
    public void adicionarPontoVendaExistente(long idEvento, long idPontoVenda) { 
        Evento evento = buscarEventoId(idEvento);

        Optional<PontoVenda> pontoVendaBusca = pontoVendaRepository.findById(idPontoVenda);
        if(pontoVendaBusca.isEmpty())
            throw new PontoVendaInvalidoException("O ponto de venda informado não existe.");

        PontoVenda pontoVenda = pontoVendaBusca.get();

        evento.addPontoVenda(pontoVenda);
        eventoRepository.save(evento);
    }

    @Override
    public void adicionarPontoVendaNovo(long idEvento, PontoVenda pontoVenda) { //Precisa do Service de PontoVenda
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adicionarPontoVenda'");
    }

    @Override
    public void removerPontoVenda(long idEvento, long idPontoVenda) {
        Evento evento = buscarEventoId(idEvento);

        Optional<PontoVenda> pontoVendaBusca = pontoVendaRepository.findById(idPontoVenda);
        if (pontoVendaBusca.isEmpty())
            throw new PontoVendaInvalidoException("O ponto de venda informado não está vinculado ao evento " + evento.getNome() + ".");

        PontoVenda pontoVenda = pontoVendaBusca.get();

        evento.removerPontoVenda(pontoVenda);
        eventoRepository.save(evento);
    }

    // Avaliações
    @Override
    public void adicionarAvaliacao(long idEvento, Avaliacao avaliacao) {
        Evento evento = buscarEventoId(idEvento);

        evento.addAvaliacao(avaliacao);
    }

    @Override
    public void removerAvaliacao(long idEvento, long idAvaliacao) {
        Evento evento = buscarEventoId(idEvento);

        evento.removerAvaliacao(idAvaliacao);
    }

    // Sub-Eventos
    @Override
    @Transactional
    public void criarSubevento(long idEvento, Evento subevento) {
        Evento evento = buscarEventoId(idEvento);
        
        // Definindo a altura da árvore com esse sub-evento incluso
        subevento.setAltura(evento.getAltura() + 1); 

        if (!(evento instanceof EventoComposto)) 
            throw new EventoInvalidoException("O evento com id " + idEvento + " não suporta sub-eventos.");

        if(subevento.getAltura() > 5) // Limitação da altura da árvore de sub-eventos
            throw new EventoInvalidoException("Este evento já atingiu o número máximo de níveis de sub-eventos permitidos (5).");

        if (checagemDataSubevento(evento, subevento))
            throw new EventoInvalidoException("O horário do sub-evento precisa estar dentro do horário do evento principal.");
        
        criarEvento(subevento);
        ((EventoComposto)evento).addSubevento(subevento);
        eventoRepository.save(evento);  
    }

    @Override
    public Page<EventoResponse> listarSubeventos(long idEvento, Pageable pageable) {
        Evento evento = buscarEventoId(idEvento);

        if (!(evento instanceof EventoComposto)) 
            throw new EventoInvalidoException("O evento com id " + idEvento + " não suporta sub-eventos.");
    
        return this.paraDTOPage(
            eventoRepository.findSubeventosByParentId(idEvento, pageable)
        );
    }

    @Override
    @Transactional
    public void removerSubevento(long idEvento, long idSubevento) {
        Evento evento = buscarEventoId(idEvento);
        Evento subEvento = buscarEventoId(idSubevento);

        if (!(evento instanceof EventoComposto))  
            throw new EventoInvalidoException("O evento com id " + idEvento + " não suporta sub-eventos.");

        ((EventoComposto)evento).removerSubevento(subEvento);
        deletarEvento(idSubevento);
        eventoRepository.save(evento);
    }
}
