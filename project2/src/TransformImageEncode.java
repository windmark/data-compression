import java.io.*;
import java.util.ArrayList;

public class TransformImageEncode {
    private static final int IMAGE_WIDTH = 512;
    private static final int IMAGE_HEIGHT = 512;
    private static final int BLOCK_SIZE = 8;



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

//////////////////
        double[][] m = {
            {
                162, 162, 162, 161, 160, 160, 161, 162
            },
            {
                161, 162, 162, 162, 160, 160, 161, 162
            },
            {
                162, 162, 162, 162, 161, 160, 161, 161
            },
            {
                163, 163, 162, 161, 161, 161, 161, 162
            },
            {
                165, 163, 161, 160, 160, 160, 161, 162
            },
            {
                164, 163, 160, 159, 159, 160, 161, 161
            },
            {
                162, 160, 159, 158, 159, 159, 158, 158
            },
            {
                159, 159, 158, 158, 159, 158, 156, 155
            }
        };



        //////// DO STUFF
        DCT dctTransformation = new DCT(BLOCK_SIZE);

        int[][] imageMatrix = imageToMatrix(inputStream);
        ArrayList<double[][]> tileList = imageToMatrix(imageMatrix);
        ArrayList<double[][]> dctTileList = new ArrayList<double[][]>(tileList.size());

        ////
        tileList.add(0, m);

        ////

        for (double[][] tile : tileList) {
            dctTileList.add(dctTransformation.forwardDCT(tile));
        }


        ArrayList<double[][]> inverseDctTileList = new ArrayList<double[][]>(dctTileList.size());
        for (double[][] tile : dctTileList) {
            inverseDctTileList.add(dctTransformation.inverseDCT(tile));
        }
        inverseDctTileList.get(0);


        //////////A TEMPORARY CHECK TO SEE THE DIFFERENCE BETWEEN BEFORE AND AFTER DCT AND INVERSE DCT
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
            System.err.println("INVERSE DCT ERROR LARGER THAN 1");
        }
        System.out.println("Inverse DCT error: " + sum);
        ///////////////////////////////


        outputStream.close();
        inputStream.close();
        HTOutputStream.close();

        final long endTime = System.currentTimeMillis();
        System.out.println("Encoding execution time: " + (endTime - startTime) + " ms" );
    }




    private static ArrayList<double[][]> imageToMatrix(int[][] imageMatrix) {
        int width = imageMatrix.length;
        int height = imageMatrix[0].length;

        ArrayList<double[][]> tileList = new ArrayList<double[][]>();


        for (int i = 0; i < height / BLOCK_SIZE; i++) {
            for (int j = 0; j < width / BLOCK_SIZE; j++) {
                int xpos = j * BLOCK_SIZE;
                int ypos = i * BLOCK_SIZE;

                // Doing this for structural purposes
                double[][] tile = new double[BLOCK_SIZE][BLOCK_SIZE];

                for (int a = 0; a < BLOCK_SIZE; a++) {
                    for (int b = 0; b < BLOCK_SIZE; b++) {
                        tile[a][b] = imageMatrix[xpos + a][ypos + b];
                    }
                }
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
