package com.windmark.marcus;

import java.io.*;
import java.util.List;


public class Main {
    public static void main(String args[]) throws IOException {
        final int bitsInImage = 8;
        final int size = (int) Math.pow(2, bitsInImage);

/*
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);
*/

            File inputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/test1.raw");
            File outputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/compressed/test1_compressed.raw");


            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));


            FrequencyTable frequencies = new FrequencyTable(size);
            frequencies.generate(inputFile);

            HuffmanTree huffmanTree = new HuffmanTree(frequencies);

            System.out.println("Value\t\tHuffman Code\t\tFrequency");
            huffmanTree.print(huffmanTree.getTree(), new StringBuffer());

            HuffmanCode huffmanCode = new HuffmanCode(huffmanTree.getTree(), size);

            encode(huffmanCode, inputFile, outputFile);

    }

    private static void encode(HuffmanCode code, File inputFile, File outputFile) throws IOException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {
            while (true) {
                int value = inputStream.read();
                if (value == -1)
                    break;
                List<Integer> bits = code.getValueCode(value);
                write(bits, outputStream);
            }
        } finally {
            inputStream.close();
            outputStream.close();
        }

/*

        do {
            value = in.read();
            List<Integer> bits = code.getValueCode(value);
            write(bits, out);

        } while ((value = in.read()) != -1);
*/
    }

    private static void write(List<Integer> code, OutputStream out) throws IOException {
        for (int bit : code)
                out.write(bit);
        }
    }


