package com.oxentepass.oxentepass.controller.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.oxentepass.oxentepass.entity.MetodoPagamento;
import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.entity.StatusPagamento;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequest(

    @NotNull(message = "O campo \"valor\" é obrigatório.")
    @Min(value = 0, message = "O valor do pagamento deve ser positivo.")
    BigDecimal valor,

    @NotNull(message = "O campo \"metodo\" é obrigatório.")
    MetodoPagamento metodo,

    @NotNull(message = "O campo \"status\" é obrigatório.")
    StatusPagamento status,

    @NotNull(message = "O campo \"dataPagamento\" é obrigatório.")
    LocalDateTime dataPagamento

) {
    public Pagamento paraEntidade() {
        Pagamento pagamento = new Pagamento();

        pagamento.setValor(valor);
        pagamento.setMetodo(metodo);
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setDataPagamento(LocalDateTime.now());
        
        return pagamento;
    }
}
    

