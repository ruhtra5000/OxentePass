package com.oxentepass.oxentepass.exceptions;

public class RecursoDuplicadoException extends RuntimeException {
    
    //Cod. HTTP: 409
    //Tentativa de criação dum recurso já existente
    public RecursoDuplicadoException(String mensagem) { 
        super(mensagem);
    }
}
