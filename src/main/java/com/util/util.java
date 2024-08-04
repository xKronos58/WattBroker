package com.util;
import com.wattbroker.wattbroker.Data;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;

public class util {
    public static void ClearTemp() throws IOException {
        Path path = Path.of("src/org.etrade.temp");
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

    }

    public static int untilString(int i, String x, String y) {
        // X String being searched for, Y String being searched through, i Start index

        for(; i < y.length(); i++) {
            for (int k = 0; x.charAt(0) != y.charAt(k); k++);
            if(x.equals(y.substring(i, i + x.length()))) return i + x.length();
        }

        errorMessage("' String" + "' not found inside string", "Error");
        throw new IllegalArgumentException("Char not found inside string");

    }

    public static int until(int i, String search, char c) {
        for(; i < search.length(); i++)
            if(search.charAt(i) == c) return i;

        System.out.printf("Char %s not found inside string %s\n",c,search);
        return 0;
    }

    public int BinarySearch(int[] arr, int x) {
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;

            // Check if x is present at mid
            if (arr[mid] == x)
                return mid;

            // If x greater, ignore left half
            if (arr[mid] < x)
                low = mid + 1;

                // If x is smaller, ignore right half
            else
                high = mid - 1;
        }

        // If we reach here, then element was
        // not present
        return -1;
    }
    public int BinarySearch_tV(List<Data.tV> arr, int x) {
        int low = 0, high = arr.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;

            // Check if x is present at mid
            if (arr.get(mid).value() == x)
                return mid;

            // If x greater, ignore left half
            if (arr.get(mid).value() < x)
                low = mid + 1;

                // If x is smaller, ignore right half
            else
                high = mid - 1;
        }

        // If we reach here, then element was
        // not present
        return -1;
    }

    /**
     * Takes a message and builds an information message with the message and title.
     * @param message message to be displayed
     * @param title title of the error message
     * */
    public static void infoMessage(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Takes a message and builds an error message with the message and title.
     * @param message message to be displayed
     * @param title title of the error message
     * */
    public static void errorMessage(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * A class for storing two values x and y
     * @param <X> value of x
     * @param <Y> value of y
     * */
    public static class Vector<X, Y> {
        private X x;
        private Y y;

        // Constructor
        public Vector(X x, Y y) {
            this.x = x;
            this.y = y;
        }

        // Getter for x
        public X getX() {
            return x;
        }

        // Setter for x
        public void setX(X x) {
            this.x = x;
        }

        // Getter for y
        public Y getY() {
            return y;
        }

        // Setter for y
        public void setY(Y y) {
            this.y = y;
        }

        // Override toString() method for better readability
        @Override
        public String toString() {
            return "Vector{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    /**
     * Takes a file name and returns a list of all the lines inside the file.
     * @param fileName name of the file to read
     * @return List of all the lines inside the file
     * @throws IOException if the file does not exist
     */
    public static List<String> readFile(String fileName) throws IOException {
        Path filePath = Path.of(System.getProperty("user.dir"), "SavedEquations", fileName);
        if(!filePath.toFile().exists())
            throw new IllegalArgumentException("File does not exist");

        return Files.readAllLines(filePath);
    }

    public static Path convertFileToPath(String fileName) {
        return Path.of(System.getProperty("user.dir"), "SavedEquations", fileName);
    }

    /**
     * Takes a file name and a line and appends the line to the file.
     * @param  fileName name of the file to write to
     * @param  line String to be appended to the file
     * @throws IOException if the file does not exist
     * */
    public static void writeFile(String fileName, String line) throws IOException {
        Path filePath = Path.of(System.getProperty("user.dir"), "Accounts", fileName);
        Files.writeString(filePath, line + "\n", StandardOpenOption.APPEND);
    }

    /**
     * Takes a file name and clears the file.
     * @param  fileName name of the file to clear
     * @throws IOException if the file does not exist
     * */
    public static void clearFile(String fileName) throws IOException {
        Path filePath = Path.of(System.getProperty("user.dir"), "SavedEquations", fileName);
        Files.writeString(filePath, "", StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void replaceLine(Path filePath, int line) throws IOException {
        String t = Files.readString(filePath);
        System.out.println(t);
    }

    /**
     * Rounds a double value to however many places provided
     * @param value double value to be rounded
     * @param places how many decimal places to be rounded to. */
    public static double Round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
