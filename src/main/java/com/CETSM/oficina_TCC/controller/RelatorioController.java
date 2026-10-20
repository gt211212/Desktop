package com.CETSM.oficina_TCC.controller;

import com.CETSM.oficina_TCC.model.Estoque;
import com.CETSM.oficina_TCC.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * CONTROLLER DE RELATÓRIOS
 * Expõe endpoints analíticos para visualização de métricas de estoque e financeiras.
 */
@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    /**
     * Endpoint para consultar peças em nível crítico de estoque.
     * Rota: GET -> http://localhost:8080/api/relatorios/estoque-critico
     */
    @GetMapping("/estoque-critico")
    public ResponseEntity<List<Estoque>> obterEstoqueCritico() {
        List<Estoque> pecasCriticas = relatorioService.listarPecasAbaixoDoMinimo();
        return ResponseEntity.ok(pecasCriticas);
    }

    /**
     * Endpoint para consultar indicadores gerais e faturamento das OS.
     * Rota: GET -> http://localhost:8080/api/relatorios/financeiro-os
     */
    @GetMapping("/financeiro-os")
    public ResponseEntity<Map<String, Object>> obterIndicadoresFinanceiros() {
        Map<String, Object> indicadores = relatorioService.obterIndicadoresGeraisOS();
        return ResponseEntity.ok(indicadores);
    }
}
