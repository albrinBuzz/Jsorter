module org.example.organizerfile {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.organizerfile to javafx.fxml;
    exports org.example.organizerfile;
}