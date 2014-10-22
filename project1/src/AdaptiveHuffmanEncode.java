import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class AdaptiveHuffmanEncode {
    private static final int IMAGE_BIT_SIZE = 257; // 8 bit image + EOF for Decoder

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

            encode(inputStream, outputStream);

            outputStream.close();
            inputStream.close();
        }
    }

    private static void encode(InputStream in, BitOutputStream out) throws IOException {
        CodeFrequency frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
        CodeTree generatedCodeTree= frequencyTable.generateCodeTree();
        CodeWriter codeWriter = new CodeWriter(out, generatedCodeTree);

        int count = 0;
        while (true) {
            int bit = in.read();
            if (bit == -1) break; // End of stream

            codeWriter.write(bit);
            frequencyTable.increment(bit);
            count++;

            //////////////////////////////////////////////////////////////////////////
            if (count < 262144 && isPowerOfTwo(count) || count % 262144 == 0) {
                CodeTree updatedCodeTree = frequencyTable.generateCodeTree();
                codeWriter.setCodeTree(updatedCodeTree);
            }

            if (count % 262144 == 0) {
                frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
            }
            //////////////////////////////////////////////////////////////////////////
        }
        codeWriter.write(256); // EOF
    }

    private static boolean isPowerOfTwo(int n) {
        return (n > 0) && ((n & (n - 1)) == 0);
    }
}
