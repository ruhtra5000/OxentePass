package com.oxentepass.oxentepass.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oxentepass.oxentepass.entity.Pagamento;
import com.querydsl.core.types.Predicate;

public interface PagamentoService {

    // Operações Básicas
    public void criarPagamento(Pagamento pagamento);
    public Page<Pagamento> listarTodosPagamentos(Pageable pageable);
    public Page<Pagamento> filtrarPagamentos(Predicate predicate, Pageable pageable);
    public Pagamento buscarPagamentoPorId(Long id);

    // Modificação de status
    public Pagamento confirmarPagamento(Long id);
    public Pagamento cancelarPagamento(Long id);
    public Pagamento estornarPagamento(Long id);
    public Pagamento recusarPagamento(Long id);

}
