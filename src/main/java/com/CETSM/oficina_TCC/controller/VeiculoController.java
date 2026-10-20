package com.CETSM.oficina_TCC.controller;

import com.CETSM.oficina_TCC.model.Veiculo;
import com.CETSM.oficina_TCC.service.VeiculoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER DE VEÍCULO
 * Expõe a API REST para gerenciar os veículos associados aos clientes da oficina.
 */
@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping
    public ResponseEntity<List<Veiculo>> listarTodos() {
        return ResponseEntity.ok(veiculoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(veiculoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Veiculo> salvar(@Valid @RequestBody Veiculo veiculo) {
        Veiculo novoVeiculo = veiculoService.salvar(veiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoVeiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> atualizar(@PathVariable Integer id, @Valid @RequestBody Veiculo veiculoAtualizado) {
        return ResponseEntity.ok(veiculoService.atualizar(id, veiculoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        veiculoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}