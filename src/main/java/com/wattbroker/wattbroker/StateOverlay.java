package com.wattbroker.wattbroker;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class StateOverlay extends Application {
    @FXML
    SVGPath state;
    @FXML
    Text stateName;
    @FXML
    Button close;
    @FXML
    Text priceLabel;
    Map.State localState;
    private final FXMLLoader fxmlLoader;
    public StateOverlay(Map.State state){
        fxmlLoader = new FXMLLoader(Main.class.getResource("StateOverlay.fxml"));
        fxmlLoader.setController(this);
        localState = state;
    }

    public void Build() {
        try {
            start(new Stage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Scene s = new Scene(fxmlLoader.load(), 1186,727);
        stage.setScene(s);
        s.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE)
                stage.close();
        });
        this.state.setContent(localState.getPath());
        this.state.setFill(localState.getColour());
        this.state.setTranslateX(localState.getSVGLocation().getX());
        this.state.setTranslateY(localState.getSVGLocation().getY());
        this.stateName.setText(localState.getName());
        this.priceLabel.setText("$"+ (localState.getPrice()));
        close.setOnAction(e -> stage.close());
        stage.show();
    }
}
