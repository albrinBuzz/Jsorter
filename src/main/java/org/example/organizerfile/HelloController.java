package org.example.organizerfile;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.organizerfile.Clases.Archivos;
import org.example.organizerfile.Clases.Organizador;
import org.example.organizerfile.Clases.Task;
import org.example.organizerfile.Clases.TreeTask;
import org.example.organizerfile.Clases.historial.Archivador;
import org.example.organizerfile.Clases.historial.Folder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {
    public TextField textFDirect;
    public ProgressBar barTree;
    public ToggleButton btnTypee;
    public ToggleButton btnExtensionn;
    public ToggleButton btnCustoo;
    @FXML
    private Label lblJerarquia;
    @FXML
    private ProgressIndicator prInd;
    private ProgressIndicator brOrg;
    @FXML
    private Label lblAdmin;
    @FXML
    private ProgressBar barOrg;
    @FXML
    private Label lblTreeDirectorio;
    @FXML
    private TreeView<String> treeDirectorio;
    private TreeItem<String> rootItem ;
    @FXML
    private ListView<String> listviewCustom;
    @FXML
    private ListView<String> listViewHist;
    @FXML
    private Label lblTipo;
    @FXML
    private Label lblOrganizado;
    @FXML
    private Button btnDeshacer;
    @FXML
    private Button btnActualizar;
    @FXML
    private TabPane tabPExt;
    @FXML
    private MenuBar menu;
    private Organizador organizador;
    @FXML
    private Button btnAgregarCarpt;
    private ToggleGroup group;
    @FXML
    private CheckBox btnExt;
    @FXML
    private Button btnOrg;
    @FXML
    private CheckBox btnPers;
    @FXML
    private CheckBox btnType;
    @FXML
    private Label lblCant;
    private File carpeta;
    @FXML
    private Label lblCarp;
    @FXML
    private ListView<String> listViwExt;
    @FXML
    private TextArea textADetalle;
    private ObservableList<String> list;
    private ObservableList<String> listCust;
    private ObservableList<String> listHist;
    private Archivos archivos;
    private Archivador archivador;
    private Task<Organizador> task;
    private Task<TreeTask> task2;
    // Agrega estas líneas al principio de tu clase HelloController:
// Paleta de colores sugerida
// Paleta de colores sugerida
    private final String primaryColor = "#1976D2";  // Azul primario oscuro
    private final String secondaryColor = "#FF5722"; // Naranja oscuro
    private final String accentColor = "#FFC107";   // Amarillo de acento
    private final String backgroundColor = "#263238"; // Gris oscuro como color de fondo
    private final String textColor = "#FFFFFF";  // Color de texto blanco para contraste

    @FXML
    void agregarCarpeta(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar Directorio");
        File selectedDirectory = directoryChooser.showDialog(btnExtensionn.getScene().getWindow());
        if (selectedDirectory != null) {
            lblCarp.setText("Carpeta: \n"+selectedDirectory.getName());
            lblCant.setText("Items: "+(Objects.requireNonNull(selectedDirectory.listFiles()).length));
            carpeta=selectedDirectory;
            organizador=new Organizador(archivador,archivos, carpeta.getAbsolutePath());
            btnOrg.setDisable(false);
            btnDeshacer.setDisable(false);
            updateTree();
            if (archivador.contains(carpeta.getAbsolutePath())){
                String ordenado= null;
                try {
                    ordenado = archivador.ordenado(carpeta.getAbsolutePath())?" si ":"no";
                    lblOrganizado.setText("Organizado: "+ordenado);
                } catch (Exception e) {
                      lblOrganizado.setText("Organizado: No");
                    //throw new RuntimeException(e);
                }
                lblOrganizado.setText("Organizado: "+ordenado);
            }else lblOrganizado.setText("Organizado: No");
            lblOrganizado.setStyle("-fx-font-weight: bold; -fx-text-fill:  #ffff00;");
        }
    }
    @FXML
   private void organizar(ActionEvent event) {
        organizador.setOrgan(true);
        try {
            archivador.deserializar();
            if (btnExtensionn.isSelected()) {
                System.out.println(btnExtensionn.getText());
                setTask(0,btnExtensionn.getText());
            } else if (btnCustoo.isSelected()) {
                System.out.println(btnCustoo.getText());

                    setTask(1,btnCustoo.getText());
            } else {
                System.out.println(btnTypee.getText());

                setTask(2,btnTypee.getText());
            }
        }
        catch (NullPointerException e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Debe elegir una carpeta antes de organizar");
            alert.showAndWait();
        }
        catch (Exception e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }finally {
            lblOrganizado.setText("Organizado: Si");

            try {
                lblCant.setText("Items: " + (Objects.requireNonNull(carpeta.listFiles()).length));
                updateTree();
            }catch (NullPointerException e){

            }

        }
    }

    private void setTask(int num,String type) throws Exception {
        organizador.setTipo(num);
        organizador.setDirecotorio(carpeta);
        organizador.setOrg(type);
        task=new Task<>(organizador);
        Thread backgroundThread = new Thread(task);
        barOrg.progressProperty().bind(task.progressProperty());
        task.call();
        //backgroundThread.start();
        actualizarHist();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblTipo.setText("Tipo De Organizacion");
        btnDeshacer.setText("Deshacer \n Cambios");
        btnOrg.setText("Organizar \n Carpeta");
        /*btnExt.setOnAction(event -> uncheckOtherBoxes(btnExt,btnPers,btnType));
        btnType.setOnAction(event -> uncheckOtherBoxes(btnType,btnExt,btnPers));
        btnPers.setOnAction(event -> uncheckOtherBoxes(btnPers,btnType,btnExt));*/
        group=new ToggleGroup();

        btnTypee.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                btnTypee.setStyle("-fx-background-color: #009688; -fx-border-color: #b3b3b3; -fx-border-width: 2px; -fx-padding: 5px;");
            } else {
                btnTypee.setStyle("-fx-background-color: white; -fx-border-color: #b3b3b3; -fx-border-width: 2px; -fx-padding: 5px;");
            }
        });
        btnCustoo.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                btnCustoo.setStyle("-fx-background-color: #009688; -fx-border-color: #b3b3b3; -fx-border-width: 2px; -fx-padding: 5px;");
            } else {
                btnCustoo.setStyle("-fx-background-color: white; -fx-border-color: #b3b3b3; -fx-border-width: 2px; -fx-padding: 5px;");
            }
        });
        btnExtensionn.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                btnExtensionn.setStyle("-fx-background-color: #009688; -fx-border-color: #b3b3b3; -fx-border-width: 2px; -fx-padding: 5px;");
            } else {
                btnExtensionn.setStyle("-fx-background-color: white; -fx-border-color: #b3b3b3; -fx-border-width: 2px; -fx-padding: 5px;");
            }
        });
        btnTypee.setToggleGroup(group);
        btnCustoo.setToggleGroup(group);
        btnExtensionn.setToggleGroup(group);
        group.selectToggle(btnTypee);


        group.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
            }
        });

        /*group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                // Si no hay ningún botón seleccionado, vuelve a seleccionar el botón anterior
                oldValue.setSelected(true);
            }

        });*/
        /*btnType.setOnAction(event -> {
            if (btnType.isSelected()) {
                btnPers.setSelected(false);
                btnExt.setSelected(false);
            } else {
                btnTypee.setSelected(true);
            }
        });*/


        archivador=new Archivador();
        cargarHistorial();
        listViewHist.setItems(listHist);
        archivos=new Archivos();
        archivos.traerDatos();
        cargarLista();
        btnOrg.setDisable(true);
        btnDeshacer.setDisable(true);
        listViwExt.setItems(list);
        listViwExt.setPrefHeight(200);
        //listViwExt.setCellFactory(param -> new SeparatorListCell<>());
        listViwExt.setStyle("-fx-background-color: black;");
        List<Menu> list1=menu.getMenus();

        list1.get(1).getItems().add(new MenuItem("Agregar Extension"));
        //list1.get(1).getItems().get(0).setStyle("-fx-background-color: black; -fx-text-fill:  white;");
        list1.get(1).getItems().add(new MenuItem("Eliminar Extension"));
        //list1.get(1).getItems().get(1).setStyle("-fx-background-color: black; -fx-text-fill:  white;");
        list1.get(1).getItems().add(new MenuItem("Agregar Categoria"));
        //list1.get(1).getItems().get(2).setStyle("-fx-background-color: black; -fx-text-fill:  white;");
        list1.get(1).getItems().add(new MenuItem("Eliminar Categoria"));
        //list1.get(1).getItems().get(3).setStyle("-fx-background-color: black; -fx-text-fill:  white;");
        list1.get(1).getItems().get(0).setOnAction(event -> {
            agregarExt();
        });
        list1.get(1).getItems().get(1).setOnAction(event -> {
            eliminarExt();
        });
        list1.get(1).getItems().get(2).setOnAction(event -> {
            agregarCat();
        });
        list1.get(1).getItems().get(3).setOnAction(event -> {
            eliminarCat();
        });
        list1.get(2).getItems().get(0).setOnAction(event -> {
            Stage stage=new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("help.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setTitle("Organizador");
            stage.setMinWidth(602);
            stage.setMinHeight(630);
            stage.setMaxWidth(602);
            stage.setMaxHeight(630);
            stage.setScene(scene);
            stage.show();
        });
        cargarListaCustom();
        listviewCustom.setItems(listCust);
        rootItem = new TreeItem<>("Directorios");
        treeDirectorio.setRoot(rootItem);
        treeDirectorio.setStyle("-fx-background-color: #E6E6E6;");
        //treeDirectorio.getTreeItem(0).setValue("-fx-font-weight: bold;");
        treeDirectorio.setEditable(false);
// Asignación de colores en el método initialize()
        treeDirectorio.setStyle("-fx-background-color: " + backgroundColor + ";");
        listviewCustom.setStyle("-fx-background-color: " + backgroundColor + ";");
        listViewHist.setStyle("-fx-background-color: " + backgroundColor + ";");
        lblTipo.setStyle("-fx-font-weight: bold; -fx-text-fill: " + textColor + ";");
        //lblOrganizado.setStyle("-fx-font-weight: bold; -fx-text-fill: " + textColor + ";");
        //btnDeshacer.setStyle("-fx-background-color: " + accentColor + "; -fx-text-fill: " + backgroundColor + ";");
        btnDeshacer.setStyle("-fx-background-color: " + primaryColor + "; -fx-text-fill: " + textColor + ";");
        btnAgregarCarpt.setStyle("-fx-background-color: " + primaryColor + "; -fx-text-fill: " + textColor + ";");
        btnOrg.setStyle("-fx-background-color: " + primaryColor + "; -fx-text-fill: " + textColor + ";");
        listviewCustom.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textColor + ";");
        listViewHist.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textColor + ";");

    }
    private void agregarExt(){
        String extension = null;
        try {
            TextInputDialog extensionDialog = new TextInputDialog();
            extensionDialog.setTitle("Agregar Extensión");
            extensionDialog.setHeaderText("Ingrese la nueva extensión:");
            extensionDialog.setContentText("Extensión:");
            extensionDialog.showAndWait();
                extension = extensionDialog.getResult();
            if (!extension.isEmpty()) {
                Set<String> categoris = archivos.getCustom().keySet();
                ChoiceDialog<String> dialog = new ChoiceDialog<>("Categorias", categoris);
                dialog.showAndWait();
                String selectedOption = dialog.getSelectedItem();
                archivos.agregarExtension(extension, selectedOption);
            }
        }catch (Exception e){
            assert extension != null;
            try {
                if (!extension.isEmpty()){
                    Alert alert =new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("ERROR!");
                    alert.setHeaderText(e.getMessage());
                    alert.show();
                }
            }catch (Exception exception){
                System.out.println(e.getMessage());
            }
        }finally {
            actualizar();
        }
    }
    private void eliminarExt(){
        Set<String> categoris=archivos.getCustom().keySet();
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Categorias", categoris);
        dialog.showAndWait();
        String categorie = dialog.getSelectedItem();
        Set<String>extenions=archivos.getCustom().get(categorie);
        ChoiceDialog<String> dialog2 = new ChoiceDialog<>("Extensiones", extenions);
        dialog2.showAndWait();
        String extension = dialog2.getSelectedItem();
        if (!extension.isEmpty()) {
            try {
                System.out.println(extension);
                archivos.eliminarExtension(extension,categorie);
            }catch (Exception e){
                Alert
                alert =new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ERROR!");
                alert.setHeaderText(e.getMessage());
                alert.show();
            }finally {
                actualizar();
            }
        }
        actualizar();
    }
    private void agregarCat(){
        String result = null;
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Agregar Categoria");
            dialog.setHeaderText("Ingrese la nueva Categoria:");
            dialog.setContentText("Categoria:");
            dialog.showAndWait();
            result = dialog.getResult();
            archivos.agregarCategoria(result);
            actualizar();
        }catch (Exception e){
            try {
                if (!result.isEmpty()){
                    Alert alert =new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("ERROR!");
                    alert.setHeaderText(e.getMessage());
                    alert.show();
                }
            }catch (Exception exception){
                System.out.println(e.getMessage());
            }
        }finally {
            actualizar();
        }
    }
    private void eliminarCat(){
        Set<String> categoris=archivos.getCustom().keySet();
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Categorias", categoris);
        dialog.showAndWait();
        String categorie = dialog.getSelectedItem();

        if (!categorie.isEmpty()) {
            try {
                archivos.eliminarCategoria(categorie);
                actualizar();
            }catch (Exception e){
                Alert alert =new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ERROR!");
                alert.setHeaderText("ha ocurrido el siguiente");
                alert.setContentText(e.getMessage());
                alert.show();
            }finally {
                actualizar();
            }
        }
    }
    public void menu(MouseEvent mouseEvent) {
    }
    private void uncheckOtherBoxes(CheckBox currentBox, CheckBox... otherBoxes) {
        if (currentBox.isSelected()) {
            for (CheckBox box : otherBoxes) {
                box.setSelected(false);
            }
        }
    }
    private void cargarLista(){
        list = FXCollections.observableArrayList();
        HashMap<String, HashSet<String>> datos=archivos.getType();
        for (Map.Entry<String, HashSet<String>> entry : datos.entrySet()) {
            String key=entry.getKey();
            String value= String.valueOf(entry.getValue());
            list.add(key+" : "+value);
        }


    }
    private void cargarListaCustom(){
        listCust = FXCollections.observableArrayList();
        HashMap<String, HashSet<String>> datos=archivos.getCustom();
        for (Map.Entry<String, HashSet<String>> entry : datos.entrySet()) {
            String key=entry.getKey();
            String value= String.valueOf(entry.getValue());
            listCust.add(key+" : "+value);
        }
    }
    private void cargarHistorial(){
        listHist = FXCollections.observableArrayList();
        HashMap<String, Folder> historial=archivador.getCarpetas();
        for (Map.Entry<String, Folder> entry : historial.entrySet()) {
            String key=entry.getKey();
            String value=archivador.getCarpetas().get(key).isOrganizada()?" si ":"no";
            String organizacion=archivador.getCarpetas().get(key).getOrganizacion();
            listHist.add(key+" | Organizada: "+value+"\n Tipo Organizacion: "+organizacion);
        }
    }
    @FXML
    private void actualizar(){
        archivos.traerDatos();
        cargarListaCustom();
        listviewCustom.setItems(listCust);
    }
    private void actualizarHist(){
        archivos.traerDatos();
        cargarHistorial();
        listViewHist.setItems(listHist);
    }
    @FXML
    private void deshacer(ActionEvent actionEvent) {
        try {
            organizador.setKey(carpeta.getAbsolutePath());
            organizador.setOrgan(false);
            //organizador.deshacer(carpeta.getAbsolutePath());
            task=new Task<>(organizador);
            barOrg.progressProperty().bind(task.progressProperty());
            task.call();
            DoubleProperty progressProperty = new SimpleDoubleProperty(0.0);
            barOrg.progressProperty().bind(progressProperty);
            progressProperty.set(0.0);


        } catch (Exception e) {
            Alert alert =new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR!");
            alert.setHeaderText("ha ocurrido el siguiente");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.out.println(e.getMessage());
        }
        actualizarHist();
        lblOrganizado.setText("Organizado: No");
        lblCant.setText("Items: "+(Objects.requireNonNull(carpeta.listFiles()).length));
        updateTree();
        organizador.setOrgan(true);

    }
    private void updateTree() {
        lblJerarquia.setText("Jerarquia de: \n" + carpeta.getName());
        textFDirect.setText(carpeta.getAbsolutePath());
        treeDirectorio.getRoot().getChildren().clear();
        treeDirectorio.setVisible(true);

        TreeTask treeTask = new TreeTask(carpeta, rootItem, this);
        task2 = new Task<>(treeTask);
        barTree.progressProperty().bind(task2.progressProperty());
        //prInd.setStyle("-fx-text-fill: white;");
        prInd.progressProperty().bind(task2.progressProperty());
        new Thread(task2).start();
    }

    public ProgressBar getBarTree() {
        return barTree;
    }

    public TreeView<String> getTreeDirectorio() {
        return treeDirectorio;
    }
}
