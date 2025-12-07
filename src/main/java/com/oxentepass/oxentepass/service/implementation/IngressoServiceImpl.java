package com.oxentepass.oxentepass.service.implementation;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oxentepass.oxentepass.entity.Ingresso;
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
        if(!ingressoRepository.existsById(id)) {
            throw new IllegalArgumentException("Ingresso com id " + id + " não existe.");
        }
        ingressoRepository.deleteById(id);
    }

    @Override
    public Iterable<Ingresso> listarTodosIngressos() {
        return ingressoRepository.findAll();
    }

    @Override
    public Ingresso buscarIngressoPorId(Long id) {
        Optional<Ingresso> ingressoBusca = ingressoRepository.findById(id);

        if (ingressoBusca.isEmpty()) {
            throw new IllegalArgumentException("Ingresso com id " + id + " não existe.");
        }
        return ingressoBusca.get();

    }

    @Override
    public Ingresso buscarIngressPorTipo(String tipo) {
        Optional<Ingresso> ingressoBusca = ingressoRepository.findByTipo(tipo);

        if (ingressoBusca.isEmpty()) {
            throw new IllegalArgumentException("Ingresso do tipo " + tipo + " não existe.");
        }
        return ingressoBusca.get();
    }

    @Override
    public Ingresso ingressosDisponiveis(Long idEvento) {
        Optional<Ingresso> ingressoBusca = ingressoRepository.findByEventoId(idEvento);

        if (ingressoBusca.isEmpty()) {
            throw new IllegalArgumentException("Ingresso para o evento com id " + idEvento + " não existe.");
        }
        return ingressoBusca.get();
    }
    
}
