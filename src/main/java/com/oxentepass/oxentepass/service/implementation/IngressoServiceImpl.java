package com.oxentepass.oxentepass.service.implementation;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oxentepass.oxentepass.entity.Ingresso;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.IngressoRepository;
import com.oxentepass.oxentepass.service.IngressoService;
import com.querydsl.core.types.Predicate;

@Service
public class IngressoServiceImpl implements IngressoService {

    // Repositórios
    @Autowired
    private IngressoRepository ingressoRepository;

    // Métodos

    // Cadastra um novo ingresso
    @Override
    public void cadastrarIngresso(Ingresso ingresso) {
        ingressoRepository.save(ingresso);
    }

    // Deleta um ingresso existente
    @Override
    public void deletarIngresso(Long id) {
        Ingresso ingresso = buscarIngressoPorId(id);
        ingressoRepository.delete(ingresso);
    }

    // Lista todos os ingressos com paginação
    @Override
    public Page<Ingresso> listarTodosIngressos(Pageable pageable) {
        return ingressoRepository.findAll(pageable);
    }

    // Filtra ingressos
    @Override
    public Page<Ingresso> filtrarIngressos(Predicate predicate, Pageable pageable) {
        return ingressoRepository.findAll(predicate, pageable);
    }

    // Busca ingresso por ID
    @Override
    public Ingresso buscarIngressoPorId(Long id) {
        Optional<Ingresso> ingressoBusca = ingressoRepository.findById(id);

        if (ingressoBusca.isEmpty()) 
            throw new RecursoNaoEncontradoException("Ingresso com id " + id + " não existe.");
        
        return ingressoBusca.get();
    }

    // Lista ingressos disponíveis para um evento específico com paginação
    @Override
    public Page<Ingresso> ingressosDisponiveis(Long idEvento, Pageable pageable) {
        Page<Ingresso> ingressos = ingressoRepository.findByEventoId(idEvento, pageable);

        if (ingressos.isEmpty()) 
            throw new RecursoNaoEncontradoException("Não existem ingressos para o evento de id " + idEvento);

        return ingressos;
    }
    
}
