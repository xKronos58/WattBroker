package com.wattbroker.wattbroker;

/**
 * This class is an extension of toggleable list data types.
 * It stores algorithm data based on provided information
 * and formats it so that it can be rendered in a toggleMenu
 * @see ToggleMenu
 * */
public class Algorithms implements toggleable {
    String name;
    double efficiency;
    double holdings;
    double profit;
    boolean running;

    /**
     * Main constructor for the Algorithms class
     * @param name e.g. 'Algorithm 1'
     * @param efficiency e.g. '95.5'
     * @param holdings e.g. '1000'
     * @param profit e.g. '1000'
     * @param running e.g. 'true'*/
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

    /**
     * Gets the value of a spesific data type
     * @param data e.g. 'NAME'
     * @return ToggleData e.g. 'Algorithm 1'*/
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