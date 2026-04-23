package com.sapam.inventario.service;

import com.sapam.inventario.entity.Movimiento;
import com.sapam.inventario.entity.Producto;
import com.sapam.inventario.entity.Usuario;
import com.sapam.inventario.repository.MovimientoRepository;
import com.sapam.inventario.repository.ProductoRepository;
import com.sapam.inventario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;  // ¡Esto faltaba!

    @Transactional
    public void registrarSalida(Integer productoId, Integer cantidad, Integer usuarioId, String recibioPor, String observaciones) {
        // Buscar producto
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validar stock
        if (producto.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Stock actual: " + producto.getStockActual());
        }

        // Actualizar stock
        int nuevoStock = producto.getStockActual() - cantidad;
        producto.setStockActual(nuevoStock);
        productoRepository.save(producto);

        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setTipo(Movimiento.TipoMovimiento.salida);
        movimiento.setCantidad(cantidad);
        movimiento.setStockResultante(nuevoStock);
        movimiento.setUsuario(usuario);
        movimiento.setObservaciones(observaciones);
        movimiento.setFecha(LocalDateTime.now());

        movimientoRepository.save(movimiento);
    }
}