package com.oxentepass.oxentepass.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.oxentepass.oxentepass.exceptions.AvaliacaoInvalidaException;
import com.oxentepass.oxentepass.exceptions.IngressoInvalidoException;
import com.oxentepass.oxentepass.exceptions.PontoVendaInvalidoException;
import com.oxentepass.oxentepass.exceptions.TagInvalidaException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED) 
public abstract class Evento {
    @Id
    @GeneratedValue
    private long id;
    private String nome;
    private String descricao;

    @ManyToOne
    private Organizador organizador;
    
    @ManyToMany
    private List<Tag> tags;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingresso> ingressos;

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String classificacao;
    private String emailContato;
    private String telefoneContato;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;

    @ManyToMany
    private List<PontoVenda> pontosVenda;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes;

    private double mediaAvaliacao;

    private int altura; // Atributo para limitar possível árvore de sub-eventos

    //Métodos
    public void addTag(Tag tag) {
        if (this.tags.contains(tag))
            throw new TagInvalidaException("A tag informada já consta no evento " + this.nome + ".");

        this.tags.add(tag);
    }

    public void removerTag(Tag tag) {
        if (!this.tags.remove(tag)) 
            throw new TagInvalidaException("A tag informada não consta no evento " + this.nome + ".");
    }

    public void addIngresso(Ingresso ingresso) {
        if (this.ingressos.contains(ingresso))
            throw new IngressoInvalidoException("O ingresso informado já consta no evento " + this.nome + ".");

        this.ingressos.add(ingresso);
    }

    public void removerIngresso(Ingresso ingresso) {
        if (!this.ingressos.remove(ingresso)) 
            throw new IngressoInvalidoException("O ingresso informado não consta no evento " + this.nome + ".");
    }

    public void addPontoVenda(PontoVenda pontoVenda) {
        if (this.pontosVenda.contains(pontoVenda))
            throw new PontoVendaInvalidoException("O ponto de venda informado já consta no evento " + this.nome + ".");

        this.pontosVenda.add(pontoVenda);
    }

    public void removerPontoVenda(PontoVenda pontoVenda) {
        if (!this.pontosVenda.remove(pontoVenda)) 
            throw new PontoVendaInvalidoException("O ponto de venda informado não consta no evento " + this.nome + ".");
    }

    public void addAvaliacao(Avaliacao avaliacao) {
        if (this.avaliacoes.contains(avaliacao))
            throw new AvaliacaoInvalidaException("A avaliação já foi publicada.");

        this.avaliacoes.add(avaliacao);

        calcularMediaAvaliacao();
    }

    public void removerAvaliacao(long idAvaliacao) {
        Avaliacao busca = null;
        for (Avaliacao aval : this.avaliacoes) {
            if (aval.getId() == idAvaliacao) {
                busca = aval;
                break;
            }
        }

        if (busca == null) 
            throw new AvaliacaoInvalidaException("A avaliação informada não consta no evento " + this.nome + ".");

        avaliacoes.remove(busca);

        calcularMediaAvaliacao();
    }

    private void calcularMediaAvaliacao() {
        double total = 0;
        for (Avaliacao aval : this.avaliacoes) 
            total += aval.getNota();

        this.mediaAvaliacao = (double)(total / this.avaliacoes.size());
    }
}
