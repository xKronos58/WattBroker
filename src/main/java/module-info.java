module WattBroker {
    requires geohash;
    requires json.simple;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.xml;
    requires java.logging;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires java.sql;

    exports com.wattbroker.wattbroker to javafx.graphics, javafx.fxml;
    opens com.wattbroker.wattbroker to javafx.fxml;
    opens com.wattbroker.wattbroker.Controllers to javafx.fxml; // Add this line
}
