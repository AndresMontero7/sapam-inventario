package com.sapam.inventario.service;

import com.sapam.inventario.entity.AjusteStock;
import com.sapam.inventario.entity.Producto;
import com.sapam.inventario.repository.AjusteStockRepository;
import com.sapam.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AjusteStockService {

    @Autowired
    private AjusteStockRepository ajusteStockRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public AjusteStock realizarAjuste(Integer productoId, Integer nuevoStock, String motivo, Integer usuarioId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        int stockAnterior = producto.getStockActual();

        AjusteStock ajuste = new AjusteStock();
        ajuste.setProducto(producto);
        ajuste.setStockAnterior(stockAnterior);
        ajuste.setStockNuevo(nuevoStock);
        ajuste.setMotivo(motivo);
        ajuste.setFecha(LocalDateTime.now());

        producto.setStockActual(nuevoStock);
        productoRepository.save(producto);

        return ajusteStockRepository.save(ajuste);
    }

    public List<AjusteStock> ajustesPorProducto(Integer productoId) {
        return ajusteStockRepository.findByProductoId(productoId);
    }
}