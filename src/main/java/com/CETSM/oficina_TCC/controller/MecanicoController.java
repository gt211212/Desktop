package com.CETSM.oficina_TCC.controller;

import com.CETSM.oficina_TCC.model.Mecanico;
import com.CETSM.oficina_TCC.service.MecanicoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER DE MECÂNICO
 * Expõe a API REST para gerenciar os profissionais da oficina mecânica.
 */
@RestController
@RequestMapping("/api/mecanicos")
public class MecanicoController {

    @Autowired
    private MecanicoService mecanicoService;

    @GetMapping
    public ResponseEntity<List<Mecanico>> listarTodos() {
        return ResponseEntity.ok(mecanicoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mecanico> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(mecanicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Mecanico> salvar(@Valid @RequestBody Mecanico mecanico) {
        Mecanico novoMecanico = mecanicoService.salvar(mecanico);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoMecanico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mecanico> atualizar(@PathVariable Integer id, @Valid @RequestBody Mecanico mecanicoAtualizado) {
        return ResponseEntity.ok(mecanicoService.atualizar(id, mecanicoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        mecanicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}