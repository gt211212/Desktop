package com.CETSM.oficina_TCC.service;

import com.CETSM.oficina_TCC.exception.RecursoNaoEncontradoException;
import com.CETSM.oficina_TCC.model.Cliente;
import com.CETSM.oficina_TCC.model.Veiculo;
import com.CETSM.oficina_TCC.repository.ClienteRepository;
import com.CETSM.oficina_TCC.repository.VeiculoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SERVICE DE VEÍCULO
 * 
 * OBJETIVO PARA O TCC:
 * Gerencia as regras de negócio dos veículos da oficina. Como cada veículo depende 
 * de um cliente proprietário, este service valida a existência do cliente antes 
 * de permitir cadastros ou atualizações, garantindo a integridade referencial.
 */
@Service // Indica ao Spring que esta classe gerencia regras de negócio
public class VeiculoService {

    // Injeção do repositório de veículos para acesso ao banco de dados
    @Autowired
    private VeiculoRepository veiculoRepository;

    // Injeção do repositório de clientes para validar se o dono do veículo existe
    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Lista todos os veículos cadastrados no sistema.
     */
    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    /**
     * Busca um veículo específico pelo ID.
     * Caso não exista, dispara a exceção personalizada gerando um status HTTP 404.
     */
    public Veiculo buscarPorId(Integer id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo com ID " + id + " não foi encontrado."));
    }

    /**
     * Salva um novo veículo no banco de dados.
     * Regra de negócio: Valida se o ID do cliente enviado na requisição realmente existe na base.
     */
    @Transactional
    public Veiculo salvar(Veiculo veiculo) {
        // Valida se o objeto veículo e o cliente associado foram informados
        if (veiculo.getCliente() == null || veiculo.getCliente().getId() == null) {
            throw new IllegalArgumentException("O veículo deve estar vinculado a um ID de cliente válido.");
        }

        Integer idCliente = veiculo.getCliente().getId();

        // Busca o cliente no banco. Se não existir, lança RecursoNaoEncontradoException (404)
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente proprietário com ID " + idCliente + " não foi encontrado."));

        // Associa o cliente completo recuperado do banco ao veículo
        veiculo.setCliente(cliente);

        // Salva e retorna o veículo persistido
        return veiculoRepository.save(veiculo);
    }

    /**
     * Atualiza os dados de um veículo existente.
     */
    @Transactional
    public Veiculo atualizar(Integer id, Veiculo veiculoAtualizado) {
        // Garante que o veículo a ser atualizado realmente existe
        Veiculo veiculoExistente = buscarPorId(id);

        // Valida se o novo cliente associado foi informado
        if (veiculoAtualizado.getCliente() == null || veiculoAtualizado.getCliente().getId() == null) {
            throw new IllegalArgumentException("O veículo deve estar vinculado a um ID de cliente válido.");
        }

        Integer idCliente = veiculoAtualizado.getCliente().getId();

        // Valida se o novo cliente proprietário existe no banco
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente proprietário com ID " + idCliente + " não foi encontrado."));

        // Atualiza os atributos do veículo existente
        veiculoExistente.setPlaca(veiculoAtualizado.getPlaca());
        veiculoExistente.setModelo(veiculoAtualizado.getModelo());
        veiculoExistente.setMarca(veiculoAtualizado.getMarca());
        veiculoExistente.setAno(veiculoAtualizado.getAno());
        veiculoExistente.setCliente(cliente); // Atualiza o vínculo com o cliente

        // Salva e retorna as alterações no banco
        return veiculoRepository.save(veiculoExistente);
    }

    /**
     * Exclui um veículo do sistema pelo ID.
     */
    @Transactional
    public void excluir(Integer id) {
        // Garante que o veículo existe antes de apagar
        buscarPorId(id);
        veiculoRepository.deleteById(id);
    }
}