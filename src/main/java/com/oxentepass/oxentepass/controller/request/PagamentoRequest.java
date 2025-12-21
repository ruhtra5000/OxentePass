package com.oxentepass.oxentepass.controller.request;

import java.math.BigDecimal;

import com.oxentepass.oxentepass.entity.MetodoPagamento;
import com.oxentepass.oxentepass.entity.Pagamento;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequest(

    @NotNull(message = "O campo \"valor\" é obrigatório.")
    @Min(value = 0, message = "O valor do pagamento deve ser positivo.")
    BigDecimal valor,

    @NotNull(message = "O campo \"metodo\" é obrigatório.")
    MetodoPagamento metodo,

    @NotNull(message = "O campo \"vendaId\" é obrigatório.") 
    Long vendaId

) {
    public Pagamento paraEntidade() {
        Pagamento pagamento = new Pagamento();

        pagamento.setValor(valor);
        pagamento.setMetodo(metodo);
        
        return pagamento;
    }
}
    

