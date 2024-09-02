package com.wattbroker.wattbroker;

/**
 * This class is for the data rendering panel
 * and stores the information about regional
 * energy price hotspots provided by the
 * current most efficient algorithm
 * @see toggleable
 * @see com.wattbroker.wattbroker.Controllers.Data_controller
 */
public class HOTSPOTS implements toggleable {

    String Name;
    String Location;
    double range;
    double price;

    @Override
    public void toggle() {

    }

    /**
     * Gets the value of a spesific data type
     * @param data e.g. 'NAME'
     * @return ToggleData e.g. 'API 1'
     */
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

    /**
     * Main constructor for the HOTSPOTS class
     * @param Name e.g. 'Hotspot 1'
     * @param Location e.g. 'Sydney'
     * @param range e.g. '1000'
     * @param price e.g. '1000'
     */
    public HOTSPOTS(String Name, String Location, double range, double price) {
        this.Name = Name;
        this.Location = Location;
        this.range = range;
        this.price = price;

    }
}
