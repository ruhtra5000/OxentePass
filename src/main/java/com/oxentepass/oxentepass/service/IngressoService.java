package com.oxentepass.oxentepass.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oxentepass.oxentepass.entity.Ingresso;
import com.querydsl.core.types.Predicate;

public interface IngressoService {
    // Operações Básicas
    public void cadastrarIngresso(Ingresso ingresso);
    public void deletarIngresso(Long id);
    public Page<Ingresso> listarTodosIngressos(Predicate predicate, Pageable pageable);
    public Ingresso buscarIngressoPorId(Long id);
    public Ingresso buscarIngressPorTipo(String tipo);
    public Page<Ingresso> ingressosDisponiveis(Long idEvento, Predicate predicate, Pageable pageable);
}