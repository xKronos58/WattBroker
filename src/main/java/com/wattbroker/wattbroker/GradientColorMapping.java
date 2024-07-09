package com.wattbroker.wattbroker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class GradientColorMapping extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // Define the gradient colors
        Color startColor = Color.BLUE;
        Color endColor = Color.RED;

        // Define the range of values
        double minValue = 0.0;
        double maxValue = 100.0;

        // Create and position circles with colors mapped from the value
        for (double value = minValue; value <= maxValue; value += 10) {
            double normalizedValue = normalize(value, minValue, maxValue);
            Color color = interpolateColor(startColor, endColor, normalizedValue);
            Circle circle = new Circle(50 + value * 5, 100, 20, color);
            root.getChildren().add(circle);
        }

        Scene scene = new Scene(root, 800, 200);
        primaryStage.setTitle("Gradient Color Mapping");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private double normalize(double val, double minValue, double maxValue) {
        return (val - minValue) / (maxValue - minValue);
    }

    private Color interpolateColor(Color startColor, Color endColor, double fraction) {
        double red = startColor.getRed() + fraction * (endColor.getRed() - startColor.getRed());
        double green = startColor.getGreen() + fraction * (endColor.getGreen() - startColor.getGreen());
        double blue = startColor.getBlue() + fraction * (endColor.getBlue() - startColor.getBlue());
        return new Color(red, green, blue, 1.0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}