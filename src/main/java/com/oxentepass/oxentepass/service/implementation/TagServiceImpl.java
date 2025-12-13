package com.oxentepass.oxentepass.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oxentepass.oxentepass.entity.Tag;
import com.oxentepass.oxentepass.exceptions.RecursoDuplicadoException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.TagRepository;
import com.oxentepass.oxentepass.service.TagService;
import com.querydsl.core.types.Predicate;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public void criarTag(Tag tag) {
        Optional<Tag> tagBusca = tagRepository.findByTag(tag.getTag());

        if (tagBusca.isPresent())
            throw new RecursoDuplicadoException("A tag informada já existe.");

        tagRepository.save(tag);
    }

    @Override
    public Page<Tag> listarTags(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Page<Tag> listarTagsFiltro(Predicate predicate, Pageable pageable) {
        return tagRepository.findAll(predicate, pageable);
    }

    @Override
    public void deletarTag(long id) {
        if(!tagRepository.existsById(id))
            throw new RecursoNaoEncontradoException("A tag informada não existe.");

        tagRepository.deleteById(id);
    }
    
}
