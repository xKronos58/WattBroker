package com.wattbroker.wattbroker.Controllers.SettingsControllers;

public enum Theme {
    OCEAN_DARK, SUNSET_LIGHT, FOREST_NEUTRAL, MOUNTAINS_LIGHT, PINK_DARK, CUSTOM;

    @Override
    public String toString() {
        return switch (name()) {
            case "OCEAN_DARK" -> "Ocean Dark";
            case "SUNSET_LIGHT" -> "Sunset Light";
            case "FOREST_NEUTRAL" -> "Forest neutral";
            case "MOUNTAINS_LIGHT" -> "Mountains Light";
            case "PINK_DARK" -> "Pink Dark";
            case "CUSTOM" -> "Custom";
            default -> "NaN";
        };
    }
}
