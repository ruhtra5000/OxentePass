package com.oxentepass.oxentepass.entity;

import java.util.ArrayList;
import java.util.List;

import com.oxentepass.oxentepass.exceptions.SubeventoInvalidoException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class EventoComposto extends Evento {
    
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
            throw new SubeventoInvalidoException("O sub-evento já faz parte do evento.");

        this.subeventos.add(evento);
    }

    public void removerEvento(EventoSimples evento) {
        if (!this.subeventos.remove(evento))
            throw new SubeventoInvalidoException("O sub-evento especificado não faz parte do evento.");
    }
}
