package com.CETSM.oficina_TCC.repository;

import com.CETSM.oficina_TCC.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Integer> {
    // Aqui herdamos os métodos de persistência para a entidade Veiculo
} 
