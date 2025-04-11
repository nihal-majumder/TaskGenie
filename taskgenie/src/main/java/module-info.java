module edu.northeastern {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.northeastern to javafx.fxml;
    exports edu.northeastern;
}
