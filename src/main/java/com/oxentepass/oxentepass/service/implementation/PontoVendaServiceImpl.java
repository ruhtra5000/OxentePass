package com.oxentepass.oxentepass.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oxentepass.oxentepass.entity.PontoVenda;
import com.oxentepass.oxentepass.exceptions.RecursoDuplicadoException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.PontoVendaRepository;
import com.oxentepass.oxentepass.service.PontoVendaService;
import com.querydsl.core.types.Predicate;

@Service
public class PontoVendaServiceImpl implements PontoVendaService {

    @Autowired
    PontoVendaRepository repository;

    @Override
    public void cadastrarPontoVenda(PontoVenda pontoVenda) {

        if (repository.existsByNomeAndEnderecoCepAndEnderecoNumero(
                pontoVenda.getNome(),
                pontoVenda.getEndereco().getCep(),
                pontoVenda.getEndereco().getNumero())) {
            throw new RecursoDuplicadoException("Já existe um Ponto de Venda registrado para este Nome, Cep e Número!");
        }

        repository.save(pontoVenda);
    }

    @Override
    public void editarPontoVenda(long id, PontoVenda dados) {

        PontoVenda pontoVenda = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum Ponto de Venda encontrado para este id!"));

        pontoVenda.setNome(dados.getNome());
        pontoVenda.setDetalhes(dados.getDetalhes());
        pontoVenda.setEndereco(dados.getEndereco());

        repository.save(pontoVenda);

    }

    @Override
    public void deletarPontoVenda(long id) {

        PontoVenda pontoVenda = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum Ponto de Venda encontrado para este id!"));

        repository.delete(pontoVenda);

    }

    @Override
    public Page<PontoVenda> listarPontoVendas(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<PontoVenda> listarPontoVendasFiltro(Predicate predicate, Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

}
