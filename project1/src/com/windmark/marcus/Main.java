package com.windmark.marcus;

import java.io.*;


public class Main {
    public static void main(String args[]) throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        final int bitsInImage = 8;
        final int size = (int) Math.pow(2, bitsInImage);

        try {
            fileInputStream = new FileInputStream("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/test1.raw");
            fileOutputStream = new FileOutputStream("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/compressed/test1_compressed.raw");

            InputStream in = new BufferedInputStream(fileInputStream);
            OutputStream out = new BufferedOutputStream(fileOutputStream);


            FrequencyTable frequencies = new FrequencyTable(size);
            frequencies.generate(in);

            HuffmanTree huffmanTree = new HuffmanTree(frequencies);

            //System.out.println("Number\tFrequency\tHuffman Code");
            huffmanTree.printCodes(huffmanTree.getTree(), new StringBuffer());

            HuffmanCode huffmanCode = new HuffmanCode(huffmanTree.getTree(), size);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null)
                fileInputStream.close();
        }
    }
}