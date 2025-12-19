package com.oxentepass.oxentepass.controller.advice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.oxentepass.oxentepass.exceptions.ArquivoInvalidoException;
import com.oxentepass.oxentepass.exceptions.EstadoInvalidoException;
import com.oxentepass.oxentepass.exceptions.OperacaoProibidaException;
import com.oxentepass.oxentepass.exceptions.RecursoDuplicadoException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.exceptions.TipoArquivoNaoSuportadoException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalControllerAdvice {
    
    // Erro de validação (Spring Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> lidarComMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ErroResponse erroResponse = new ErroResponse();
        
        erroResponse.setStatus(400);
        erroResponse.setErro("Erro de validação de campo");
        erroResponse.setMensagem(exception.getMessage());
        erroResponse.setPath(request.getRequestURI());
        
        Map<String, String> errosPorCampo = new HashMap<>();
	    exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errosPorCampo.put(fieldName, errorMessage);
	    });
        erroResponse.setDetalhes(errosPorCampo);
	    
		return new ResponseEntity<ErroResponse>(
            erroResponse, 
            HttpStatus.BAD_REQUEST
        );
    }

    // Violações de banco de dados (chaves primárias e estrangeiras)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> lidarComBancoDeDados (DataIntegrityViolationException exception, HttpServletRequest request) {
        ErroResponse erroResponse = new ErroResponse();

        erroResponse.setStatus(409);
        erroResponse.setErro("Violação de Banco de Dados");
        erroResponse.setPath(request.getRequestURI());

        Throwable root = NestedExceptionUtils.getRootCause(exception);
        
        if (root instanceof SQLException)
            erroResponse.setMensagem(extrairDetalhe((SQLException)root));
        else 
            erroResponse.setMensagem(exception.getMessage());

        return new ResponseEntity<ErroResponse>(
            erroResponse, 
            HttpStatus.CONFLICT
        );
    }

    // Exceções customizadas
    @ExceptionHandler(ArquivoInvalidoException.class)
    public ResponseEntity<ErroResponse> lidarComArquivoInvalidoException(ArquivoInvalidoException exception, HttpServletRequest request) {
        ErroResponse erroResponse = new ErroResponse();

        erroResponse.setStatus(400);
        erroResponse.setErro("Arquivo Invalido");
        erroResponse.setMensagem(exception.getMessage());
        erroResponse.setPath(request.getRequestURI());

        return new ResponseEntity<ErroResponse>(
            erroResponse, 
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EstadoInvalidoException.class)
    public ResponseEntity<ErroResponse> lidarComEstadoInvalidoException(EstadoInvalidoException exception, HttpServletRequest request) {
        ErroResponse erroResponse = new ErroResponse();

        erroResponse.setStatus(409);
        erroResponse.setErro("Estado Invalido");
        erroResponse.setMensagem(exception.getMessage());
        erroResponse.setPath(request.getRequestURI());

        return new ResponseEntity<ErroResponse>(
            erroResponse, 
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(OperacaoProibidaException.class)
    public ResponseEntity<ErroResponse> lidarComOperacaoProibidaException(OperacaoProibidaException exception, HttpServletRequest request) {
        ErroResponse erroResponse = new ErroResponse();

        erroResponse.setStatus(403);
        erroResponse.setErro("Operação Proibida");
        erroResponse.setMensagem(exception.getMessage());
        erroResponse.setPath(request.getRequestURI());

        return new ResponseEntity<ErroResponse>(
            erroResponse, 
            HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErroResponse> lidarComRecursoDuplicadoException(RecursoDuplicadoException exception, HttpServletRequest request) {
        ErroResponse erroResponse = new ErroResponse();

        erroResponse.setStatus(409);
        erroResponse.setErro("Recurso Duplicado");
        erroResponse.setMensagem(exception.getMessage());
        erroResponse.setPath(request.getRequestURI());

        return new ResponseEntity<ErroResponse>(
            erroResponse, 
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> lidarComRecursoNaoEncontradoException(RecursoNaoEncontradoException exception, HttpServletRequest request) {
        ErroResponse erroResponse = new ErroResponse();

        erroResponse.setStatus(404);
        erroResponse.setErro("Recurso Não Encontrado");
        erroResponse.setMensagem(exception.getMessage());
        erroResponse.setPath(request.getRequestURI());

        return new ResponseEntity<ErroResponse>(
            erroResponse, 
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(TipoArquivoNaoSuportadoException.class)
    public ResponseEntity<ErroResponse> lidarComTipoArquivoNaoSuportadoException(TipoArquivoNaoSuportadoException exception, HttpServletRequest request) {
        ErroResponse erroResponse = new ErroResponse();

        erroResponse.setStatus(415);
        erroResponse.setErro("Tipo de arquivo não suportado");
        erroResponse.setMensagem(exception.getMessage());
        erroResponse.setPath(request.getRequestURI());

        return new ResponseEntity<ErroResponse>(
            erroResponse, 
            HttpStatus.UNSUPPORTED_MEDIA_TYPE
        );
    }

    // Fallback 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> lidarComException(Exception exception, HttpServletRequest request) {
        ErroResponse erroResponse = new ErroResponse();

        erroResponse.setStatus(500);
        erroResponse.setErro("Erro Interno");
        erroResponse.setMensagem(exception.getMessage());
        erroResponse.setPath(request.getRequestURI());

        return new ResponseEntity<ErroResponse>(
            erroResponse, 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // Funções auxiliares
    private String extrairDetalhe(SQLException ex) {
        String msg = ex.getMessage();
        if (msg == null) return null;

        int idx = msg.indexOf("Detalhe:");
        if (idx == -1) return null;

        return msg.substring(idx + "Detalhe:".length()).trim();
    }
}
