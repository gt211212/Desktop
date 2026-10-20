package com.CETSM.oficina_TCC.repository;

import com.CETSM.oficina_TCC.model.ContaReceber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaReceberRepository extends JpaRepository<ContaReceber, Integer> {
}