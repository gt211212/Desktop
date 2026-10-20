package com.CETSM.oficina_TCC.service;

import com.CETSM.oficina_TCC.exception.RecursoNaoEncontradoException;
import com.CETSM.oficina_TCC.model.ContaReceber;
import com.CETSM.oficina_TCC.model.OrdemServico;
import com.CETSM.oficina_TCC.repository.ContaReceberRepository;
import com.CETSM.oficina_TCC.repository.OrdemServicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContaReceberService {

    @Autowired
    private ContaReceberRepository contaReceberRepository;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    public List<ContaReceber> listarTodas() {
        return contaReceberRepository.findAll();
    }

    public ContaReceber buscarPorId(Integer id) {
        return contaReceberRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta a receber ID " + id + " não encontrada."));
    }

    @Transactional
    public ContaReceber salvar(ContaReceber conta) {
        OrdemServico os = ordemServicoRepository.findById(conta.getOrdemServico().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de Serviço vinculada não encontrada."));
        conta.setOrdemServico(os);
        return contaReceberRepository.save(conta);
    }

    @Transactional
    public ContaReceber atualizar(Integer id, ContaReceber contaAtualizada) {
        ContaReceber contaExistente = buscarPorId(id);
        OrdemServico os = ordemServicoRepository.findById(contaAtualizada.getOrdemServico().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de Serviço vinculada não encontrada."));

        contaExistente.setDescricao(contaAtualizada.getDescricao());
        contaExistente.setValor(contaAtualizada.getValor());
        contaExistente.setDataVencimento(contaAtualizada.getDataVencimento());
        contaExistente.setDataPagamento(contaAtualizada.getDataPagamento());
        contaExistente.setStatus(contaAtualizada.getStatus());
        contaExistente.setFormaPagamento(contaAtualizada.getFormaPagamento());
        contaExistente.setOrdemServico(os);

        return contaReceberRepository.save(contaExistente);
    }

    @Transactional
    public void excluir(Integer id) {
        buscarPorId(id);
        contaReceberRepository.deleteById(id);
    }
}