package com.oxentepass.oxentepass.service.implementation;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.entity.StatusPagamento;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.PagamentoRepository;
import com.oxentepass.oxentepass.service.PagamentoService;
import com.querydsl.core.types.Predicate;

@Service
public class PagamentoServiceImpl implements PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Override
    public void criarPagamento(Pagamento pagamento) {
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamentoRepository.save(pagamento);
    }

    @Override
    public Page<Pagamento> listarTodosPagamentos(Pageable pageable) {
        return pagamentoRepository.findAll(pageable);
    }

    @Override
    public Page<Pagamento> filtrarPagamentos(Predicate predicate, Pageable pageable) {
        return pagamentoRepository.findAll(predicate, pageable);
    }

    @Override
    public Pagamento confirmarPagamento(Long id) {
        Pagamento pagamento = buscarPagamentoPorId(id);
        pagamento.confirmar();
        return pagamentoRepository.save(pagamento);
    }

    @Override
    public Pagamento cancelarPagamento(Long id) {
        Pagamento pagamento = buscarPagamentoPorId(id);
        pagamento.cancelar();
        return pagamentoRepository.save(pagamento);
    }

    @Override
    public Pagamento estornarPagamento(Long id) {
        Pagamento pagamento = buscarPagamentoPorId(id);
        pagamento.estornar();
        return pagamentoRepository.save(pagamento);
    }

    @Override
    public Pagamento recusarPagamento(Long id) {
        Pagamento pagamento = buscarPagamentoPorId(id);
        pagamento.recusar();
        return pagamentoRepository.save(pagamento);
    }

    @Override
    public Pagamento buscarPagamentoPorId(Long id) {
        Optional<Pagamento> pagamentoBusca = pagamentoRepository.findById(id);

        if (pagamentoBusca.isEmpty()) 
            throw new RecursoNaoEncontradoException("Pagamento com id " + id + " não existe.");
        
        return pagamentoBusca.get();
    }
}
