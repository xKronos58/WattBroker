package com.wattbroker.wattbroker.Controllers;

import com.wattbroker.wattbroker.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class AlgorithmPaneController {

    public ScrollPane ScrollPaneAlg;
    @FXML
    public void initialize() {

        ScrollPaneAlg.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ScrollPaneAlg.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        loadAlgorithms(buildAlgorithmPane(getAlgorithms()));
    }

    private void loadAlgorithms(List<Node> algorithms) {
        VBox alg = new VBox(27);
        alg.setPrefWidth(1311);
        alg.setBackground(Background.fill(Color.rgb(23, 23, 23, 1)));
        alg.setPadding(new javafx.geometry.Insets(0, 0, 30, 28));
        alg.getChildren().addAll(algorithms);
        try {
            alg.getChildren().add(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("AddButtonTemplate.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ScrollPaneAlg.setContent(alg);
    }


    public List<Algorithms> getAlgorithms() {
        List<Algorithms> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/algorithms.csv"))) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return records;
    }

    public record Algorithms(String name, String description,
                             double efficiency, double profit,
                             double balance, boolean running) {

    }
    public Algorithms getRecordFromLine(String line) {
        String[] values = line.split(",");
        if(values[0].equals("NAME")) return null;
        try {
            return new Algorithms(values[0], values[1], Double.parseDouble(values[2]),
                    Double.parseDouble(values[3]), Double.parseDouble(values[4]), values[5].equals("true"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Node> buildAlgorithmPane(List<Algorithms> alg) {
        List<Node> nodes = new ArrayList<>();
        for(Algorithms algorithm : alg) {
            try {
                FXMLLoader current = new FXMLLoader(Main.class.getResource("Algorithm_Template.fxml"));
                Node temp = current.load();
                AlgTemplateController Controller = current.getController();
                Controller.setAlgorithm(algorithm);
                nodes.add(temp);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return nodes;
    }
}
