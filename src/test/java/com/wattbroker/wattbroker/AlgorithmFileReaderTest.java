package com.wattbroker.wattbroker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class AlgorithmFileReaderTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testReadFile() {
        AlgorithmFileReader afr = new AlgorithmFileReader("/Users/finleycrowther/Desktop/_SftDev/Criterion 6/WB_Code/WattBroker/src/main/resources/com/wattbroker/wattbroker/_LOCAL_Data_Storage/VIC-1.algorithm", false);
        assertEquals("VIC1", afr.asd.Name());
        assertEquals(84.5, afr.asd.Efficiency());
        assertEquals(501.2, afr.asd.Holdings());
        assertEquals(1000, afr.asd.getSettings().getMax_holdings());
        assertEquals(100, afr.asd.getSettings().getMin_holdings());
        assertEquals(1000, afr.asd.getSettings().getMax_profit());
        assertEquals(100, afr.asd.getSettings().getMin_profit());
        assertEquals(100, afr.asd.getSettings().getMax_efficiency());
        assertEquals(50, afr.asd.getSettings().getMin_efficiency());
        assertEquals(100, afr.asd.getSettings().getMax_loss());
        assertEquals(50, afr.asd.getSettings().getMin_loss());
        assertEquals(100, afr.asd.getSettings().getMax_risk());
        assertEquals(50, afr.asd.getSettings().getMin_risk());
        assertEquals(100, afr.asd.getSettings().getMax_trade());
        assertEquals(50, afr.asd.getSettings().getMin_trade());
        assertEquals(100, afr.asd.getSettings().getMax_trade_efficiency());
        assertEquals(50, afr.asd.getSettings().getMin_trade_efficiency());
        assertEquals(100, afr.asd.getSettings().getMax_trade_holdings());
        assertEquals(50, afr.asd.getSettings().getMin_trade_holdings());
        assertEquals(100, afr.asd.getSettings().getMax_trade_loss());
        assertEquals(50, afr.asd.getSettings().getMin_trade_loss());
        assertEquals(100, afr.asd.getSettings().getMax_trade_profit());
        assertEquals(50, afr.asd.getSettings().getMin_trade_profit());
        assertEquals(100, afr.asd.getSettings().getMax_trade_risk());
        assertEquals(50, afr.asd.getSettings().getMin_trade_risk());
        assertEquals(100, afr.asd.getSettings().getMax_trade_status());
        assertEquals(50, afr.asd.getSettings().getMin_trade_status());
        assertEquals(100, afr.asd.getSettings().getMax_trade_source());
        assertEquals(50, afr.asd.getSettings().getMin_trade_source());
        assertEquals(100, afr.asd.getSettings().getMax_trade_settings());
        assertEquals(50, afr.asd.getSettings().getMin_trade_settings());
    }
}