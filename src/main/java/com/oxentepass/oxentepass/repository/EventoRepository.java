package com.oxentepass.oxentepass.repository;

import java.time.LocalDateTime;
import java.util.Collection;
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

import com.oxentepass.oxentepass.entity.Evento;
import com.oxentepass.oxentepass.entity.QEvento;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>, 
                                          QuerydslPredicateExecutor<Evento>,
                                          QuerydslBinderCustomizer<QEvento> {
    /* 
    Query no padrão SQL:
       SELECT e.* 
       FROM evento as e 
       JOIN evento_composto_subeventos as ecs
       ON e.id = ecs.subeventos_id AND ecs.evento_composto_id = :id
    */
    @Query("select s from EventoComposto ec join ec.subeventos s where ec.id = :id")
    Page<Evento> findSubeventosByParentId(@Param("id") long id, Pageable pageable);
                                            
    @Override
    default void customize(QuerydslBindings bindings, QEvento root) {
        // Bind para ID
        bindings.bind(root.id).first((NumberPath<Long> path, Long value) -> path.eq(value));

        // Bind para nome
        bindings.bind(root.nome).first((StringPath path, String value) -> path.containsIgnoreCase(value));

        // Binds para datas
        bindings.bind(root.dataHoraInicio)
                .as("naoIniciados") //Eventos que ainda não começaram
                .first((path, value) -> {
                    LocalDateTime agora = LocalDateTime.now();
                    return root.dataHoraInicio.after(agora);
                });
        
        bindings.bind(root.dataHoraInicio)
                .as("ativos") //Eventos ocorrendo
                .first((path, value) -> {
                    LocalDateTime agora = LocalDateTime.now();
                    return root.dataHoraInicio.before(agora)
                         .and(root.dataHoraFim.after(agora));
                });

        bindings.bind(root.dataHoraFim)
                .as("finalizados") //Eventos que terminaram
                .first((path, value) -> {
                    LocalDateTime agora = LocalDateTime.now();
                    return root.dataHoraFim.before(agora);
                });

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
        
        // Bind para organizador (seleção por ID do organizador)
        bindings.bind(root.organizador.id)
                .as("organizador")
                .first((NumberPath<Long> path, Long value) -> path.eq(value));
    }

}
