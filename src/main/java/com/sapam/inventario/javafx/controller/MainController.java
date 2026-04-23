package com.sapam.inventario.javafx.controller;

import com.sapam.inventario.entity.Producto;
import com.sapam.inventario.entity.Usuario;
import com.sapam.inventario.javafx.JavaFxApplication;
import com.sapam.inventario.service.ProductoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.util.Optional;

@Controller
public class MainController {

    private ProductoService productoService;
    private Usuario usuarioActual;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> colCodigo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colCategoria;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    @FXML
    private TableColumn<Producto, Integer> colMinimo;

    @FXML
    private TableColumn<Producto, Void> colAcciones;

    @FXML
    private TextField txtBuscar;

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    @FXML
    public void initialize() {
        productoService = JavaFxApplication.getContext().getBean(ProductoService.class);
        
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(cellData -> {
            Producto p = cellData.getValue();
            if (p.getCategoria() != null) {
                return new SimpleStringProperty(p.getCategoria().getNombre());
            }
            return new SimpleStringProperty("");
        });
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colMinimo.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));
        
        configurarBotones();
        cargarProductos();
    }

    private void configurarBotones() {
        colAcciones.setCellFactory(col -> new TableCell<Producto, Void>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnSalida = new Button("Salida");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox pane = new HBox(5, btnEditar, btnSalida, btnEliminar);

            {
                btnEditar.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black;");
                btnSalida.setStyle("-fx-background-color: #17A2B8; -fx-text-fill: white;");
                btnEliminar.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white;");
                
                btnEditar.setOnAction(event -> {
                    Producto p = getTableView().getItems().get(getIndex());
                    editarProducto(p);
                });
                
                btnSalida.setOnAction(event -> {
                    Producto p = getTableView().getItems().get(getIndex());
                    darSalida(p);
                });
                
                btnEliminar.setOnAction(event -> {
                    Producto p = getTableView().getItems().get(getIndex());
                    eliminarProducto(p);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void cargarProductos() {
        if (productoService != null) {
            ObservableList<Producto> productos = FXCollections.observableArrayList(
                productoService.listarTodos()
            );
            tablaProductos.setItems(productos);
        }
    }
    @FXML
    private void buscarProducto() {
        String codigo = txtBuscar.getText();
        if (codigo == null || codigo.trim().isEmpty()) {
            cargarProductos();  // Si está vacío, mostrar todos
            return;
        }
        
        Optional<Producto> producto = productoService.buscarPorCodigoBarras(codigo);
        if (producto.isPresent()) {
            tablaProductos.setItems(FXCollections.observableArrayList(producto.get()));
        } else {
            // Si no encuentra, mostrar mensaje y volver a cargar todos
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Búsqueda");
            alert.setHeaderText(null);
            alert.setContentText("No se encontró producto con código: " + codigo);
            alert.showAndWait();
            
            // Limpiar el campo de búsqueda y recargar todos los productos
            txtBuscar.clear();
            cargarProductos();
        }
    }
   
    @FXML
    private void abrirNuevoProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/producto-form.fxml"));
            loader.setControllerFactory(JavaFxApplication.getContext()::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nuevo Producto");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cargarProductos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void generarReporte() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reporte Mensual");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidad de reporte en desarrollo");
        alert.showAndWait();
    }

    private void editarProducto(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/producto-form.fxml"));
            loader.setControllerFactory(JavaFxApplication.getContext()::getBean);
            Parent root = loader.load();
            ProductoFormController controller = loader.getController();
            controller.setProducto(producto);
            Stage stage = new Stage();
            stage.setTitle("Editar Producto");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cargarProductos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void darSalida(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/salida-form.fxml"));
            loader.setControllerFactory(JavaFxApplication.getContext()::getBean);
            Parent root = loader.load();
            SalidaFormController controller = loader.getController();
            controller.setProducto(producto);
            controller.setUsuarioActual(usuarioActual);
            Stage stage = new Stage();
            stage.setTitle("Dar Salida - " + producto.getNombre());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cargarProductos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarProducto(Producto producto) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar producto: " + producto.getNombre() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            productoService.eliminar(producto.getId());
            cargarProductos();
        }
    }

    @FXML
    private void cerrarSesion() {
        Stage stage = (Stage) tablaProductos.getScene().getWindow();
        stage.close();
    }
}