package com.oxentepass.oxentepass.service.implementation;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oxentepass.oxentepass.entity.IngressoVenda;
import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.entity.Venda;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.VendaRepository;
import com.oxentepass.oxentepass.service.VendaService;
import com.querydsl.core.types.Predicate;

@Service
public class VendaServiceImpl implements VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Override
    public void criarVenda(Venda venda){
        vendaRepository.save(venda);
    }

    @Override
    public Venda finalizarVenda(long id){
        Venda venda = buscarVendaPorId(id);
        venda.finalizar();
        return vendaRepository.save(venda);
    }
   
    @Override
    public Page<Venda> listarTodasVendas(Predicate predicate, Pageable pageable) {
        return vendaRepository.findAll(predicate, pageable);
    }

    @Override
    public void cancelarVenda(long id) {
        Venda venda = buscarVendaPorId(id);
        venda.cancelar();
        vendaRepository.save(venda);
    }

    @Override
    public Venda buscarVendaPorId(long id) {
        Optional<Venda> venda = vendaRepository.findById(id);
        if (venda.isEmpty()) 
            throw new RecursoNaoEncontradoException("Venda não encontrada com o ID: " + id);
        
        return venda.get();
    }

    @Override
    public Page<Venda> buscarVendaPorUsuario(Long idUsuario, Predicate predicate, Pageable pageable) {
        return vendaRepository.findByUsuarioId(idUsuario, predicate, pageable);
    }

    @Override
    public Venda confirmarPagamento(long id, Pagamento pagamento) {
        Venda venda = buscarVendaPorId(id);
        venda.setPagamento(pagamento);
        return vendaRepository.save(venda);
    }

    @Override
    public Venda adicionarIngresso(IngressoVenda ingressoVenda, long id) {
        Venda venda = buscarVendaPorId(id);
        venda.addIngresso(ingressoVenda);
        return vendaRepository.save(venda);
    }

    @Override
    public Venda removerIngresso(Long IdIngressoVenda, long id) {
        Venda venda = buscarVendaPorId(id);
        venda.removerIngresso(IdIngressoVenda);
        return vendaRepository.save(venda);
    }
}
