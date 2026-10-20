package com.CETSM.oficina_TCC.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * ENTIDADE MECÂNICO
 * 
 * OBJETIVO PARA O TCC:
 * Armazena os dados dos profissionais da oficina, permitindo vinculá-los diretamente 
 * às Ordens de Serviço executadas.
 */
@Entity
@Table(name = "mecanico")
@Getter @Setter
public class Mecanico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mecanico")
    private Integer id;

    @NotBlank(message = "O nome do mecânico é obrigatório")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O telefone é obrigatório")
    @Column(name = "telefone", nullable = false, unique = true, length = 20)
    private String telefone;

    @Column(name = "especialidade", length = 50)
    private String especialidade;
}