module com.cmdpresta.cookmaster.cookmasterapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires org.apache.pdfbox;
    requires org.jfree.jfreechart;
    requires java.desktop;

    opens com.cmdpresta.cookmaster.cookmasterapp to javafx.fxml;
    exports com.cmdpresta.cookmaster.cookmasterapp;
}