package com.windmark.marcus;

import java.util.List;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HuffmanEncode {
    public static void main(String args[]) throws IOException {
        final int bitsInImage = 8;
        final int size = (int) Math.pow(2, bitsInImage);

/*
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);
*/

        File inputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/test5.raw");
        File outputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/compressed/test5_mw.raw");

        FrequencyTable frequencies = new FrequencyTable(size, inputFile);
        HuffmanTree huffmanTree = new HuffmanTree(frequencies);

        huffmanTree.toString(new StringBuffer());

        CodeTree code = new CodeTree(huffmanTree.getTree(), size);
        compress(code, inputFile, outputFile);
    }


    private static void compress(CodeTree code, File inputFile, File outputFile) throws IOException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
        BitOutputStream outputStream = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));

        try {
            while (true) {
                int b = inputStream.read();
                if (b == -1)
                    break;
                List<Integer> bits = code.getValueCode(b);

                for (int bit : bits) {
                    outputStream.write(bit);
                }
            }
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }
}