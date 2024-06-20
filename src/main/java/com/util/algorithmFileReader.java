package com.util;

import ECC.ECC;

import javax.crypto.Cipher;
import java.io.File;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class algorithmFileReader {

    File file;
    String key;
    List<String> EncryptedContent;

    public algorithmFileReader(File file, String key) {
        this.file = file;
        this.key = key;

    }

    public static List<String> decrypt(){
        return null;
    }

    public static List<String> encrypt(List<String> content){
        List<String> encrypted = new ArrayList<>();
        for(String s : content)
            encrypted.add(ECC.main(s));

        return encrypted;
    }

    public static List<String> read(){
        return null;
    }

    public static List<String> write(){
        return null;
    }

    public static void update(){
    }
}
