import java.io.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.*;

public class TransformImageEncode {
    private static final int IMAGE_WIDTH = 1600;
    private static final int IMAGE_HEIGHT = 1200;
    private static final int TILE_SIZE = 8;
    private static final int QUANTIZE_QUALITY = 25;

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
        // ENCODE
        int[][] imageMatrix = imageToMatrix(inputStream);

        ArrayList<Tile> tileList = initTiles(imageMatrix);

        DCT dctTransformation = new DCT(TILE_SIZE);
        ScalarQuantization scalarQuantization = new ScalarQuantization(TILE_SIZE, QUANTIZE_QUALITY);



        ArrayList<double[][]> dctTileList = new ArrayList<double[][]>(tileList.size());
        ArrayList<int[]> quantizeList = new ArrayList<int[]>(TILE_SIZE * TILE_SIZE);

        AdaptiveHuffmanEncode encode = new AdaptiveHuffmanEncode(outputStream);

        double[][] tile;

        for (int i = 0; i < tileList.size(); i++) {
            tile = tileList.get(i).getTile();
            dctTileList.add(dctTransformation.forwardDCT(tile));
            quantizeList.add(scalarQuantization.quantize(dctTileList.get(i)));
            encode.encodeQuantized(quantizeList.get(i), outputStream, HTOutputStream);
        }



        outputStream.close();
        inputStream.close();
        HTOutputStream.close();

        final long endTime = System.currentTimeMillis();
        System.out.println("Encoding execution time: " + (endTime - startTime) + " ms" );
    }



    private static ArrayList<Tile> initTiles(int[][] imageMatrix) {
        int width = imageMatrix.length;
        int height = imageMatrix[0].length;

        ArrayList<Tile> tileList = new ArrayList<Tile>();

        for (int i = 0; i < height / TILE_SIZE; i++) {
            for (int j = 0; j < width / TILE_SIZE; j++) {
                int xpos = j * TILE_SIZE;
                int ypos = i * TILE_SIZE;

                // Doing this for structural purposes. ArrayLists and such. Despite using more memory and accesses.
                double[][] tile = new double[TILE_SIZE][TILE_SIZE];
                double totalValue = 0;

                for (int a = 0; a < TILE_SIZE; a++) {
                    for (int b = 0; b < TILE_SIZE; b++) {
                        tile[a][b] = imageMatrix[xpos + a][ypos + b];
                        totalValue += imageMatrix[xpos + a][ypos + b];
                    }
                }
                double meanValue = totalValue / (TILE_SIZE * TILE_SIZE);
                Tile tileObject = new Tile(tile, meanValue);
                tileList.add(tileObject);
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
