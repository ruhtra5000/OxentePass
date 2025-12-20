package com.oxentepass.oxentepass.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oxentepass.oxentepass.controller.request.OrganizadorRequest;
import com.oxentepass.oxentepass.entity.Organizador;

public interface OrganizadorService {

    public void promoverUsuario(OrganizadorRequest dados);

    public void editarOrganizador(long id, Organizador dados);

    public Page<Organizador> listarOrganizadores(Pageable pageable);

}