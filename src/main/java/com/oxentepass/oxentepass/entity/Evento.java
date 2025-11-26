package com.oxentepass.oxentepass.entity;

import java.time.LocalDateTime;
import java.util.List;

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
}
