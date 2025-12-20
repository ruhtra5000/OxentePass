package com.oxentepass.oxentepass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.IngressoVenda;

@Repository
public interface IngressoVendaRepository extends JpaRepository<IngressoVenda, Long> {
    
}
