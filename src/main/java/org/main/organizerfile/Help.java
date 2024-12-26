package org.main.organizerfile;

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
        helpStage.setTitle("Ayuda");
        helpStage.initModality(Modality.APPLICATION_MODAL);

        // Crear un contenedor principal
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Crear un texto con la información de la ayuda
        Label helpText = new Label("Esta es la página de ayuda de la aplicación.\n\n"
                + "Aquí puedes proporcionar instrucciones sobre cómo utilizar la aplicación.");

        // Crear un botón para cerrar la ventana de ayuda
        Button closeButton = new Button("Cerrar");
        closeButton.setOnAction(event -> helpStage.close());

        // Crear un contenedor para los créditos
        VBox creditsBox = new VBox();
        creditsBox.setSpacing(5);
        creditsBox.getChildren().addAll(new Label("Créditos:"), new Label("Desarrollado por: [Tu nombre]"));

        // Agregar los elementos al contenedor principal
        root.setCenter(helpText);
        root.setBottom(closeButton);
        root.setRight(creditsBox);

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
