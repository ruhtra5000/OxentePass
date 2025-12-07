package com.oxentepass.oxentepass.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.Ingresso;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Long> { 

    public Optional<Ingresso> findByTipo(String tipo);
    public Optional<Ingresso> findByEventoId(Long idEvento);
    
}
