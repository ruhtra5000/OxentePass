package com.oxentepass.oxentepass.service.implementation;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oxentepass.oxentepass.entity.IngressoVenda;
import com.oxentepass.oxentepass.entity.MetodoPagamento;
import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.entity.Venda;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.VendaRepository;
import com.oxentepass.oxentepass.service.VendaService;
import com.querydsl.core.types.Predicate;

@Service
public class VendaServiceImpl implements VendaService {

    // Repositórios
    @Autowired
    private VendaRepository vendaRepository;

    // Métodos

    // Cria uma nova venda
    @Override
    public void criarVenda(Venda venda){
        vendaRepository.save(venda);
    }

    // Finaliza uma venda existente
    @Override
    public Venda finalizarVenda(long id){
        Venda venda = buscarVendaPorId(id);
        venda.finalizar();
        return vendaRepository.save(venda);
    }
   
    // Lista todas as vendas com paginação
    @Override
    public Page<Venda> listarTodasVendas(Pageable pageable) {
        return vendaRepository.findAll(pageable);
    }

    // Filtra vendas
    @Override
    public Page<Venda> filtrarVendas(Predicate predicate, Pageable pageable) {
        return vendaRepository.findAll(predicate, pageable);
    }

    // Cancela uma venda existente
    @Override
    public void cancelarVenda(long id) {
        Venda venda = buscarVendaPorId(id);
        venda.cancelar();
        vendaRepository.save(venda);
    }

    // Busca uma venda pelo ID
    @Override
    public Venda buscarVendaPorId(long id) {
        Optional<Venda> venda = vendaRepository.findById(id);
        if (venda.isEmpty()) 
            throw new RecursoNaoEncontradoException("Venda não encontrada com o ID: " + id);
        
        return venda.get();
    }

    // Busca vendas associadas a um usuário específico
    @Override
    public Page<Venda> buscarVendaPorUsuario(Long idUsuario, Predicate predicate, Pageable pageable) {
        return vendaRepository.findByUsuarioId(idUsuario, predicate, pageable);
    }

    // Confirma o pagamento de uma venda
    @Override
    public Venda confirmarPagamento(long idVenda, MetodoPagamento metodo) {

        Venda venda = buscarVendaPorId(idVenda);

        Pagamento pagamento = venda.getPagamento();
        if (pagamento == null) {
            throw new IllegalStateException("Venda não possui pagamento associado");
        }

        pagamento.setMetodoPagamento(metodo);
        pagamento.confirmar();

        venda.marcarComoPaga();

        return vendaRepository.save(venda);
    }

    // Adiciona um ingresso a uma venda existente
    @Override
    public Venda adicionarIngresso(IngressoVenda ingressoVenda, long id) {
        Venda venda = buscarVendaPorId(id);
        venda.addIngresso(ingressoVenda);
        return vendaRepository.save(venda);
    }

    // Remove um ingresso de uma venda existente
    @Override
    public Venda removerIngresso(Long idIngressoVenda, long id) {

        Venda venda = buscarVendaPorId(id);

        IngressoVenda ingressoVenda = null;

        for (IngressoVenda i : venda.getIngressos()) {
            if (i.getId() == (idIngressoVenda)) {
                ingressoVenda = i;
                break;
            }
        }

        if (ingressoVenda == null) {
            throw new RecursoNaoEncontradoException("Ingresso não encontrado");
        }

        venda.removerIngresso(ingressoVenda);
        return vendaRepository.save(venda);
    }
}
