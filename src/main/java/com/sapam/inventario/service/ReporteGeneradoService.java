package com.sapam.inventario.service;

import com.sapam.inventario.entity.ReporteGenerado;
import com.sapam.inventario.repository.ReporteGeneradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteGeneradoService {

    @Autowired
    private ReporteGeneradoRepository reporteGeneradoRepository;

    public ReporteGenerado guardar(ReporteGenerado reporte) {
        reporte.setFechaGeneracion(LocalDateTime.now());
        return reporteGeneradoRepository.save(reporte);
    }

    public List<ReporteGenerado> listarPorUsuario(Integer usuarioId) {
        return reporteGeneradoRepository.findByUsuarioId(usuarioId);
    }

    public List<ReporteGenerado> listarTodos() {
        return reporteGeneradoRepository.findAll();
    }
}
