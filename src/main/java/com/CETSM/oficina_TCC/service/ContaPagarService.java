package com.CETSM.oficina_TCC.service;

import com.CETSM.oficina_TCC.exception.RecursoNaoEncontradoException;
import com.CETSM.oficina_TCC.model.ContaPagar;
import com.CETSM.oficina_TCC.model.Fornecedor;
import com.CETSM.oficina_TCC.repository.ContaPagarRepository;
import com.CETSM.oficina_TCC.repository.FornecedorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContaPagarService {

    @Autowired
    private ContaPagarRepository contaPagarRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public List<ContaPagar> listarTodas() {
        return contaPagarRepository.findAll();
    }

    public ContaPagar buscarPorId(Integer id) {
        return contaPagarRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta a pagar ID " + id + " não encontrada."));
    }

    @Transactional
    public ContaPagar salvar(ContaPagar conta) {
        Fornecedor fornecedor = fornecedorRepository.findById(conta.getFornecedor().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor vinculado não encontrado."));
        conta.setFornecedor(fornecedor);
        return contaPagarRepository.save(conta);
    }

    @Transactional
    public ContaPagar atualizar(Integer id, ContaPagar contaAtualizada) {
        ContaPagar contaExistente = buscarPorId(id);
        Fornecedor fornecedor = fornecedorRepository.findById(contaAtualizada.getFornecedor().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor vinculado não encontrado."));

        contaExistente.setDescricao(contaAtualizada.getDescricao());
        contaExistente.setValor(contaAtualizada.getValor());
        contaExistente.setDataVencimento(contaAtualizada.getDataVencimento());
        contaExistente.setDataPagamento(contaAtualizada.getDataPagamento());
        contaExistente.setStatus(contaAtualizada.getStatus());
        contaExistente.setFornecedor(fornecedor);

        return contaPagarRepository.save(contaExistente);
    }

    @Transactional
    public void excluir(Integer id) {
        buscarPorId(id);
        contaPagarRepository.deleteById(id);
    }
}