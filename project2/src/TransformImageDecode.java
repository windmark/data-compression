import java.io.*;
import java.util.ArrayList;

public class TransformImageDecode {
    private static final int IMAGE_WIDTH = 1600;
    private static final int IMAGE_HEIGHT = 1200;
    private static final int TILE_SIZE = 8;


    public static void main(String[] args) throws IOException {
        if (!isPowerOfTwo(TILE_SIZE)) {
            throw new IllegalArgumentException("Tile size must be a power of 2");
        }

        if (args.length == 0) {
            System.err.println("Usage: java AdaptiveHuffmanDecode input_file_name output_file_name");
            System.exit(1);
            return;
        }

        File inputFile;// = new File(args[0]);
        File outputFile;// = new File(args[1]);

        for (int i = 1; i <= 5; i++) {
            inputFile = new File("testdata/encoded/test" + i + "_mw.raw"); //args[0]);
            outputFile = new File("testdata/decoded/test" + i + "_mw.raw"); //args[1]);

            BitInputStream inputStream = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            final long startTime = System.currentTimeMillis();

            decode(inputStream, outputStream);

            final long endTime = System.currentTimeMillis();
            System.out.println("File " + i + " decoding execution time: " + (endTime - startTime) + " ms");

            outputStream.close();
            inputStream.close();
        }
    }


    private static void decode(BitInputStream inputStream, BufferedOutputStream outputStream) throws IOException {
        AdaptiveHuffmanDecode decode = new AdaptiveHuffmanDecode(inputStream, TILE_SIZE);
        ScalarQuantization scalarQuantization = new ScalarQuantization(TILE_SIZE);
        DCT dctTransformation = new DCT(TILE_SIZE);
        ArrayList<double[][]> inverseDctTileList = new ArrayList<double[][]>();

        int[] tileValues;
        int i = 0;
        while(true) {
            tileValues = decode.decodeQuantized(inputStream);
            if (tileValues.length == 1) {
                break; // DEFINED AS EOS REACHED
            }

            inverseDctTileList.add(
                    dctTransformation.inverseDCT(
                            scalarQuantization.deQuantize(tileValues)
                    )
            );
            i++;
        }
        double[][] imageMatrix = tilesToMatrix(inverseDctTileList);
        matrixToFile(imageMatrix, outputStream);
    }


    private static double[][] tilesToMatrix(ArrayList<double[][]> tileList) {
        double[][] matrix = new double[IMAGE_WIDTH][IMAGE_HEIGHT];
        int width = IMAGE_WIDTH;
        int height = IMAGE_HEIGHT;
        int counter = 0;

        for (int i = 0; i < height / TILE_SIZE; i++) {
            for (int j = 0; j < width / TILE_SIZE; j++) {
                int x = j * TILE_SIZE;
                int y = i * TILE_SIZE;

                for (int a = 0; a < TILE_SIZE; a++) {
                    for (int b = 0; b < TILE_SIZE; b++) {
                        matrix[x + a][y + b] = tileList.get(counter)[a][b];
                    }
                }
                counter++;
            }
        }
        return matrix;
    }


    private static void matrixToFile(double[][] matrix, BufferedOutputStream out) throws IOException {
        for (int x = 0; x < IMAGE_WIDTH; x++) {
            for (int y = 0; y < IMAGE_HEIGHT; y++) {
                int value = (int) Math.round(matrix[x][y]);
                out.write(value);
            }
        }
    }

    private static boolean isPowerOfTwo(int n) {
        return (n > 0) && ((n & (n - 1)) == 0);
    }
}