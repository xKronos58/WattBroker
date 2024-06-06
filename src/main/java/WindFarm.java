import java.io.IOException;
import java.util.List;

public class WindFarm {

    String Participant, StationName, Region, Dispatch,
            Category, Classification, FuelSource_Primary,
            FuelSource_Descriptor, TechnologyType_Primary,
            TechnologyType_Descriptor, PhysicalUnitNo;
    double lat, lng, UnitSize_MW, RegCapMW, MaxCapMW,
            MaxROC_MI;
    List<Double> Efficiency;
    boolean Aggregation;
    Weather weather;
    Weather w = new Weather(lat, lng);

    public WindFarm(String Participant, double lat, double lng, String StationName, String Region, String Dispatch,
                    String Category, String Classification, String FuelSource_Primary, String FuelSource_Descriptor,
                    String TechnologyType_Primary, String TechnologyType_Descriptor, String PhysicalUnitNo, double UnitSize_MW,
                    boolean Aggregation, double RegCapMW, double MaxCapMW, double MaxROC_MI) throws IOException {
        this.Participant = Participant;
        this.StationName = StationName;
        this.Region = Region;
        this.Dispatch = Dispatch;
        this.Category = Category;
        this.Classification = Classification;
        this.FuelSource_Primary = FuelSource_Primary;
        this.FuelSource_Descriptor = FuelSource_Descriptor;
        this.TechnologyType_Primary = TechnologyType_Primary;
        this.TechnologyType_Descriptor = TechnologyType_Descriptor;
        this.PhysicalUnitNo = PhysicalUnitNo;
        this.lat = lat;
        this.lng = lng;
        this.UnitSize_MW = UnitSize_MW;
        this.RegCapMW = RegCapMW;
        this.MaxCapMW = MaxCapMW;
        this.MaxROC_MI = MaxROC_MI;
        this.Aggregation = Aggregation;
        this.weather = new Weather(lat, lng);

        this.Efficiency.get(0);
    }


    public double CalculateEfficiency(int hour) {
        Weather.Wind w = this.weather.getWind(hour);
        if(w.getSpeed_km() == 0.0)
            return 0.0;

        int zRef = 10; // Reference height in meters
        double a = 0.143; // Wind shear exponent
        int z = 110; // Turbine height in meters
        int vRef = w.getSpeed_km(); // Wind speed at reference height in m/s
        Weather.DIRECTION dir = w.getDirection();

        // Formula for wind turbine efficiency;
        // V = V_{ref} * (Z / Z_{ref})^a.
        // Where V is the wind speed at the turbine height, V_{ref} is the wind speed at the reference height,
        // Z is the turbine height, Z_{ref} is the reference height, and a is the wind shear exponent.

        // To calculate power output 1/2 x ρ x A x v^3; where ρ is the air density,
        //      A is the rotor-swept area, v is the wind speed.

        // Get the number of physical units in the range
        double physicalUnitNumberMaxRange = Double.parseDouble(this.PhysicalUnitNo.substring(util.until(0, this.PhysicalUnitNo, '-')));
        // Calculate wind speed at average turbine height
        double v = vRef * Math.pow((double) z / zRef, a);
        // Calculate power output
        double power = 0.5 * 1.225 * Math.PI * Math.pow(40, 2) * Math.pow(v, 3);
        // Take those variables and calculate the efficiency per unit
        double maxEfficiencyPerUnit = this.MaxCapMW / physicalUnitNumberMaxRange;

        return power / maxEfficiencyPerUnit;
    }
}
