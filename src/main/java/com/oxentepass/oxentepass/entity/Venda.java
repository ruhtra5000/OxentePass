package com.oxentepass.oxentepass.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.oxentepass.oxentepass.exceptions.EstadoInvalidoException;
import com.oxentepass.oxentepass.exceptions.OperacaoProibidaException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private StatusVenda status;
    // Construtor
    public Venda () {
        this.ingressos = new ArrayList<IngressoVenda>();
        this.status = StatusVenda.ABERTA;
    }

    // Métodos
    public void addIngresso(IngressoVenda ingressoVenda) {
        this.ingressos.add(ingressoVenda);
        calcularValorTotal();
    }

    public void removerIngresso(IngressoVenda ingressoVenda) {
        boolean resultado = this.ingressos.remove(ingressoVenda);

        if (!resultado)
            throw new RecursoNaoEncontradoException("Este ingresso não se encontra na venda!");

        calcularValorTotal();
    }

    public void calcularValorTotal() {
        BigDecimal valorTotal = BigDecimal.ZERO;
        
        for (var ingressoVenda : ingressos) 
            valorTotal = valorTotal.add(ingressoVenda.getValorTotal());
        
        this.valorTotal = valorTotal;
    }

    public void finalizar() {
        if (this.status != StatusVenda.ABERTA) {
            throw new EstadoInvalidoException("A venda só pode ser finalizada se estiver aberta");
        }
    
        this.status = StatusVenda.FINALIZADA;
        this.dataHoraVenda = LocalDateTime.now();
        calcularValorTotal();
    }

    public void cancelar() {
        if (this.status == StatusVenda.CANCELADA) {
            throw new OperacaoProibidaException("Venda já está cancelada");
        }
    
        devolverIngressos();
        this.status = StatusVenda.CANCELADA;
        this.dataHoraVenda = null;
        this.valorTotal = BigDecimal.ZERO;
    }

    private void devolverIngressos() {
        for (IngressoVenda ingressoVenda : ingressos) {
            ingressoVenda.getIngresso().devolverQuantidade(ingressoVenda.getQuantidade());
        }
    }
}
