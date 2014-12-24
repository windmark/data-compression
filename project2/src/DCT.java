import static java.lang.Math.*;



public class DCT {
    private double cosTable[][];
    private double transposedCosTable[][];
    private final int tileSize;
    private final double sqrtTwoDivTileSize;
    private final double oneDivSqrtTwo;


    public DCT(int tileSize) {
        if (!isPowerOfTwo(tileSize)) {
            throw new IllegalArgumentException("Tile size must be a power of 2");
        }

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


    private double[] getRow(double[][] block, int row){
        double[] output = new double[tileSize];

        for(int column = 0; column < tileSize; column++){
            output[column] = block[row][column];
        }
        return output;
    }


    private void insertRow(double[][] block, double[] row, int rowNumber){
        for(int column = 0; column < tileSize; column++){
            block[rowNumber][column] = row[column];
        }
    }


    private double[] getColumn(double[][] block, int column){
        double[] output = new double[tileSize];

        for(int row = 0; row < tileSize; row++){
            output[row] = block[row][column];
        }
        return output;
    }


    private void insertColumn(double[][] block, double[] column, int columnNumber){
        for(int row = 0; row < tileSize; row++){
            block[row][columnNumber] = column[row];
        }
    }


    public double[][] forwardDCT(double[][] inBlock){
        if (inBlock.length != tileSize || inBlock[0].length != tileSize) {
            throw new IllegalArgumentException("Input matrix must be tile sized");
        }

        double[][] outBlock = new double[tileSize][tileSize];

        // Transforms rows
        for (int row = 0; row < tileSize; row++) {
            double[] transformedRow = forwardDCT1D(getRow(inBlock, row));
            insertRow(outBlock, transformedRow, row);
        }

        // then transforms columns
        for (int column = 0; column < tileSize; column++) {
            double[] transformedColumn = forwardDCT1D(getColumn(outBlock, column));
            insertColumn(outBlock, transformedColumn, column);
        }
        return outBlock;
    }


    private double[] forwardDCT1D(double[] valueArray) {
        double[] outRow = new double[tileSize];

        for (int k = 0; k < tileSize; k++) {
            double sum = 0;

            for (int n = 0; n < tileSize; n++) {
                double cosine = cosTable[k][n];
                double product = valueArray[n] * cosine;
                sum += product;
            }

            double alpha;
            if (k == 0) {
                alpha = oneDivSqrtTwo;
            } else {
                alpha = 1;
            }

            outRow[k] = sum * alpha * sqrtTwoDivTileSize;
        }
        return outRow;
    }

















    public double[][] inverseDCT(double[][] inBlock) {
        if (inBlock.length != tileSize || inBlock[0].length != tileSize) {
            throw new IllegalArgumentException("Input matrix must be block sized");
        }

        double[][] outBlock = new double[tileSize][tileSize];

        // Inverse transform rows
        for (int row = 0; row < tileSize; row++) {
            double[] transformedRow = inverseDCT1D(getRow(inBlock, row));
            insertRow(outBlock, transformedRow, row);
        }

        // then columns
        for (int column = 0; column < tileSize; column++) {
            double[] transformedColumn = inverseDCT1D(getColumn(outBlock, column));
            insertColumn(outBlock, transformedColumn, column);
        }
        return outBlock;
    }



    private double[] inverseDCT1D(double[] valueArray) {
        double[] outRow = new double[tileSize];

        for (int n = 0; n < tileSize; n++) {
            double sum = 0;

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


    private boolean isPowerOfTwo(int n) {
        return (n > 0) && ((n & (n - 1)) == 0);
    }
}


