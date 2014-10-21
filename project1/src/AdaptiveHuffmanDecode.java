import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class AdaptiveHuffmanDecode {
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

        File inputFile, outputFile;
        BitInputStream inputStream;
        BufferedOutputStream outputStream;

        for (int i = 1; i <= 5; i++) {
            inputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/compressed/test" + i + "-adaptive-mw.raw");
            outputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/decompressed/test" + i + "-adaptive-mw.raw");
            inputStream = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
            outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            decode(inputStream, outputStream);

            outputStream.close();
            inputStream.close();
        }
    }


    static void decode(BitInputStream in, OutputStream out) throws IOException {
        CodeFrequency freqTable = new CodeFrequency(IMAGE_BIT_SIZE);
        CodeTree generatedCodeTree = freqTable.generateCodeTree();
        CodeReader codeReader = new CodeReader(in, generatedCodeTree);

        int count = 0;
        while (true) {
            int symbol = codeReader.read();
            if (symbol == 256) break; // EOF

            out.write(symbol);

            freqTable.increment(symbol);
            count++;

            /////////////////////////////////////////////////////
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
                codeReader.setCodeTree(freqTable.generateCodeTree());
            if (count % 262144 == 0)  // Reset frequency table
                freqTable = new CodeFrequency(IMAGE_BIT_SIZE);
            /////////////////////////////////////////////////////
        }
    }


    private static boolean isPowerOf2(int x) {
        return x > 0 && (x & -x) == x;
    }

}
