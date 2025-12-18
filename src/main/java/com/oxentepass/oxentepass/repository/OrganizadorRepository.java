package com.oxentepass.oxentepass.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.Organizador;
import com.oxentepass.oxentepass.entity.QOrganizador;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface OrganizadorRepository extends
        JpaRepository<Organizador, Long>,
        QuerydslPredicateExecutor<Organizador>,
        QuerydslBinderCustomizer<QOrganizador> {

    @Override
    default void customize(QuerydslBindings bindings, QOrganizador root) {

        bindings.bind(root.cnpj).first((StringPath path, String value) -> {
            String digitsOnly = value.replaceAll("\\D", "");
            return path.eq(digitsOnly);
        });

        bindings.bind(root.biografia).first((StringPath path, String value) -> path.containsIgnoreCase(value));

        bindings.bind(root.telefone).first((StringPath path, String value) -> {
            String onlyDigitsTelefone = value.replaceAll("\\D", "");
            return path.containsIgnoreCase(onlyDigitsTelefone);
        });
    }

    Optional<Organizador> findByCnpj(String cnpj);
}