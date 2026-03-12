package com.tyrone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Font.loadFont(getClass().getResource("/fonts/Minecraft-Regular.ttf").toExternalForm(), 12);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/home.fxml")); 

            Scene scene = new Scene(root);

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
