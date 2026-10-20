package com.CETSM.oficina_TCC.controller;

import com.CETSM.oficina_TCC.model.ContaReceber;
import com.CETSM.oficina_TCC.service.ContaReceberService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas-receber")
public class ContaReceberController {

    @Autowired
    private ContaReceberService contaReceberService;

    @GetMapping
    public ResponseEntity<List<ContaReceber>> listarTodas() {
        return ResponseEntity.ok(contaReceberService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaReceber> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(contaReceberService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ContaReceber> salvar(@Valid @RequestBody ContaReceber conta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contaReceberService.salvar(conta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaReceber> atualizar(@PathVariable Integer id, @Valid @RequestBody ContaReceber contaAtualizada) {
        return ResponseEntity.ok(contaReceberService.atualizar(id, contaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        contaReceberService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}