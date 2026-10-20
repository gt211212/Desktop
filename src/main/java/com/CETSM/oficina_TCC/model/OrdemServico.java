package com.CETSM.oficina_TCC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * ENTIDADE ORDEM DE SERVIÇO (OS)
 * 
 * OBJETIVO PARA O TCC:
 * O coração do sistema da oficina. Gerencia a abertura, o status, o valor total 
 * e vincula o atendimento ao veículo e ao mecânico responsável.
 */
@Entity
@Table(name = "ordem_servico")
@Getter @Setter
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_os")
    private Integer id;

    @NotNull(message = "A data de abertura é obrigatória")
    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "Em aberto";

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @NotNull
    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @NotNull(message = "A OS deve estar vinculada a um veículo")
    @ManyToOne
    @JoinColumn(name = "id_veiculo", nullable = false)
    private Veiculo veiculo;

    @NotNull(message = "A OS deve estar vinculada a um mecânico")
    @ManyToOne
    @JoinColumn(name = "id_mecanico", nullable = false)
    private Mecanico mecanico;
}