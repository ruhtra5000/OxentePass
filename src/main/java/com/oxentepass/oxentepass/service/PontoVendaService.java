package com.oxentepass.oxentepass.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oxentepass.oxentepass.entity.PontoVenda;
import com.querydsl.core.types.Predicate;

public interface PontoVendaService {

    public void cadastrarPontoVenda(PontoVenda pontoVenda);

    public void editarPontoVenda(long id, PontoVenda pontoVenda);

    public void deletarPontoVenda(long id);

    public Page<PontoVenda> listarPontoVendas(Pageable pageable);

    public Page<PontoVenda> listarPontoVendasFiltro(Predicate predicate, Pageable pageable);

}
