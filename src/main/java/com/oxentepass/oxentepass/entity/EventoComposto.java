package com.oxentepass.oxentepass.entity;

import java.util.ArrayList;
import java.util.List;

import com.oxentepass.oxentepass.exceptions.SubeventoInvalidoException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class EventoComposto extends Evento { // Aplicação do padrão de projeto Composite
    
    // Talvez não seja necessário cascade
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventoSimples> subeventos;

    // Construtor
    public EventoComposto() {
        this.subeventos = new ArrayList<EventoSimples>();
    }

    // Métodos
    public void addEvento(EventoSimples evento) {
        if (this.subeventos.contains(evento))
            throw new SubeventoInvalidoException("O sub-evento " + evento.getNome() + " já faz parte do evento " + this.getNome() + ".");

        this.subeventos.add(evento);
    }

    public void removerEvento(EventoSimples evento) {
        if (!this.subeventos.remove(evento))
            throw new SubeventoInvalidoException("O sub-evento " + evento.getNome() + " não faz parte do evento " + this.getNome() + ".");
    }

    // Getters e Setters (feito manualmente por causa de algum bug do lombok)
    public List<EventoSimples> getSubeventos() {
        return this.subeventos;
    }

    public void setSubeventos(List<EventoSimples> subeventos) {
        this.subeventos = subeventos;
    }
}
