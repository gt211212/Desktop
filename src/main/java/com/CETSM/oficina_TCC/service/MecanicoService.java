package com.CETSM.oficina_TCC.service;

import com.CETSM.oficina_TCC.exception.RecursoNaoEncontradoException;
import com.CETSM.oficina_TCC.model.Mecanico;
import com.CETSM.oficina_TCC.repository.MecanicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SERVICE DE MECÂNICO
 * Gerencia o cadastro e as operações de negócio referentes aos profissionais da oficina.
 */
@Service
public class MecanicoService {

    @Autowired
    private MecanicoRepository mecanicoRepository;

    public List<Mecanico> listarTodos() {
        return mecanicoRepository.findAll();
    }

    public Mecanico buscarPorId(Integer id) {
        return mecanicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Mecânico com ID " + id + " não foi encontrado."));
    }

    @Transactional
    public Mecanico salvar(Mecanico mecanico) {
        return mecanicoRepository.save(mecanico);
    }

    @Transactional
    public Mecanico atualizar(Integer id, Mecanico mecanicoAtualizado) {
        Mecanico mecanicoExistente = buscarPorId(id);

        mecanicoExistente.setNome(mecanicoAtualizado.getNome());
        mecanicoExistente.setTelefone(mecanicoAtualizado.getTelefone());
        mecanicoExistente.setEspecialidade(mecanicoAtualizado.getEspecialidade());

        return mecanicoRepository.save(mecanicoExistente);
    }

    @Transactional
    public void excluir(Integer id) {
        buscarPorId(id);
        mecanicoRepository.deleteById(id);
    }
}