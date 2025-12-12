package com.oxentepass.oxentepass.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.oxentepass.oxentepass.exceptions.IngressoInvalidoException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Venda {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToMany 
    private List<IngressoVenda> ingressos;

    @OneToOne
    private Pagamento pagamento;

    private LocalDateTime dataHoraVenda;
    private BigDecimal valorTotal;

    // Construtor
    public Venda () {
        this.ingressos = new ArrayList<IngressoVenda>();
    }

    // Métodos
    public void addIngresso(IngressoVenda ingressoVenda) {
        this.ingressos.add(ingressoVenda);
    }

    public void removerIngresso(IngressoVenda ingressoVenda) {
        boolean resultado = this.ingressos.remove(ingressoVenda);

        if (!resultado)
            throw new IngressoInvalidoException("Este ingresso não se encontra na venda!");
    }

    public void calcularValorTotal() {
        BigDecimal valorTotal = BigDecimal.ZERO;
        
        for (var ingressoVenda : ingressos) 
            valorTotal = valorTotal.add(ingressoVenda.getValorTotal());
        
        this.valorTotal = valorTotal;
    }

    public void setFinalizada(boolean finalizada) {
        if(finalizada) {
            this.dataHoraVenda = LocalDateTime.now();
            calcularValorTotal();
        } else {
            this.dataHoraVenda = null;
            this.valorTotal = BigDecimal.ZERO;
        }
    }

    public void setCancelada(boolean cancelada) {
        if(cancelada) {
            this.dataHoraVenda = null;
            this.valorTotal = BigDecimal.ZERO;
        }
    }
}
