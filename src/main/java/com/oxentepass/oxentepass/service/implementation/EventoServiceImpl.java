package com.oxentepass.oxentepass.service.implementation;

import java.math.BigDecimal;
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
import com.oxentepass.oxentepass.exceptions.EstadoInvalidoException;
import com.oxentepass.oxentepass.exceptions.OperacaoProibidaException;
import com.oxentepass.oxentepass.exceptions.RecursoDuplicadoException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.EventoRepository;
import com.oxentepass.oxentepass.repository.PontoVendaRepository;
import com.oxentepass.oxentepass.repository.TagRepository;
import com.oxentepass.oxentepass.service.EventoService;
import com.oxentepass.oxentepass.service.ImagemEventoService;
import com.oxentepass.oxentepass.service.IngressoService;
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

    @Autowired
    private IngressoService ingressoService;

    @Autowired
    private ImagemEventoService imagemEventoService;

    //Métodos Auxiliares
    private Evento buscarEventoId(long id) {
        Optional<Evento> eventoBusca = eventoRepository.findById(id);

        if (eventoBusca.isEmpty())
            throw new RecursoNaoEncontradoException("O evento com id " + id + " não existe.");

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
    
    private boolean pontoVendaExistente(PontoVenda pontoVenda){
        String nome = pontoVenda.getNome();
        String cep = pontoVenda.getEndereco().getCep();
        int numero = pontoVenda.getEndereco().getNumero();
        
        return pontoVendaRepository.existsByNomeAndEnderecoCepAndEnderecoNumero(nome, cep, numero);
    }

    private boolean pontoVendaContido(PontoVenda pontoVenda) {
        return eventoRepository.existsByPontosVendaNomeAndPontosVendaEnderecoCepAndPontosVendaEnderecoNumero(
            pontoVenda.getNome(),
            pontoVenda.getEndereco().getCep(),
            pontoVenda.getEndereco().getNumero()
        );
    }

    //Métodos da Interface

    // Operações Básicas
    @Override
    public void criarEvento(Evento evento) {
        if(checagemDataEvento(evento))
            throw new EstadoInvalidoException("A data e hora de início do evento deve ser anterior a de fim.");

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
            throw new EstadoInvalidoException("A data e hora de início do evento deve ser anterior a de fim.");

        if (eventoRepository.isSubevento(idEvento)) {
            Evento eventoPai = eventoRepository.findEventoPaiBySubeventoId(idEvento).get();
            
            if(checagemDataSubevento(eventoPai, evento))
                throw new EstadoInvalidoException("O horário do sub-evento precisa estar dentro do horário do evento principal.");
        }

        Evento eventoEdicao = buscarEventoId(idEvento);

        // Atributos modificados pela "edição padrão"
        eventoEdicao.setNome(evento.getNome());                         // Nome
        eventoEdicao.setDescricao(evento.getDescricao());               // Descricao
        eventoEdicao.setOrganizador(evento.getOrganizador());           // Organizador
        eventoEdicao.setCidade(evento.getCidade());                     // Cidade
        eventoEdicao.setDataHoraInicio(evento.getDataHoraInicio());     // DataHoraInicio
        eventoEdicao.setDataHoraFim(evento.getDataHoraFim());           // DataHoraFim
        eventoEdicao.setClassificacao(evento.getClassificacao());       // Classificacao
        eventoEdicao.setEmailContato(evento.getEmailContato());         // EmailContato
        eventoEdicao.setTelefoneContato(evento.getTelefoneContato());   // TelefoneContato
        eventoEdicao.setEndereco(evento.getEndereco());                 // Endereco

        eventoRepository.save(eventoEdicao);
    }

    @Override
    @Transactional
    public void deletarEvento(long idEvento) {
        Evento evento = buscarEventoId(idEvento);

        // É preciso remover as imagens do AWS S3 ao deletar o evento
        for (int i = 0; i < evento.getImagens().size(); i++) 
            imagemEventoService.removerImagem(evento, evento.getImagens().get(i));  
        
        eventoRepository.delete(evento);
    }

    // Tags
    @Override
    public void adicionarTagExistente(long idEvento, long idTag) {
        Evento evento = buscarEventoId(idEvento);

        Optional<Tag> tagBusca = tagRepository.findById(idTag);
        if (tagBusca.isEmpty())
            throw new RecursoNaoEncontradoException("A tag informada não existe.");

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
            throw new RecursoNaoEncontradoException("A tag informada não existe.");

        Tag tag = tagBusca.get();

        evento.removerTag(tag);
        eventoRepository.save(evento);
    }

    // Ingressos
    @Override
    @Transactional
    public void adicionarIngresso(long idEvento, Ingresso ingresso) {
        Evento evento = buscarEventoId(idEvento);

        ingressoService.cadastrarIngresso(ingresso);

        evento.addIngresso(ingresso);
        
        if (evento.possuiTagGratuidade()) 
            ingresso.setValorBase(BigDecimal.ZERO);

        eventoRepository.save(evento);
    }

    @Override
    @Transactional
    public void removerIngresso(long idEvento, long idIngresso) {
        Evento evento = buscarEventoId(idEvento);
        Ingresso ingresso = ingressoService.buscarIngressoPorId(idIngresso);

        ingressoService.deletarIngresso(idIngresso);

        evento.removerIngresso(ingresso);
        eventoRepository.save(evento);
    }

    // Pontos de Venda
    @Override
    public void adicionarPontoVendaExistente(long idEvento, long idPontoVenda) { 
        Evento evento = buscarEventoId(idEvento);

        Optional<PontoVenda> pontoVendaBusca = pontoVendaRepository.findById(idPontoVenda);
        if(pontoVendaBusca.isEmpty())
            throw new RecursoNaoEncontradoException("O ponto de venda informado não existe.");

        PontoVenda pontoVenda = pontoVendaBusca.get();
        
        if(pontoVendaContido(pontoVenda))
            throw new EstadoInvalidoException("Esse Ponto de Venda já está registrado neste evento!");

        evento.addPontoVenda(pontoVenda);
        eventoRepository.save(evento);
    }

    @Override
    @Transactional
    public void adicionarPontoVendaNovo(long idEvento, PontoVenda pontoVenda) {
        Evento evento = buscarEventoId(idEvento);
        
        if(pontoVendaExistente(pontoVenda))
            throw new RecursoDuplicadoException("Esse Ponto de Venda já existe no sistema! Tente o outro método.");

        pontoVendaRepository.save(pontoVenda);
        
        evento.addPontoVenda(pontoVenda);
        eventoRepository.save(evento);
    }

    @Override
    public void removerPontoVenda(long idEvento, long idPontoVenda) {
        Evento evento = buscarEventoId(idEvento);

        Optional<PontoVenda> pontoVendaBusca = pontoVendaRepository.findById(idPontoVenda);
        if (pontoVendaBusca.isEmpty())
            throw new RecursoNaoEncontradoException("O ponto de venda informado não está vinculado ao evento " + evento.getNome() + ".");

        PontoVenda pontoVenda = pontoVendaBusca.get();

        evento.removerPontoVenda(pontoVenda);
        eventoRepository.save(evento);
    }

    // Avaliações
    @Override
    public void adicionarAvaliacao(long idEvento, Avaliacao avaliacao) {
        Evento evento = buscarEventoId(idEvento);

        evento.addAvaliacao(avaliacao);

        eventoRepository.save(evento);
    }

    @Override
    public void removerAvaliacao(long idEvento, long idAvaliacao) {
        Evento evento = buscarEventoId(idEvento);

        evento.removerAvaliacao(idAvaliacao);

        eventoRepository.save(evento);
    }

    // Sub-Eventos
    @Override
    @Transactional
    public void criarSubevento(long idEvento, Evento subevento) {
        Evento evento = buscarEventoId(idEvento);
        
        // Definindo a altura da árvore com esse sub-evento incluso
        subevento.setAltura(evento.getAltura() + 1); 

        if (!(evento instanceof EventoComposto)) 
            throw new OperacaoProibidaException("O evento com id " + idEvento + " não suporta sub-eventos.");

        if(subevento.getAltura() > 5) // Limitação da altura da árvore de sub-eventos
            throw new EstadoInvalidoException("Este evento já atingiu o número máximo de níveis de sub-eventos permitidos (5).");

        if (checagemDataSubevento(evento, subevento))
            throw new EstadoInvalidoException("O horário do sub-evento precisa estar dentro do horário do evento principal.");
        
        criarEvento(subevento);
        ((EventoComposto)evento).addSubevento(subevento);
        eventoRepository.save(evento);  
    }

    @Override
    public Page<EventoResponse> listarSubeventos(long idEvento, Pageable pageable) {
        Evento evento = buscarEventoId(idEvento);

        if (!(evento instanceof EventoComposto)) 
            throw new OperacaoProibidaException("O evento com id " + idEvento + " não suporta sub-eventos.");
    
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
            throw new OperacaoProibidaException("O evento com id " + idEvento + " não suporta sub-eventos.");

        ((EventoComposto)evento).removerSubevento(subEvento);
        deletarEvento(idSubevento);
        eventoRepository.save(evento);
    }
}
