package com.wattbroker.wattbroker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Data {
    public static final String CONTROL_SERVER_IP = "192.168.1.1:0000"; /* CHANGED INSIDE APPLICATION, CHECK RUN IF UNCHANGED */
    public static final String APPLICATION_SERVER_IP = "192.168.1.1:0000"; /* CHANGED INSIDE APPLICATION, CHECK RUN IF UNCHANGED */

    // TODO : Handle data with C++ for correct memory management
    public List<tV> getMarketData(Date date) {
        // Path to the file containing the data
        String fileName = "/market@2024-06-06_00:00:00-23:59:00";
        /* - LOCAL DATA LOCATION TODO: Add location definition for control server */
        String filePath = String.format("src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/%s",fileName);
        String filePath_2 = "src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/market@2024-06-06_00:00:00-23:59:00.csv";
        List<tV> temp = new ArrayList<>();

        int it = 0;
        try (Scanner scanner = new Scanner(new File(filePath_2))) {

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

    public record tV(String dateTime, double value) {}
}
