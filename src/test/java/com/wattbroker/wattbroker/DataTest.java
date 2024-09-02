package com.wattbroker.wattbroker;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DataTest {

    @Test
    void testGetMarketData() throws IOException {
        // Arrange
        Data data = new Data();
        String tempFileName = "market_temp.csv";
        Path tempFilePath = Path.of("src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/", tempFileName);

        String csvContent = "Date,Value\n" +
                "2024-06-11 00:00:00,100.0\n" +
                "2024-06-11 01:00:00,105.0\n";

        Files.createDirectories(tempFilePath.getParent());
        Files.writeString(tempFilePath, csvContent);

        // Act
        List<Data.tV> marketData = data.getMarketData(null, tempFileName);

        // Assert
        assertNotNull(marketData);
        assertEquals(2, marketData.size());
        assertEquals(100.0, marketData.get(0).value());
        assertEquals(105.0, marketData.get(1).value());

        // Cleanup
        Files.deleteIfExists(tempFilePath);
    }

    @Test
    void testGetAEMOdata() throws IOException {
        // Arrange
        Data data = new Data();
        String tempFileName = "aemo_temp.csv";
        Path tempFilePath = Path.of("src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/", tempFileName);

        String csvContent = "Date,Spot Price,Scheduled Demand,Scheduled Generation,Semi Scheduled Generation,Net Import,Type\n" +
                "2024-06-11 00:00:00,100.0,200.0,300.0,400.0,500.0,NEM\n" +
                "2024-06-11 01:00:00,110.0,210.0,310.0,410.0,510.0,NEM\n";

        Files.createDirectories(tempFilePath.getParent());
        Files.writeString(tempFilePath, csvContent);

        // Act
        List<AEMO> aemoData = data.getAEMOdata(tempFileName);

        // Assert
        assertNotNull(aemoData);
        assertEquals(2, aemoData.size());
        assertEquals(100.0, aemoData.get(0)._spotPrice);
        assertEquals(210.0, aemoData.get(1)._demand);

        // Cleanup
        Files.deleteIfExists(tempFilePath);
    }

    @Test
    void testTvDateTimeAsDouble() {
        // Arrange
        Data.tV tvInstance = new Data.tV("2024-06-11 00:00:00", 100.0);

        // Act
        double result = tvInstance.dateTimeAsDouble();

        // Assert
        assertEquals(2041.0, result, "The dateTimeAsDouble method should return the correct sum of date and time parts.");
    }
}