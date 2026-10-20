package com.CETSM.oficina_TCC.service;

import com.CETSM.oficina_TCC.exception.RecursoNaoEncontradoException;
import com.CETSM.oficina_TCC.model.Cliente;
import com.CETSM.oficina_TCC.repository.ClienteRepository;
import com.CETSM.oficina_TCC.util.CpfCnpjUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Listagem paginada (Substitui o findAll simples para grandes volumes)
    public Page<Cliente> listarComPaginacao(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente com ID " + id + " não foi encontrado."));
    }

    public Cliente salvar(Cliente cliente) {
        if (!CpfCnpjUtils.isCpfCnpjValido(cliente.getCpfCnpj())) {
            throw new IllegalArgumentException("O CPF ou CNPJ informado é inválido: " + cliente.getCpfCnpj());
        }
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Integer id, Cliente clienteAtualizado) {
        Cliente clienteExistente = buscarPorId(id);

        if (!CpfCnpjUtils.isCpfCnpjValido(clienteAtualizado.getCpfCnpj())) {
            throw new IllegalArgumentException("O CPF ou CNPJ informado é inválido: " + clienteAtualizado.getCpfCnpj());
        }

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setCpfCnpj(clienteAtualizado.getCpfCnpj());
        clienteExistente.setEndereco(clienteAtualizado.getEndereco());

        return clienteRepository.save(clienteExistente);
    }

    public void excluir(Integer id) {
        buscarPorId(id);
        clienteRepository.deleteById(id);
    }
}