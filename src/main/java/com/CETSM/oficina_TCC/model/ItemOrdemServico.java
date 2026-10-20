package com.CETSM.oficina_TCC.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * ENTIDADE ITEM DA ORDEM DE SERVIÇO (PEÇAS USADAS NA OS)
 * 
 * OBJETIVO PARA O TCC:
 * Relaciona a Ordem de Serviço (OS) com as peças do Estoque, armazenando 
 * a quantidade exata daquela peça utilizada no reparo do veículo.
 */
@Entity
@Table(name = "item_ordem_servico")
@Getter @Setter
public class ItemOrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_os")
    private Integer id;

    @NotNull(message = "A quantidade utilizada é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser de pelo menos 1 unidade")
    @Column(name = "quantidade_utilizada", nullable = false)
    private Integer quantidadeUtilizada;

    @NotNull(message = "O item deve estar vinculado a uma Ordem de Serviço")
    @ManyToOne
    @JoinColumn(name = "id_os", nullable = false)
    private OrdemServico ordemServico; // OS à qual este item pertence

    @NotNull(message = "O item deve estar vinculado a uma peça do estoque")
    @ManyToOne
    @JoinColumn(name = "id_peca", nullable = false)
    private Estoque peca; // Peça do estoque que está sendo consumida
}