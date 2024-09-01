package com.wattbroker.wattbroker;

public class API_INFO implements toggleable {
    boolean status;
    String name;
    double responseTime;


    public API_INFO(String name, double responseTime, boolean status) {
        this.name = name;
        this.responseTime = responseTime;
        this.status = status;
    }

    public void toggle() {
    }

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
