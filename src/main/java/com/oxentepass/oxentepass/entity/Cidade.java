package com.oxentepass.oxentepass.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Cidade {
    @Id
    @GeneratedValue
    private long id;
    private String nome;
    private String descricao;
    
    @ManyToMany
    private List<Tag> tags;

    @OneToMany
    private List<Evento> eventos;
}
