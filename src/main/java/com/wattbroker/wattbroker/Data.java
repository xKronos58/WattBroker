package com.wattbroker.wattbroker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Arrays.*;

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
