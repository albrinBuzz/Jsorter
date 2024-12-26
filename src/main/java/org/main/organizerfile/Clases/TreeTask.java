package org.main.organizerfile.Clases;

import javafx.scene.control.TreeItem;
import org.main.organizerfile.HelloController;

import java.io.File;
import java.util.Arrays;

/**
 * Clase que representa una tarea para generar la estructura de directorios en forma de árbol de archivos.
 * Utiliza hilos para realizar operaciones de manera asíncrona y no bloquear la interfaz de usuario.
 */

public class TreeTask extends HiloAbst {
    private File directory;
    private TreeItem<String> parentItem;
    private HelloController controller;

    /**
     * Constructor de la tarea del árbol.
     *
     * @param directory   Directorio raíz.
     * @param parentItem  Nodo padre del árbol.
     * @param controller  Controlador de la interfaz.
     */
    public TreeTask(File directory, TreeItem<String> parentItem, HelloController controller) {
        this.directory = directory;
        this.parentItem = parentItem;
        this.controller = controller;
    }


    /**
     * Método para contar la cantidad de archivos en un directorio recursivamente.
     *
     * @param directory Directorio a contar.
     * @return Cantidad de archivos.
     */

    private double countFiles(File directory) {
        if (directory.isFile()) {
            return 1;
        } else {
            double count = 0;
            File[] files = directory.listFiles();
            System.out.println(Arrays.toString(files));
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

    /**
     * Método para agregar nodos al árbol representando la estructura de directorios.
     *
     * @param directory  Directorio actual.
     * @param parentItem Nodo padre del árbol.
     */
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
        parentItem.getChildren().add(rootItem);
    }

    /**
     * Método principal de ejecución de la tarea del árbol.
     * Cuenta los archivos en el directorio y agrega nodos al árbol.
     */
    @Override
    public void run() {
        double tiempo = System.nanoTime();
        /*total = (int) countFiles(directory);
        System.out.println("Version  concurrente: "+ ((System.nanoTime() - tiempo) / 1000000) + " milisegundos");
        addNodes(directory, parentItem);
        // Actualizar la interfaz de usuario desde el hilo principal
        Platform.runLater(() -> {
            //controller.getBarTree().setProgress(1.0);
            controller.getTreeDirectorio().setVisible(true);
        });*/
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
