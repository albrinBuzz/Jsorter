package org.main.organizerfile.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import org.main.organizerfile.Clases.historial.Archivador;
import org.main.organizerfile.Clases.historial.Folder;
import org.main.organizerfile.MainApplication;
import org.main.organizerfile.MainController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HistoryController extends Pane implements Initializable {



    public ListView<Object> listViewHist;
    private Archivador archivador;
    @FXML
    public ListView<String> listViwExt;
    private ObservableList<Object> listHist;
    private MainController parentController;

    public void actualizarHist(){
        cargarHistorial();
        listViewHist.setItems(listHist);
    }

    private void cargarHistorial(){

        archivador.deserializar();

        listHist = FXCollections.observableArrayList();
        HashMap<String, Folder> historial=archivador.getCarpetas();
        for (Map.Entry<String, Folder> entry : historial.entrySet()) {
            String key=entry.getKey();
            String value=archivador.getCarpetas().get(key).isOrganizada()?" si ":"no";
            String organizacion=archivador.getCarpetas().get(key).getOrganizacion();
            //listHist.add(key+" | Organizada: "+value+"\n Tipo Organizacion: "+organizacion);

            Button button = new Button(key + " | Organizada: " + value + "\nTipo Organizacion: " + organizacion);

// Estilo del botón con azul oscuro y verde oscuro
            button.setStyle("-fx-font-size: 14px; "
                    + "-fx-font-family: Arial; "
                    + "-fx-text-fill: #FFFFFF; "  // Color del texto en blanco
                    + "-fx-background-color: #003366; "  // Azul oscuro como fondo
                    + "-fx-padding: 10px 20px; "
                    + "-fx-border-radius: 5px; "
                    + "-fx-border-color: #003366; "  // Bordes en azul oscuro
                    + "-fx-border-width: 2px;");  // Ancho del borde

// Efectos cuando el mouse pasa por encima (hover) con verde oscuro
            button.setOnMouseEntered(event -> {
                button.setStyle("-fx-font-size: 14px; "
                        + "-fx-font-family: Arial; "
                        + "-fx-text-fill: #FFFFFF; "  // Mantener el texto blanco
                        + "-fx-background-color: #2F4F4F; "  // Verde oscuro para el hover
                        + "-fx-padding: 10px 20px; "
                        + "-fx-border-radius: 5px; "
                        + "-fx-border-color: #2F4F4F; "  // Bordes en verde oscuro al pasar el mouse
                        + "-fx-border-width: 2px;");
            });
            button.setOnMouseExited(event -> {
                button.setStyle("-fx-font-size: 14px; "
                        + "-fx-font-family: Arial; "
                        + "-fx-text-fill: #FFFFFF; "  // Texto blanco
                        + "-fx-background-color: #003366; "  // Azul oscuro como fondo
                        + "-fx-padding: 10px 20px; "
                        + "-fx-border-radius: 5px; "
                        + "-fx-border-color: #003366; "  // Bordes en azul oscuro
                        + "-fx-border-width: 2px;");
            });

            // Acción del botón
            button.setOnAction(event -> {
                File file = new File(key);
                parentController.setearCarpeta(file);
            });

            listHist.add(button);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        archivador=new Archivador();
        actualizarHist();

    }

    public void setParentController(MainController mainController) {
         parentController=mainController;
    }
}
