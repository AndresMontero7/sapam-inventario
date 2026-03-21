package com.sapam.inventario.repository;

import com.sapam.inventario.entity.ReporteGenerado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReporteGeneradoRepository extends JpaRepository<ReporteGenerado, Integer> {
    List<ReporteGenerado> findByUsuarioId(Integer usuarioId);
}