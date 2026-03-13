package com.tyrone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {

            // Load custom font
            Font.loadFont(
                getClass().getResourceAsStream("/font/Minecraft.ttf"),
                14
            );
    

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/fxml/style.css").toExternalForm()
            );

            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Home");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
