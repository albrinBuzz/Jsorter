package org.example.organizerfile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Organizador");
        stage.setMinWidth(1102);
        stage.setMinHeight(660);
        stage.setMaxWidth(1102);
        stage.setMaxHeight(660);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {

        launch();
    }
}