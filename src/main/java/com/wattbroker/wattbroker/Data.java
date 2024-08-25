package com.wattbroker.wattbroker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Arrays.*;

/**
 * Data reading class for the graphing protocols. Formats and provides data from sources in the _LOCAL_Data_Storage resource dir. */
public class Data {
    public static final String CONTROL_SERVER_IP = "192.168.1.1:0000"; /* CHANGED INSIDE APPLICATION, CHECK RUN IF UNCHANGED */
    public static final String APPLICATION_SERVER_IP = "192.168.1.1:0000"; /* CHANGED INSIDE APPLICATION, CHECK RUN IF UNCHANGED */

    public List<tV> getMarketData(Date date, String fileName) {
        // Path to the file containing the data
        //                "market@2024-06-11_00:00:00-23:59:00.csv";
        /* - LOCAL DATA LOCATION TODO: Add location definition for control server */
        String filePath = String.format("src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/%s",fileName);
        List<tV> temp = new ArrayList<>();

        int it = 0;
        try (Scanner scanner = new Scanner(new File(filePath))) {

            while(scanner.hasNextLine()) {
                if(it == 0) {
                    scanner.nextLine();
                    it++;
                    continue;
                }
                String[] line = scanner.nextLine().split(",");
                temp.add(new tV(line[0], Double.parseDouble(line[1])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }

    /**
     * Read AEMO specific data structure as it is different to generated data from algorithm.
     * @param fileName Name of the file * under the /_LOCAL_Data_Storage/ dir. Do not include in filename
     * @see AEMO data struct
     * @see Data getMarketData for regular data*/
    public List<AEMO> getAEMOdata(String fileName) {
        String filePath = String.format("src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/%s",fileName);

        // Create a temporary list to store the data
        List<AEMO> temp = new ArrayList<>();

        // Read the file and store the data in the temporary list
        try (Scanner reader = new Scanner(new File(filePath))) {

            while (reader.hasNextLine()) {
                String[] line = reader.nextLine().split(",");
                if (line.length == 7 && !line[0].startsWith("S")) // Ignore header of csv and incomplete data.
                    temp.add(new AEMO(line[0], Double.parseDouble(line[1]), Double.parseDouble(line[2]), Double.parseDouble(line[3]), Double.parseDouble(line[4]), Double.parseDouble(line[5]), line[6]));
            }       // Date, Spot Price, Scheduled Demand, Scheduled Generation, Semi Scheduled Generation, Net Import, Type
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return temp;
    }

    public record tV(String dateTime, double value) {
        public double dateTimeAsDouble() {
            String ymd = dateTime.split(" ")[0];
            String hms = dateTime.split(" ")[1];
            String[] ymd_ = ymd.split("-");
            String[] hms_ = hms.split(":");
            return stream(ymd_).mapToInt(Integer::parseInt).sum() + stream(hms_).mapToInt(Integer::parseInt).sum();
        }
    }
}
