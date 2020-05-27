module gol {
  requires javafx.controls;
  requires javafx.fxml;
  requires transitive javafx.graphics;
  requires java.xml;

  opens org.uvsq.fr.gui to javafx.fxml;

  exports org.uvsq.fr;
}
