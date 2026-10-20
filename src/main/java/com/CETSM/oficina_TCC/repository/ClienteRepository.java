package com.CETSM.oficina_TCC.repository;

import com.CETSM.oficina_TCC.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    // O JpaRepository já suporta Page<Cliente> findAll(Pageable pageable) nativamente!
}