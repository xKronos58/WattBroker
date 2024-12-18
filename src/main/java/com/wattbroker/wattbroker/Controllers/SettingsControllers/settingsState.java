package com.wattbroker.wattbroker.Controllers.SettingsControllers;

import com.util.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reads settings from the _LOCAL_Data_Storage/settings.txt file allowing for the storage of settings after the application is closed.
 * The settings.txt file contains '#' indicating comments for the layout for easy modification outside of the application enviroment and will clean any data or have a default state if modified by the user
 * */
public class settingsState {

    // GENERAL SETTINGS
        // USER PROFILE
        // NOTIFICATION
            boolean email_notifications;
            boolean sms_notifications;
            boolean push_notifications;
            // NOTIFICATION PREFERENCES
                boolean price_alerts;
                boolean trade_confirmations;
                boolean news_and_updates;
        // LANGUAGE AND LOCALIZATION
            Language language;
            String time_zone;
            String regional_format;
        // CUSTOMIZATION
            Theme theme;
            boolean high_contrast;
    // TRADING
        // ACCOUNT MANAGEMENT
            // Linked bank accounts
            // Payment methods
            // ... -> SUPPORT

    public settingsState() {
        ReadSettings();
    }

    private void ReadSettings() {
        String pathToSettings = String.valueOf(this.getClass().getResource("_LOCAL_Data_Storage/settings.txt"));
        String tpts = "src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/settings.txt";
        System.out.println(pathToSettings);
        try (BufferedReader reader = new BufferedReader(new FileReader(tpts))) {
            String line;
            while((line = reader.readLine()) != null) {
                line.trim();
                if(line.startsWith("#") || line.isEmpty())
                    continue;

                int line_break = util.until(0, line, ':');

                String value = line.substring(line_break + 1);
                String key = line.substring(0, line_break);

                switch (key) {
                    case "EMAIL-NOTIFICATIONS":
                        email_notifications = value.equals("TRUE");
                        break;
                    case "SMS-NOTIFICATIONS":
                        sms_notifications = value.equals("TRUE");
                        break;
                    case "PUSH-NOTIFICATIONS":
                        push_notifications = value.equalsIgnoreCase("TRUE");
                        break;
                    case "PRICE-ALERTS":
                        price_alerts = value.equalsIgnoreCase("TRUE");
                        break;
                    case "TRADE-CONFIRMATIONS":
                        trade_confirmations = value.equalsIgnoreCase("TRUE");
                        break;
                    case "NEWS-AND-UPDATES":
                        news_and_updates = value.equalsIgnoreCase("TRUE");
                        break;
                    case "LANGUAGE":
                        language = convertToLanguage(value);
                        break;
                    case "TIME-ZONE":
                        time_zone = value;
                        break;
                    case "REGIONAL-FORMAT":
                        regional_format = value;
                        break;
                    case "THEME":
                        theme = convertToTheme(value);
                        break;
                    case "HIGH-CONTRAST":
                        high_contrast = value.equalsIgnoreCase("TRUE");
                        break;
                    default:
                        System.out.println("Unknown configuration key: " + key);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new settingsState();
    }

    public Language convertToLanguage(String value) {
        try {
            return Language.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Reverting to default language...");
            return Language.ENGLISH; // DEFAULT LANGUAGE
        }
    }

    public Theme convertToTheme(String value) {
        try {
            return Theme.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Reverting to default theme...");
            return Theme.OCEAN_DARK;
        }
    }
}
