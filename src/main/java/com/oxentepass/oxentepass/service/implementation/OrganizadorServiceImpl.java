package com.oxentepass.oxentepass.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oxentepass.oxentepass.controller.request.OrganizadorRequest;
import com.oxentepass.oxentepass.entity.Organizador;
import com.oxentepass.oxentepass.entity.Usuario;
import com.oxentepass.oxentepass.exceptions.RecursoDuplicadoException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.OrganizadorRepository;
import com.oxentepass.oxentepass.repository.UsuarioRepository;
import com.oxentepass.oxentepass.service.OrganizadorService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class OrganizadorServiceImpl implements OrganizadorService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OrganizadorRepository organizadorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void promoverUsuario(OrganizadorRequest dados) {
        Usuario usuario = buscaUsuario(dados.usuarioId());

        if (organizadorRepository.existsById(usuario.getId()))
            throw new RecursoDuplicadoException("Usuário com id " + usuario.getId() + " já é um organizador.");

        // 3. Query Nativa para inserção na tabela filha (Herança JOINED)
        String sql = "INSERT INTO organizador (id, cnpj, telefone, biografia, nota_reputacao) VALUES (:id, :cnpj, :tel, :bio, 5.0)";

        entityManager.createNativeQuery(sql)
                .setParameter("id", usuario.getId())
                .setParameter("cnpj", dados.cnpj())
                .setParameter("tel", dados.telefone())
                .setParameter("bio", dados.biografia())
                .executeUpdate();
    }

    @Override
    public void editarOrganizador(long id, OrganizadorRequest dados) {
        Organizador organizador = buscaOrganizador(id);
        
        organizador.setCnpj(dados.cnpj());
        organizador.setTelefone(dados.telefone());
        organizador.setBiografia(dados.biografia());

        organizadorRepository.save(organizador);
    }

    @Override
    public Page<Organizador> listarOrganizadores(Pageable pageable) {
        return organizadorRepository.findAll(pageable);
    }

    // Métodos auxiliares
    private Organizador buscaOrganizador(long id) {
        return organizadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Organizador com id " + id + " não encontrado."));
    }

    private Usuario buscaUsuario(long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário com id " + id + " não encontrado."));
    }
}