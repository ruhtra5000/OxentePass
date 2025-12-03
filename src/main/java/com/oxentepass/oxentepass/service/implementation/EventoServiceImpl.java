package com.oxentepass.oxentepass.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //Métodos da Interface

    // Operações Básicas
    @Override
    public void criarEvento(Evento evento) {
        eventoRepository.save(evento);
    }

    @Override
    public Page<Evento> listarEventos(Pageable pageable) {
        return eventoRepository.findAll(pageable);
    }

    @Override
    public Page<Evento> listarEventosFiltro(Predicate predicate, Pageable pageable) {
        return eventoRepository.findAll(predicate, pageable);
    }

    @Override
    public void editarEvento(Long idEvento, Evento evento) {
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
        if(!eventoRepository.existsById(idEvento))
            throw new EventoInvalidoException("O evento a ser deletado não existe.");

        eventoRepository.deleteById(idEvento); // Pensar se soft delete faz sentido para evento
    }

    // Tags
    @Override
    public void adicionarTag(long idEvento, long idTag) {
        Evento evento = buscarEventoId(idEvento);

        Optional<Tag> tagBusca = tagRepository.findById(idTag);
        if (tagBusca.isEmpty())
            throw new TagInvalidaException("A tag informada não existe.");

        Tag tag = tagBusca.get();

        evento.addTag(tag);
        eventoRepository.save(evento);
    }

    @Override
    public void adicionarTag(long idEvento, Tag tag) {
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
    public void adicionarPontoVenda(long idEvento, long idPontoVenda) { 
        Evento evento = buscarEventoId(idEvento);

        Optional<PontoVenda> pontoVendaBusca = pontoVendaRepository.findById(idPontoVenda);
        if(pontoVendaBusca.isEmpty())
            throw new PontoVendaInvalidoException("O ponto de venda informado não existe.");

        PontoVenda pontoVenda = pontoVendaBusca.get();

        evento.addPontoVenda(pontoVenda);
        eventoRepository.save(evento);
    }

    @Override
    public void adicionarPontoVenda(long idEvento, PontoVenda pontoVenda) { //Precisa do Service de PontoVenda
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
        
        criarEvento(subevento);
        ((EventoComposto)evento).addSubevento(subevento);
        eventoRepository.save(evento);
         
    }

    @Override
    public Page<Evento> listarSubeventos(long idEvento, Pageable pageable) {
        Evento evento = buscarEventoId(idEvento);

        if (!(evento instanceof EventoComposto)) 
            throw new EventoInvalidoException("O evento com id " + idEvento + " não suporta sub-eventos.");
    
        return eventoRepository.findSubeventosByParentId(idEvento, pageable); //Testar
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
