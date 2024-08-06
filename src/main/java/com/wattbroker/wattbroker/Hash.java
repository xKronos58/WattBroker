package com.wattbroker.wattbroker;

import org.bouncycastle.crypto.digests.Blake2bDigest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Class used to salt and hash user passwords and check user passwords against the hash for sign-in. Based off the argon 2 method of text / password hashing returning a byte array in string form of the salted and hashed string.
 * @see . Hash.hashText() for implimentation of the algorithm.
 */
public class Hash {

    /**
     * Computing the hash for the input message
     * @param message to be hashed
     * @param digestSize size of bytes to be hashed against
     * @return hashed result of message */
    private static byte[] hash(byte[] message, int digestSize) {
        if (digestSize <= 64) {
            return blake2b(digestSize, message);
        }

        int r = (int) Math.ceil(digestSize / 32.0) - 2;

        byte[] v1 = blake2b(64, concat(intToBytes(digestSize), message));
        byte[] vPrevious = v1;

        byte[] result = new byte[digestSize];
        for (int i = 0; i <= r; i++) {
            byte[] vi = blake2b(64, vPrevious);
            vPrevious = vi;
            System.arraycopy(vi, 0, result, i * 32, Math.min(32, digestSize - i * 32));
        }

        int partialBytesNeeded = digestSize - 32 * r;
        byte[] vLast = blake2b(partialBytesNeeded, vPrevious);
        System.arraycopy(vLast, 0, result, 32 * r, partialBytesNeeded);

        return result;
    }

    /**
     * Using Blake2bDigest to encrypt the input
     * @see Blake2bDigest Blake2bDigest
     * @param digestSize Digest Size
     * @param input String to be encrypted */
    private static byte[] blake2b(int digestSize, byte[] input) {
        Blake2bDigest blake2bDigest = new Blake2bDigest(digestSize * 8);
        blake2bDigest.update(input, 0, input.length);
        byte[] output = new byte[digestSize];
        blake2bDigest.doFinal(output, 0);
        return output;
    }

    /**
     * Swaps an int to byte format
     * @param value int to be swapped
     * @return byte[] of the integer */
    private static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
    }

    /**
     * Concatenates multiple byte arrays into one
     * @param arrays the arrays to concatenate
     * @return the concatenated array
     */
    private static byte[] concat(byte[]... arrays) {
        int totalLength = Arrays.stream(arrays).mapToInt(arr -> arr.length).sum();
        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * Implementation of the Argon2 algorithm
     * @param password the password to hash
     * @param salt the salt to use
     * @param parallelism the parallelism factor
     * @param tagLength the length of the output tag
     * @param memorySizeKB the memory size in KB
     * @param iterations the number of iterations
     * @param version the version of the algorithm
     * @param key the key to use
     * @param associatedData the associated data
     * @param hashType the hash type
     * @return the hash of the password
     */
    public static byte[] argon2(byte[] password, byte[] salt, int parallelism, int tagLength, int memorySizeKB, int iterations, int version, byte[] key, byte[] associatedData, int hashType) {
        int totalLength = 6 * 4 // 6 integers (parallelism, tagLength, memorySizeKB, iterations, version, hashType)
                + 4 + password.length // length of password + password bytes
                + 4 + salt.length // length of salt + salt bytes
                + 4 + key.length // length of key + key bytes
                + 4 + associatedData.length; // length of associatedData + associatedData bytes

        ByteBuffer buffer = ByteBuffer.allocate(totalLength).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putInt(parallelism)
                .putInt(tagLength)
                .putInt(memorySizeKB)
                .putInt(iterations)
                .putInt(version)
                .putInt(hashType)
                .putInt(password.length)
                .put(password)
                .putInt(salt.length)
                .put(salt)
                .putInt(key.length)
                .put(key)
                .putInt(associatedData.length)
                .put(associatedData);

        byte[] h0 = blake2b(64, buffer.array());

        int blockCount = memorySizeKB / (4 * parallelism);
        int columnCount = blockCount / parallelism;

        byte[][][] memory = new byte[parallelism][columnCount][1024];

        for (int i = 0; i < parallelism; i++) {
            memory[i][0] = hash(concat(h0, intToBytes(0), intToBytes(i)), 1024);
            memory[i][1] = hash(concat(h0, intToBytes(1), intToBytes(i)), 1024);
        }

        // Compute remaining columns of each lane
        for (int i = 0; i < parallelism; i++) {
            for (int j = 2; j < columnCount; j++) {
                int[] indexes = getBlockIndexes(i, j, parallelism, columnCount);
                int iPrime = indexes[0], jPrime = indexes[1];
                memory[i][j] = xor(memory[i][j - 1], hash(concat(memory[iPrime][jPrime]), 1024));
            }
        }

        for (int iter = 1; iter < iterations; iter++) {
            for (int i = 0; i < parallelism; i++) {
                for (int j = 0; j < columnCount; j++) {
                    int[] indexes = getBlockIndexes(i, j, parallelism, columnCount);
                    int iPrime = indexes[0], jPrime = indexes[1];
                    if (j == 0) {
                        memory[i][0] = xor(memory[i][0], hash(concat(memory[i][columnCount - 1], memory[iPrime][jPrime]), 1024));
                    } else {
                        memory[i][j] = xor(memory[i][j - 1], hash(concat(memory[iPrime][jPrime]), 1024));
                    }
                }
            }
        }

        byte[] finalBlock = memory[0][columnCount - 1];
        for (int i = 1; i < parallelism; i++) {
            finalBlock = xor(finalBlock, memory[i][columnCount - 1]);
        }

        return hash(finalBlock, tagLength);
    }

    /**
     * Implementation of the Argon2i algorithm
     * @param i the i-th block
     * @param j the j-th block
     * @param parallelism the parallelism factor
     * @param columnCount the number of columns
     * @return the indexes of the block to be used for hashing
     * @implNote CURRENTLY USING PLACEHOLDER FOR LOGIC TODO IMPLEMENT LOGIC */
    private static int[] getBlockIndexes(int i, int j, int parallelism, int columnCount) {
        // This function should implement the index selection based on the hashType
        // For simplicity, this function needs to be defined according to section 3.4
        return new int[]{i, (j - 1) % columnCount}; // Placeholder, replace with actual logic
    }

    /**
     * Runs an xor algorithm replacing bytes based of length and the '^' function
     * @param a first byte array
     * @param b second byte array
     * @return byte[] scrambled based off length */
    private static byte[] xor(byte[] a, byte[] b) {
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

    /**
     * Argon 2 based hashing algorithm used for hashing passwords stored within the database for security reasons.
     * @param args String password or content to be encrypted
     * @return String result of the byte array after the hash
     * @implNote Does not include salting and is just the raw hash */
    public static String hashText(String[] args) {
        if(args.length != 1)
            throw new IllegalArgumentException("Please provide the password as an argument");
        byte[] password = args[0].getBytes();
        byte[] salt = "".getBytes();
        int parallelism = 1;
        int tagLength = 32;
        int memorySizeKB = 1024;
        int iterations = 1;
        int version = 0x13;
        byte[] key = new byte[0];
        byte[] associatedData = new byte[0];
        int hashType = 1; // Argon2i

        byte[] result = argon2(password, salt, parallelism, tagLength, memorySizeKB, iterations, version, key, associatedData, hashType);

        return Arrays.toString(result);
    }
}