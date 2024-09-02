package com.wattbroker.wattbroker;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ToggleMenu extends AnchorPane {

    private String openButton = "M12.3389 19.125L24.6776 31.875L37.0163 19.125";
    private String closedButton = "M12.3389 31.875L24.6776 19.125L37.0163 31.875";

    private final VBox contentRoot = new VBox(10);
    double contentRootHeight = 0;

    public ToggleMenu(List<? extends toggleable> objects, toggleable.dataType dataType) {
        List<ToggleData> data = new ArrayList<>();
        for(toggleable object : objects) {
            data.add(object.getData(dataType));
            contentRoot.getChildren().add(generateElement(object.getData(dataType)));
        }

        VBox Content = new VBox(10);
        Content.getChildren().addAll(generateButton(dataType), contentRoot);
        contentRoot.setVisible(false);
        contentRoot.setMaxHeight(0);
        contentRoot.setMinHeight(0);
        contentRootHeight = data.size() * 67 + 10 * data.size();


        this.getChildren().add(Content);
    }

    private AnchorPane generateButton(toggleable.dataType dataType) {
        AnchorPane root = new AnchorPane();

        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #313131; -fx-background-radius: 14");
        Text Name = new Text(dataType.getName());
        SVGPath buttonIcon = new SVGPath();
        buttonIcon.setContent(closedButton);
        root.setPrefWidth(360);
        root.setPrefHeight(77);
        Name.setWrappingWidth(155);

        buttonIcon.setOnMouseClicked(e -> {
            if(buttonIcon.getContent().equals(closedButton)) {
                buttonIcon.setContent(openButton);
                contentRoot.setVisible(true);
                contentRoot.setMaxHeight(contentRootHeight);
            } else {
                buttonIcon.setContent(closedButton);
                contentRoot.setVisible(false);
                contentRoot.setMaxHeight(0);
            }
        });

        Name.setFill(javafx.scene.paint.Color.WHITE);
        Name.setFont(new Font("Inter SemiBold", 24));
        buttonIcon.setStroke(javafx.scene.paint.Color.WHITE);
        buttonIcon.setStrokeWidth(3);
        buttonIcon.setFill(javafx.scene.paint.Color.TRANSPARENT);

        root.getChildren().addAll(Name, buttonIcon);
        AnchorPane.setLeftAnchor(Name, 10.0);
        AnchorPane.setTopAnchor(Name, 10.0);
        AnchorPane.setRightAnchor(buttonIcon, 10.0);
        AnchorPane.setTopAnchor(buttonIcon, 27.0);

        return root;
    }

    AnchorPane generateElement(ToggleData data) {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #313131; -fx-background-radius: 14");
        Text Name = new Text(String.valueOf(data.getName()));
        Text value = new Text(String.valueOf(data.getValue()));
        Name.setFill(javafx.scene.paint.Color.WHITE);
        Name.setFont(new Font("Inter SemiBold", 24));
        value.setFill(javafx.scene.paint.Color.WHITE);
        value.setFont(new Font("Inter SemiBold", 24));
        root.getChildren().addAll(Name, value);
        AnchorPane.setLeftAnchor(Name, 10.0);
        AnchorPane.setTopAnchor(Name, 18.0);
        AnchorPane.setRightAnchor(value, 10.0);
        AnchorPane.setTopAnchor(value, 18.0);
        root.setPrefWidth(348);
        root.setPrefHeight(67);
        root.setPadding(new Insets(0, 5, 0, 5));
        return root;
    }
}

