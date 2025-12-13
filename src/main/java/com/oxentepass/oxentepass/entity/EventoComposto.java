package com.oxentepass.oxentepass.entity;

import java.util.ArrayList;
import java.util.List;

import com.oxentepass.oxentepass.exceptions.EstadoInvalidoException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class EventoComposto extends Evento { // Aplicação do padrão de projeto Composite
    
    // Talvez não seja necessário cascade
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evento> subeventos;

    // Construtor
    public EventoComposto() {
        this.subeventos = new ArrayList<Evento>();
    }

    // Métodos
    public void addSubevento(Evento evento) {
        if (this.subeventos.contains(evento))
            throw new EstadoInvalidoException("O sub-evento " + evento.getNome() + " já faz parte do evento " + this.getNome() + ".");

        this.subeventos.add(evento);
    }

    public void removerSubevento(Evento evento) {
        if (!this.subeventos.remove(evento))
            throw new RecursoNaoEncontradoException("O sub-evento " + evento.getNome() + " não faz parte do evento " + this.getNome() + ".");
    }

    // Getter (feito manualmente por causa de algum bug do lombok)
    public List<Evento> getSubeventos() {
        return this.subeventos;
    }
}
