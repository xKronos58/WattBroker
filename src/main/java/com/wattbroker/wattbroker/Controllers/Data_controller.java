package com.wattbroker.wattbroker.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Data_controller {


    public ScrollPane rootScroll;
    public Text hsp_1_state,
                hsp_1_type,
                hsp_1_profit,
                hsp_2_state,
                hsp_2_type,
                hsp_2_profit,
                hsp_3_state,
                hsp_3_type,
                hsp_3_profit,
                hsp_4_state,
                hsp_4_type,
                hsp_4_profit,
                hsp_5_state,
                hsp_5_type,
                hsp_5_profit,
                hsp_6_state,
                hsp_6_type,
                hsp_6_profit,
                AEMO_viewButton,
                BOM_viewButton,
                ASX_viewButton,
                OTHER_viewButton;

    @FXML
    private void initialize() {
        // Remove scroll bars
        rootScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        List<hotSpot> hotSpots = getHotSpots();
        if(hotSpots.size() != 6) throw new IllegalArgumentException("There should be 6 hotspots");

        hsp_1_state.setText(hotSpots.get(0).state.code);
        hsp_1_type.setText(hotSpots.get(0).type);
        hsp_1_profit.setText(String.valueOf(hotSpots.get(0).profit));

        hsp_2_state.setText(hotSpots.get(1).state.code);
        hsp_2_type.setText(hotSpots.get(1).type);
        hsp_2_profit.setText(String.valueOf(hotSpots.get(1).profit));

        hsp_3_state.setText(hotSpots.get(2).state.code);
        hsp_3_type.setText(hotSpots.get(2).type);
        hsp_3_profit.setText(String.valueOf(hotSpots.get(2).profit));

        hsp_4_state.setText(hotSpots.get(3).state.code);
        hsp_4_type.setText(hotSpots.get(3).type);
        hsp_4_profit.setText(String.valueOf(hotSpots.get(3).profit));

        hsp_5_state.setText(hotSpots.get(4).state.code);
        hsp_5_type.setText(hotSpots.get(4).type);
        hsp_5_profit.setText(String.valueOf(hotSpots.get(4).profit));

        hsp_6_state.setText(hotSpots.get(5).state.code);
        hsp_6_type.setText(hotSpots.get(5).type);
        hsp_6_profit.setText(String.valueOf(hotSpots.get(5).profit));
    }

    private List<hotSpot> getHotSpots() {
        List<hotSpot> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/hotspots.csv"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(!line.toLowerCase().startsWith("state")) records.add(getHotspotFromLine(line));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    private hotSpot getHotspotFromLine(String line) {
        String[] values = line.split(",");
        try {
            return new hotSpot(State.convertStringToCode(values[0]), values[1], Double.parseDouble(values[2]));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error in line: " + line);
        }
    }

    record hotSpot(State state, String type, double profit) {

    }

    enum State {
        VIC("VIC"), NSW("NSW"), QLD("QLD"), SA("SA"), TAS("TAS"), WA("WA"), NT("NT"), ACT("ACT");

        private final String code;
        State(String code) {
            this.code = code;
        }

        static State convertStringToCode(String s) {
            return switch (s.toUpperCase()) {
                case "VIC" -> VIC;
                case "NSW" -> NSW;
                case "QLD" -> QLD;
                case "SA" -> SA;
                case "TAS" -> TAS;
                case "WA" -> WA;
                case "NT" -> NT;
                case "ACT" -> ACT;
                default -> throw new IllegalArgumentException("Unexpected value: " + s );
            };
        }
    }
}
