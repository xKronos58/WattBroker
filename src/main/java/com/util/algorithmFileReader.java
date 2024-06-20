package com.util;

import ECC.ECC;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class algorithmFileReader {

    String file;
    String key;
    List<String> EncryptedContent;
    List<String> Content;

    public algorithmFileReader(String file, String key) {
        this.file = file;
        this.key = key;
        Content = read(file);
        EncryptedContent = encrypt(Content);

        for(String s : Content)
            System.out.println(s);
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

    public static List<String> read(String path){
        List<String> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine())
                records.add(scanner.nextLine());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return records;
    }

    public static List<String> write(){
        return null;
    }

    public static void update(){
    }
}
