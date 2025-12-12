package com.oxentepass.oxentepass.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.Cidade;
import com.oxentepass.oxentepass.entity.QCidade;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long>,
                                          QuerydslPredicateExecutor<Cidade>,
                                          QuerydslBinderCustomizer<QCidade> {
                                        
    Optional<Cidade> findByNome(String nome); // <- Correspondencia exata de nome
                                            
    @Override
    default void customize(QuerydslBindings bindings, QCidade root) {
        // Bind para ID
        bindings.bind(root.id).first((NumberPath<Long> path, Long value) -> path.eq(value));

        // Bind para nome
        bindings.bind(root.nome).first((StringPath path, String value) -> path.containsIgnoreCase(value));

        // Bind para qualquer conjunto de tags
        bindings.bind(root.tags.any().tag)
                .as("tag")
                .all((StringPath path, Collection<? extends String> values) -> {
                    if (values == null || values.isEmpty()) 
                        return Optional.empty();

                    BooleanExpression expr = null;
                    for (String v : values) {
                        // para cada valor, exigimos que exista uma tag com esse nome
                        BooleanExpression single = root.tags.any().tag.equalsIgnoreCase(v);
                        expr = (expr == null) ? single : expr.and(single);
                    }
                    return Optional.of(expr);
                });
    }
}
