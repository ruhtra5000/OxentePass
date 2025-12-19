package com.oxentepass.oxentepass.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.Ingresso;
import com.oxentepass.oxentepass.entity.QIngresso;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Long>, QuerydslPredicateExecutor<Ingresso>, QuerydslBinderCustomizer<QIngresso> { 

    @Query("select i from Evento e join e.ingressos i where e.id = :idEvento")
    Page<Ingresso> findByEventoId(@Param("idEvento") Long idEvento, Pageable pageable);

    public Optional<Ingresso> findByTipo(String tipo);	

    @Override
    default void customize(QuerydslBindings bindings, QIngresso root) {

        bindings.bind(root.id).first((NumberPath<Long> path, Long value) -> path.eq(value));

        bindings.bind(root.tipo).first((StringPath path, String value) -> path.containsIgnoreCase(value));

        bindings.bind(root.valorBase).first((path, value) -> path.eq(value));

        bindings.bind(root.quantidadeDisponivel).first((path, value) -> path.goe(value));

        bindings.bind(root.temMeiaEntrada).first((path, value) -> path.eq(value));
        
    }
      
}
