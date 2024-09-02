package com.wattbroker.wattbroker.Controllers;

import com.wattbroker.wattbroker.AlgorithmFileReader;
import com.wattbroker.wattbroker.LargeGraph;
import com.wattbroker.wattbroker.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.io.IOException;

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

    @FXML Text state_val,
            max_holdings_val,
            min_holdings_val,
            max_profit_val,
            min_profit_val,
            max_efficiency_val,
            min_efficiency_val,
            max_loss_val,
            min_loss_val,
            max_risk_val,
            min_risk_val,
            max_trade_val,
            max_trade_efficiency_val,
            min_trade_efficiency_val,
            max_trade_holdings_val,
            min_trade_holdings_val,
            min_trade_val,
            max_trade_time_val,
            min_trade_time_val,
            max_trade_amount_val,
            min_trade_amount_val,
            max_trade_loss_val,
            min_trade_loss_val,
            max_trade_risk_val,
            min_trade_risk_val,
            max_trade_profit_val,
            min_trade_profit_val,
            max_trade_status_val,
            min_trade_status_val,
            max_trade_source_val,
            min_trade_source_val,
            max_trade_settings_val,
            min_trade_settings_val,
            Holdings_val,
            Efficiency_val;

    @FXML
    LargeGraph EP_graph;

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

        rootAnchorPane.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE) {
                rootAnchorPane.getChildren().clear();
            try {
                rootAnchorPane.getChildren().add(new FXMLLoader(Main.class.getResource("Algorithm_Pane.fxml")).load());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }});

        AlgorithmFileReader afr = new AlgorithmFileReader("/Users/finleycrowther/Desktop/_SftDev/Criterion 6/WB_Code/WattBroker/src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/" + algoName + ".algorithm", false);

        Holdings_val.setText("HOLDINGS : " + afr.asd.Holdings().toString());
        Efficiency_val.setText(afr.asd.Efficiency().toString());
        Efficiency_val.setFill(afr.asd.Efficiency() > 80.0 ? Color.rgb(0, 255, 0, 1) : ( afr.asd.Efficiency() > 50.0 ? Color.rgb(255, 255, 0) : Color.rgb(255, 0, 0, 1)));

        max_holdings_val.setText(afr.asd.getSettings().getMax_holdings().toString());
        min_holdings_val.setText(afr.asd.getSettings().getMin_holdings().toString());
        max_profit_val.setText(afr.asd.getSettings().getMax_profit().toString());
        min_profit_val.setText(afr.asd.getSettings().getMin_profit().toString());
        max_efficiency_val.setText(afr.asd.getSettings().getMax_efficiency().toString());
        min_efficiency_val.setText(afr.asd.getSettings().getMin_efficiency().toString());
        max_loss_val.setText(afr.asd.getSettings().getMax_loss().toString());
        min_loss_val.setText(afr.asd.getSettings().getMin_loss().toString());
        max_risk_val.setText(afr.asd.getSettings().getMax_risk().toString());
        min_risk_val.setText(afr.asd.getSettings().getMin_risk().toString());
        max_trade_val.setText(afr.asd.getSettings().getMax_trade().toString());
        max_trade_efficiency_val.setText(afr.asd.getSettings().getMax_trade_efficiency().toString());
        min_trade_efficiency_val.setText(afr.asd.getSettings().getMin_trade_efficiency().toString());
        max_trade_holdings_val.setText(afr.asd.getSettings().getMax_trade_holdings().toString());
        min_trade_holdings_val.setText(afr.asd.getSettings().getMin_trade_holdings().toString());
        min_trade_val.setText(afr.asd.getSettings().getMin_trade().toString());
        max_trade_time_val.setText(afr.asd.getSettings().getMax_trade_time().toString());
        min_trade_time_val.setText(afr.asd.getSettings().getMin_trade_time().toString());
        max_trade_amount_val.setText(afr.asd.getSettings().getMax_trade_amount().toString());
        min_trade_amount_val.setText(afr.asd.getSettings().getMin_trade_amount().toString());
        max_trade_loss_val.setText(afr.asd.getSettings().getMax_trade_loss().toString());
        min_trade_loss_val.setText(afr.asd.getSettings().getMin_trade_loss().toString());
        max_trade_risk_val.setText(afr.asd.getSettings().getMax_trade_risk().toString());
        min_trade_risk_val.setText(afr.asd.getSettings().getMin_trade_risk().toString());
        max_trade_profit_val.setText(afr.asd.getSettings().getMax_trade_profit().toString());
        min_trade_profit_val.setText(afr.asd.getSettings().getMin_trade_profit().toString());
        max_trade_status_val.setText(afr.asd.getSettings().getMax_trade_status().toString());
        min_trade_status_val.setText(afr.asd.getSettings().getMin_trade_status().toString());
        max_trade_source_val.setText(afr.asd.getSettings().getMax_trade_source().toString());
        min_trade_source_val.setText(afr.asd.getSettings().getMin_trade_source().toString());
        max_trade_settings_val.setText(afr.asd.getSettings().getMax_trade_settings().toString());
        min_trade_settings_val.setText(afr.asd.getSettings().getMin_trade_settings().toString());

        EP_graph.RebindGraph();
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
        EP_graph.setGraphType("Efficiency");
        EP_graph.RebindGraph();
    }

    private void setToProfit() {
        this.profit_text.setFill(Color.WHITE);
        l.setStartX(profit_text.getX());
        l.setEndX(profit_text.getX() + profit_text.getLayoutBounds().getWidth());
        l.setStartY(profit_text.getY() + profit_text.getLayoutBounds().getHeight() + 5);
        System.out.println("X1 = " + l.getStartX() + ", X2 = " + l.getEndX() + ", Y = " + l.getStartY());
        efficiency_text.setFill(Color.rgb(52, 52, 52, 1));
        EP_graph.setGraphType("Profit");
        EP_graph.RebindGraph();
    }
}
