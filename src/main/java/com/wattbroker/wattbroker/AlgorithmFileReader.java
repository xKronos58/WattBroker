package com.wattbroker.wattbroker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class AlgorithmFileReader {
    
    private String path;
    private File file;

    public AlgorithmSettingData asd;
    /**
     * 
     * @param path file path of NAME.algorithm file from the <Strong>RESOURCE</Strong> directory if not a resource add false at the end and provide a full file path */
    public AlgorithmFileReader(String path) {
        this.path = path;
        this.file = new File(String.valueOf(Main.class.getResource(path)));

        this.asd = convertToAlgorithmSettingsData(getLines());

    }
    
    public AlgorithmFileReader(String path, boolean isResource) {
        if(isResource)
            throw new IllegalArgumentException("Please remove 'true' from the end of the initialization");
        
        this.path = path;
        this.file = new File(path);

        this.asd = convertToAlgorithmSettingsData(getLines());
    }

    private AlgorithmSettingData convertToAlgorithmSettingsData(List<String> Content) {
        if(Content.size() != 63)
            throw new IllegalArgumentException("Invalid file format, Custom tags a not yet implemented please use the default tags.");
        AlgorithmSettingData asd = new AlgorithmSettingData(
                Content.get(0).substring("NAME:".length()),
                switch (Content.get(1).substring("STATUS:".length()).toUpperCase()) {
                    case "STOPPED" -> Status.STOPPED;
                    case "RUNNING" -> Status.RUNNING;
                    case "SUSPENDED" -> Status.SUSPENDED;
                    default -> throw new IllegalArgumentException("Invalid status");
                },
                Double.parseDouble(Content.get(2).substring("PROFIT:".length())),
                Double.parseDouble(Content.get(3).substring("EFFICIENCY:".length())),
                Double.parseDouble(Content.get(4).substring("HOLDINGS:".length())),
                Content.get(5).substring("SOURCE_LINE:{".length(), Content.get(5).length() -1).split(","),
                new AlgorithmSettings(Double.parseDouble(Content.get(7).substring("MAX_HOLDINGS:".length())), Double.parseDouble(Content.get(8).substring("MIN_HOLDINGS:".length())), Double.parseDouble(Content.get(9).substring("MAX_PROFIT:".length())), Double.parseDouble(Content.get(10).substring("MIN_PROFIT:".length())), Double.parseDouble(Content.get(11).substring("MAX_EFFICIENCY:".length())), Double.parseDouble(Content.get(12).substring("MIN_EFFICIENCY:".length())), Double.parseDouble(Content.get(13).substring("MAX_LOSS:".length())), Double.parseDouble(Content.get(14).substring("MIN_LOSS:".length())), Double.parseDouble(Content.get(15).substring("MAX_RISK:".length())), Double.parseDouble(Content.get(16).substring("MIN_RISK:".length())), Double.parseDouble(Content.get(17).substring("MAX_TRADE:".length())), Double.parseDouble(Content.get(18).substring("MIN_TRADE:".length())), Double.parseDouble(Content.get(19).substring("MAX_TRADE_TIME:".length())), Double.parseDouble(Content.get(20).substring("MIN_TRADE_TIME:".length())), Double.parseDouble(Content.get(21).substring("MAX_TRADE_AMOUNT:".length())), Double.parseDouble(Content.get(22).substring("MIN_TRADE_AMOUNT:".length())), Double.parseDouble(Content.get(23).substring("MAX_TRADE_LOSS:".length())), Double.parseDouble(Content.get(24).substring("MIN_TRADE_LOSS:".length())),Double.parseDouble(Content.get(25).substring("MAX_TRADE_RISK:".length())), Double.parseDouble(Content.get(26).substring("MIN_TRADE_RISK:".length())), Double.parseDouble(Content.get(27).substring("MAX_TRADE_PROFIT:".length())), Double.parseDouble(Content.get(28).substring("MIN_TRADE_PROFIT:".length())), Double.parseDouble(Content.get(29).substring("MAX_TRADE_EFFICIENCY:".length())), Double.parseDouble(Content.get(30).substring("MIN_TRADE_EFFICIENCY:".length())), Double.parseDouble(Content.get(31).substring("MAX_TRADE_HOLDINGS:".length())), Double.parseDouble(Content.get(32).substring("MIN_TRADE_HOLDINGS:".length())), Double.parseDouble(Content.get(33).substring("MAX_TRADE_STATUS:".length())), Double.parseDouble(Content.get(34).substring("MIN_TRADE_STATUS:".length())), Double.parseDouble(Content.get(35).substring("MAX_TRADE_STATUS".length())), Double.parseDouble(Content.get(36).substring("MIN_TRADE_STATUS".length())), Double.parseDouble(Content.get(37).substring("MAX_TRADE_SETTINGS".length())), Double.parseDouble(Content.get(38).substring("MIN_TRADE_SETTINGS".length())), Double.parseDouble(Content.get(39).substring("MAX_TRADE_SETTINGS_MAX_HOLDINGS".length())), Double.parseDouble(Content.get(40).substring("MIN_TRADE_SETTINGS_MAX_HOLDINGS".length())), Double.parseDouble(Content.get(41).substring("MAX_TRADE_SETTINGS_MIN_HOLDINGS".length())), Double.parseDouble(Content.get(42).substring("MIN_TRADE_SETTINGS_MIN_HOLDINGS".length())), Double.parseDouble(Content.get(43).substring("MAX_TRADE_SETTINGS_MAX_PROFIT".length())), Double.parseDouble(Content.get(44).substring("MIN_TRADE_SETTINGS_MIN_PROFIT".length())), Double.parseDouble(Content.get(45).substring("MAX_TRADE_SETTINGS_MAX_EFFICIENCY".length())), Double.parseDouble(Content.get(46).substring("MIN_TRADE_SETTINGS_MIN_EFFICIENCY".length())), Double.parseDouble(Content.get(47).substring("MAX_TRADE_SETTINGS_MAX_LOSS".length())), Double.parseDouble(Content.get(48).substring("MIN_TRADE_SETTINGS_MIN_LOSS".length())), Double.parseDouble(Content.get(49).substring("MAX_TRADE_SETTINGS_MAX_RISK".length())), Double.parseDouble(Content.get(50).substring("MIN_TRADE_SETTINGS_MIN_RISK".length())), Double.parseDouble(Content.get(51).substring("MAX_TRADE_SETTINGS_MAX_TRADE".length())), Double.parseDouble(Content.get(52).substring("MIN_TRADE_SETTINGS_MIN_TRADE".length())), Double.parseDouble(Content.get(53).substring("MAX_TRADE_SETTINGS_MAX_TRADE_TIME".length())), Double.parseDouble(Content.get(54).substring("MIN_TRADE_SETTINGS_MIN_TRADE_TIME".length())), Double.parseDouble(Content.get(55).substring("MAX_TRADE_SETTINGS_MAX_TRADE_AMOUNT".length())), Double.parseDouble(Content.get(56).substring("MIN_TRADE_SETTINGS_MIN_TRADE_AMOUNT".length())), Double.parseDouble(Content.get(57).substring("MAX_TRADE_SETTINGS_MAX_TRADE_LOSS".length())), Double.parseDouble(Content.get(58).substring("MIN_TRADE_SETTINGS_MIN_TRADE_LOSS".length())), Double.parseDouble(Content.get(59).substring("MAX_TRADE_SETTINGS_MAX_TRADE_RISK".length())), Double.parseDouble(Content.get(60).substring("MIN_TRADE_SETTINGS_MIN_TRADE_RISK".length())), Double.parseDouble(Content.get(61).substring("MAX_TRADE_SETTINGS_MAX_TRADE_PROFIT".length())), Double.parseDouble(Content.get(62).substring("MIN_TRADE_SETTINGS_MIN_TRADE_PROFIT".length()))));
        return asd;
    }
    
    private List<String> getLines() {
        if(!Files.exists(Path.of(file.getPath())))
            throw new IllegalArgumentException(file.toPath() + " does not exist");
        
        // Reads all lines in the NAME.algorithm file
        List<String> temp = null;
        try {
            temp = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return temp;
    }
    
    
    public record AlgorithmSettingData(
            String Name, 
            Status status,
            Double Profit,
            Double Efficiency,
            Double Holdings, 
            String[] Source_location,
            AlgorithmSettings Settings
    ) {
        @Override
        public String toString() {
            return String.format("Name: %s\nStatus: %s\nProfit: %f\nEfficiency: %f\nHoldings: %f\nSource_location: %s\nSettings: %s", Name, status, Profit, Efficiency, Holdings, Arrays.toString(Source_location), Settings.toString());
        }

    };
    
    record AlgorithmSettings(
            Double max_holdings,
            Double min_holdings,
            Double max_profit,
            Double min_profit,
            Double max_efficiency,
            Double min_efficiency,
            Double max_loss,
            Double min_loss,
            Double max_risk,
            Double min_risk,
            Double max_trade,
            Double min_trade,
            Double max_trade_time,
            Double min_trade_time,
            Double max_trade_amount,
            Double min_trade_amount,
            Double max_trade_loss,
            Double min_trade_loss,
            Double max_trade_risk,
            Double min_trade_risk,
            Double max_trade_profit,
            Double min_trade_profit,
            Double max_trade_efficiency,
            Double min_trade_efficiency,
            Double max_trade_holdings,
            Double min_trade_holdings,
            Double max_trade_status,
            Double min_trade_status,
            Double max_trade_source,
            Double min_trade_source,
            Double max_trade_settings,
            Double min_trade_settings,
            Double max_trade_settings_max_holdings,
            Double min_trade_settings_max_holdings,
            Double max_trade_settings_min_holdings,
            Double min_trade_settings_min_holdings,
            Double max_trade_settings_max_profit,
            Double min_trade_settings_min_profit,
            Double max_trade_settings_max_efficiency,
            Double min_trade_settings_min_efficiency,
            Double max_trade_settings_max_loss,
            Double min_trade_settings_min_loss,
            Double max_trade_settings_max_risk,
            Double min_trade_settings_min_risk,
            Double max_trade_settings_max_trade,
            Double min_trade_settings_min_trade,
            Double max_trade_settings_max_trade_time,
            Double min_trade_settings_min_trade_time,
            Double max_trade_settings_max_trade_amount,
            Double min_trade_settings_min_trade_amount,
            Double max_trade_settings_max_trade_loss,
            Double min_trade_settings_min_trade_loss,
            Double max_trade_settings_max_trade_risk,
            Double min_trade_settings_min_trade_risk,
            Double max_trade_settings_max_trade_profit,
            Double min_trade_settings_min_trade_profit
    ) {
        @Override
        public String toString() {
            return String.format("max_holdings: %f\nmin_holdings: %f\nmax_profit: %f\nmin_profit: %f\nmax_efficiency: %f\nmin_efficiency: %f\nmax_loss: %f\nmin_loss: %f\nmax_risk: %f\nmin_risk: %f\nmax_trade: %f\nmin_trade: %f\nmax_trade_time: %f\nmin_trade_time: %f\nmax_trade_amount: %f\nmin_trade_amount: %f\nmax_trade_loss: %f\nmin_trade_loss: %f\nmax_trade_risk: %f\nmin_trade_risk: %f\nmax_trade_profit: %f\nmin_trade_profit: %f\nmax_trade_efficiency: %f\nmin_trade_efficiency: %f\nmax_trade_holdings: %f\nmin_trade_holdings: %f\nmax_trade_status: %f\nmin_trade_status: %f\nmax_trade_source: %f\nmin_trade_source: %f\nmax_trade_settings: %f\nmin_trade_settings: %f\nmax_trade_settings_max_holdings: %f\nmin_trade_settings_max_holdings: %f\nmax_trade_settings_min_holdings: %f\nmin_trade_settings_min_holdings: %f\nmax_trade_settings_max_profit: %f\nmin_trade_settings_min_profit: %f\nmax_trade_settings_max_efficiency: %f\nmin_trade_settings_min_efficiency: %f\nmax_trade_settings_max_loss: %f\nmin_trade_settings_min_loss: %f\nmax_trade_settings_max_risk: %f\nmin_trade_settings_min_risk: %f\nmax_trade_settings_max_trade: %f\nmin_trade_settings_min_trade: %f\nmax_trade_settings_max_trade_time: %f\nmin_trade_settings_min_trade_time: %f\nmax_trade_settings_max_trade_amount: %f\nmin_trade_settings_min_trade_amount: %f\nmax_trade_settings_max_trade_loss: %f\nmin_trade_settings_min_trade_loss: %f\nmax_trade_settings_max_trade_risk: %f\nmin_trade_settings_min_trade_risk: %f\nmax_trade_settings_max_trade_profit: %f\nmin_trade_settings_min_trade_profit: %f", max_holdings, min_holdings, max_profit, min_profit, max_efficiency, min_efficiency, max_loss, min_loss, max_risk, min_risk, max_trade, min_trade, max_trade_time, min_trade_time, max_trade_amount, min_trade_amount, max_trade_loss, min_trade_loss, max_trade_risk, min_trade_risk, max_trade_profit, min_trade_profit, max_trade_efficiency, min_trade_efficiency, max_trade_holdings, min_trade_holdings, max_trade_status, min_trade_status, max_trade_source, min_trade_source, max_trade_settings, min_trade_settings, max_trade_settings_max_holdings, min_trade_settings_max_holdings, max_trade_settings_min_holdings, min_trade_settings_min_holdings, max_trade_settings_max_profit, min_trade_settings_min_profit, max_trade_settings_max_efficiency, min_trade_settings_min_efficiency, max_trade_settings_max_loss, min_trade_settings_min_loss, max_trade_settings_max_risk, min_trade_settings_min_risk, max_trade_settings_max_trade, min_trade_settings_min_trade, max_trade_settings_max_trade_time, min_trade_settings_min_trade_time, max_trade_settings_max_trade_amount, min_trade_settings_min_trade_amount, max_trade_settings_max_trade_loss, min_trade_settings_min_trade_loss, max_trade_settings_max_trade_risk, min_trade_settings_min_trade_risk, max_trade_settings_max_trade_profit, min_trade_settings_min_trade_profit);
        }
    }
    
    public enum Status {
        STOPPED(false), RUNNING(true), SUSPENDED(false);

        Status(boolean b) {}

    }
}
