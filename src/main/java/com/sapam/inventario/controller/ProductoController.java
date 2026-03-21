package com.sapam.inventario.controller;

import com.sapam.inventario.entity.Producto;
import com.sapam.inventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listar() {
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @GetMapping("/codigo/{codigo}")
    public Producto obtenerPorCodigo(@PathVariable String codigo) {
        return productoService.buscarPorCodigoBarras(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @GetMapping("/stock-bajo")
    public List<Producto> stockBajo() {
        return productoService.productosConStockBajo();
    }

    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
    }
}