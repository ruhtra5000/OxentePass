package com.oxentepass.oxentepass.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.Ingresso;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Long> { 

    @Query("select i from Evento e join e.ingressos i where e.id = :idEvento")
    Page<Ingresso> findByEventoId(@Param("idEvento") Long idEvento, Pageable pageable);

    public Optional<Ingresso> findByTipo(String tipo);	
    
    
}
