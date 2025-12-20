package com.oxentepass.oxentepass.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Avaliacao {
    @Id
    @GeneratedValue
    private long id;
    private int nota; //Valores no intervalo [1, 5]
    private String comentario;
    // TODO: vincular avaliação a um usuário (?)
}
