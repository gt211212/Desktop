package com.CETSM.oficina_TCC.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * ENTIDADE VEÍCULO
 * 
 * OBJETIVO PARA O TCC:
 * Gerencia os veículos da oficina, mantendo o relacionamento de N para 1 com a entidade Cliente 
 * (vários veículos pertencem a um único cliente).
 */
@Entity
@Table(name = "veiculo")
@Getter @Setter
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veiculo")
    private Integer id;

    @NotBlank(message = "A placa do veículo é obrigatória")
    @Column(name = "placa", nullable = false, unique = true, length = 10)
    private String placa;

    @NotBlank(message = "O modelo do veículo é obrigatório")
    @Column(name = "modelo", nullable = false, length = 50)
    private String modelo;

    @NotBlank(message = "O marca do veículo é obrigatória")
    @Column(name = "marca", nullable = false, length = 50)
    private String marca;

    @Column(name = "ano")
    private Integer ano;

    @NotNull(message = "O veículo deve estar associado a um cliente")
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente; // Chave estrangeira ligando ao proprietário
}