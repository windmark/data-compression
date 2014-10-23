import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class AdaptiveHuffmanDecode {
    private static final int IMAGE_BIT_SIZE = 257; // 8 bit image + EOF for Decoder
    private static final int BIT_COUNT_LIMIT = 65536; // Chosen appropriately for grayscale images

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: java AdaptiveHuffmanDecode input_file_name output_file_name");
            System.exit(1);
            return;
        }

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        BitInputStream inputStream = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        decode(inputStream, outputStream);

        outputStream.close();
        inputStream.close();
    }


    private static void decode(BitInputStream in, OutputStream out) throws IOException {
        CodeFrequency frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
        CodeTree generatedCodeTree = frequencyTable.generateCodeTree();
        CodeReader codeReader = new CodeReader(in, generatedCodeTree);

        int bitCount = 0;
        while (true) {
            int symbol = codeReader.read();
            if (symbol == 256) break; // EOF

            out.write(symbol);
            frequencyTable.increment(symbol);
            bitCount++;

            if (isUnbalanced(bitCount)) {
                CodeTree updatedCodeTree = frequencyTable.generateCodeTree();
                codeReader.setCodeTree(updatedCodeTree);
            }
            if (bitCount % BIT_COUNT_LIMIT == 0)
                frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
        }
    }

    private static boolean isUnbalanced(int count) {
        boolean isUnbalanced = false;
        if (count < BIT_COUNT_LIMIT && isPowerOfTwo(count) || count % BIT_COUNT_LIMIT == 0) {
            isUnbalanced = true;
        }
        return isUnbalanced;
    }


    private static boolean isPowerOfTwo(int n) {
        return (n > 0) && ((n & (n - 1)) == 0);
    }
}
