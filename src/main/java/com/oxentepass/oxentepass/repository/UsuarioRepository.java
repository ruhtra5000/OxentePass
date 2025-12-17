package com.oxentepass.oxentepass.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.QUsuario;
import com.oxentepass.oxentepass.entity.Usuario;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface UsuarioRepository extends
        JpaRepository<Usuario, Long>,
        QuerydslPredicateExecutor<Usuario>,
        QuerydslBinderCustomizer<QUsuario> {

    @Override
    default void customize(QuerydslBindings bindings, QUsuario root) {

        bindings.excluding(root.senha);

        bindings.bind(root.id).first((NumberPath<Long> path, Long value) -> path.eq(value));

        bindings.bind(root.nome).first((StringPath path, String value) -> path.containsIgnoreCase(value));

        bindings.bind(root.cpf).first((StringPath path, String value) -> {
            String onlyDigitsCPF = value.replaceAll("\\D", "");
            return path.eq(onlyDigitsCPF);
        });

        bindings.bind(root.email).first((StringPath path, String value) -> path.equalsIgnoreCase(value));

    };

    List<Usuario> findAll();

    Optional<Usuario> findByCpf(String cpf);

    // TODO (Guilherme): remover?
    // Optional<Usuario> findByEmail(String email);

}
