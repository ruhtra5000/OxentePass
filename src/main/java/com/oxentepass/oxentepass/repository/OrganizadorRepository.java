package com.oxentepass.oxentepass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.Organizador;
import com.oxentepass.oxentepass.entity.QOrganizador;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface OrganizadorRepository extends
        JpaRepository<Organizador, Long>,
        QuerydslPredicateExecutor<Organizador>,
        QuerydslBinderCustomizer<QOrganizador> {

    @Override
    default void customize(QuerydslBindings bindings, QOrganizador root) {

        // Bindings de usuário comum
        bindings.excluding(root.senha);
        bindings.bind(root.id).first((NumberPath<Long> path, Long value) -> path.eq(value));
        bindings.bind(root.nome).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.bind(root.cpf).first((StringPath path, String value) -> {
            String onlyDigitsCPF = value.replaceAll("\\D", "");
            return path.eq(onlyDigitsCPF);
        });
        bindings.bind(root.email).first((StringPath path, String value) -> path.equalsIgnoreCase(value));

        // Bindings de organizador
        bindings.bind(root.cnpj).first((StringPath path, String value) -> {
            String onlyDigitsCNPJ = value.replaceAll("\\D", "");
            return path.eq(onlyDigitsCNPJ);
        });

        bindings.bind(root.telefone).first((StringPath path, String value) -> {
            String onlyDigitsTelefone = value.replaceAll("\\D", "");
            return path.eq(onlyDigitsTelefone);
        });

        bindings.bind(root.biografia).first((StringPath path, String value) -> path.containsIgnoreCase(value));

        bindings.bind(root.notaReputacao).first((NumberPath<Double> path, Double value) -> path.eq(value));

    };

}