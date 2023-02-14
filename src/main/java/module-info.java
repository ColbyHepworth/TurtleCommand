module TurtleCommand {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires org.eclipse.jetty.websocket.api;
    requires spark.core;

    opens render to javafx.fxml;
    opens backend;
    exports render;
}