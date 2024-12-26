module org.example.organizerfile {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.main.organizerfile to javafx.fxml;
    exports org.main.organizerfile;
}