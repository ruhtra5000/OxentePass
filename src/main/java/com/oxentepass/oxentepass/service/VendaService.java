package com.oxentepass.oxentepass.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oxentepass.oxentepass.entity.IngressoVenda;
import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.entity.Venda;

public interface VendaService {
    // Operações Básicas
    public void criarVenda(Venda venda);
    public Page<Venda> listarTodasVendas(Pageable pageable);
    public Venda buscarVendaPorId(long id);
    public Page<Venda> buscarVendaPorUsuario(Long idUsuario, Pageable pageable);
    // Modificação de status
    public Venda finalizarVenda(long id);
    public void cancelarVenda(long id);
    // Pagamento
    public Venda confirmarPagamento(long id, Pagamento pagamento);
    // Ingressos
    public Venda adicionarIngresso(IngressoVenda ingressoVenda, long id);
    public Venda removerIngresso(IngressoVenda ingressoVenda, long id);
}
