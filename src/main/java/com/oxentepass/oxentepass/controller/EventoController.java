package com.oxentepass.oxentepass.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oxentepass.oxentepass.controller.request.EventoRequest;
import com.oxentepass.oxentepass.controller.request.IngressoRequest;
import com.oxentepass.oxentepass.controller.request.PontoVendaRequest;
import com.oxentepass.oxentepass.controller.request.TagRequest;
import com.oxentepass.oxentepass.controller.response.EventoResponse;
import com.oxentepass.oxentepass.entity.Evento;
import com.oxentepass.oxentepass.service.EventoService;
import com.querydsl.core.types.Predicate;

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


@RestController
@RequestMapping("/evento")
public class EventoController {

    @Autowired
    private EventoService eventoService;
    
    //Operações Básicas
    @PostMapping("/simples")
    public ResponseEntity<String> criarEventoSimples (@RequestBody @Valid EventoRequest dto) {
        eventoService.criarEvento(dto.paraEntidade(true));
        
        return new ResponseEntity<String>(
            "Evento simples " + dto.nome() + " criado com sucesso!", 
            HttpStatus.CREATED
        );
    }

    @PostMapping("/composto")
    public ResponseEntity<String> criarEventoComposto (@RequestBody @Valid EventoRequest dto) {
        eventoService.criarEvento(dto.paraEntidade(false));
        
        return new ResponseEntity<String>(
            "Evento composto " + dto.nome() + " criado com sucesso!", 
            HttpStatus.CREATED
        );
    }
    
    @GetMapping
    public ResponseEntity<Page<EventoResponse>> listarTodosEventos (Pageable pageable) {
        return new ResponseEntity<Page<EventoResponse>>(
            eventoService.listarEventos(pageable), 
            HttpStatus.OK
        );
    }
    
    @GetMapping("/filtro")
    public ResponseEntity<Page<EventoResponse>> listarEventosFiltro (@QuerydslPredicate(root = Evento.class) Predicate predicate, Pageable pageable) {
        return new ResponseEntity<Page<EventoResponse>>(
            eventoService.listarEventosFiltro(predicate, pageable), 
            HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editarEvento (@PathVariable long id, @RequestBody @Valid EventoRequest dto) {
        eventoService.editarEvento(id, dto.paraEntidade(true));  

        return new ResponseEntity<String>(
            "Evento " + dto.nome() + " editado com sucesso!", 
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarEvento (@PathVariable long id) {
        eventoService.deletarEvento(id); 

        return new ResponseEntity<String>(
            "Evento com id " + id + " deletado com sucesso!", 
            HttpStatus.OK
        );
    }

    //Tags
    @PatchMapping("/{idEvento}/addTag/{idTag}")
    public ResponseEntity<String> adicionarTagExistente (@PathVariable long idEvento, @PathVariable long idTag) {
        eventoService.adicionarTagExistente(idEvento, idTag);

        return new ResponseEntity<String>(
            "Tag com id " + idTag + " adicionada ao evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    @PatchMapping("/{idEvento}/addTag")
    public ResponseEntity<String> adicionarTagNova (@PathVariable long idEvento, @RequestBody TagRequest dto) {
        eventoService.adicionarTagNova(idEvento, dto.paraEntidade());

        return new ResponseEntity<String>(
            "Tag " + dto.tag() + " adicionada ao evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    @PatchMapping("/{idEvento}/removerTag/{idTag}")
    public ResponseEntity<String> removerTag (@PathVariable long idEvento, @PathVariable long idTag) {
        eventoService.removerTag(idEvento, idTag);

        return new ResponseEntity<String>(
            "Tag com id " + idTag + " removida do evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    //Ingressos
    @PatchMapping("/{idEvento}/addIngresso")
    public ResponseEntity<String> adicionarIngresso (@PathVariable long idEvento, @RequestBody @Valid IngressoRequest dto) {
        eventoService.adicionarIngresso(idEvento, dto.paraEntidade());

        return new ResponseEntity<String>(
            "Ingresso " + dto.tipoIngresso() + " associado ao evento com id " + idEvento + " com sucesso!", 
            HttpStatus.CREATED
        );
    }

    @PatchMapping("/{idEvento}/removerIngresso/{idIngresso}")
    public ResponseEntity<String> removerIngresso (@PathVariable long idEvento, @PathVariable long idIngresso) {
        eventoService.removerIngresso(idEvento, idIngresso);

        return new ResponseEntity<String>(
            "Ingresso com id " + idIngresso + " removido do evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    //Pontos de venda
    @PatchMapping("/{idEvento}/addPontoVenda/{idPontoVenda}")
    public ResponseEntity<String> adicionarPontoVendaExistente (@PathVariable long idEvento, @PathVariable long idPontoVenda) {
        eventoService.adicionarPontoVendaExistente(idEvento, idPontoVenda);

        return new ResponseEntity<String>(
            "Ponto de venda com id " + idPontoVenda + " adicionado ao evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    @PatchMapping("/{idEvento}/addPontoVenda")
    public ResponseEntity<String> adicionarPontoVendaNovo (@PathVariable long idEvento, @RequestBody @Valid PontoVendaRequest dto) {
        eventoService.adicionarPontoVendaNovo(idEvento, dto.paraEntidade());

        return new ResponseEntity<String>(
            "Ponto de venda " + dto.nome() + " adicionado ao evento com id " + idEvento + " com sucesso!",
            HttpStatus.CREATED
        );
    }

    @PatchMapping("/{idEvento}/removerPontoVenda/{idPontoVenda}")
    public ResponseEntity<String> removerPontoVenda (@PathVariable long idEvento, @PathVariable long idPontoVenda) {
        eventoService.removerPontoVenda(idEvento, idPontoVenda);

        return new ResponseEntity<String>(
            "Ponto de venda com id " + idPontoVenda + " removido do evento com id " + idEvento + " com sucesso!", 
            HttpStatus.OK
        );
    }

    //Avaliações
    @PatchMapping("/{idEvento}/addAvaliacao") //Adicionar DTO de entrada
    public ResponseEntity<String> adicionarAvaliacao (@PathVariable long idEvento) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adicionarAvaliacao'");
    }

    @PatchMapping("/{idEvento}/removerAvaliacao") //Adicionar DTO de entrada
    public ResponseEntity<String> removerAvaliacao (@PathVariable long idEvento) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removerAvaliacao'");
    }

    //Sub-Eventos
    @PatchMapping("/{idEvento}/addSubeventoSimples")
    public ResponseEntity<String> criarSubEventoSimples (@PathVariable long idEvento, @RequestBody @Valid EventoRequest dto) {
        eventoService.criarSubevento(idEvento, dto.paraEntidade(true));
        
        return new ResponseEntity<String>(
            "Sub-evento simples " + dto.nome() + " criado com sucesso!", 
            HttpStatus.CREATED
        );
    }

    @PatchMapping("/{idEvento}/addSubeventoComposto")
    public ResponseEntity<String> criarSubEventoComposto (@PathVariable long idEvento, @RequestBody @Valid EventoRequest dto) {
        eventoService.criarSubevento(idEvento, dto.paraEntidade(false));
        
        return new ResponseEntity<String>(
            "Sub-evento composto " + dto.nome() + " criado com sucesso!", 
            HttpStatus.CREATED
        );
    }

    @GetMapping("/{idEvento}/subeventos")
    public ResponseEntity<Page<EventoResponse>> listarSubeventos (@PathVariable long idEvento, Pageable pageable) {
        return new ResponseEntity<Page<EventoResponse>>(
            eventoService.listarSubeventos(idEvento, pageable), 
            HttpStatus.OK
        );
    }
   
    @PatchMapping("/{idEvento}/removerSubevento/{idSubevento}")
    public ResponseEntity<String> removerSubEvento (@PathVariable long idEvento, @PathVariable long idSubevento) {
        eventoService.removerSubevento(idEvento, idSubevento);
        
        return new ResponseEntity<String>(
            "Sub-evento com id " + idSubevento + " removido com sucesso!", 
            HttpStatus.OK
        );
    }
}
