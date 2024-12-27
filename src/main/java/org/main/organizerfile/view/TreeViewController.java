package org.main.organizerfile.view;

import javafx.application.Platform;
import javafx.fxml.Initializable;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.main.organizerfile.MainController;

public class TreeViewController extends Pane implements Initializable {

    @FXML
    public TreeView<String> treeDirectorio;
    public Label titleLabel;
    public Label descriptionLabel;
    File directory;
    @FXML
    public AnchorPane rootPane; // Solo para referencia si necesitas trabajar con otros elementos
    private MainController parentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void addNodes(File directory, TreeItem<String> parentItem) {
        TreeItem<String> rootItem = new TreeItem<>(directory.getName());
        // Agregar subdirectorios como hijos del nodo actual
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {

                addNodes(file, rootItem);

            }
        }
        // Agregar el nodo actual como hijo del nodo padre
        parentItem.getChildren().add(rootItem);
    }

    /**
     * Método principal de ejecución de la tarea del árbol.
     * Cuenta los archivos en el directorio y agrega nodos al árbol.
     */

    public void setearCarpeta(File directory) {
        double tiempo = System.nanoTime();
        //total = (int) countFiles(directory);
        TreeItem<String> rootItem = new TreeItem<>("Carpeta: "+directory.getName());
        titleLabel.setText("Distribución de Archivos y Directorios : "+directory.getName());
        descriptionLabel.setText("Contenido de la carpeta '" + directory.getName() + "' mostrado a continuación.");
        treeDirectorio.setRoot(rootItem);
        treeDirectorio.setStyle("-fx-background-color: #E6E6E6;");
        treeDirectorio.setEditable(false);
        addNodes(directory, rootItem);

    }

    public void setParentController(MainController mainController) {
        parentController=mainController;
    }

}
