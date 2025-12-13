package com.oxentepass.oxentepass.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    public Page<Venda> findByUsuarioId(Long idUsuario, Pageable pageable);

}
