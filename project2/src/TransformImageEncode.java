import java.io.*;
import java.util.ArrayList;

public class TransformImageEncode {
    private static final int IMAGE_WIDTH = 512;
    private static final int IMAGE_HEIGHT = 512;
    private static final int BLOCK_SIZE = 8;
    private static final int QUANTIZE_QUALITY = 25; // Test values, higher should be better

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: java TransformImageEncode input_file_name output_file_name huffman_table_output_file_name");
            System.exit(1);
            return;
        }

        final long startTime = System.currentTimeMillis();

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        File HTOutputFile = new File(args[2]);

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
        BitOutputStream outputStream = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
        PrintWriter HTOutputStream = new PrintWriter(HTOutputFile);



        ////////////////////////////////////////
        ////////////////DO STUFF////////////////
        ////////////////////////////////////////
        int[][] imageMatrix = imageToMatrix(inputStream);
        ArrayList<double[][]> tileList = imageToTiles(imageMatrix);

        DCT dctTransformation = new DCT(BLOCK_SIZE);
        ScalarQuantization scalarQuantization = new ScalarQuantization(BLOCK_SIZE, QUANTIZE_QUALITY);



        ArrayList<double[][]> dctTileList = new ArrayList<double[][]>(tileList.size());
        ArrayList<int[]> quantizeList = new ArrayList<int[]>(BLOCK_SIZE * BLOCK_SIZE);
        ArrayList<double[][]> deQuantizeList = new ArrayList<double[][]>(BLOCK_SIZE * BLOCK_SIZE);
        ArrayList<double[][]> inverseDctTileList = new ArrayList<double[][]>(dctTileList.size());

        double[][] x, y;



        for (int i = 0; i < tileList.size(); i++) {
            dctTileList.add(dctTransformation.forwardDCT(tileList.get(i)));
            quantizeList.add(scalarQuantization.quantize(dctTileList.get(i)));

            x = scalarQuantization.deQuantize(quantizeList.get(i));
            y = dctTransformation.inverseDCT(x);
            deQuantizeList.add(y);
        }












        //////////A TEMPORARY CHECK TO SEE THE DIFFERENCE BEFORE AND AFTER DCT AND INVERSE DCT
        double sum = 0;

        for (int h = 0; h < tileList.size(); h++) {
            double[][] a = tileList.get(h);
            double[][] b = inverseDctTileList.get(h);

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    sum += (b[i][j] - a[i][j]);
                }
            }
        }
        if (sum > 1) {
            System.err.println("INVERSE DCT ERROR, IS TOO LARGE: " + sum);
        }
        ///////////////////////////////


        outputStream.close();
        inputStream.close();
        HTOutputStream.close();

        final long endTime = System.currentTimeMillis();
        System.out.println("Encoding execution time: " + (endTime - startTime) + " ms" );
    }




    private static ArrayList<double[][]> imageToTiles(int[][] imageMatrix) {
        int width = imageMatrix.length;
        int height = imageMatrix[0].length;

        ArrayList<double[][]> tileList = new ArrayList<double[][]>();


        for (int i = 0; i < height / BLOCK_SIZE; i++) {
            for (int j = 0; j < width / BLOCK_SIZE; j++) {
                int xpos = j * BLOCK_SIZE;
                int ypos = i * BLOCK_SIZE;

                // Doing this for structural purposes. ArrayLists and such. Despite using more memory and accesses.
                double[][] tile = new double[BLOCK_SIZE][BLOCK_SIZE];
                double meanValue = 0;

                for (int a = 0; a < BLOCK_SIZE; a++) {
                    for (int b = 0; b < BLOCK_SIZE; b++) {
                        tile[a][b] = imageMatrix[xpos + a][ypos + b];
                        meanValue += imageMatrix[xpos + a][ypos + b];
                    }
                }
                /// Possibly remove mean value later. See slides. Not sure how to recreate.
                meanValue = meanValue / (BLOCK_SIZE * BLOCK_SIZE);
                tileList.add(tile);
            }
        }
        return tileList;
    }



    private static int[][] imageToMatrix(BufferedInputStream in) throws IOException {
        int [][] matrix = new int[IMAGE_WIDTH][IMAGE_HEIGHT];

        for (int x = 0; x < IMAGE_WIDTH; x++) {
            for (int y = 0; y < IMAGE_HEIGHT; y++) {
                matrix[x][y] = in.read();
            }
        }
        return matrix;
    }
}
