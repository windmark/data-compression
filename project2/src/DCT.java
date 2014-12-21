import static java.lang.Math.*;



public class DCT {
    private double cosTable[][];
    private double transposedCosTable[][];
    private final int blockSize;


    public DCT(int blockSize) {
        if (!isPowerOfTwo(blockSize)) {
            throw new IllegalArgumentException("Block size must be a power of 2");
        }

        this.blockSize = blockSize;
        cosTable = new double[blockSize][blockSize];
        transposedCosTable = new double[blockSize][blockSize];

        initCosTables();
    }


    // According to the DCT algorithm at
    // http://www.cmlab.csie.ntu.edu.tw/cml/dsp/training/coding/transform/dct.html
    private void initCosTables() {
        for(int k = 0; k < blockSize; k++){
            for(int n = 0; n < blockSize; n++){
                double arg = PI * k * (2.0 * n + 1) / (2 * blockSize);
                cosTable[k][n] = cos(arg);
                transposedCosTable[n][k] = cosTable[k][n];
            }
        }
    }


    private double[] getRow(double[][] block, int row){
        double[] output = new double[blockSize];

        for(int column = 0; column < blockSize; column++){
            output[column] = block[row][column];
        }
        return output;
    }


    private void insertRow(double[][] block, double[] row, int rowNumber){
        for(int column = 0; column < blockSize; column++){
            block[rowNumber][column] = row[column];
        }
    }


    private double[] getColumn(double[][] block, int column){
        double[] output = new double[blockSize];

        for(int row = 0; row < blockSize; row++){
            output[row] = block[row][column];
        }
        return output;
    }


    private void insertColumn(double[][] block, double[] column, int columnNumber){
        for(int row = 0; row < blockSize; row++){
            block[row][columnNumber] = column[row];
        }
    }


    public double[][] forwardDCT(double[][] inBlock){
        if (inBlock.length != blockSize || inBlock[0].length != blockSize) {
            throw new IllegalArgumentException("Input matrix must be block sized");
        }

        double[][] outBlock = new double[blockSize][blockSize];

        // Transforms rows
        for (int row = 0; row < blockSize; row++) {
            double[] transformedRow = forwardDCT1D(getRow(inBlock, row));
            insertRow(outBlock, transformedRow, row);
        }

        // then transforms columns
        for (int column = 0; column < blockSize; column++) {
            double[] transformedColumn = forwardDCT1D(getColumn(outBlock, column));
            insertColumn(outBlock, transformedColumn, column);
        }
        return outBlock;
    }


    private double[] forwardDCT1D(double[] valueArray) {
        double[] outRow = new double[blockSize];

        for (int k = 0; k < blockSize; k++) {
            double sum = 0;

            for (int n = 0; n < blockSize; n++) {
                double cosine = cosTable[k][n];
                double product = valueArray[n] * cosine;
                sum += product;
            }

            double alpha;
            if (k == 0) {
                alpha = 1.0 / sqrt(2);
            } else {
                alpha = 1;
            }
            outRow[k] = sum * alpha * sqrt(2.0 / blockSize);
        }
        return outRow;
    }


    public double[][] inverseDCT(double[][] inBlock) {
        if (inBlock.length != blockSize || inBlock[0].length != blockSize) {
            throw new IllegalArgumentException("Input matrix must be block sized");
        }

        double[][] outBlock = new double[blockSize][blockSize];

        // Inverse transform rows
        for (int row = 0; row < blockSize; row++) {
            double[] transformedRow = inverseDCT1D(getRow(inBlock, row));
            insertRow(outBlock, transformedRow, row);
        }

        // then columns
        for (int column = 0; column < blockSize; column++) {
            double[] transformedColumn = inverseDCT1D(getColumn(outBlock, column));
            insertColumn(outBlock, transformedColumn, column);
        }
        return outBlock;
    }


    private double[] inverseDCT1D(double[] valueArray) {
        double[] outRow = new double[blockSize];

        for (int n = 0; n < blockSize; n++) {
            double sum = 0.0;

            for (int k = 0; k < blockSize; k++) {
                double cosine = transposedCosTable[n][k];
                double product = valueArray[k] * cosine;
                double alpha;

                if (k == 0) {
                    alpha = 1.0 / sqrt(2);
                } else {
                    alpha = 1;
                }
                sum += alpha * product;
            }
            outRow[n] = sum * sqrt(2.0 / blockSize);
        }
        return outRow;
    }


    private boolean isPowerOfTwo(int n) {
        return (n > 0) && ((n & (n - 1)) == 0);
    }
}


