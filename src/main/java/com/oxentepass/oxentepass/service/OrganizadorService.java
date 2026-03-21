package com.oxentepass.oxentepass.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oxentepass.oxentepass.controller.request.OrganizadorEdicaoRequest;
import com.oxentepass.oxentepass.controller.request.OrganizadorRequest;
import com.oxentepass.oxentepass.entity.Organizador;
import com.querydsl.core.types.Predicate;

public interface OrganizadorService {

    public void promoverUsuario(OrganizadorRequest dados);

    public Organizador buscarOrganizadorPorId(long id);

    public void editarOrganizador(long id, OrganizadorRequest dados);
    public void editarOrganizadorParcial(long id, OrganizadorEdicaoRequest dados);

    public Page<Organizador> listarOrganizadores(Pageable pageable);
    
    public Page<Organizador> listarOrganizadoresFiltro(Predicate predicate, Pageable pageable);

}
