package com.sapam.inventario.javafx.controller;

import com.sapam.inventario.entity.Usuario;
import com.sapam.inventario.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.Optional;

@Controller
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @Autowired
    private AuthService authService;

    @FXML
    private void iniciarSesion() {
        String email = usernameField.getText();
        String password = passwordField.getText();

        Optional<Usuario> usuarioOpt = authService.obtenerUsuarioPorEmail(email);
        
        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                loader.setControllerFactory(com.sapam.inventario.javafx.JavaFxApplication.getContext()::getBean);
                Parent root = loader.load();
                
                MainController mainController = loader.getController();
                mainController.setUsuarioActual(usuarioOpt.get());
                
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("SAPAM - Inventario");
                stage.setMaximized(true);
                
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error al cargar la aplicación");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Login");
            alert.setHeaderText(null);
            alert.setContentText("Usuario o contraseña incorrectos");
            alert.showAndWait();
        }
    }
}