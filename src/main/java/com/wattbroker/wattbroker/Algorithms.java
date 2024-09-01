package com.wattbroker.wattbroker;

public class Algorithms implements toggleable {
    String name;
    double efficiency;
    double holdings;
    double profit;
    boolean running;

    public Algorithms(String name, double efficiency, double holdings, double profit, boolean running) {
        this.name = name;
        this.efficiency = efficiency;
        this.holdings = holdings;
        this.profit = profit;
        this.running = running;
    }

    @Override
    public void toggle() {

    }

    @Override
    public ToggleData getData(dataType data) {
        return switch (data) {
            case NAME -> new ToggleData<>(name, name);
            case EFFICIENCY -> new ToggleData<>(name, efficiency + "%" /* '‰' had to be used as apposed to '%' as java-fx treats '%' as a string interpolator and thus cannot be used in a string*/);
            case HOLDINGS -> new ToggleData<>(name, holdings + "MW");
            case PROFIT -> new ToggleData<>(name, profit);
            case RUNNING -> new ToggleData<>(name, running);
            default -> throw new IllegalStateException("Unexpected value: " + data);
        };
    }
}

