import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class AdaptiveHuffmanDecode {
    private static int IMAGE_BIT_SIZE;
    private static final int BIT_COUNT_LIMIT = 65536; // Chosen appropriately for grayscale images.
                                                      // Encoder and decoder must have the same value

    private CodeFrequency frequencyTable;
    private CodeTree generatedCodeTree;
    private CodeReader codeReader;
    private final int tileSize;
    private int bitCount;


    public AdaptiveHuffmanDecode(BitInputStream in, int tileSize) {
        this.IMAGE_BIT_SIZE = 513; // 8 bit for positive + 8 bit for negative + 1 EOF
        this.frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
        this.generatedCodeTree = frequencyTable.generateCodeTree();
        this.codeReader = new CodeReader(in, generatedCodeTree);
        this.tileSize = tileSize;
        this.bitCount = 0;
    }


    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: java AdaptiveHuffmanDecode input_file_name output_file_name");
            System.exit(1);
            return;
        }

        IMAGE_BIT_SIZE = 257; // 8 bit image + EOF for Decoder

        final long startTime = System.currentTimeMillis();

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        BitInputStream inputStream = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        decode(inputStream, outputStream);

        outputStream.close();
        inputStream.close();

        final long endTime = System.currentTimeMillis();
        System.out.println("Decoding execution time: " + (endTime - startTime) );
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
            if (toLimitFreqTable(bitCount)) {
                frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
            }
        }
    }


    public int[] decodeQuantized(BitInputStream in) throws IOException {
        int i = 0;
        int[] values = new int[tileSize * tileSize];

        while (true) {
            int symbol = codeReader.read();
            if (symbol == -1) {
                return new int[1]; // EOS reached
            }
            if (symbol == 256) {
                break; // EOF
            }

            int value;
            if (symbol > 256) {
                value = (symbol - 256) * (-1);
            } else {
                value = symbol;
            }
            values[i] = value;
            frequencyTable.increment(symbol);
            bitCount++;

            if (isUnbalanced(bitCount)) {
                CodeTree updatedCodeTree = frequencyTable.generateCodeTree();
                codeReader.setCodeTree(updatedCodeTree);
            }
            if (toLimitFreqTable(bitCount)) {
                frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
            }
            i++;
        }
        return values;
    }


    private static boolean isUnbalanced(int count) {
        boolean isUnbalanced = false;
        if (count < BIT_COUNT_LIMIT && isPowerOfTwo(count) || count % BIT_COUNT_LIMIT == 0) {
            isUnbalanced = true;
        }
        return isUnbalanced;
    }


    private static boolean toLimitFreqTable(int bitCount) {
        boolean toLimit = false;
        if (bitCount % BIT_COUNT_LIMIT == 0) {
            toLimit = true;
        }
        return toLimit;
    }


    private static boolean isPowerOfTwo(int n) {
        return (n > 0) && ((n & (n - 1)) == 0);
    }
}
