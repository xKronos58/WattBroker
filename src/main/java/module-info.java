module WattBroker {
    requires geohash;
    requires json.simple;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires org.bouncycastle.provider;
    requires jakarta.mail;
    requires annotations;

    exports com.wattbroker.wattbroker to javafx.graphics, javafx.fxml;
    opens com.wattbroker.wattbroker to javafx.fxml;
    opens com.wattbroker.wattbroker.Controllers to javafx.fxml;
    opens com.wattbroker.wattbroker.Controllers.SettingsControllers to javafx.fxml;
}
