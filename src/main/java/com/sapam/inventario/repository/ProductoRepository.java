package com.sapam.inventario.repository;

import com.sapam.inventario.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByCodigoBarras(String codigoBarras);
    List<Producto> findByStockActualLessThanEqual(Integer stockMinimo);
    
    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId")
    List<Producto> findByCategoriaId(@Param("categoriaId") Integer categoriaId);
}
