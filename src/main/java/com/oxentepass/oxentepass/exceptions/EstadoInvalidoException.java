package com.oxentepass.oxentepass.exceptions;

public class EstadoInvalidoException extends RuntimeException {
    
    //Cod. HTTP: 409
    //Operação que deixe o sistema num estado inválido (fora das regras de negócio)
    public EstadoInvalidoException(String mensagem) { 
        super(mensagem);
    }

}
