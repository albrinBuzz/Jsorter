package org.main.organizerfile.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.main.organizerfile.MainApplication;

import java.io.IOException;

public class HistoryViewAplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("history-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Organizador");
        stage.setMinWidth(670);
        stage.setMinHeight(400);
        stage.setMaxWidth(670);
        stage.setMaxHeight(400);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {

        launch();
    }
}