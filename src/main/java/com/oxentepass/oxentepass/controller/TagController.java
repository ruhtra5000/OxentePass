package com.oxentepass.oxentepass.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oxentepass.oxentepass.controller.request.TagRequest;
import com.oxentepass.oxentepass.entity.Tag;
import com.oxentepass.oxentepass.service.TagService;
import com.querydsl.core.types.Predicate;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Arthur de Sá
 * Controller para manipular Tag, através de TagService
 */

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @Operation(summary = "Criar Tag", description = "Cria uma nova Tag")
    @PostMapping
    public ResponseEntity<String> criarTag(@RequestBody @Valid TagRequest dto) { 
        tagService.criarTag(dto.paraEntidade());        
        return new ResponseEntity<String>("Tag criada com sucesso", HttpStatus.CREATED);
    }

    @Operation(summary = "Listar Tags", description = "Retorna todas as Tags c/ paginação")
    @GetMapping
    public ResponseEntity<Page<Tag>> listarTodasTags (Pageable pageable) {
        return new ResponseEntity<Page<Tag>>(tagService.listarTags(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Listar Tags com filtro", description = "Filtra as Tags c/ QueryDSL e paginação")
    @GetMapping("/filtro")
    public ResponseEntity<Page<Tag>> listarTagFiltro (@QuerydslPredicate(root = Tag.class) Predicate predicate, Pageable pageable) {
        return new ResponseEntity<Page<Tag>>(tagService.listarTagsFiltro(predicate, pageable), HttpStatus.OK);
    }
    
    @Operation(summary = "Deletar Tag", description = "Deleta a Tag com id especificado")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarTag (@PathVariable long id) {
        tagService.deletarTag(id);
        return new ResponseEntity<String>("Tag deletada com sucesso", HttpStatus.OK);
    }

}
