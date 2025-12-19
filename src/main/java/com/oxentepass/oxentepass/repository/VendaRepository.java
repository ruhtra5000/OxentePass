package com.oxentepass.oxentepass.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.QVenda;
import com.oxentepass.oxentepass.entity.Venda;
import com.querydsl.core.types.dsl.NumberPath;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long>, QuerydslPredicateExecutor<Venda>, QuerydslBinderCustomizer<QVenda> {

    Page<Venda> findByUsuarioId(Long idUsuario, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QVenda root) {

        bindings.bind(root.id).first((path, value) -> path.eq(value));

        bindings.bind(root.usuario.id).first((NumberPath<Long> path, Long value) -> path.eq(value));

        bindings.bind(root.status).first((path, value) -> path.eq(value));

        bindings.bind(root.valorTotal).first((path, value) -> path.goe(value));

        bindings.bind(root.dataHoraVenda).first((path, value) -> path.after(value));
        
    }
}
