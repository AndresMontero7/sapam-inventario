package com.sapam.inventario.service;

import com.sapam.inventario.entity.Movimiento;
import com.sapam.inventario.entity.Producto;
import com.sapam.inventario.repository.MovimientoRepository;
import com.sapam.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Movimiento> listarTodos() {
        return movimientoRepository.findAll();
    }

    public List<Movimiento> buscarPorProducto(Integer productoId) {
        return movimientoRepository.findByProductoId(productoId);
    }

    @Transactional
    public void registrarEntrada(Integer productoId, Integer cantidad, Integer usuarioId, String observaciones) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        int stockAnterior = producto.getStockActual();
        int stockNuevo = stockAnterior + cantidad;

        producto.setStockActual(stockNuevo);
        productoRepository.save(producto);

        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setTipo(Movimiento.TipoMovimiento.entrada);
        movimiento.setCantidad(cantidad);
        movimiento.setStockResultante(stockNuevo);
        movimiento.setObservaciones(observaciones);
        movimiento.setFecha(LocalDateTime.now());

        movimientoRepository.save(movimiento);
    }

    @Transactional
    public void registrarSalida(Integer productoId, Integer cantidad, Integer usuarioId, String recibioPor, String observaciones) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        int stockAnterior = producto.getStockActual();
        int stockNuevo = stockAnterior - cantidad;

        producto.setStockActual(stockNuevo);
        productoRepository.save(producto);

        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setTipo(Movimiento.TipoMovimiento.salida);
        movimiento.setCantidad(cantidad);
        movimiento.setStockResultante(stockNuevo);
        movimiento.setObservaciones(observaciones);
        movimiento.setFecha(LocalDateTime.now());

        movimientoRepository.save(movimiento);
    }
}