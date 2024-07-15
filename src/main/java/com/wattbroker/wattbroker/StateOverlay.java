package com.wattbroker.wattbroker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class StateOverlay {
    @FXML
    SVGPath state;
    @FXML
    Text stateName;
    @FXML
    Button close;

    Map.State localState;
    private FXMLLoader fxmlLoader;
    public StateOverlay(Map.State state){
        fxmlLoader = new FXMLLoader(getClass().getResource("StateOverlay.fxml"));
        fxmlLoader.setController(this);
        localState = state;
    }

    public void Build(Stage stage) {
        Scene scene = stage.getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();
        try {
            Node node = (Node) fxmlLoader.load();
            node.setId("stateOverlay");
            System.out.println(root.getId());
            root.getChildren().add(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.state.setContent(localState.getPath());
        this.state.setTranslateX(localState.getSVGLocation().getX());
        this.state.setTranslateY(localState.getSVGLocation().getY());
        this.stateName.setText(localState.getName());
//        this.close.setOnAction(e -> root.getChildren().
//                removeIf(item -> item.getId().equals("close")));
    }
}
