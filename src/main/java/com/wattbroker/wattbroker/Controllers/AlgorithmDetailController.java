package com.wattbroker.wattbroker.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class AlgorithmDetailController {
    FXMLLoader root;

    @FXML
    public ScrollPane scrollRoot;
    @SuppressWarnings("unused") // Used by FXML
    @FXML
    public AnchorPane graphRoot;
    @FXML
    public Text name;
    @FXML
    public Text efficiency_text;
    @FXML
    public Text profit_text;
    @FXML
    public AnchorPane rootAnchorPane;

    private final String algoName;
    private final Line l = new Line();
    /**
     * Constructor for the AlgorithmDetailController allowing for data to be passed in from the AlgorithmPaneController
     * @param loader Algorithm detail template page in unloaded fxml
     * @param algoName Name of the algorithm to be displayed
     */
    public AlgorithmDetailController(FXMLLoader loader, String algoName) {
        this.root = loader;
        this.root.setController(this);
        this.algoName = algoName;
    }


    /**
     * Initialises the AlgorithmDetailController run after the raw FXML is loaded. Does not need to be called.
     */
    @SuppressWarnings("unused")
    @FXML
    public void initialize() {
        scrollRoot.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollRoot.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        name.setText(algoName);

        setToEfficiency();
        addLine();

        efficiency_text.setOnMouseClicked(e -> setToEfficiency());
        profit_text.setOnMouseClicked(e -> setToProfit());
    }

    private void addLine() {
        l.setStartX(efficiency_text.getX());
        l.setEndX(efficiency_text.getX() + efficiency_text.getLayoutBounds().getWidth());
        l.setStartY(efficiency_text.getY() + efficiency_text.getLayoutBounds().getHeight() + 5);
        l.setFill(Color.rgb(120, 32, 150, 1));
        rootAnchorPane.getChildren().add(l);
    }

    private void setToEfficiency() {
        this.efficiency_text.setFill(Color.WHITE);
        l.setStartX(efficiency_text.getX());
        l.setEndX(efficiency_text.getX() + efficiency_text.getLayoutBounds().getWidth());
        l.setStartY(efficiency_text.getY() + efficiency_text.getLayoutBounds().getHeight() + 5);
        System.out.println("X1 = " + l.getStartX() + ", X2 = " + l.getEndX() + ", Y = " + l.getStartY());
        profit_text.setFill(Color.rgb(52, 52, 52, 1));
    }

    private void setToProfit() {
        this.profit_text.setFill(Color.WHITE);
        l.setStartX(profit_text.getX());
        l.setEndX(profit_text.getX() + profit_text.getLayoutBounds().getWidth());
        l.setStartY(profit_text.getY() + profit_text.getLayoutBounds().getHeight() + 5);
        System.out.println("X1 = " + l.getStartX() + ", X2 = " + l.getEndX() + ", Y = " + l.getStartY());
        efficiency_text.setFill(Color.rgb(52, 52, 52, 1));
    }
}
