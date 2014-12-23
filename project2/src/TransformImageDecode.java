import java.io.*;
import java.util.ArrayList;

public class TransformImageDecode {
    private static final int IMAGE_WIDTH = 1600;
    private static final int IMAGE_HEIGHT = 1200;
    private static final int TILE_SIZE = 8;
    private static final int QUANTIZE_QUALITY = 25;


    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: java AdaptiveHuffmanDecode input_file_name output_file_name");
            System.exit(1);
            return;
        }

        final long startTime = System.currentTimeMillis();

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        BitInputStream inputStream = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        AdaptiveHuffmanDecode decode = new AdaptiveHuffmanDecode(inputStream, TILE_SIZE);
        ScalarQuantization scalarQuantization = new ScalarQuantization(TILE_SIZE, QUANTIZE_QUALITY);
        DCT dctTransformation = new DCT(TILE_SIZE);

        ArrayList<double[][]> inverseDctTileList = new ArrayList<double[][]>();

        int[] tileValues;
        int i = 0;
        while(true) {
            tileValues = decode.decodeQuantized(inputStream);
            if (tileValues.length == 1) {
                break; // DEFINED AS EOS REACHED
            }

            double[][] a = scalarQuantization.deQuantize(tileValues);
            double[][] b = dctTransformation.inverseDCT(a);
            inverseDctTileList.add(b);
/*
            inverseDctTileList.add(
                    dctTransformation.inverseDCT(
                            scalarQuantization.deQuantize(tileValues)
                    )
            );
*/
            i++;
        }

        double[][] imageMatrix = tilesToMatrix(inverseDctTileList);
        matrixToFile(imageMatrix, outputStream);

        outputStream.close();
        inputStream.close();

        final long endTime = System.currentTimeMillis();
        System.out.println("Decoding execution time: " + (endTime - startTime) + " ms" );
    }


    private static double[][] tilesToMatrix(ArrayList<double[][]> tileList) {
        double[][] matrix = new double[IMAGE_WIDTH][IMAGE_HEIGHT];
        int width = IMAGE_WIDTH;
        int height = IMAGE_HEIGHT;
        int counter = 0;

        for (int i = 0; i < height / TILE_SIZE; i++) {
            for (int j = 0; j < width / TILE_SIZE; j++) {
                int xpos = j * TILE_SIZE;
                int ypos = i * TILE_SIZE;


                for (int a = 0; a < TILE_SIZE; a++) {
                    for (int b = 0; b < TILE_SIZE; b++) {
                        matrix[xpos + a][ypos + b] = tileList.get(counter)[a][b];
                    }
                }
                counter++;
            }
        }
        return matrix;
    }

    private static void matrixToFile(double[][] matrix, OutputStream out) throws IOException {
        for (int x = 0; x < IMAGE_WIDTH; x++) {
            for (int y = 0; y < IMAGE_HEIGHT; y++) {
                int value = (int) Math.round(matrix[x][y]);
                out.write(value);
            }
        }
    }


}