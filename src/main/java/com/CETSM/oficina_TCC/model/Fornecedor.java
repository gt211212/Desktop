package com.CETSM.oficina_TCC.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * ENTIDADE FORNECEDOR
 * 
 * OBJETIVO PARA O TCC:
 * Gerencia os fornecedores de autopeças, servindo de base para o controle de estoque 
 * e para o módulo de contas a pagar.
 */
@Entity
@Table(name = "fornecedor")
@Getter
@Setter
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor")
    private Integer id;

    @NotBlank(message = "O nome ou razão social é obrigatório")
    @Column(name = "nome_razao", nullable = false, length = 100)
    private String nomeRazao;

    @NotBlank(message = "O telefone é obrigatório")
    @Column(name = "telefone", nullable = false, unique = true, length = 20)
    private String telefone;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
}