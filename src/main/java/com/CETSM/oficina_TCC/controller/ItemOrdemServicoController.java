package com.CETSM.oficina_TCC.controller;

import com.CETSM.oficina_TCC.model.ItemOrdemServico;
import com.CETSM.oficina_TCC.service.OrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CONTROLLER DE ITENS DA ORDEM DE SERVIÇO
 * Expõe a rota para adicionar peças à OS acionando a baixa automática no estoque.
 */
@RestController
@RequestMapping("/api/ordens-servico")
public class ItemOrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @PostMapping("/{idOs}/pecas")
    public ResponseEntity<ItemOrdemServico> adicionarPeca(@PathVariable Integer idOs, @Valid @RequestBody ItemOrdemServico item) {
        ItemOrdemServico novoItem = ordemServicoService.adicionarPecaNaOs(idOs, item);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
    }
}
