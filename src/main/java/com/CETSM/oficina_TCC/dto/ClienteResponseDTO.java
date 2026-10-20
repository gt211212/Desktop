package com.CETSM.oficina_TCC.dto;

import com.CETSM.oficina_TCC.model.Cliente;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteResponseDTO {
    private Integer id;
    private String nome;
    private String telefone;
    private String email;
    private String cpfCnpj;
    private String endereco;

    // Construtor que converte a Entidade em DTO
    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.telefone = cliente.getTelefone();
        this.email = cliente.getEmail();
        this.cpfCnpj = cliente.getCpfCnpj();
        this.endereco = cliente.getEndereco();
    }
}