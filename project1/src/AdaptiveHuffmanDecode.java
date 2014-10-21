import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;


public final class AdaptiveHuffmanDecode {

    private static final int IMAGE_BIT_SIZE = 257; // 8 bit image, value range of 0-255

    public static void main(String[] args) throws IOException {
/*
        if (args.length == 0) {
            System.err.println("Usage: java AdaptiveHuffmanDecompress InputFile OutputFile");
            System.exit(1);
            return;
        }

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
*/

        for (int i = 1; i <= 5; i++) {
            File inputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/compressed/test" + i + "-adaptive-mw.raw");
            File outputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/decompressed/test" + i + "-adaptive-mw.raw");

            BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));
            try {
                decompress(in, out);
            } finally {
                out.close();
                in.close();
            }
        }
    }


    static void decompress(BitInputStream in, OutputStream out) throws IOException {
        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);

        CodeFrequency freqTable = new CodeFrequency(IMAGE_BIT_SIZE);
        HuffmanDecoder dec = new HuffmanDecoder(in);
        dec.codeTree = freqTable.generateCodeTree();
        int count = 0;
        while (true) {
            int symbol = dec.read();
            if (symbol == 256)  // EOF value
                break;
            out.write(symbol);

            freqTable.increment(symbol);
            count++;
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
                dec.codeTree = freqTable.generateCodeTree();
            if (count % 262144 == 0)  // Reset frequency table
                freqTable = new CodeFrequency(IMAGE_BIT_SIZE);
        }
    }


    private static boolean isPowerOf2(int x) {
        return x > 0 && (x & -x) == x;
    }

}
