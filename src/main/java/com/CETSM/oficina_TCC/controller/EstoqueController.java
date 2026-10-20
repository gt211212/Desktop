 package com.CETSM.oficina_TCC.controller;

import com.CETSM.oficina_TCC.model.Estoque;
import com.CETSM.oficina_TCC.service.EstoqueService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER DE ESTOQUE
 * Expõe a API REST para gerenciamento de inventário de peças e alertas de estoque mínimo.
 */
@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping
    public ResponseEntity<List<Estoque>> listarTodas() {
        return ResponseEntity.ok(estoqueService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estoque> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(estoqueService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Estoque> salvar(@Valid @RequestBody Estoque peca) {
        Estoque novaPeca = estoqueService.salvar(peca);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPeca);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estoque> atualizar(@PathVariable Integer id, @Valid @RequestBody Estoque pecaAtualizada) {
        return ResponseEntity.ok(estoqueService.atualizar(id, pecaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        estoqueService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
