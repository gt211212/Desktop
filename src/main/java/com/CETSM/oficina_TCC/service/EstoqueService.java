package com.CETSM.oficina_TCC.service;

import com.CETSM.oficina_TCC.exception.RecursoNaoEncontradoException;
import com.CETSM.oficina_TCC.model.Estoque;
import com.CETSM.oficina_TCC.model.Fornecedor;
import com.CETSM.oficina_TCC.repository.EstoqueRepository;
import com.CETSM.oficina_TCC.repository.FornecedorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SERVICE DE ESTOQUE (PEÇAS)
 * Controla o inventário, valida a existência do fornecedor vinculado e previne erros de cadastro.
 */
@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public List<Estoque> listarTodas() {
        return estoqueRepository.findAll();
    }

    public Estoque buscarPorId(Integer id) {
        return estoqueRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Peça com ID " + id + " não foi encontrada no estoque."));
    }

    @Transactional
    public Estoque salvar(Estoque peca) {
        if (peca.getFornecedor() == null || peca.getFornecedor().getId() == null) {
            throw new IllegalArgumentException("A peça deve estar vinculada a um ID de fornecedor válido.");
        }

        Integer idFornecedor = peca.getFornecedor().getId();
        Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor com ID " + idFornecedor + " não encontrado."));

        peca.setFornecedor(fornecedor);
        return estoqueRepository.save(peca);
    }

    @Transactional
    public Estoque atualizar(Integer id, Estoque pecaAtualizada) {
        Estoque pecaExistente = buscarPorId(id);

        if (pecaAtualizada.getFornecedor() == null || pecaAtualizada.getFornecedor().getId() == null) {
            throw new IllegalArgumentException("A peça deve estar vinculada a um ID de fornecedor válido.");
        }

        Integer idFornecedor = pecaAtualizada.getFornecedor().getId();
        Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor com ID " + idFornecedor + " não encontrado."));

        pecaExistente.setCodigoBarras(pecaAtualizada.getCodigoBarras());
        pecaExistente.setDescricao(pecaAtualizada.getDescricao());
        pecaExistente.setQuantidade(pecaAtualizada.getQuantidade());
        pecaExistente.setQuantidadeMinima(pecaAtualizada.getQuantidadeMinima());
        pecaExistente.setValorUnitario(pecaAtualizada.getValorUnitario());
        pecaExistente.setLocalizacao(pecaAtualizada.getLocalizacao());
        pecaExistente.setFornecedor(fornecedor);

        return estoqueRepository.save(pecaExistente);
    }

    @Transactional
    public void excluir(Integer id) {
        buscarPorId(id);
        estoqueRepository.deleteById(id);
    }
}