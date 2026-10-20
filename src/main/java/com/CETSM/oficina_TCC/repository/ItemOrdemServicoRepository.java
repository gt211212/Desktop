package com.CETSM.oficina_TCC.repository;

import com.CETSM.oficina_TCC.model.ItemOrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemOrdemServicoRepository extends JpaRepository<ItemOrdemServico, Integer> {
    // Permite buscar todos os itens vinculados a uma OS específica
    List<ItemOrdemServico> findByOrdemServicoId(Integer idOs);
}