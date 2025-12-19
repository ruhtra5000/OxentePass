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

@Service
public class IngressoServiceImpl implements IngressoService {

    @Autowired
    private IngressoRepository ingressoRepository;

    @Override
    public void cadastrarIngresso(Ingresso ingresso) {
        ingressoRepository.save(ingresso);
    }

    @Override
    public void deletarIngresso(Long id) {
        Ingresso ingresso = buscarIngressoPorId(id);
        ingressoRepository.delete(ingresso);
    }

    @Override
    public Page<Ingresso> listarTodosIngressos(Pageable pageable) {
        return ingressoRepository.findAll(pageable);
    }

    @Override
    public Ingresso buscarIngressoPorId(Long id) {
        Optional<Ingresso> ingressoBusca = ingressoRepository.findById(id);

        if (ingressoBusca.isEmpty()) 
            throw new RecursoNaoEncontradoException("Ingresso com id " + id + " não existe.");
        
        return ingressoBusca.get();
    }

    @Override
    public Ingresso buscarIngressPorTipo(String tipo) {
        Optional<Ingresso> ingressoBusca = ingressoRepository.findByTipo(tipo);

        if (ingressoBusca.isEmpty()) 
            throw new RecursoNaoEncontradoException("Ingresso do tipo " + tipo + " não existe.");
        
        return ingressoBusca.get();
    }

    @Override
    public Page<Ingresso> ingressosDisponiveis(Long idEvento, Pageable pageable) {
        Page<Ingresso> ingressos = ingressoRepository.findByEventoId(idEvento, pageable);

        if (ingressos.isEmpty()) 
            throw new RecursoNaoEncontradoException("Não existem ingressos para o evento de id " + idEvento);

        return ingressos;
    }
    
}
