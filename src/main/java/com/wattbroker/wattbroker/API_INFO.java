package com.wattbroker.wattbroker;

/**
 * This class is an extension of toggleable list data types.
 * It stores API data based on provided information
 * and formats it so that it can be rendered in a toggleMenu
 * @see ToggleMenu
 * */
public class API_INFO implements toggleable {
    boolean status;
    String name;
    double responseTime;

    /**
     * Main constructor for the API_INFO class
     * @param name e.g. 'API 1'
     * @param responseTime e.g. '95.5'
     * @param status e.g. 'true'*/
    public API_INFO(String name, double responseTime, boolean status) {
        this.name = name;
        this.responseTime = responseTime;
        this.status = status;
    }

    public void toggle() {
    }

    /**
     * Gets the value of a spesific data type
     * @param data e.g. 'NAME'
     * @return ToggleData e.g. 'API 1'*/
    @Override
    public ToggleData getData(dataType data) {
        return switch (data) {
            case NAME -> new ToggleData<>(name, name);
            case STATUS -> new ToggleData<>(name, status);
            case RESPONSE_TIME -> new ToggleData<>(name, responseTime + "ms");
            default -> throw new IllegalStateException("Unexpected value: " + data);
        };
    }
}
