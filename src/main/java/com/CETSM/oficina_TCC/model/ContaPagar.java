package com.CETSM.oficina_TCC.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "conta_pagar")
@Getter @Setter
public class ContaPagar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta_pagar")
    private Integer id;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(name = "descricao", nullable = false, length = 150)
    private String descricao;

    @NotNull(message = "O valor é obrigatório")
    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "A data de vencimento é obrigatória")
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "status", length = 20)
    private String status = "Pendente";

    @NotNull(message = "A conta a pagar deve estar vinculada a um fornecedor")
    @ManyToOne
    @JoinColumn(name = "id_fornecedor", nullable = false)
    private Fornecedor fornecedor;
}