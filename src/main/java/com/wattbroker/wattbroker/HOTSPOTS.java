package com.wattbroker.wattbroker;

public class HOTSPOTS implements toggleable {

    String Name;
    String Location;
    double range;
    double price;

    @Override
    public void toggle() {

    }

    @Override
    public ToggleData getData(dataType data) {
        return switch (data) {
            case NAME -> new ToggleData<>(Name, Name);
            case LOCATION -> new ToggleData<>(Name, Location);
            case RANGE -> new ToggleData<>(Name, range + "m");
            case PRICE -> new ToggleData<>(Name, price + "$");
            default -> throw new IllegalStateException("Unexpected value: " + data);
        };
    }

    public HOTSPOTS(String Name, String Location, double range, double price) {
        this.Name = Name;
        this.Location = Location;
        this.range = range;
        this.price = price;

    }
}
