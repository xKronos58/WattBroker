package com.wattbroker.wattbroker;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DB_queryTest {

    private DB_query dbQuery;
    private Connection mockConnection;
    private Statement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        dbQuery = new DB_query();
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);

        // Mocking the DriverManager to return our mock connection
        Mockito.mockStatic(DriverManager.class);
        when(DriverManager.getConnection(anyString())).thenReturn(mockConnection);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
    }

    @AfterEach
    public void tearDown() {
        Mockito.clearAllCaches();
    }

    @Test
    public void testCheckCredentials_ValidCredentials_ReturnsTrue() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("PASSWORD")).thenReturn("admin");

        // Act
        boolean result = dbQuery.checkCredentials("admin", "admin");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCheckCredentials_InvalidCredentials_ReturnsFalse() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("PASSWORD")).thenReturn("wrongpassword");

        // Act
        boolean result = dbQuery.checkCredentials("admin", "admin");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCheckCredentials_NoUserFound_ReturnsFalse() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false);

        // Act
        boolean result = dbQuery.checkCredentials("admin", "admin");

        // Assert
        assertFalse(result);
    }
}