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
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberPath;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long>, QuerydslPredicateExecutor<Venda>, QuerydslBinderCustomizer<QVenda> {

    Page<Venda> findByUsuarioId(Long idUsuario, Predicate predicate, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QVenda root) {

        // Bind para id
        bindings.bind(root.id).first((path, value) -> path.eq(value));

        // Bind para usuario.id
        bindings.bind(root.usuario.id).first((NumberPath<Long> path, Long value) -> path.eq(value));

        // Bind para status
        bindings.bind(root.status).first((path, value) -> path.eq(value));

        // Binds para valorTotal
        bindings.bind(root.valorTotal).as("ValorIgual").first((path, value) -> path.eq(value));

        // Bind para valorTotal menor que
        bindings.bind(root.valorTotal).as("ValorMenor").first((path, value) -> path.loe(value));

        // Bind para valorTotal maior que
        bindings.bind(root.valorTotal).as("ValorMaior").first((path, value) -> path.goe(value));

        // Binds para dataHoraVenda
        bindings.bind(root.dataHoraVenda).first((path, value) -> path.after(value));
        
    }
}
