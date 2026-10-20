package com.CETSM.oficina_TCC.service;

import com.CETSM.oficina_TCC.exception.RecursoNaoEncontradoException;
import com.CETSM.oficina_TCC.model.*;
import com.CETSM.oficina_TCC.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SERVICE DE ORDEM DE SERVIÇO COM BAIXA AUTOMÁTICA DE ESTOQUE (RF4)
 * 
 * OBJETIVO PARA O TCC:
 * Além de gerenciar o fluxo da OS, implementa a regra de negócio crítica onde 
 * o uso de peças desconta automaticamente o inventário do estoque, validando 
 * se há saldo disponível antes de concluir a operação[cite: 13].
 */
@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ContaReceberRepository contaReceberRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private MecanicoRepository mecanicoRepository;

    @Autowired
    private ItemOrdemServicoRepository itemOrdemServicoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    public List<OrdemServico> listarTodas() {
        return ordemServicoRepository.findAll();
    }

    public OrdemServico buscarPorId(Integer id) {
        return ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de Serviço com ID " + id + " não foi encontrada."));
    }

    @Transactional
    public OrdemServico salvar(OrdemServico os) {
        Veiculo veiculo = veiculoRepository.findById(os.getVeiculo().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado."));
        
        Mecanico mecanico = mecanicoRepository.findById(os.getMecanico().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Mecânico não encontrado."));

        os.setVeiculo(veiculo);
        os.setMecanico(mecanico);
        os.setDataAbertura(LocalDateTime.now());

        return ordemServicoRepository.save(os);
    }

    /**
     * ADICIONAR PEÇA NA OS E DAR BAIXA AUTOMÁTICA NO ESTOQUE (RF4)
     */
    @Transactional
    public ItemOrdemServico adicionarPecaNaOs(Integer idOs, ItemOrdemServico novoItem) {
        // 1. Busca a Ordem de Serviço
        OrdemServico os = buscarPorId(idOs);

        // 2. Busca a peça no Estoque
        Estoque peca = estoqueRepository.findById(novoItem.getPeca().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada no estoque."));

        // 3. Valida se há quantidade suficiente em estoque
        if (peca.getQuantidade() < novoItem.getQuantidadeUtilizada()) {
            throw new IllegalArgumentException("Estoque insuficiente para a peça: " + peca.getDescricao() 
                + ". Disponível: " + peca.getQuantidade());
        }

        // 4. Realiza a BAIXA AUTOMÁTICA no estoque
        peca.setQuantidade(peca.getQuantidade() - novoItem.getQuantidadeUtilizada());
        estoqueRepository.save(peca); // Salva o estoque atualizado

        // 5. Vincula o item à OS e salva
        novoItem.setOrdemServico(os);
        novoItem.setPeca(peca);

        return itemOrdemServicoRepository.save(novoItem);
    }

    @Transactional
    public void excluir(Integer id) {
        buscarPorId(id);
        ordemServicoRepository.deleteById(id);
    }

    /**
     * Atualiza a OS e, caso o status mude para Concluído/Fechado, 
     * gera automaticamente o registo em Contas a Receber.
     */
    @Transactional
    public OrdemServico atualizar(Integer id, OrdemServico osAtualizada) {
        OrdemServico osExistente = buscarPorId(id);

        Veiculo veiculo = veiculoRepository.findById(osAtualizada.getVeiculo().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado."));
        
        Mecanico mecanico = mecanicoRepository.findById(osAtualizada.getMecanico().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Mecânico não encontrado."));

        // Verifica se a OS está a ser fechada agora (e antes não estava)
        boolean acabouDeConcluir = !"Concluído".equalsIgnoreCase(osExistente.getStatus()) 
                && "Concluído".equalsIgnoreCase(osAtualizada.getStatus());

        // Atualiza os dados da OS
        osExistente.setDataAbertura(osAtualizada.getDataAbertura());
        osExistente.setDataFechamento(osAtualizada.getDataFechamento());
        osExistente.setStatus(osAtualizada.getStatus());
        osExistente.setObservacoes(osAtualizada.getObservacoes());
        osExistente.setValorTotal(osAtualizada.getValorTotal());
        osExistente.setVeiculo(veiculo);
        osExistente.setMecanico(mecanico);

        OrdemServico osSalva = ordemServicoRepository.save(osExistente);

        // AUTOMAÇÃO FINANCEIRA (Integração OS -> Conta a Receber)
        if (acabouDeConcluir && osSalva.getValorTotal() != null) {
            ContaReceber conta = new ContaReceber();
            conta.setDescricao("Referente à Ordem de Serviço #" + osSalva.getId() + " - Veículo: " + veiculo.getModelo());
            conta.setValor(osSalva.getValorTotal());
            conta.setDataVencimento(java.time.LocalDate.now().plusDays(7)); // Vencimento para 7 dias por defeito
            conta.setStatus("Pendente");
            
            contaReceberRepository.save(conta);
        }

        return osSalva;
    }
}