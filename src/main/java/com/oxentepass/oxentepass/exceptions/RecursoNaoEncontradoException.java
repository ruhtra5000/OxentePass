package com.oxentepass.oxentepass.exceptions;

public class RecursoNaoEncontradoException extends RuntimeException {
    
    //Cod. HTTP: 404
    //Tentativa de acesso a um recurso não existente
    public RecursoNaoEncontradoException (String mensagem) {
        super(mensagem);
    }

}
