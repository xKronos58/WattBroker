package com.wattbroker.wattbroker;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the object class for the search function.
 * The search function allows users to traverse
 * wattbroker easily.
 * As a search option was not included in the SRS.
 * It is a Work in progress and will be completed
 * in a later patch, so the code written here is
 * not finalised or finished.
 * */
public class SearchBar extends AnchorPane{

    public TextField Search_Bar;
    public AnchorPane root;
    public SearchBar() {
        FXMLLoader load = new FXMLLoader(getClass().getResource("SearchBar.fxml"));
        load.setRoot(this);
        load.setController(this);

        try {
            load.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (Search_Bar != null) Search_Bar.setOnKeyTyped(e -> {
            if(e.getCode() == KeyCode.ENTER) {
                Search(Search_Bar.getText());
            }
        });
    }

    private void Search(String text) {
//        System.out.println(root.getParent());
    }

    public enum panes {
        Algorithms(new String[]{"algorithms", "algo", "algorithm"}),
        Dashboard(new String[]{"dashboard", "dash"}),
        Data(new String[]{"data"}),
        Settings(new String[]{"settings", "setting", "preferences"}),
        General_Settings(new String[]{"general", "gen", "general settings", "gen settings"}),
        Integration_Settings(new String[]{"integration", "int", "integration settings", "int settings"}),
        Legal_Settings(new String[]{"legal", "leg", "legal settings", "leg settings"}),
        Market(new String[]{"market"}),
        Support(new String[]{"support", "sup", "help", "helpful", "helpful information"});

        panes(String[] keywords) {
            this.keywords.addAll(List.of(keywords));
        }

        private final List<String> keywords = new ArrayList<>();

        public static panes fromString(String text) {
            for (panes p : panes.values())
                if (p.keywords.contains(text))
                    return p;
            return null; // or throw an exception or return an Optional
        }
    }
}
