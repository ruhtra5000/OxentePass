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

    // Busca ingressos por ID do evento
    @Query("select i from Evento e join e.ingressos i where e.id = :idEvento")
    Page<Ingresso> findByEventoId(@Param("idEvento") Long idEvento, Pageable pageable);

    // Busca ingresso por tipo
    public Optional<Ingresso> findByTipo(String tipo);	

    @Override
    default void customize(QuerydslBindings bindings, QIngresso root) {

        // Bind para id
        bindings.bind(root.id).first((NumberPath<Long> path, Long value) -> path.eq(value));

        // Bind para tipo
        bindings.bind(root.tipo).first((StringPath path, String value) -> path.containsIgnoreCase(value));

        // Bind para valorBase
        bindings.bind(root.valorBase).as("ValorIgual").first((path, value) -> path.eq(value));

        // Bind para ValorMenor
        bindings.bind(root.valorBase).as("ValorMenor").first((path, value) -> path.loe(value));

        // Bind para ValorMaior
        bindings.bind(root.valorBase).as("ValorMaior").first((path, value) -> path.goe(value));

        // Binds para quantidadeDisponivel
        bindings.bind(root.quantidadeDisponivel).first((path, value) -> path.goe(value));

        // Bind para temMeiaEntrada
        bindings.bind(root.temMeiaEntrada).first((path, value) -> path.eq(value));
        
    }
      
}
