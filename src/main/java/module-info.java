module WattBroker {
    requires geohash;
    requires json.simple;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.xml;

    exports com.wattbroker.wattbroker to javafx.graphics, javafx.fxml;
    opens com.wattbroker.wattbroker to javafx.fxml;
    // Add this line
}