package com.sapam.inventario.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.sapam.inventario")
public class JavaFxApplication extends Application {

    private static ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = SpringApplication.run(JavaFxApplication.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("SAPAM - Sistema de Inventario");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        context.close();
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
