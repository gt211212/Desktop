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


@Entity
@Table(name = "cliente")
@Getter @Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id; // Chave primária autoincrementada do cliente

    @NotBlank(message = "O nome do cliente não pode estar em branco")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O telefone é obrigatório")
    @Column(name = "telefone", nullable = false, unique = true, length = 20)
    private String telefone;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "cpf_cnpj", unique = true, length = 20)
    private String cpfCnpj;

    @Column(name = "endereco", length = 100)
    private String endereco;
}