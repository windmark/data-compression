import static java.lang.Math.*;



public class DCT {
    private double cosTable[][];
    private double transposedCosTable[][];
    private final int tileSize;
    private final double sqrtTwoDivTileSize;
    private final double oneDivSqrtTwo;


    public DCT(int tileSize) {
        this.tileSize = tileSize;
        cosTable = new double[tileSize][tileSize];
        transposedCosTable = new double[tileSize][tileSize];
        sqrtTwoDivTileSize = sqrt(2.0 / tileSize);
        oneDivSqrtTwo = 1.0 / sqrt(2);

        initCosTables();
    }


    // According to the DCT algorithm at
    // http://www.cmlab.csie.ntu.edu.tw/cml/dsp/training/coding/transform/dct.html
    private void initCosTables() {
        for(int k = 0; k < tileSize; k++){
            for(int n = 0; n < tileSize; n++){
                double arg = PI * k * (2.0 * n + 1) / (2 * tileSize);
                cosTable[k][n] = cos(arg);
                transposedCosTable[n][k] = cosTable[k][n];
            }
        }
    }


    private double[] getRow(double[][] tile, int row){
        double[] output = new double[tileSize];

        for(int column = 0; column < tileSize; column++){
            output[column] = tile[row][column];
        }
        return output;
    }


    private void insertRow(double[][] tile, double[] row, int rowNumber){
        for(int column = 0; column < tileSize; column++){
            tile[rowNumber][column] = row[column];
        }
    }


    private double[] getColumn(double[][] tile, int column){
        double[] output = new double[tileSize];

        for(int row = 0; row < tileSize; row++){
            output[row] = tile[row][column];
        }
        return output;
    }


    private void insertColumn(double[][] tile, double[] column, int columnNumber){
        for(int row = 0; row < tileSize; row++){
            tile[row][columnNumber] = column[row];
        }
    }


    public double[][] forwardDCT(double[][] inTile){
        if (inTile.length != tileSize || inTile[0].length != tileSize) {
            throw new IllegalArgumentException("Input matrix must be tile sized");
        }

        // Transforms rows
        for (int row = 0; row < tileSize; row++) {
            double[] transformedRow = forwardDCT1D(getRow(inTile, row));
            insertRow(inTile, transformedRow, row);
        }

        // then transforms columns
        for (int column = 0; column < tileSize; column++) {
            double[] transformedColumn = forwardDCT1D(getColumn(inTile, column));
            insertColumn(inTile, transformedColumn, column);
        }
        return inTile;
    }


    private double[] forwardDCT1D(double[] valueArray) {
        double[] outArray = new double[tileSize];

        for (int k = 0; k < tileSize; k++) {
            double sum = 0.0;

            for (int n = 0; n < tileSize; n++) {
                double cos = cosTable[k][n];
                double product = valueArray[n] * cos;
                sum += product;
            }

            double alpha;
            if (k == 0) {
                alpha = oneDivSqrtTwo;
            } else {
                alpha = 1;
            }

            outArray[k] = sum * alpha * sqrtTwoDivTileSize;
        }
        return outArray;
    }


    public double[][] inverseDCT(double[][] inTile) {
        if (inTile.length != tileSize || inTile[0].length != tileSize) {
            throw new IllegalArgumentException("Input matrix must be block sized");
        }

        // Inverse transform rows
        for (int row = 0; row < tileSize; row++) {
            double[] transformedRow = inverseDCT1D(getRow(inTile, row));
            insertRow(inTile, transformedRow, row);
        }

        // then columns
        for (int column = 0; column < tileSize; column++) {
            double[] transformedColumn = inverseDCT1D(getColumn(inTile, column));
            insertColumn(inTile, transformedColumn, column);
        }

        normalize(inTile);
        return inTile;
    }


    private double[] inverseDCT1D(double[] valueArray) {
        double[] outRow = new double[tileSize];

        for (int n = 0; n < tileSize; n++) {
            double sum = 0.0;

            for (int k = 0; k < tileSize; k++) {
                double cosine = transposedCosTable[n][k];
                double product = valueArray[k] * cosine;

                double alpha;
                if (k == 0) {
                    alpha = oneDivSqrtTwo;
                } else {
                    alpha = 1;
                }
                sum += alpha * product;
            }
            outRow[n] = sum * sqrtTwoDivTileSize;
        }
        return outRow;
    }

    // Converting to 8-bit unsigned
    private void normalize(double[][] inTile) {
        for(int row = 0;row < tileSize;row++){
            for(int col = 0;col < tileSize;col++){
                if(inTile[row][col] < 0){
                    inTile[row][col] = 0;
                }

                if(inTile[row][col] > 255){
                    inTile[row][col] = 255;
                }
            }
        }
    }
}


