package com.sapam.inventario.service;

import com.sapam.inventario.entity.Producto;
import com.sapam.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> buscarPorCodigoBarras(String codigo) {
        return productoRepository.findByCodigoBarras(codigo);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }

    public List<Producto> productosConStockBajo() {
        return productoRepository.findByStockActualLessThanEqual(5);
    }
}