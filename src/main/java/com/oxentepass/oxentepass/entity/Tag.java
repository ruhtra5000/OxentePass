package com.oxentepass.oxentepass.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Tag {
    @Id
    @GeneratedValue
    private long id;
    private String tag;

    public String getNome() {
        return tag;
    }

    
}
