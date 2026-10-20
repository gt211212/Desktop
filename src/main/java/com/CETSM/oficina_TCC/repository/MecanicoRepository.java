package com.CETSM.oficina_TCC.repository;

import com.CETSM.oficina_TCC.model.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MecanicoRepository extends JpaRepository<Mecanico, Integer> {
}