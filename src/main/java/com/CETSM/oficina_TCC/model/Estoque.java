package com.CETSM.oficina_TCC.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * ENTIDADE ESTOQUE (PEÇAS)
 * 
 * OBJETIVO PARA O TCC:
 * Controla o inventário de peças da oficina, incluindo código de barras, quantidades, 
 * limites mínimos para alerta e fornecedor responsável.
 */
@Entity
@Table(name = "estoque")
@Getter @Setter
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_peca")
    private Integer id;

    @NotBlank(message = "O código de barras é obrigatório")
    @Column(name = "codigo_barras", nullable = false, unique = true, length = 50)
    private String codigoBarras;

    @NotBlank(message = "A descrição da peça é obrigatória")
    @Column(name = "descricao", nullable = false, length = 150)
    private String descricao;

    @NotNull
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade = 0;

    @NotNull
    @Min(value = 0, message = "A quantidade mínima não pode ser negativa")
    @Column(name = "quantidade_minima", nullable = false)
    private Integer quantidadeMinima = 0;

    @NotNull(message = "O valor unitário é obrigatório")
    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "localizacao", length = 50)
    private String localizacao;

    @NotNull(message = "A peça deve estar vinculada a um fornecedor")
    @ManyToOne
    @JoinColumn(name = "id_fornecedor", nullable = false)
    private Fornecedor fornecedor;
}