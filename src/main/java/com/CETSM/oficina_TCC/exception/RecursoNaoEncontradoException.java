package com.CETSM.oficina_TCC.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * EXCEÇÃO PERSONALIZADA DE RECURSO NÃO ENCONTRADO
 * 
 * OBJETIVO PARA O TCC:
 * Sempre que o sistema tentar buscar, atualizar ou deletar um ID que não existe 
 * no banco de dados MySQL, esta exceção é disparada e o Spring retorna 
 * automaticamente o código HTTP 404 (Not Found) para o cliente/frontend.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNaoEncontradoException extends RuntimeException {
    
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}