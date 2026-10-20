package com.CETSM.oficina_TCC.service;

import com.CETSM.oficina_TCC.model.Estoque;
import com.CETSM.oficina_TCC.model.OrdemServico;
import com.CETSM.oficina_TCC.repository.EstoqueRepository;
import com.CETSM.oficina_TCC.repository.OrdemServicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SERVICE DE RELATÓRIOS DE GESTÃO
 * 
 * OBJETIVO PARA O TCC:
 * Agrega e processa dados operacionais para fornecer indicadores de desempenho 
 * e alertas estratégicos para o gestor da oficina mecânica.
 */
@Service
public class RelatorioService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    /**
     * Relatório 1: Lista todas as peças que estão com quantidade abaixo do limite mínimo.
     * Essencial para o controle de compras e reposição de inventário.
     */
    public List<Estoque> listarPecasAbaixoDoMinimo() {
        List<Estoque> todasPecas = estoqueRepository.findAll();
        return todasPecas.stream()
                .filter(peca -> peca.getQuantidade() <= peca.getQuantidadeMinima())
                .collect(Collectors.toList());
    }

    /**
     * Relatório 2: Indicadores gerais e faturamento total das Ordens de Serviço.
     */
    public Map<String, Object> obterIndicadoresGeraisOS() {
        List<OrdemServico> todasAsOs = ordemServicoRepository.findAll();

        long totalOs = todasAsOs.size();
        
        // Soma os valores do tipo BigDecimal com segurança
        double faturamentoTotal = todasAsOs.stream()
                .filter(os -> os.getValorTotal() != null)
                .map(OrdemServico::getValorTotal)
                .mapToDouble(java.math.BigDecimal::doubleValue)
                .sum();

        Map<String, Object> indicadores = new HashMap<>();
        indicadores.put("quantidadeTotalOrdensServico", totalOs);
        indicadores.put("faturamentoTotalEstimado", faturamentoTotal);

        return indicadores;
    }
}