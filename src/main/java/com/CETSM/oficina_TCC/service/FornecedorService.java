package com.CETSM.oficina_TCC.service;

import com.CETSM.oficina_TCC.exception.RecursoNaoEncontradoException;
import com.CETSM.oficina_TCC.model.Fornecedor;
import com.CETSM.oficina_TCC.repository.FornecedorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SERVICE DE FORNECEDOR
 * Gerencia os fornecedores de autopeças que alimentam o estoque e as contas a pagar.
 */
@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    public Fornecedor buscarPorId(Integer id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor com ID " + id + " não foi encontrado."));
    }

    @Transactional
    public Fornecedor salvar(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    @Transactional
    public Fornecedor atualizar(Integer id, Fornecedor fornecedorAtualizado) {
        Fornecedor fornecedorExistente = buscarPorId(id);

        fornecedorExistente.setNomeRazao(fornecedorAtualizado.getNomeRazao());
        fornecedorExistente.setTelefone(fornecedorAtualizado.getTelefone());
        fornecedorExistente.setEmail(fornecedorAtualizado.getEmail());

        return fornecedorRepository.save(fornecedorExistente);
    }

    @Transactional
    public void excluir(Integer id) {
        buscarPorId(id);
        fornecedorRepository.deleteById(id);
    }
}