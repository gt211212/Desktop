package com.CETSM.oficina_TCC.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    private String telefone;

    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "O CPF ou CNPJ é obrigatório")
    private String cpfCnpj;

    private String endereco;
}