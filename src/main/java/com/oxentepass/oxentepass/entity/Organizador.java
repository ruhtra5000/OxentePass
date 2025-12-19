package com.oxentepass.oxentepass.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "id")
public class Organizador extends Usuario {

    private String cnpj;
    private String telefone;
    private String biografia;
    private double notaReputacao;
    
}
