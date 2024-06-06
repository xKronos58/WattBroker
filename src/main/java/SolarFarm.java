import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SolarFarm {
    String Participant, StationName, Region, Dispatch,
            Category, Classification, FuelSource_Primary,
            FuelSource_Descriptor, TechnologyType_Primary,
            TechnologyType_Descriptor, PhysicalUnitNo;
    double lat, lng, UnitSize_MW, RegCapMW, MaxCapMW,
            MaxROC_MI, Efficiency;
    boolean Aggregation;

    Weather weather;

    List<Clouds> clouds;

    File file;

    /**
     * A data structure to store relevant information about a solar farm
     * @param Participant The company of the solar farm
     * @param lat The latitude of the solar farm
     * @param lng The longitude of the solar farm
     * @param StationName The name of the solar farm
     * @param Region The region of the solar farm
     * @param Dispatch The dispatch of the solar farm
     * @param Category The category of the solar farm
     * @param Classification The classification of the solar farm
     * @param FuelSource_Primary The primary fuel source of the solar farm
     * @param FuelSource_Descriptor The descriptor of the fuel source
     * @param TechnologyType_Primary The primary technology type of the solar farm
     * @param TechnologyType_Descriptor The descriptor of the technology type
     * @param PhysicalUnitNo The physical unit number of the solar farm
     * @param UnitSize_MW The size of the unit in MW
     * @param Aggregation Whether the solar farm is aggregated
     * @param RegCapMW The registered capacity of the solar farm in MW
     * @param MaxCapMW The maximum capacity of the solar farm in MW
     * @param MaxROC_MI The maximum rate of change in MW
     * */
    public SolarFarm(String Participant, double lat, double lng, String StationName, String Region, String Dispatch,
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
        getCloudCoverage();
        this.Efficiency = calculateEfficiencyHour(0);

        System.out.println("Efficiency: " + this.Efficiency);
    }

    public Weather getWeather() throws IOException {
        return this.weather;
    }

    /**
     * Calculates the efficiency of the solar panel on a given hour based of the weather conditions
     * @param hour The hour of the day to calculate the efficiency
     * @return the efficiency of the solar panel as a double
     * */
    public double calculateEfficiencyHour(int hour) {
        double finalVal;

        // If it is night time, the efficiency is 0
        if(this.weather.isNight(hour))
            return 0.0;

        // Takes the cloud coverage efficiency.
        // If it is above 50% then the efficiency is 25%,
        // if it is above 25% then the efficiency is 50%,
        // otherwise it is 100%
        if(this.clouds.get(hour).cloudCover > 50)
            finalVal = 0.175; // Note this ranges from 0.25 to 0.10 depending on the panel manufacturer
        else if(this.clouds.get(hour).cloudCover > 25)
            finalVal = 0.5;
        else
            finalVal = 1.0;

        // the output value is degraded by ~0.345%/°C above 25°C
        if(this.weather.getTemperature(hour) > 25)
            finalVal -= (this.weather.getTemperature(hour) - 25) * 0.00345;

        return finalVal;
    }

    public void getCloudCoverage() throws IOException {
        // https://api.open-meteo.com/v1/bom?latitude=52.52&longitude=13.41&hourly=cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high
        // https://api.open-meteo.com/v1/bom?r1vh7d&hourly=cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high

        CustomHttpRequest cr = new CustomHttpRequest("https://api.open-meteo.com/v1/bom?latitude="+lat+"&longitude="+lng+"&hourly=cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high");
        File f = new File("src/org.etrade.temp/clouds_"+StationName+"_"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"))+".json");
        if(Files.exists(f.toPath()))
            Files.delete(f.toPath());

        Files.createFile(f.toPath());
        Files.write(f.toPath(), cr.getResponse().getBytes());
        System.out.println(cr.getResponse());
        this.file = f;
        this.clouds = getJSONArray();
    }

    private List<Clouds> getJSONArray() {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(file.getPath())) {
            // Parse the JSON file
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;

            // Get the "hourly" array
            JSONObject hourly = (JSONObject) jsonObject.get("hourly");
            JSONArray cloudCoverage = (JSONArray) hourly.get("cloud_cover");
            JSONArray cloudCoverLow = (JSONArray) hourly.get("cloud_cover_low");
            JSONArray cloudCoverMid = (JSONArray) hourly.get("cloud_cover_mid");
            JSONArray cloudCoverHigh = (JSONArray) hourly.get("cloud_cover_high");
            JSONArray time = (JSONArray) hourly.get("time");

            List<Clouds> temp = new ArrayList<>();

            for(int i = 0; i < time.size(); i++)
                temp.add(new Clouds((String) time.get(i), (long) cloudCoverage.get(i), (long) cloudCoverLow.get(i), (long) cloudCoverMid.get(i), (long) cloudCoverHigh.get(i)));


            return temp;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public record Clouds(String time, long cloudCover, long cloudCoverLow, long cloudCoverMid, long cloudCoverHigh) {};
}
