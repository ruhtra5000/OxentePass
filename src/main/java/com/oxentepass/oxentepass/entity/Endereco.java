package com.oxentepass.oxentepass.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Endereco {
    @Id
    @GeneratedValue
    private long id;
    private String cep;
    private String bairro;
    private String rua;
    private int numero;
}
