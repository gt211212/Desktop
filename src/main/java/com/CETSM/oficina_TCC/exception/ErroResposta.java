package com.CETSM.oficina_TCC.exception;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * CLASSE DE ESTRUTURAÇÃO DO ERRO
 * 
 * OBJETIVO PARA O TCC:
 * Padroniza o formato de todas as respostas de erro da API, incluindo carimbo de data/hora,
 * código de status HTTP, mensagem amigável e o caminho da requisição.
 */
@Getter @Setter
public class ErroResposta {
    private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String mensagem;
    private String caminho;

    public ErroResposta(int status, String erro, String mensagem, String caminho) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.caminho = caminho;
    }
}