package org.example.organizerfile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Help extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Crear la ventana de ayuda
        Stage helpStage = new Stage();
       
        // Crear la escena y mostrar la ventana de ayuda
        Scene scene = new Scene(root, 400, 300);
        helpStage.setScene(scene);
        stage.setMinWidth(602);
        stage.setMinHeight(630);
        stage.setMaxWidth(602);
        stage.setMaxHeight(630);
        stage.setScene(scene);
        helpStage.show();
    }
}
