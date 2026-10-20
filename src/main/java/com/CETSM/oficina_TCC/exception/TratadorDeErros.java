
package com.CETSM.oficina_TCC.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> naoEncontrado(RecursoNaoEncontradoException e) {
        return resposta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, Object>> regraNegocio(RegraNegocioException e) {
        return resposta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacao(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(f ->
            sb.append(f.getField()).append(": ").append(f.getDefaultMessage()).append("; "));
        return resposta(HttpStatus.BAD_REQUEST, sb.toString());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridade(DataIntegrityViolationException e) {
        return resposta(HttpStatus.CONFLICT,
            "Operação viola o banco de dados (registro duplicado ou em uso por outro cadastro).");
    }

    private ResponseEntity<Map<String, Object>> resposta(HttpStatus status, String msg) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("data", LocalDateTime.now());
        corpo.put("status", status.value());
        corpo.put("erro", msg);
        return ResponseEntity.status(status).body(corpo);
    }
}