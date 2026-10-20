package com.CETSM.oficina_TCC.repository;

import com.CETSM.oficina_TCC.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
    // Futuramente, podemos adicionar buscas personalizadas aqui, como buscar por código de barras:
    // Optional<Estoque> findByCodigoBarras(String codigoBarras);
}