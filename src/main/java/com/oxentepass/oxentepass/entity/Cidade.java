package com.oxentepass.oxentepass.entity;

import java.util.List;

import com.oxentepass.oxentepass.exceptions.EstadoInvalidoException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;

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

    //Métodos
    public void addTag(Tag tag) {
        if (this.tags.contains(tag))
            throw new EstadoInvalidoException("A tag informada já consta na cidade " + this.nome + ".");

        this.tags.add(tag);
    }

    public void removerTag(Tag tag) {
        if (!this.tags.remove(tag)) 
            throw new RecursoNaoEncontradoException("A tag informada não consta na cidade " + this.nome + ".");
    }

    public void addEvento(Evento evento) {
        if (this.eventos.contains(evento))
            throw new EstadoInvalidoException("O evento informado já se encontra vinculado a cidade " + this.nome + ".");

        this.eventos.add(evento);
    }

    public void removerEvento(Evento evento) {
        if (!this.eventos.remove(evento))
            throw new RecursoNaoEncontradoException("O evento informado não se encontra vinculado a cidade " + this.nome + ".");
    }
}
