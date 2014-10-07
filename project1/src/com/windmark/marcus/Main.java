package com.windmark.marcus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {

        File file = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/test1.raw");
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

            System.out.println("Total file size to read (in bytes) : "
            + fis.available());

            int content;

            while ((content = fis.read()) != -1) {
                // convert to char and display it
                System.out.print((char) content);
            }

        } catch (IOException e) {
                e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}