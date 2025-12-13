package com.oxentepass.oxentepass.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oxentepass.oxentepass.entity.IngressoVenda;
import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.entity.Venda;

public interface VendaService {

    public void criarVenda(Venda venda);
    public Venda finalizarVenda(long id);
    public Page<Venda> listarTodasVendas(Pageable pageable);
    public void cancelarVenda(long id);
    public Venda adicionarIngresso(IngressoVenda ingressoVenda, long id);
    public Venda removerIngresso(IngressoVenda ingressoVenda, long id);
    public Venda buscarVendaPorId(long id);
    public Page<Venda> buscarVendaPorUsuario(Long idUsuario, Pageable pageable);
    public Venda confirmarPagamento(long id, Pagamento pagamento);

}
