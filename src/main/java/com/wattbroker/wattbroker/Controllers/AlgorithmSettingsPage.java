package com.wattbroker.wattbroker.Controllers;

import com.wattbroker.wattbroker.AlgorithmFileReader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class AlgorithmSettingsPage {
    public static void main(String[] args) {
        AlgorithmFileReader settings = new AlgorithmFileReader("_LOCAL_Data_Storage/default.algorithm");
        System.out.println(settings.toString());
    }

    /*FXML VARIABLES FOR buildPage()*/

    /*END OF VARIABLES*/
    private AlgorithmPaneController.Algorithms algorithm;
    public AlgorithmSettingsPage(AlgorithmPaneController.Algorithms algorithm) {
        this.algorithm = algorithm;
    }

    public Node buildPage() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AlgorithmSettingsTemplate.fxml"));
        loader.setController(this);


        try {
            Node main = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AlgorithmFileReader settings = new AlgorithmFileReader("_LOCAL_Data_Storage/default.algorithm");
        System.out.println(settings.toString());
        return null;
    }

    public String[] readSettings() {


        return null;
    }
}
