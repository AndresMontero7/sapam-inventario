package com.sapam.inventario.javafx.controller;

import com.sapam.inventario.entity.Categoria;
import com.sapam.inventario.entity.Producto;
import com.sapam.inventario.service.CategoriaService;
import com.sapam.inventario.service.ProductoService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProductoFormController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtCategoria;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtMinimo;

    private Producto producto;

    @FXML
    public void initialize() {
        this.producto = new Producto();
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        txtCodigo.setText(producto.getCodigoBarras());
        txtNombre.setText(producto.getNombre());
        txtDescripcion.setText(producto.getDescripcion());
        if (producto.getCategoria() != null) {
            txtCategoria.setText(producto.getCategoria().getNombre());
        }
        txtStock.setText(String.valueOf(producto.getStockActual()));
        txtMinimo.setText(String.valueOf(producto.getStockMinimo()));
    }

    @FXML
    private void guardarProducto() {
        try {
            producto.setCodigoBarras(txtCodigo.getText());
            producto.setNombre(txtNombre.getText());
            producto.setDescripcion(txtDescripcion.getText());
            
            String nombreCategoria = txtCategoria.getText();
            if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
                Categoria categoria = categoriaService.buscarPorNombre(nombreCategoria)
                        .orElseGet(() -> {
                            Categoria nueva = new Categoria();
                            nueva.setNombre(nombreCategoria);
                            return categoriaService.guardar(nueva);
                        });
                producto.setCategoria(categoria);
            } else {
                producto.setCategoria(null);
            }
            
            producto.setStockActual(Integer.parseInt(txtStock.getText()));
            producto.setStockMinimo(Integer.parseInt(txtMinimo.getText()));

            productoService.guardar(producto);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Producto guardado correctamente");
            alert.showAndWait();

            Stage stage = (Stage) txtCodigo.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al guardar: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) txtCodigo.getScene().getWindow();
        stage.close();
    }
}