package com.oxentepass.oxentepass.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oxentepass.oxentepass.entity.Tag;
import com.oxentepass.oxentepass.service.TagService;
import com.querydsl.core.types.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;
    
    //FALTA ADICIONAR OS DTOs

    @PostMapping
    public String criarTag(@RequestBody Tag tag) { 
        tagService.criarTag(tag);        
        return "Tag criada";
    }

    @GetMapping
    public Page<Tag> listarTodasTags (Pageable pageable) {
        return tagService.listarTags(pageable);
    }

    @GetMapping("/filtro")
    public Page<Tag> listarTagFiltro (@QuerydslPredicate(root = Tag.class) Predicate predicate, Pageable pageable) {
        return tagService.listarTagsFiltro(predicate, pageable);
    }
    
    @DeleteMapping("/{id}")
    public String deletarTag (@PathVariable long id) {
        tagService.deletarTag(id);
        return "Tag deletada";
    }

}
