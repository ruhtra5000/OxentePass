package com.oxentepass.oxentepass.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oxentepass.oxentepass.controller.request.AvaliacaoRequest;
import com.oxentepass.oxentepass.controller.request.EventoRequest;
import com.oxentepass.oxentepass.controller.request.IngressoRequest;
import com.oxentepass.oxentepass.controller.request.PontoVendaRequest;
import com.oxentepass.oxentepass.controller.request.TagRequest;
import com.oxentepass.oxentepass.controller.response.EventoImagemResponse;
import com.oxentepass.oxentepass.controller.response.EventoResponse;
import com.oxentepass.oxentepass.entity.Avaliacao;
import com.oxentepass.oxentepass.entity.Evento;
import com.oxentepass.oxentepass.entity.Usuario;
import com.oxentepass.oxentepass.service.AuthSessionService;
import com.oxentepass.oxentepass.service.EventoService;
import com.querydsl.core.types.Predicate;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.oxentepass.oxentepass.controller.response.AvaliacaoResponse;

/**
 * @author Arthur de Sá
 * Controller para manipular Evento, através de EventoService
 */

@RestController
@RequestMapping("/evento")
public class EventoController {

    @Autowired
    private EventoService eventoService;
    @Autowired
    private AuthSessionService authSessionService;

    @PostMapping("/simples")
    public ResponseEntity<EventoResponse> criarEventoSimples (@RequestBody @Valid EventoRequest dto) {
        return new ResponseEntity<EventoResponse>(
            eventoService.criarEvento(dto.paraEntidade(true)), 
            HttpStatus.CREATED
        );
    }

    @Operation(summary = "Criar novo Evento Composto", description = "Cria um novo EventoComposto, que suporta sub-eventos")
    @PostMapping("/composto")
    public ResponseEntity<EventoResponse> criarEventoComposto (@RequestBody @Valid EventoRequest dto) {
        return new ResponseEntity<EventoResponse>(
            eventoService.criarEvento(dto.paraEntidade(false)), 
            HttpStatus.CREATED
        );
    }
    
    @Operation(summary = "Listar Eventos", description = "Retorna todos os Eventos c/ paginação")
    @GetMapping
    public ResponseEntity<Page<EventoResponse>> listarTodosEventos (Pageable pageable) {
        return new ResponseEntity<Page<EventoResponse>>(
            eventoService.listarEventos(pageable), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Listar Eventos com filtro", description = "Filtra os Eventos c/ QueryDSL e paginação")
    @GetMapping("/filtro")
    public ResponseEntity<Page<EventoResponse>> listarEventosFiltro (@QuerydslPredicate(root = Evento.class) Predicate predicate, Pageable pageable) {
        return new ResponseEntity<Page<EventoResponse>>(
            eventoService.listarEventosFiltro(predicate, pageable), 
            HttpStatus.OK
        );
    }

    @Operation(summary = "Listar Eventos com uma imagem e com filtro", description = "Filtra os Eventos com imagem c/ QueryDSL e paginação")
    @GetMapping("/comImg")
    public ResponseEntity<Page<EventoImagemResponse>> listarEventosComImagem (@QuerydslPredicate(root = Evento.class) Predicate predicate, Pageable pageable) {
        return new ResponseEntity<Page<EventoImagemResponse>>(
            eventoService.listarEventosComImagem(predicate, pageable), 
            HttpStatus.OK
        );
    }

    @Operation(summary = "Editar Evento", description = "Altera dados de um Evento com id especificado")
    @PutMapping("/{id}")
    public ResponseEntity<String> editarEvento (@PathVariable long id, @RequestBody @Valid EventoRequest dto) {
        eventoService.editarEvento(id, dto.paraEntidade(true));  

        return new ResponseEntity<String>(
            "Evento " + dto.nome() + " editado com sucesso!", 
            HttpStatus.OK
        );
    }

    @Operation(summary = "Deletar Evento", description = "Exclui um Evento com id especificado")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarEvento (@PathVariable long id) {
        eventoService.deletarEvento(id); 

        return new ResponseEntity<String>(
            "Evento com id " + id + " deletado com sucesso!", 
            HttpStatus.OK
        );
    }

    //Tags
    @Operation(summary = "Adicionar uma Tag Existente a Evento", description = "Adiciona uma Tag previamente criada ao Evento com id especificado")
    @PatchMapping("/{idEvento}/addTag/{idTag}")
    public ResponseEntity<String> adicionarTagExistente (@PathVariable long idEvento, @PathVariable long idTag) {
        eventoService.adicionarTagExistente(idEvento, idTag);

        return new ResponseEntity<String>(
            "Tag com id " + idTag + " adicionada ao evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    @Operation(summary = "Adicionar uma Tag Nova a Evento", description = "Cria uma nova Tag, e a vincula ao Evento com id especificado")
    @PatchMapping("/{idEvento}/addTag")
    public ResponseEntity<String> adicionarTagNova (@PathVariable long idEvento, @RequestBody TagRequest dto) {
        eventoService.adicionarTagNova(idEvento, dto.paraEntidade());

        return new ResponseEntity<String>(
            "Tag " + dto.tag() + " adicionada ao evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    @Operation(summary = "Remover uma Tag de um Evento", description = "Desvincula a Tag com id especificado do Evento com id especificado")
    @PatchMapping("/{idEvento}/removerTag/{idTag}")
    public ResponseEntity<String> removerTag (@PathVariable long idEvento, @PathVariable long idTag) {
        eventoService.removerTag(idEvento, idTag);

        return new ResponseEntity<String>(
            "Tag com id " + idTag + " removida do evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    //Ingressos
    @Operation(summary = "Adicionar um Ingresso a Evento", description = "Cria um novo Ingresso e o vincula ao Evento com id especificado")
    @PatchMapping("/{idEvento}/addIngresso")
    public ResponseEntity<String> adicionarIngresso (@PathVariable long idEvento, @RequestBody @Valid IngressoRequest dto) {
        eventoService.adicionarIngresso(idEvento, dto.paraEntidade());

        return new ResponseEntity<String>(
            "Ingresso " + dto.tipoIngresso() + " associado ao evento com id " + idEvento + " com sucesso!", 
            HttpStatus.CREATED
        );
    }

    @Operation(summary = "Remover um Ingresso de Evento", description = "Desvincula e exclui o Ingresso com id especificado do Evento com id especificado")
    @PatchMapping("/{idEvento}/removerIngresso/{idIngresso}")
    public ResponseEntity<String> removerIngresso (@PathVariable long idEvento, @PathVariable long idIngresso) {
        eventoService.removerIngresso(idEvento, idIngresso);

        return new ResponseEntity<String>(
            "Ingresso com id " + idIngresso + " removido do evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    //Pontos de venda
    @Operation(summary = "Adicionar um Ponto de Venda Existente a Evento", description = "Adiciona um Ponto de Venda previamente criado ao Evento com id especificado")
    @PatchMapping("/{idEvento}/addPontoVenda/{idPontoVenda}")
    public ResponseEntity<String> adicionarPontoVendaExistente (@PathVariable long idEvento, @PathVariable long idPontoVenda) {
        eventoService.adicionarPontoVendaExistente(idEvento, idPontoVenda);

        return new ResponseEntity<String>(
            "Ponto de venda com id " + idPontoVenda + " adicionado ao evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    @Operation(summary = "Adicionar um Ponto de Venda Novo a Evento", description = "Cria um novo Ponto de Venda, e o vincula ao Evento com id especificado")
    @PatchMapping("/{idEvento}/addPontoVenda")
    public ResponseEntity<String> adicionarPontoVendaNovo (@PathVariable long idEvento, @RequestBody @Valid PontoVendaRequest dto) {
        eventoService.adicionarPontoVendaNovo(idEvento, dto.paraEntidade());

        return new ResponseEntity<String>(
            "Ponto de venda " + dto.nome() + " adicionado ao evento com id " + idEvento + " com sucesso!",
            HttpStatus.CREATED
        );
    }

    @Operation(summary = "Remover um Ponto de Venda de um Evento", description = "Desvincula o Ponto de Venda com id especificado do Evento com id especificado")
    @PatchMapping("/{idEvento}/removerPontoVenda/{idPontoVenda}")
    public ResponseEntity<String> removerPontoVenda (@PathVariable long idEvento, @PathVariable long idPontoVenda) {
        eventoService.removerPontoVenda(idEvento, idPontoVenda);

        return new ResponseEntity<String>(
            "Ponto de venda com id " + idPontoVenda + " removido do evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    //Avaliações
    @Operation(summary = "Listar avaliações de um Evento", description = "Retorna as avaliações vinculadas ao Evento com id especificado, com paginação")
    @GetMapping("/{idEvento}/avaliacoes")
    public ResponseEntity<Page<AvaliacaoResponse>> listarAvaliacoes(@PathVariable long idEvento, Pageable pageable) {
        return new ResponseEntity<Page<AvaliacaoResponse>>(
            eventoService.listarAvaliacoes(idEvento, pageable),
            HttpStatus.OK
        );
    }

    @Operation(summary = "Adicionar Avaliação a Evento", description = "Cria uma nova Avaliação e a vincula ao Evento com id especificado")
    @PatchMapping("/{idEvento}/addAvaliacao")
    public ResponseEntity<String> adicionarAvaliacao(@PathVariable long idEvento, @RequestBody @Valid AvaliacaoRequest dto, HttpServletRequest request) {
        Usuario usuario = authSessionService.obterUsuarioAutenticado(request);
        Avaliacao avaliacao = dto.paraEntidade();
        avaliacao.setUsuarioId(usuario.getId());
        avaliacao.setNomeUsuario(usuario.getNome());

        eventoService.adicionarAvaliacao(idEvento, avaliacao);

        return new ResponseEntity<String>(
            "Avaliação adicionada ao evento com id " + idEvento + " com sucesso!",
            HttpStatus.CREATED
        );
    }

    @Operation(summary = "Buscar avaliação do usuário autenticado", description = "Retorna a avaliação do usuário autenticado vinculada ao Evento com id especificado")
    @GetMapping("/{idEvento}/minha-avaliacao")
    public ResponseEntity<AvaliacaoResponse> buscarMinhaAvaliacao(@PathVariable long idEvento, HttpServletRequest request) {
        Usuario usuario = authSessionService.obterUsuarioAutenticado(request);

        return new ResponseEntity<AvaliacaoResponse>(
            eventoService.buscarAvaliacaoUsuario(idEvento, usuario.getId()),
            HttpStatus.OK
        );
    }

    @Operation(summary = "Editar avaliação do usuário autenticado", description = "Atualiza a avaliação do usuário autenticado vinculada ao Evento com id especificado")
    @PutMapping("/{idEvento}/minha-avaliacao")
    public ResponseEntity<AvaliacaoResponse> editarMinhaAvaliacao(@PathVariable long idEvento, @RequestBody @Valid AvaliacaoRequest dto, HttpServletRequest request) {
        Usuario usuario = authSessionService.obterUsuarioAutenticado(request);
        Avaliacao avaliacao = dto.paraEntidade();
        avaliacao.setUsuarioId(usuario.getId());
        avaliacao.setNomeUsuario(usuario.getNome());

        return new ResponseEntity<AvaliacaoResponse>(
            eventoService.editarAvaliacaoUsuario(idEvento, usuario.getId(), avaliacao),
            HttpStatus.OK
        );
    }

    @Operation(summary = "Remover avaliação do usuário autenticado", description = "Remove a avaliação do usuário autenticado vinculada ao Evento com id especificado")
    @DeleteMapping("/{idEvento}/minha-avaliacao")
    public ResponseEntity<String> removerMinhaAvaliacao(@PathVariable long idEvento, HttpServletRequest request) {
        Usuario usuario = authSessionService.obterUsuarioAutenticado(request);
        eventoService.removerAvaliacaoUsuario(idEvento, usuario.getId());

        return new ResponseEntity<String>(
            "Avaliação do usuário autenticado removida do evento com id " + idEvento + " com sucesso!",
            HttpStatus.OK
        );
    }

    @Operation(summary = "Remover Avaliação de Evento", description = "Desvincula a Avaliação com id especificado do Evento com id especificado")
    @PatchMapping("/{idEvento}/removerAvaliacao/{idAvaliacao}")
    public ResponseEntity<String> removerAvaliacao (@PathVariable long idEvento, @PathVariable long idAvaliacao) {
        eventoService.removerAvaliacao(idEvento, idAvaliacao);

        return new ResponseEntity<String>(
            "Avaliação com id " + idAvaliacao + " removida do evento com id " + idEvento + " com sucesso!",
            HttpStatus.OK
        );
    }

    //Sub-Eventos
    @Operation(summary = "Criar novo Sub-Evento Simples", description = "Cria um sub-evento simples e o vincula ao EventoComposto com id especificado")
    @PatchMapping("/{idEvento}/addSubeventoSimples")
    public ResponseEntity<String> criarSubEventoSimples (@PathVariable long idEvento, @RequestBody @Valid EventoRequest dto) {
        eventoService.criarSubevento(idEvento, dto.paraEntidade(true));
        
        return new ResponseEntity<String>(
            "Sub-evento simples " + dto.nome() + " criado com sucesso!", 
            HttpStatus.CREATED
        );
    }

    @Operation(summary = "Criar novo Sub-Evento Composto", description = "Cria um sub-evento composto e o vincula ao EventoComposto com id especificado")
    @PatchMapping("/{idEvento}/addSubeventoComposto")
    public ResponseEntity<String> criarSubEventoComposto (@PathVariable long idEvento, @RequestBody @Valid EventoRequest dto) {
        eventoService.criarSubevento(idEvento, dto.paraEntidade(false));
        
        return new ResponseEntity<String>(
            "Sub-evento composto " + dto.nome() + " criado com sucesso!", 
            HttpStatus.CREATED
        );
    }

    @Operation(summary = "Listar todos os Sub-Eventos de um EventoComposto", description = "Retorna todos os sub-eventos vinculados ao EventoComposto com id especificado")
    @GetMapping("/{idEvento}/subeventos")
    public ResponseEntity<Page<EventoResponse>> listarSubeventos (@PathVariable long idEvento, Pageable pageable) {
        return new ResponseEntity<Page<EventoResponse>>(
            eventoService.listarSubeventos(idEvento, pageable), 
            HttpStatus.OK
        );
    }
   
    @Operation(summary = "Deletar Sub-Evento de um EventoComposto", description = "Desvincula e exclui o sub-evento com id especificado do EventoComposto com id especificado")
    @PatchMapping("/{idEvento}/removerSubevento/{idSubevento}")
    public ResponseEntity<String> removerSubEvento (@PathVariable long idEvento, @PathVariable long idSubevento) {
        eventoService.removerSubevento(idEvento, idSubevento);
        
        return new ResponseEntity<String>(
            "Sub-evento com id " + idSubevento + " removido com sucesso!", 
            HttpStatus.OK
        );
    }
}
