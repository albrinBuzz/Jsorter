package org.main.organizerfile.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.main.organizerfile.MainApplication;

import java.io.IOException;

public class TreeViewAplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("tree-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Organizador");
        stage.setMinWidth(1292);
        stage.setMinHeight(751);
        stage.setMaxWidth(1292);
        stage.setMaxHeight(751);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {

        launch();
    }
}