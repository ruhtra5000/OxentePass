package com.oxentepass.oxentepass.entity;

import java.math.BigDecimal;

import com.oxentepass.oxentepass.exceptions.EstadoInvalidoException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class IngressoVenda {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Ingresso ingresso;

    private int quantidade;
    private boolean meiaEntrada;
    private BigDecimal valorTotal;

    // Setters
    public void setQuantidade(int quantidade) {
        if (quantidade > this.ingresso.getQuantidadeDisponivel()) 
            throw new EstadoInvalidoException("Quantidade de ingressos inválida.");

        this.quantidade = quantidade;
    }

    public void setMeiaEntrada(boolean meiaEntrada) {
        if (meiaEntrada && !this.ingresso.isTemMeiaEntrada()) 
            throw new EstadoInvalidoException("O tipo de ingresso " + ingresso.getTipo() +  " não suporta meia entrada.");

        this.meiaEntrada = meiaEntrada;
    }

    public void calcularValorTotal() {
        BigDecimal valorUnidade = this.ingresso.getValorBase();

        if (this.meiaEntrada) 
            valorUnidade = valorUnidade.divide(new BigDecimal(2));

        this.valorTotal = valorUnidade.multiply(new BigDecimal(this.quantidade));
    }

}
