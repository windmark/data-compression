import java.io.*;
import java.util.ArrayList;

public class TransformImageEncode {
    private static final int IMAGE_WIDTH = 1600;
    private static final int IMAGE_HEIGHT = 1200;
    private static final int TILE_SIZE = 8;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: java TransformImageEncode input_file_name output_file_name huffman_table_output_file_name");
            System.exit(1);
            return;
        }

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        File HTOutputFile = new File(args[2]);

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
        BitOutputStream outputStream = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
        PrintWriter HTOutputStream = new PrintWriter(HTOutputFile);

        final long startTime = System.currentTimeMillis();

        encode(inputStream, outputStream, HTOutputStream);

        final long endTime = System.currentTimeMillis();
        System.out.println("Encoding execution time: " + (endTime - startTime) + " ms" );

        outputStream.close();
        inputStream.close();
        HTOutputStream.close();
    }


    private static void encode(BufferedInputStream inputStream, BitOutputStream outputStream, PrintWriter HTOutputStream)  throws IOException  {
        int[][] imageMatrix = imageToMatrix(inputStream);

        ArrayList<Tile> tileList = initTiles(imageMatrix);
        DCT dctTransformation = new DCT(TILE_SIZE);
        ScalarQuantization scalarQuantization = new ScalarQuantization(TILE_SIZE);

        ArrayList<double[][]> dctTileList = new ArrayList<double[][]>(tileList.size());
        ArrayList<int[]> quantizeList = new ArrayList<int[]>(TILE_SIZE * TILE_SIZE);

        AdaptiveHuffmanEncode encode = new AdaptiveHuffmanEncode(outputStream);

        ///////////////////////TESTING
/*
        double[][] testMatrix = {
                {52,55,61,66,70,61,64,73},
                {63,59,55,90,109,85,69,72},
                {62,59,68,113,144,104,66,73},
                {63,58,71,122,154,106,70,69},
                {67,61,68,104,126,88,68,70},
                {79,65,60,70,77,68,58,73},
                {85,71,64,59,55,61,65,83},
                {87,79,69,68,65,76,78,94}
        };

        double meanValue = 0;
        for (int o = 0; o < 8; o++) {
            for (int p = 0; p < 8; p++) {
                meanValue += testMatrix[o][p];
            }
        }
        meanValue = meanValue / 64;


        double[][] x = dctTransformation.forwardDCT(testMatrix);
        int[] y = scalarQuantization.quantize(x);
        encode.encodeQuantized(y, outputStream, HTOutputStream);
*/
        //////////////////////////

        double[][] tile;
        for (int i = 0; i < tileList.size(); i++) {
            tile = tileList.get(i).getTile();
            dctTileList.add(dctTransformation.forwardDCT(tile));
            quantizeList.add(scalarQuantization.quantize(dctTileList.get(i)));
            encode.encodeQuantized(quantizeList.get(i), outputStream, HTOutputStream);
        }
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
