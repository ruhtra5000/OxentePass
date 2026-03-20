package com.oxentepass.oxentepass.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oxentepass.oxentepass.entity.Cidade;
import com.oxentepass.oxentepass.service.outrasInterfaces.TagManipulacao;
import com.querydsl.core.types.Predicate;

public interface CidadeService extends TagManipulacao {
    // Operações básicas
    public Cidade criarCidade(Cidade cidade);
    public Page<Cidade> listarCidades(Pageable pageable);
    public Page<Cidade> listarCidadesFiltro(Predicate predicate, Pageable pageable);
    public void editarCidade(long idCidade, Cidade cidade);
    public void deletarCidade(long idCidade);
    // Tags -> TagManipulacao
} 