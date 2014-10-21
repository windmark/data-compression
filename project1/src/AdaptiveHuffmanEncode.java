import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public class AdaptiveHuffmanEncode {
    private static final int IMAGE_BIT_SIZE = 257; // 8 bit image + EOF


    public static void main(String[] args) throws IOException {
/*
		if (args.length == 0) {
			System.err.println("Usage: java AdaptiveHuffmanCompress InputFile OutputFile");
			System.exit(1);
			return;
		}
		
		File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);
*/

        File inputFile, outputFile;
        BufferedInputStream inputStream;
        BitOutputStream outputStream;

        for (int i = 1; i <= 1; i++) {
            inputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/test" + i + ".raw");
            outputFile = new File("/home/marcus/Skola/ntnu/dataCompression/project1/testdata/compressed/test" + i + "-adaptive-mw.raw");
            inputStream = new BufferedInputStream(new FileInputStream(inputFile));
            outputStream = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));

            compress(inputStream, outputStream);

            outputStream.close();
            inputStream.close();
        }
    }


    private static void compress(InputStream in, BitOutputStream out) throws IOException {
        CodeFrequency frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
        CodeTree generatedCodeTree= frequencyTable.buildCodeTree();
        CodeWriter codeWriter = new CodeWriter(out, generatedCodeTree);

        int count = 0;
        while (true) {
            int b = in.read();
            if (b == -1)
                break;
            codeWriter.write(b);

            frequencyTable.increment(b);
            count++;
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
                codeWriter.setCodeTree((frequencyTable.buildCodeTree()));
            if (count % 262144 == 0)  // Reset frequency table
                frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
        }
        codeWriter.write(256);  // EOF
    }


    private static boolean isPowerOf2(int x) {
        return x > 0 && (x & -x) == x;
    }

}
