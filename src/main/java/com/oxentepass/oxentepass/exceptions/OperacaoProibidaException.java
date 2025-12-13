package com.oxentepass.oxentepass.exceptions;

public class OperacaoProibidaException extends RuntimeException {
    
    //Cod. HTTP: 403
    //Operações que não devem ser executadas por algum tipo de objeto
    //por razões de falta de permissão
    public OperacaoProibidaException (String mensagem) {
        super(mensagem);
    }
}
