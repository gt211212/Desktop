package com.CETSM.oficina_TCC.controller;

import com.CETSM.oficina_TCC.model.OrdemServico;
import com.CETSM.oficina_TCC.service.OrdemServicoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER DE ORDEM DE SERVIÇO
 * Expõe a API REST para gerenciamento completo do fluxo de atendimento da oficina.
 */
@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @GetMapping
    public ResponseEntity<List<OrdemServico>> listarTodas() {
        return ResponseEntity.ok(ordemServicoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServico> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ordemServicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<OrdemServico> salvar(@Valid @RequestBody OrdemServico os) {
        OrdemServico novaOs = ordemServicoService.salvar(os);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemServico> atualizar(@PathVariable Integer id, @Valid @RequestBody OrdemServico osAtualizada) {
        return ResponseEntity.ok(ordemServicoService.atualizar(id, osAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        ordemServicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
    
}