package com.wattbroker.wattbroker.Controllers;

import com.wattbroker.wattbroker.Controllers.SettingsControllers.EditType;
import com.wattbroker.wattbroker.Controllers.SettingsControllers.GeneralSettingsController;
import com.wattbroker.wattbroker.Main;
import com.wattbroker.wattbroker.UI_Controller;
import com.wattbroker.wattbroker.WattBroker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
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
    public AnchorPane root;

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
            Node addButton = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("AddButtonTemplate.fxml")));
            addButton.setOnMouseClicked(e -> new GeneralSettingsController.EDIT(EditType.ALGORITHM, null));

            alg.getChildren().add(addButton);
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
                             double balance, boolean running,
                             String filename) {

    }
    public Algorithms getRecordFromLine(String line) {
        String[] values = line.split(",");
        if(values[0].equals("NAME")) return null;
        try {
            return new Algorithms(values[0], values[1], Double.parseDouble(values[2]),
                    Double.parseDouble(values[3]), Double.parseDouble(values[4]), values[5].equalsIgnoreCase("true"), values[6]);
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
                temp.setOnMouseClicked(e -> {
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("Algorithm_Details_template.fxml"));
                    root.getChildren().clear();
                    try {
                        new AlgorithmDetailController(loader, algorithm.name());

                        root.getChildren().add(loader.load());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                AlgTempla