package com.wattbroker.wattbroker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * /!\ IMPORTANT /!\
 * This class is only used to generate fake
 * data to test the graph while waiting for
 * the algorithm to be finalised.
 * */
public class fakeDataBuilder {
    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);
        System.out.println("Type of data: \"Day\" [1] | \"Week\" [2] | \"Hour\" [3]");
        switch (inp.nextLine()) {
            case "1":
                System.out.println(buildDayData() ? "Data built successfully" : "Data build failed");
                break;
            case "2":
                System.out.println(buildWeekData() ? "Data built successfully" : "Data build failed");
                break;
            case "3":
                System.out.println(buildHourData() ? "Data built successfully" : "Data build failed");
                break;
            default:
                System.out.println("Invalid input");
                break;
        };
    }
    private static Boolean buildDayData() {
        System.out.println("Building day data");

//      "2024-06-06 XX:XX:00,value\n";

        List<String> data = new ArrayList<>();


        for(int i = 0; i < 24*60; i++){
            // \sin\left(x+2\pi\right)\cos\left(4x+\pi\right)+2
            data.add(String.format("2024-06-06 %02d:%02d:00,%f", i/60, i%60, Math.sin((double) (i) /300 + 2*Math.PI)*Math.cos((double) (4 * i) /100 + Math.PI) + 2));
        }

        writeListToFile(data, "src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/market@2024-06-01_00:00:00-23:59:00.csv");
        return true;
    }

    public static void writeListToFile(List<String> lines, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Write a new line after each string
            }
        } catch (IOException e) {
            System.err.println("An IOException was caught: " + e.getMessage());
        }
    }

    double findNext(double value, double pmChance) {
        return 0.0;
    }

    private static Boolean buildWeekData() {
        System.out.println("Building week data");
        return false;
    }

    private static Boolean buildHourData() {
        System.out.println("Building hour data");
        return false;
    }
}
