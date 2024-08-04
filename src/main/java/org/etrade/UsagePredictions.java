package org.etrade;

import org.w3c.dom.ranges.Range;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class UsagePredictions {

    /**
     * Current weather for predictions */
    Weather w;

    /**
     * Takes weather and time data to predict the average household energy usage based off shoulder times season and temeperature. The values the class creates should be cross referenced with current production to solve for current spot price. Note this data is calculated for one household.
     * @param w Current weather using the org.etrade.Weather class
     * @see org.etrade.Weather Weather class*/
    public UsagePredictions(Weather w) {
        this.w = w;


    }

    /**
     * Gets the current season of the year and then applies the average household usage multiplier for that season.
     * @see org.etrade.Weather.Season Summer = 1.36
     * @see org.etrade.Weather.Season Autumn = 1.05,
     * @see org.etrade.Weather.Season Winter = 1.54,
     * @see org.etrade.Weather.Season Spring = 1.21,
     * @return Double multiplier. Typical range = ([-]1.0 <-> 2.0[+]) 1.0 = 1000kwh
     * */
    private double SeasonMultiplier() {
        // Seasonal usage.
        // file:///Users/finleycrowther/Downloads/Electricity_Demand_Profile_of_Australian_Low_Energ.pdf
        // https://www.researchgate.net/publication/270053187_Electricity_Demand_Profile_of_Australian_Low_Energy_Houses/download?_tp=eyJjb250ZXh0Ijp7ImZpcnN0UGFnZSI6Il9kaXJlY3QiLCJwYWdlIjoiX2RpcmVjdCJ9fQ
        // The base is 1000kwh per season. The multiplier sets it to the average household usage for that season.

        if(w.getSeason().toString().equals("SUMMER")) { // Summer 1.36
            return 1.36;
        } else if (w.getSeason().toString().equals("AUTUMN")) { // Autumn 1.05
            return 1.05;
        } else if (w.getSeason().toString().equals("WINTER")) { // Winter 1.54
            return 1.54;
        } else if (w.getSeason().toString().equals("SPRING")) { // Spring 1.21
            return 1.21;
        }

        // Unreachable code
        return -0.0;
    }

    /**
     * If it is currently night returns a slight modifier for usage as household appliances such a lights will be used but this is more to do with peak times. */
    private double NightModifier() {
        return w.isNight(0) ? 0 : 1;
    }

    /**
     * Takes current day time to calculate if it is a peak or off peak time this will then return a multiplier from the enum TimeRange.
     * @see TimeRange peak price ranges.
     * @see Range range methods.
     * @return TimeRange with a Range of typical and expected prices*/
    private TimeRange timePeriod() {
        var hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        var minute = Calendar.getInstance().get(Calendar.MINUTE);

        if(w.getSeason().equals("SPRING") || w.getSeason().equals("AUTUMN"))
            return null;

        if((hour >= 17 && minute >= 30) // High peak (17:30-18:30)
                || (hour <= 18 && minute <= 30))
            return TimeRange.HIGH_PEAK;
        else if ((hour >= 6 && minute >= 30) // Shoulder morning (06:30-07:30)
                || (hour <= 7 && minute <= 30))
            return TimeRange.SHOULDER_MORNING;
        else if ((hour >= 9 && minute >= 30) // Shoulder midday (09:30-10:30)
                || (hour <= 10 && minute <= 30))
            return TimeRange.SHOULDER_MIDDAY;
        else if ((hour >= 12 && minute >= 30) // Midday trough (12:30-13:30)
                || (hour <= 13 && minute <= 30))
            return TimeRange.MIDDAY_TROUGH;
        else if ((hour >= 15 && minute >= 30) // Shoulder afternoon (15:30-16:30)
                || (hour <= 16 && minute <= 30))
            return TimeRange.SHOULDER_AFTERNOON;
        else if ((hour >= 0 && minute >= 30) // Midnight trough (00:30-01:30)
                || (hour <= 1 && minute <= 30))
            return TimeRange.MIDNIGHT_TROUGH;
        else return null;
    }

    /**
     * Stores typical price ranges for peak and off peak times also based off season.
     * @see UsagePredictions.Range Data storage Range
     */
    enum TimeRange {
        HIGH_PEAK(new UsagePredictions.Range(6000, 8000)),
        SHOULDER_MORNING(new UsagePredictions.Range(3500, 6000)),
        SHOULDER_MIDDAY(new UsagePredictions.Range(5000, 7000)),
        MIDDAY_TROUGH(new UsagePredictions.Range(3000, 5000)),
        SHOULDER_AFTERNOON(new UsagePredictions.Range(4000, 6000)),
        MIDNIGHT_TROUGH(new UsagePredictions.Range(2000, 5000));

        private final UsagePredictions.Range range;
        TimeRange(UsagePredictions.Range range) {
            this.range = range;
        }

        /**
         * Update the value of the range for the stored values.
         * @throws IllegalArgumentException if the value is not within the range
         * @see UsagePredictions.Range Data type
         * @param value value to be set. */
        public void setValue(double value) {
            if(this.range.inRange(value))
                this.range.setValue(value);
            else throw new IllegalArgumentException("Value outside of range.");
        }
    }

    /**
     * Data storage for a typical range of values. Can store a numerical value within the range and a start and end value.
     * */
    private static class Range {
        double start;
        double end;
        double value;

        /**
         * Constructor for the Range class setting the end and start values
         * @param start Start value of the range
         * @param end end value of the range */
        public Range(double start, double end) {
            this.start = start;
            this.end = end;
        }

        /**
         * Constructor for the Range class setting the end and start values
         * @param start Start value of the range
         * @param end end value of the range
         * @param value set the numerical value */
        public Range(double start, double end, double value) {
            this.start = start;
            this.end = end;
            this.value = value;
        }

        /**
         * Checks if a value is within the range efficiently
         * @param value a double to be checked
         * @return <Strong>True</Strong> if it is within the range or <Strong>False</Strong> otherwise. */
        public boolean inRange(double value) {
            return value >= start && value <= end;
        }
        public double getStart() {
            return start;
        }
        public double getEnd() {
            return end;
        }
        public double getValue() {
            return value;
        }
        public void setStart(double val) {
            start = val;
        }
        public void setEnd(double val) {
            end = val;
        }
        public void setValue(double val) {
            value = val;
        }
    }
}
