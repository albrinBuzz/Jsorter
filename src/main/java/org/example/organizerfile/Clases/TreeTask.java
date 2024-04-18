package org.example.organizerfile.Clases;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem;
import org.example.organizerfile.HelloController;

import java.io.File;
import java.util.Objects;

public class TreeTask extends HiloAbst {
    private File directory;
    private TreeItem<String> parentItem;
    private HelloController controller;
    public TreeTask(File directory, TreeItem<String> parentItem, HelloController controller) {
        this.directory = directory;
        this.parentItem = parentItem;
        this.controller = controller;
    }
    private double countFiles(File directory) {
        if (directory.isFile()) {
            return 1;
        } else {
            double count = 0;
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    //cuenta los archivos de manera recursiva, por cada archivo. para poder 
                    //llegar a todos los niveles de profundidad
                    count += countFiles(file);
                }
            }
            return count;
        }
    }
    private void addNodes(File directory, TreeItem<String> parentItem) {
        TreeItem<String> rootItem = new TreeItem<>(directory.getName());
        // Agregar subdirectorios como hijos del nodo actual
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                progress++;
                addNodes(file, rootItem);
                /*if (file.isDirectory()) {
                    addNodes(file, rootItem);
                }*/
            }
        }
        // Agregar el nodo actual como hijo del nodo padre
        //lo hace de manera recursiva por cada nodo del padre
        parentItem.getChildren().add(rootItem);
    }

    @Override
    public void run() {
        total = (int) countFiles(directory);
        addNodes(directory, parentItem);
        // Actualizar la interfaz de usuario desde el hilo principal
        Platform.runLater(() -> {
            //controller.getBarTree().setProgress(1.0);
            controller.getTreeDirectorio().setVisible(true);
        });
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public int getTotal() {
        return total;
    }

    @Override
    protected void setProgress(int progress) {

    }

    @Override
    protected void setTotal(int total) {

    }
}
