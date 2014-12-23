import java.io.*;

public class AdaptiveHuffmanEncode {
    private static int IMAGE_BIT_SIZE;
    private static int BIT_COUNT_LIMIT = 65536; // Chosen appropriately for grayscale images.
                                                      // Encoder and decoder must have the same value
    private CodeFrequency frequencyTable;
    private CodeTree codeTree;
    private CodeWriter codeWriter;


    public AdaptiveHuffmanEncode(BitOutputStream out) {
        this.IMAGE_BIT_SIZE = 513; // 8 bit for positive + 8 bit for negative + 1 EOF
        this.frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
        this.codeTree = frequencyTable.generateCodeTree();
        this.codeWriter = new CodeWriter(out, codeTree);


    }


    public static void main(String[] args) throws IOException {
		if (args.length == 0) {
            System.err.println("Usage: java AdaptiveHuffmanEncode input_file_name output_file_name huffman_table_output_file_name");
			System.exit(1);
			return;
		}

        IMAGE_BIT_SIZE = 257; // 8 bit image + EOF for Decoder


        final long startTime = System.currentTimeMillis();

		File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);
        File HTOutputFile = new File(args[2]);

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
        BitOutputStream outputStream = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
        PrintWriter HTOutputStream = new PrintWriter(HTOutputFile);

        encode(inputStream, outputStream, HTOutputStream);

        outputStream.close();
        inputStream.close();
        HTOutputStream.close();

        final long endTime = System.currentTimeMillis();
        System.out.println("Encoding execution time: " + (endTime - startTime) );
    }


    private static void encode(InputStream in, BitOutputStream out, PrintWriter HTOutputStream) throws IOException {
        CodeFrequency frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
        CodeTree codeTree = frequencyTable.generateCodeTree();
        CodeWriter codeWriter = new CodeWriter(out, codeTree);

        int bitCount = 0;
        while (true) {
            int bit = in.read();
            if (bit == -1) break; // EOS

            codeWriter.write(bit);
            frequencyTable.increment(bit);
            bitCount++;

            if (isUnbalanced(bitCount)) {
                CodeTree updatedCodeTree = frequencyTable.generateCodeTree();
                codeWriter.setCodeTree(updatedCodeTree);
            }
            if (toLimitFreqTable(bitCount)) {
                frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
            }
        }
        codeWriter.write(256); // EOF

        String codeString = codeTree.toString(frequencyTable);
        HTOutputStream.print(codeString);
    }

    private int bitCount = 0;

    public void encodeQuantized(int[] values, BitOutputStream out, PrintWriter HTOutputStream) throws IOException {
/*
        this.frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
        this.codeTree = frequencyTable.generateCodeTree();
        this.codeWriter = new CodeWriter(out, codeTree);
*/

        for (int i = 0; i < values.length; i++) {
            int value;
            if (values[i] < 0) {
                value = 256 + Math.abs(values[i]);
            } else {
                value = values[i];
            }

            codeWriter.write(value);
            frequencyTable.increment(value);
            bitCount++;

            if (isUnbalanced(bitCount)) {
                CodeTree updatedCodeTree = frequencyTable.generateCodeTree();
                codeWriter.setCodeTree(updatedCodeTree);
            }
            if (toLimitFreqTable(bitCount)) {
                frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
            }
        }
        codeWriter.write(256); // EOF

        /*
        String codeString = codeTree.toString(frequencyTable);
        HTOutputStream.print(codeString);
        */
    }


    private static boolean isUnbalanced(int bitCount) {
        boolean isUnbalanced = false;
        if (bitCount < BIT_COUNT_LIMIT && isPowerOfTwo(bitCount) || bitCount % BIT_COUNT_LIMIT == 0) {
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
