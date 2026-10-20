package com.CETSM.oficina_TCC.controller;

import com.CETSM.oficina_TCC.model.Fornecedor;
import com.CETSM.oficina_TCC.service.FornecedorService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER DE FORNECEDOR
 * Expõe a API REST para cadastro e controle de fornecedores de peças.
 */
@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public ResponseEntity<List<Fornecedor>> listarTodos() {
        return ResponseEntity.ok(fornecedorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(fornecedorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Fornecedor> salvar(@Valid @RequestBody Fornecedor fornecedor) {
        Fornecedor novoFornecedor = fornecedorService.salvar(fornecedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFornecedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> atualizar(@PathVariable Integer id, @Valid @RequestBody Fornecedor fornecedorAtualizado) {
        return ResponseEntity.ok(fornecedorService.atualizar(id, fornecedorAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        fornecedorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}