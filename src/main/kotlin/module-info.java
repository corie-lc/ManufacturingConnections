module org.corieleclair.travellers {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires opencsv;

    opens org.corieleclair.travellers to javafx.fxml;
    exports org.corieleclair.travellers;
}