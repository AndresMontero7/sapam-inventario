package com.sapam.inventario.javafx.controller;

import com.sapam.inventario.entity.Producto;
import com.sapam.inventario.service.MovimientoService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SalidaFormController {

    @Autowired
    private MovimientoService movimientoService;

    @FXML
    private Label lblProducto;

    @FXML
    private Label lblStock;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtRecibio;

    private Producto producto;

    public void setProducto(Producto producto) {
        this.producto = producto;
        lblProducto.setText(producto.getNombre());
        lblStock.setText(String.valueOf(producto.getStockActual()));
    }

    @FXML
    private void guardarSalida() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            String recibio = txtRecibio.getText();

            if (recibio.isEmpty()) {
                throw new Exception("Debe indicar quién recibe");
            }

            // Usuario ID 1 (admin) - temporal
            Integer usuarioId = 1;
            
            movimientoService.registrarSalida(producto.getId(), cantidad, usuarioId, recibio, "Salida desde interfaz");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Salida registrada correctamente");
            alert.showAndWait();

            Stage stage = (Stage) txtCantidad.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Cantidad debe ser un número");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) txtCantidad.getScene().getWindow();
        stage.close();
    }
}