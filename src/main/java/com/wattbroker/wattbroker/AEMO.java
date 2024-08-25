package com.wattbroker.wattbroker;

public class AEMO {
    String _date, _type;
    double _spotPrice, _demand, _generation, _semiGeneration, _import;

    public AEMO(String Date, Double SpotPrice, Double Demand, Double Generation, Double SemiGeneration, Double Import, String type) {
        if (Date == null || SpotPrice == null || Demand == null || Generation == null || Import == null) {
            throw new IllegalArgumentException("Null values are not allowed");
        }

        this._date = Date;
        this._spotPrice = SpotPrice;
        this._demand = Demand;
        this._generation = Generation;
        this._semiGeneration = SemiGeneration;
        this._import = Import;
        this._type = type;

    }

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

    public double value(type t) {
        return switch (t) {
            case SPOT_PRICE -> _spotPrice;
            case DEMAND -> _demand;
            case GENERATION -> _generation;
            case SEMI_GENERATION -> _semiGeneration;
            case IMPORT -> _import;
        };
    }

    public void incrimentVal(type t, double amount) {
        switch (t) {
            case SPOT_PRICE -> _spotPrice += amount;
            case DEMAND -> _demand += amount;
            case GENERATION -> _generation += amount;
            case SEMI_GENERATION -> _semiGeneration += amount;
            case IMPORT -> _import += amount;
        };
    }

    public double get_spotPrice() {
        return _spotPrice;


    }

    public double get_demand() {
        return _demand;
    }

    public double get_generation() {
        return _generation;
    }

    public double get_import() {
        return _import;
    }

    public double get_semiGeneration() {
        return _semiGeneration;
    }

    public String get_date() {
        return _date;
    }

    public String get_type() {
        return _type;
    }

    public double get_special(type graphTypeDataset) {
        return switch (graphTypeDataset) {
            case SPOT_PRICE -> _spotPrice;
            case DEMAND -> _demand;
            case GENERATION -> _generation;
            case SEMI_GENERATION -> _semiGeneration;
            case IMPORT -> _import;
        };
    }

    public enum type {
        SPOT_PRICE, DEMAND, GENERATION, SEMI_GENERATION, IMPORT
    }
}