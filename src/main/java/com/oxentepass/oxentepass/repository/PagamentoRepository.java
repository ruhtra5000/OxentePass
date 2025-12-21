package com.oxentepass.oxentepass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.oxentepass.oxentepass.entity.QPagamento;
import com.querydsl.core.types.dsl.NumberPath;
import com.oxentepass.oxentepass.entity.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long>, QuerydslPredicateExecutor<Pagamento>, QuerydslBinderCustomizer<QPagamento> {
    
    @Override
    default void customize(QuerydslBindings bindings, QPagamento root) {

        bindings.bind(root.id).first((NumberPath<Long> path, Long value) -> path.eq(value));

        bindings.bind(root.metodo).first((path, value) -> path.eq(value));

        bindings.bind(root.status).first((path, value) -> path.eq(value));

        bindings.bind(root.valor).as("ValorIgual").first((path, value) -> path.eq(value));

        bindings.bind(root.valor).as("ValorMenor").first((path, value) -> path.loe(value));

        bindings.bind(root.valor).as("ValorMaior").first((path, value) -> path.goe(value));

        bindings.bind(root.dataPagamento).as("dataInicio").first((path, value) -> path.goe(value));

        bindings.bind(root.dataPagamento).as("dataFim").first((path, value) -> path.loe(value));

    }

}