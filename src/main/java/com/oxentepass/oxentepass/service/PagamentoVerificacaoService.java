package com.oxentepass.oxentepass.service;

public interface PagamentoVerificacaoService {

    // Verifica pagamentos pendentes e atualiza seus status
    void verificarPagamentosPendentes();
    
}