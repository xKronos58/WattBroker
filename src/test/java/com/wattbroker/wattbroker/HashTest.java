package com.wattbroker.wattbroker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashTest {

    @Test
    public void testHashText_ValidInput_ReturnsExpectedHash() {
        // Arrange
        String[] args = {"password"};

        // Act
        String hashedPassword = Hash.hashText(args);

        // Assert
        assertNotNull(hashedPassword);
        assertFalse(hashedPassword.isEmpty());
    }

    @Test
    public void testHashText_InvalidInput_ThrowsIllegalArgumentException() {
        // Arrange
        String[] args = {}; // No arguments provided

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Hash.hashText(args);
        });
    }

    @Test
    public void testArgon2_ValidInput_ReturnsExpectedResult() {
        // Arrange
        byte[] password = "password".getBytes();
        byte[] salt = "".getBytes();
        int parallelism = 1;
        int tagLength = 32;
        int memorySizeKB = 1024;
        int iterations = 1;
        int version = 0x13;
        byte[] key = new byte[0];
        byte[] associatedData = new byte[0];
        int hashType = 1; // Argon2i

        // Act
        byte[] result = Hash.argon2(password, salt, parallelism, tagLength, memorySizeKB, iterations, version, key, associatedData, hashType);

        // Assert
        assertNotNull(result);
        assertEquals(tagLength, result.length);
    }

    @Test
    public void testArgon2_InvalidMemorySize_ThrowsIllegalArgumentException() {
        // Arrange
        byte[] password = "password".getBytes();
        byte[] salt = "".getBytes();
        int parallelism = 1;
        int tagLength = 32;
        int memorySizeKB = 0; // Invalid memory size
        int iterations = 1;
        int version = 0x13;
        byte[] key = new byte[0];
        byte[] associatedData = new byte[0];
        int hashType = 1; // Argon2i

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Hash.argon2(password, salt, parallelism, tagLength, memorySizeKB, iterations, version, key, associatedData, hashType);
        });
    }

    @Test
    public void testXor_ValidInput_ReturnsExpectedXorResult() {
        // Arrange
        byte[] a = new byte[]{0x01, 0x02, 0x03};
        byte[] b = new byte[]{0x01, 0x02, 0x03};
        byte[] expected = new byte[]{0x00, 0x00, 0x00};

        // Act
        byte[] result = Hash.xor(a, b);

        // Assert
        assertArrayEquals(expected, result);
    }

    @Test
    public void testConcat_ValidInput_ReturnsConcatenatedArray() {
        // Arrange
        byte[] a = new byte[]{0x01, 0x02};
        byte[] b = new byte[]{0x03, 0x04};
        byte[] expected = new byte[]{0x01, 0x02, 0x03, 0x04};

        // Act
        byte[] result = Hash.concat(a, b);

        // Assert
        assertArrayEquals(expected, result);
    }

    @Test
    public void testIntToBytes_ValidInput_ReturnsByteArray() {
        // Arrange
        int value = 256;
        byte[] expected = new byte[]{0x00, 0x01, 0x00, 0x00}; // Little endian representation of 256

        // Act
        byte[] result = Hash.intToBytes(value);

        // Assert
        assertArrayEquals(expected, result);
    }
}