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
            int[] frequencies = new int[256];

            while ((data = fileInputStream.read()) != -1) {
                data = fileInputStream.read();
                frequencies[data]++;

                //System.out.print(" " + data);
            }

            HuffmanTree tree = huffmanCode.buildTree(frequencies);
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