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

    public Endereco() {
    }

    public Endereco(String cep, String bairro, String rua, int numero) {
        this.cep = cep;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
    }
}
