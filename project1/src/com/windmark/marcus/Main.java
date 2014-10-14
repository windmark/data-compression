package com.windmark.marcus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;




public class Main {

    public static void main(String args[]) throws IOException {
        FileInputStream fileInputStream = null;

        try {
            int data;
            fileInputStream = new FileInputStream("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/test1.raw");
            for (int i = 0; i < 5; i++) {
                data = fileInputStream.read();
                System.out.print(" " + data);
            }
            /*
            while ((data = fileInputStream.read()) != -1) {
                System.out.print(" " + data);
            }
            */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null)
                fileInputStream.close();
        }
    }
}