package com.oxentepass.oxentepass.controller.request;

import java.util.List;

import com.oxentepass.oxentepass.entity.IngressoVenda;
import com.oxentepass.oxentepass.entity.Pagamento;
import com.oxentepass.oxentepass.entity.Usuario;
import com.oxentepass.oxentepass.entity.Venda;

import jakarta.validation.constraints.NotBlank;

public record VendaRequest(

    @NotBlank(message = "O campo \"usuario\" é obrigatório.")
    Usuario usuario,
    @NotBlank(message = "O campo \"ingressos\" é obrigatório.")
    List<IngressoVenda> ingressos,
    @NotBlank(message = "O campo \"pagamento\" é obrigatório.")
    Pagamento pagamento

) {

    public Venda paraEntidade() {

    Venda venda = new Venda();

        venda.setUsuario(usuario);
        venda.setIngressos(ingressos);
        venda.setPagamento(pagamento);
        return venda;

    }

    public List<IngressoVenda> getIngressos() {
        return ingressos;
    }
}
    

