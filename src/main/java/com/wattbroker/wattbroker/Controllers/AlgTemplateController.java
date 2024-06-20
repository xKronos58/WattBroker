package com.wattbroker.wattbroker.Controllers;

import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class AlgTemplateController {
    public Text Name;
    public Text Efficiency;
    public Text Running;
    public Text Balance;
    public Text Profits;


    public void setAlgorithm(AlgorithmPaneController.Algorithms algorithm) {
        this.Name.setText(algorithm.name());
        this.Efficiency.setText(algorithm.efficiency() + "%");
        this.Efficiency.setFill(algorithm.efficiency() > 75 ? javafx.scene.paint.Color.rgb(79, 221, 93, 1) : javafx.scene.paint.Color.rgb(221, 79, 79, 1));
        this.Running.setText(algorithm.running() ? "Running" : "Stopped");
        this.Running.setFill(algorithm.running() ? javafx.scene.paint.Color.rgb(79, 221, 93, 1) : javafx.scene.paint.Color.rgb(221, 79, 79, 1));
        this.Balance.setText("$" + algorithm.balance());
        this.Profits.setText("$" + algorithm.profit());
        this.Profits.setFill(algorithm.profit() > 0 ? javafx.scene.paint.Color.rgb(79, 221, 93, 1) : javafx.scene.paint.Color.rgb(221, 79, 79, 1));
    }

    public void openAlgorithm(MouseEvent mouseEvent) {
        switch (this.Name.getText()) {

        }
    }
}
