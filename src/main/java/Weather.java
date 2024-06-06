package org.etrade;

import ch.hsr.geohash.GeoHash;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Weather {
    Path filePath;
    List<Hour> Content;
    public Weather(double lat, double lng) throws IOException {
        this.filePath = getWeather(lat, lng);
        this.Content = getJSONArray();

        System.out.printf("org.etrade.Weather data has been loaded from %s\nWith %s hours loaded", filePath, Content.size());
    }

    public int getTemperature(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).getTemp();
    }

    public int getFeelsLike(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).getFeels_like();
    }

    public int getDewPoint(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).getDew_point();
    }

    public int getRelativeHumidity(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).getRelative_humidity();
    }

    public byte getUV(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).getUv();
    }

    public String getRain(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).getRain().formatRain();
    }

    public Wind getWind(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).wind;
    }

    public String getIcon(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).getIcon();
    }

    public String getTime(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).getTime().toString();
    }

    public String formatForOutput(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).formatForOutput();
    }

    public boolean isNight(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).is_night();
    }

    public Date getThreeHourForecast(int hour) {
        if(hour > Content.size())
            throw new ArrayIndexOutOfBoundsException("Hour is out of bounds");

        return Content.get(hour).getThree_hour_forecast();
    }

    private List<Hour> getJSONArray() {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(String.valueOf(filePath))) {
            // Parse the JSON file
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;

            // Get the "data" array
            JSONArray dataArray = (JSONArray) jsonObject.get("data");
            List<Hour> tempList = new ArrayList<>();

            // Iterate through the array and do something with each element
            for (Object dataObj : dataArray) {
                JSONObject data = (JSONObject) dataObj;
                // Adds the information into the list
                tempList.add(BuildHour(data));
            }
            return tempList;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Hour BuildHour(JSONObject data) throws IOException {
        boolean is_night = (boolean) data.get("is_night");
        Rain rain = BuildRain((JSONObject) data.get("rain"));
        byte uv = ((Long) data.get("uv")).byteValue();
        int temp = ((Long) data.get("temp")).intValue();
        Date three_hour_forecast = (Date) data.get("three_hour_forecast");
        int dew_point = ((Long) data.get("dew_point")).intValue();
        int feels_like = data.get("feels_like") == null ? temp : ((Long) data.get("feels_like")).intValue();
//        Date time =/*(Date) data.get("time");*/
        byte relative_humidity = ((Long) data.get("relative_humidity")).byteValue();
        String icon = (String) data.get("icon");
        Wind wind = BuildWind((JSONObject) data.get("wind"));

        return new Hour(is_night, rain, uv, temp, three_hour_forecast, dew_point, feels_like, null, relative_humidity, icon, wind);
    }

    private Wind BuildWind(JSONObject wind) {
        int gust_speed_km = ((Long) wind.get("gust_speed_kilometre")).intValue();
        int speed_km = ((Long) wind.get("speed_kilometre")).intValue();
        int gust_speed_knots = ((Long) wind.get("gust_speed_knot")).intValue();
        int speed_knots = ((Long) wind.get("speed_knot")).intValue();
        DIRECTION direction = DIRECTION.valueOf((String) wind.get("direction"));

        return new Wind(gust_speed_km, speed_km, gust_speed_knots, speed_knots, direction);
    }

    private Rain BuildRain(JSONObject rain) {
        int precipitation_50_chance = ((Long) rain.get("precipitation_amount_50_percent_chance")).intValue();
        Amount amount = BuildAmount((JSONObject) rain.get("amount"));
        int chance = ((Long) rain.get("chance")).intValue();
        int precipitation_10_chance = ((Long) rain.get("precipitation_amount_10_percent_chance")).intValue();
        int precipitation_25_chance = ((Long) rain.get("precipitation_amount_25_percent_chance")).intValue();

        return new Rain(precipitation_50_chance, amount, chance, precipitation_10_chance, precipitation_25_chance);
    }

    private Amount BuildAmount(JSONObject amount) {
        int min = ((Long) amount.get("min")).intValue();
        int max = amount.get("max") == null ? min : ((Long) amount.get("max")).intValue();
        Unit units = Unit.valueOf((String) amount.get("units"));

        return new Amount(min, max, units);
    }

    public Path getWeather(double lat, double lng) throws IOException {


        GeoHash geohash = GeoHash.withCharacterPrecision(lat, lng, 12);
        String geohashString = geohash.toBase32().substring(0, 6);

        String path = "src/org.etrade.temp/weather" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")) + "_" + geohashString + ".json";
        Path path1 = Path.of(path);
        if(Files.exists(path1))
            return path1;

        String url = "https://api.weather.bom.gov.au/v1/locations/" + geohashString + "/forecasts/hourly";
        CustomHttpRequest request = new CustomHttpRequest(url);
        System.out.println(url);

        File file = new File(path);

        if(!Files.exists(Path.of("src/org.etrade.temp")))
            new File("src/org.etrade.temp").mkdir();

        if(!file.createNewFile())
            throw new FileAlreadyExistsException("File already exists");

        Files.write(file.toPath(), request.getResponse().getBytes());

        return file.toPath();
    }

    private class Hour {
        private final boolean is_night;
        private final Rain rain;
        private final byte uv;
        private final int temp;
        private final Date three_hour_forecast;
        private final int dew_point;
        private final int feels_like;
        private final Date time;
        private final byte relative_humidity;
        private final String icon;
        private final Wind wind;
        public Hour(boolean is_night, Rain rain, byte uv, int temp, Date three_hour_forecast, int dew_point, int feels_like, Date time, byte relative_humidity, String icon, Wind wind) throws IOException {
            this.is_night = is_night;
            this.rain = rain;
            this.uv = uv;
            this.temp = temp;
            this.three_hour_forecast = three_hour_forecast;
            this.dew_point = dew_point;
            this.feels_like = feels_like;
            this.time = time;
            this.relative_humidity = relative_humidity;
            this.icon = icon;
            this.wind = wind;
        }

        public boolean is_night() {
            return is_night;
        }

        public Rain getRain() {
            return rain;
        }

        public byte getUv() {
            return uv;
        }

        public int getTemp() {
            return temp;
        }

        public Date getThree_hour_forecast() {
            return three_hour_forecast;
        }

        public int getDew_point() {
            return dew_point;
        }

        public int getFeels_like() {
            return feels_like;
        }

        public Date getTime() {
            return time;
        }

        public byte getRelative_humidity() {
            return relative_humidity;
        }

        public String getIcon() {
            return icon;
        }

        public Wind getWind() {
            return wind;
        }

        public String formatForOutput() {
            return String.format("" +
                    "Time: %s" +
                    "\nTemp: %s" +
                    "\nFeels Like: %s" +
                    "\nDew Point: %s" +
                    "\nRelative Humidity: %s" +
                    "\nUV: %s" +
                    "\nRain: %s" +
                    "\nWind: %s" +
                    "\nIcon: %s\n", time, temp, feels_like, dew_point, relative_humidity, uv, rain.formatRain(), wind.formatWind(), icon);
        }
    }

    private class Rain {
        int precipitation_50_chance;
        Amount amount;
        int chance;
        int precipitation_10_chance;
        int precipitation_25_chance;

        public Rain(int precipitation_50_chance, Amount amount, int chance, int precipitation_10_chance, int precipitation_25_chance) {
            this.precipitation_50_chance = precipitation_50_chance;
            this.amount = amount;
            this.chance = chance;
            this.precipitation_10_chance = precipitation_10_chance;
            this.precipitation_25_chance = precipitation_25_chance;
        }

        public int getPrecipitation_50_chance() {
            return precipitation_50_chance;
        }

        public Amount getAmount() {
            return amount;
        }

        public int getChance() {
            return chance;
        }

        public int getPrecipitation_10_chance() {
            return precipitation_10_chance;
        }

        public int getPrecipitation_25_chance() {
            return precipitation_25_chance;
        }

        public String formatRain() {
            return String.format("Chance of rain: %s\nAmount: %s\nChance of 10 percent: %s\nChance of 25 percent: %s", chance, amount.formatAmount(), precipitation_10_chance, precipitation_25_chance);
        }
    }

    private class Amount {
        private int min;
        private int max;
        private Unit units;
        public Amount(int min, int max, Unit untis) {
            this.min = min;
            this.max = max;
            this.units = untis;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public Unit getUnits() {
            return units;
        }

        public String formatAmount() {
            return String.format("Min: %s\nMax: %s\nUnits: %s", min, max, units);
        }
    }

    public enum Unit {
        mm("mm"),
        cm("cm");

        Unit(String mm) {}
    }

    public class Wind {
        private int gust_speed_km;
        private int speed_km;
        private int gust_speed_knots;
        private int speed_knots;
        private DIRECTION direction;

        public Wind(int gust_speed_km, int speed_km, int gust_speed_knots, int speed_knots, DIRECTION direction) {
            this.gust_speed_km = gust_speed_km;
            this.speed_km = speed_km;
            this.gust_speed_knots = gust_speed_knots;
            this.speed_knots = speed_knots;
            this.direction = direction;
        }

        public int getGust_speed_km() {
            return gust_speed_km;
        }

        public int getSpeed_km() {
            return speed_km;
        }

        public int getGust_speed_knots() {
            return gust_speed_knots;
        }

        public int getSpeed_knots() {
            return speed_knots;
        }

        public DIRECTION getDirection() {
            return direction;
        }

        public String formatWind() {
            return String.format("Gust Speed KM: %s\nSpeed KM: %s\nGust Speed Knots: %s\nSpeed Knots: %s\nDirection: %s", gust_speed_km, speed_km, gust_speed_knots, speed_knots, direction);
        }
    }

    public enum DIRECTION {
        N("N"),
        NNE("NNE"),
        NE("NE"),
        ENE("ENE"),
        E("E"),
        ESE("ESE"),
        SE("SE"),
        SSE("SSE"),
        S("S"),
        SSW("SSW"),
        SW("SW"),
        WSW("WSW"),
        W("W"),
        WNW("WNW"),
        NW("NW"),
        NNW("NNW");

        DIRECTION(String d) {}
    }
}