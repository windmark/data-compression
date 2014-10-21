package com.windmark.marcus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public final class AdaptiveHuffmanEncode {

    public static void main(String[] args) throws IOException {
		/*
		if (args.length == 0) {
			System.err.println("Command: java AdaptiveHuffmanCompress InputFile OutputFile");
			System.exit(1);
			return;
		}

		File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);
		*/

        File inputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/test1.raw");
        File outputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/compressed/test1-adaptive-mw.raw");

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(inputFile));
        BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
        try {
            compress(in, out);
        } finally {
            out.close();
            in.close();
        }
    }


    private static void compress(BufferedInputStream in, BitOutputStream out) throws IOException {
        int[] initialFrequencies = new int[257];
        for (int i : initialFrequencies) {
            initialFrequencies[i] = 1;
        }


        FrequencyTable frequencyTable = new FrequencyTable(initialFrequencies);
        HuffmanEncoder enc = new HuffmanEncoder(out);
        enc.codeTree = freqTable.buildCodeTree();
        int count = 0;
        while (true) {
            int b = in.read();
            if (b == -1)
                break;
            enc.write(b);

            freqTable.increment(b);
            count++;
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
                enc.codeTree = freqTable.buildCodeTree();
            if (count % 262144 == 0)  // Reset frequency table
                freqTable = new FrequencyTable(initFreqs);
        }
        enc.write(256);  // EOF
    }


    private static boolean isPowerOf2(int x) {
        return x > 0 && (x & -x) == x;
    }

}