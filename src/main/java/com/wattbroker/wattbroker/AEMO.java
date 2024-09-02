package com.wattbroker.wattbroker;

/**
 * This class stores AEMO API based data.
 * This class contains methods to get spesific datasets for graphing and rendering
 * @see Graph Usages
 * */
public class AEMO {
    String _date, _type;
    double _spotPrice, _demand, _generation, _semiGeneration, _import;

    /**
     * Main constructor for the AEMO data class
     * @param Date e.g. '19/08/2024 18:05'
     * @param SpotPrice e.g. '249.45624'
     * @param Generation e.g. '7509.93'
     * @param SemiGeneration e.g. '7913.40231'
     * @param Import e.g. '128.86769'
     * @param type e.g. 'ACTUAL'*/
    public AEMO(String Date, Double SpotPrice, Double Demand, Double Generation, Double SemiGeneration, Double Import, String type) {
        if (Date == null || SpotPrice == null || Demand == null || Generation == null || Import == null) {
            throw new IllegalArgumentException("Null values are not allowed");
        }

        // Value setters
        this._date = Date;
        this._spotPrice = SpotPrice;
        this._demand = Demand;
        this._generation = Generation;
        this._semiGeneration = SemiGeneration;
        this._import = Import;
        this._type = type;

    }

    /**
     * Converts a string to a type
     * @param type e.g. 'SPOT_PRICE'
     * @return type e.g. AEMO.type.SPOT_PRICE*/
    public static type ConvertToType(String type) {
        return switch (type) {
            case "SPOT_PRICE" -> AEMO.type.SPOT_PRICE;
            case "DEMAND" -> AEMO.type.DEMAND;
            case "GENERATION" -> AEMO.type.GENERATION;
            case "SEMI_GENERATION" -> AEMO.type.SEMI_GENERATION;
            case "IMPORT" -> AEMO.type.IMPORT;
            default -> throw new IllegalStateException(type + " is not a valid data type");
        };
    }

    /**
     * Gets the value of a spesific type
     * @param t e.g. 'SPOT_PRICE'
     * @return double e.g. '249.45624'*/
    public double value(type t) {
        return switch (t) {
            case SPOT_PRICE -> _spotPrice;
            case DEMAND -> _demand;
            case GENERATION -> _generation;
            case SEMI_GENERATION -> _semiGeneration;
            case IMPORT -> _import;
        };
    }

    /**
     * Increments the value of a spesific type
     * @param t e.g. 'SPOT_PRICE'
     * @param amount e.g. '249.45624'*/
    public void incrimentVal(type t, double amount) {
        switch (t) {
            case SPOT_PRICE -> _spotPrice += amount;
            case DEMAND -> _demand += amount;
            case GENERATION -> _generation += amount;
            case SEMI_GENERATION -> _semiGeneration += amount;
            case IMPORT -> _import += amount;
        };
    }

    /**
     * gets current spot price
     * @return double spot_price
     */
    public double get_spotPrice() {
        return _spotPrice;
    }

    /**
     * gets current demand
     * @return double demand
     */
    public double get_demand() {
        return _demand;
    }

    /**
     * gets current generation
     * @return double generation
     */
    public double get_generation() {
        return _generation;
    }

    /**
     * gets current import
     * @return double import
     */
    public double get_import() {
        return _import;
    }

    /**
     * gets current semi generation
     * @return double semi_generation
     */
    public double get_semiGeneration() {
        return _semiGeneration;
    }

    /**
     * gets current date
     * @return String date
     */
    public String get_date() {
        return _date;
    }

    /**
     * gets current type
     * @return String type
     */
    public String get_type() {
        return _type;
    }

    /**
     * gets a spesific value
     * @param graphTypeDataset e.g. 'SPOT_PRICE'
     * @return double e.g. '249.45624'*/
    public double get_special(type graphTypeDataset) {
        return switch (graphTypeDataset) {
            case SPOT_PRICE -> _spotPrice;
            case DEMAND -> _demand;
            case GENERATION -> _generation;
            case SEMI_GENERATION -> _semiGeneration;
            case IMPORT -> _import;
        };
    }

    /**
     * Stores type of data used to eliminate string edge cases to minimize potential errors */
    public enum type {
        SPOT_PRICE, DEMAND, GENERATION, SEMI_GENERATION, IMPORT
    }
}