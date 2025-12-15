package com.oxentepass.oxentepass.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oxentepass.oxentepass.entity.Cidade;
import com.oxentepass.oxentepass.entity.Tag;
import com.oxentepass.oxentepass.exceptions.RecursoDuplicadoException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.CidadeRepository;
import com.oxentepass.oxentepass.repository.TagRepository;
import com.oxentepass.oxentepass.service.CidadeService;
import com.oxentepass.oxentepass.service.TagService;
import com.querydsl.core.types.Predicate;

@Service
public class CidadeServiceImpl implements CidadeService {
    // Repositórios
    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private TagRepository tagRepository;

    // Outros Services
    @Autowired
    private TagService tagService;

    // Métodos auxiliares
    private Cidade buscarCidadePorId(long id) {
        Optional<Cidade> cidadeBusca = cidadeRepository.findById(id);

        if (cidadeBusca.isEmpty())
            throw new RecursoNaoEncontradoException("Cidade com id " + id + " não existe.");

        return cidadeBusca.get();
    }

    private void verificarCidadePorNome(Cidade cidade) {
        Optional<Cidade> cidadeBusca = cidadeRepository.findByNome(cidade.getNome());

        if (cidadeBusca.isPresent()) 
            throw new RecursoDuplicadoException("Uma cidade com o nome \"" + cidade.getNome() + "\" já se encontra cadastrada.");
    }

    // Métodos da Interface

    // Operações Básicas
    @Override
    public void criarCidade(Cidade cidade) {
        verificarCidadePorNome(cidade);

        cidadeRepository.save(cidade);
    }

    @Override
    public Page<Cidade> listarCidades(Pageable pageable) {
        return cidadeRepository.findAll(pageable);
    }

    @Override
    public Page<Cidade> listarCidadesFiltro(Predicate predicate, Pageable pageable) {
        return cidadeRepository.findAll(predicate, pageable);
    }

    @Override
    public void editarCidade(long idCidade, Cidade cidade) {
        Cidade cidadeEdicao = buscarCidadePorId(idCidade);
        
        if (!cidadeEdicao.getNome().equals(cidade.getNome()))
            verificarCidadePorNome(cidade);

        //Atributos modificados pela "edição padrão"
        cidadeEdicao.setNome(cidade.getNome());             //Nome
        cidadeEdicao.setDescricao(cidade.getDescricao());   //Descrição

        cidadeRepository.save(cidadeEdicao);
    }

    @Override
    public void deletarCidade(long idCidade) {
        Cidade cidade = buscarCidadePorId(idCidade);

        cidadeRepository.delete(cidade);
    }

    // Tags
    @Override
    public void adicionarTagExistente(long idObjBase, long idTag) {
        Cidade cidade = buscarCidadePorId(idObjBase);

        Optional<Tag> tagBusca = tagRepository.findById(idTag);
        if (tagBusca.isEmpty()) 
            throw new RecursoNaoEncontradoException("A Tag com id " + idTag + " não existe.");

        cidade.addTag(tagBusca.get());
        cidadeRepository.save(cidade);
    }

    @Override
    @Transactional
    public void adicionarTagNova(long idObjBase, Tag tag) {
        Cidade cidade = buscarCidadePorId(idObjBase);

        tagService.criarTag(tag);

        cidade.addTag(tag);
        cidadeRepository.save(cidade);
    }

    @Override
    public void removerTag(long idObjBase, long idTag) {
        Cidade cidade = buscarCidadePorId(idObjBase);

        Optional<Tag> tagBusca = tagRepository.findById(idTag);
        if (tagBusca.isEmpty()) 
            throw new RecursoNaoEncontradoException("A Tag com id " + idTag + " não existe.");

        cidade.removerTag(tagBusca.get());
        cidadeRepository.save(cidade);
    }
}
