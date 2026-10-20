package com.CETSM.oficina_TCC.controller;

import com.CETSM.oficina_TCC.model.Cliente;
import com.CETSM.oficina_TCC.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Lista clientes com suporte a paginação e ordenação.
     * Exemplo de uso: /api/clientes?page=0&size=5&sort=nome,asc
     */
    @GetMapping
    public ResponseEntity<Page<Cliente>> listar(Pageable pageable) {
        Page<Cliente> clientes = clienteService.listarComPaginacao(pageable);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<Cliente> salvar(@Valid @RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.salvar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Integer id, @Valid @RequestBody Cliente cliente) {
        Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}