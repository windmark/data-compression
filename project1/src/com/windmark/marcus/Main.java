package com.windmark.marcus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;


public class Main {
    public static void main(String args[]) throws IOException {
        FileInputStream fileInputStream = null;
        HuffmanCode huffmanCode = new HuffmanCode();

        try {
            fileInputStream = new FileInputStream("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/test1.raw");
            int data;
            int[] pixelFreq = new int[256];

            /*
            for (int i = 0; i < 20; i++) {
                data = fileInputStream.read();
                pixelFreq[data]++;

                System.out.print(" " + data);
            }
            */

            while ((data = fileInputStream.read()) != -1) {
                data = fileInputStream.read();
                pixelFreq[data]++;

                //System.out.print(" " + data);
            }

            HuffmanTree tree = huffmanCode.buildTree(pixelFreq);
            System.out.println("Number\tFrequency\tHuffman Code");
            huffmanCode.printCodes(tree, new StringBuffer());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null)
                fileInputStream.close();
        }
    }
}