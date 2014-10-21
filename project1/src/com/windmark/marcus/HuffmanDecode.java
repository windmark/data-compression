package com.windmark.marcus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class HuffmanDecode {
    public static void main(String args[]) throws IOException {
        final int bitsInImage = 8;
        final int size = (int) Math.pow(2, bitsInImage);

/*
        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
*/

        File inputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/compressed/test1_mw.raw");
        File outputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/decompressed/test1_mw.raw");

        BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {
            CanonicalCode canonCode = readCode(in);
            CodeTree code = canonCode.toCodeTree();
            decompress(code, in, out);
        } finally {
            out.close();
            in.close();
        }

    }

    static HuffmanTree readCode(BitInputStream in) throws IOException {
        int[] codeLengths = new int[257];
        for (int i = 0; i < codeLengths.length; i++) {
            // For this file format, we read 8 bits in big endian
            int val = 0;
            for (int j = 0; j < 8; j++)
                val = val << 1 | in.readNoEof();
            codeLengths[i] = val;
        }
        return new HuffmanTree(codeLengths);
    }


    private static void decompress(HuffmanTree code, BitInputStream in, OutputStream out) throws IOException {
        while (true) {
            int bit = read(in, code);
            if (bit == -1)
                break;
            out.write(bit);
        }
    }


    private static int read(BitInputStream inputStream, HuffmanTree huffmanTree) throws IOException {
        Node node = (Node) huffmanTree.getTree();

        if (node == null)
            throw new NullPointerException("Empty binary tree");

        while (true) {
            int bit = inputStream.read();
            BinaryTree nextNode;
            if (bit == 0) {
                nextNode = node.left;
            } else if (bit == 1) {
                nextNode = node.right;
            } else if (bit == -1) {
                return -1;
            } else {
                throw new Error("Input stream contains other bit than 0 or 1.");
            }

            if (nextNode instanceof Leaf) {
                return ((Leaf) nextNode).value;
            } else if (nextNode instanceof Node) {
                node = (Node) nextNode;
            } else {
                throw new Error("Binary tree contains something else than Node and Leaf.");
            }
        }
    }
}
