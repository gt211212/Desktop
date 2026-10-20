package com.CETSM.oficina_TCC.controller;

import com.CETSM.oficina_TCC.model.ContaPagar;
import com.CETSM.oficina_TCC.service.ContaPagarService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas-pagar")
public class ContaPagarController {

    @Autowired
    private ContaPagarService contaPagarService;

    @GetMapping
    public ResponseEntity<List<ContaPagar>> listarTodas() {
        return ResponseEntity.ok(contaPagarService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaPagar> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(contaPagarService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ContaPagar> salvar(@Valid @RequestBody ContaPagar conta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contaPagarService.salvar(conta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaPagar> atualizar(@PathVariable Integer id, @Valid @RequestBody ContaPagar contaAtualizada) {
        return ResponseEntity.ok(contaPagarService.atualizar(id, contaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        contaPagarService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}