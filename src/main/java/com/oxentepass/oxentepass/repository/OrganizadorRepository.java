package com.oxentepass.oxentepass.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oxentepass.oxentepass.entity.Organizador;

@Repository
public interface OrganizadorRepository extends JpaRepository<Organizador, Long> {


}