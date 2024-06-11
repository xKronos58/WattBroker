package org.etrade;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Algo_Run {
    public static void main(String[] args) throws IOException {
        List<SolarFarm> solarFarmsList = getSolarFarms();
    }

    private static List<Weather> fillWeather(List<SolarFarm> farms) {
        List<Weather> temp = new ArrayList<>();
        for(SolarFarm farm : farms) {
            try {
                temp.add(farm.getWeather());
                System.out.println(farm.weather.getTemperature(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public static List<SolarFarm> getSolarFarms() {
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/_PV_SOLAR_FARMS.csv"))) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return formatToSolarfarm(records);
    }

    private static List<SolarFarm> formatToSolarfarm(List<List<String>> records) {
        List<SolarFarm> temp = new ArrayList<>();
        int itterator = 0;
        for(List<String> T1 : records) {
            if(itterator == 0) {
                itterator++;
                continue;
            }

            try {
                temp.add(new SolarFarm(T1.get(0), Double.parseDouble(T1.get(1)), Double.parseDouble(T1.get(2)), T1.get(3), T1.get(4),
                        T1.get(5), T1.get(6), T1.get(7), T1.get(8), T1.get(9), T1.get(10), T1.get(11), T1.get(12),
                        Double.parseDouble(T1.get(13)), T1.get(14).equals("Y"), Double.parseDouble(T1.get(16)),
                        Double.parseDouble(T1.get(17)), Double.parseDouble(T1.get(18))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return temp;
    }

    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}