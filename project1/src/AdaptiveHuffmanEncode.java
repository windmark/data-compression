import java.io.*;


public class AdaptiveHuffmanEncode {
    private static final int IMAGE_BIT_SIZE = 257; // 8 bit image + EOF for Decoder


    public static void main(String[] args) throws IOException {
		if (args.length == 0) {
            System.err.println("Command line: java AdaptiveHuffmanEncode input_file_name output_file_name huffman_table_output_file_name");
			System.exit(1);
			return;
		}
		
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
    }


    private static void encode(InputStream in, BitOutputStream out, PrintWriter HTOutputStream) throws IOException {
        CodeFrequency frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
        CodeTree codeTree = frequencyTable.generateCodeTree();
        CodeWriter codeWriter = new CodeWriter(out, codeTree);

        int count = 0;
        while (true) {
            int bit = in.read();
            if (bit == -1) break; // End of stream

            codeWriter.write(bit);
            frequencyTable.increment(bit);
            count++;

            //////////////////////////////////////////////////////////////////////////
            if (isUnbalanced(count)) {
                CodeTree updatedCodeTree = frequencyTable.generateCodeTree();
                codeWriter.setCodeTree(updatedCodeTree);
            }

            if (count % 262144 == 0) {
                frequencyTable = new CodeFrequency(IMAGE_BIT_SIZE);
            }
            //////////////////////////////////////////////////////////////////////////
        }
        codeWriter.write(256); // EOF

        String codeString = codeTree.toString(frequencyTable);
        HTOutputStream.print(codeString);
    }


    private static boolean isUnbalanced(int count) {
        boolean isUnbalanced = false;
        if (count < 262144 && isPowerOfTwo(count) || count % 262144 == 0) {
            isUnbalanced = true;
        }
            return isUnbalanced;
    }


    private static boolean isPowerOfTwo(int n) {
        return (n > 0) && ((n & (n - 1)) == 0);
    }
}
