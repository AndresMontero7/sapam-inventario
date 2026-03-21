package com.sapam.inventario.repository;

import com.sapam.inventario.entity.AjusteStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AjusteStockRepository extends JpaRepository<AjusteStock, Integer> {
    List<AjusteStock> findByProductoId(Integer productoId);
}