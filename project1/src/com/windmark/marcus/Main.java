package com.windmark.marcus;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;


public class Main {
    public static void main(String args[]) throws IOException {
        FileInputStream fileInputStream = null;
        HuffmanCode huffmanCode = new HuffmanCode();
        final int bitsInImage = 8;

        try {
            fileInputStream = new FileInputStream("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/test1.raw");

            FrequencyTable frequencies = new FrequencyTable((int) Math.pow(2, bitsInImage));
            frequencies.generate(fileInputStream);

            BinaryTree tree = huffmanCode.buildTree(frequencies);
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