package com.CETSM.oficina_TCC.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * MANIPULADOR GLOBAL DE EXCEÇÕES
 * 
 * OBJETIVO PARA O TCC:
 * Intercepta exceções lançadas em qualquer parte da aplicação (como recursos não encontrados,
 * erros de validação do Bean Validation ou regras de negócio) e retorna códigos HTTP corretos
 * acompanhados de um JSON limpo e informativo.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata a exceção de Recurso Não Encontrado (Retorna HTTP 404).
     */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> handleRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex, HttpServletRequest request) {
        
        ErroResposta erro = new ErroResposta(
            HttpStatus.NOT_FOUND.value(),
            "Não Encontrado",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    /**
     * Trata erros de validação de campos (Bean Validation como @NotBlank, @Email, @Min) (Retorna HTTP 400).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        
        // Mapeia cada campo com o respetivo erro de validação
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensagem = error.getDefaultMessage();
            erros.put(campo, mensagem);
        });
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }

    /**
     * Trata exceções de argumentos ilegais ou regras de negócio violadas (Retorna HTTP 400).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResposta> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        
        ErroResposta erro = new ErroResposta(
            HttpStatus.BAD_REQUEST.value(),
            "Requisição Inválida",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    /**
     * Trata qualquer outro erro genérico não previsto (Retorna HTTP 500).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        ErroResposta erro = new ErroResposta(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro Interno do Servidor",
            "Ocorreu um erro inesperado no servidor: " + ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}