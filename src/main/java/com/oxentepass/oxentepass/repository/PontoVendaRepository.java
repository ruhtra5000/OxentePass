package com.oxentepass.oxentepass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.PontoVenda;
import com.oxentepass.oxentepass.entity.QPontoVenda;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface PontoVendaRepository extends
        JpaRepository<PontoVenda, Long>,
        QuerydslPredicateExecutor<PontoVenda>,
        QuerydslBinderCustomizer<QPontoVenda> {

    @Override
    default void customize(QuerydslBindings bindings, QPontoVenda root) {

        bindings.bind(root.nome).first((StringPath path, String value) -> path.containsIgnoreCase(value));

        bindings.bind(root.detalhes).first((StringPath path, String value) -> path.containsIgnoreCase(value));

        bindings.bind(root.endereco.cep)
                .as("cep")
                .first((StringPath path, String value) -> {
                    String onlyDigitsCep = value.replaceAll("\\D", "");
                    return path.eq(onlyDigitsCep);
                });

        bindings.bind(root.endereco.bairro)
                .as("bairro")
                .first((StringPath path, String value) -> path.containsIgnoreCase(value));

        bindings.bind(root.endereco.rua)
                .as("rua")
                .first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }

    boolean existsByNomeAndEnderecoCepAndEnderecoNumero(String nome, String cep, int numero);

}
