import java.util.ArrayList;
import java.util.List;

public class ScalarQuantization {
    private final int blockSize;
    private final int blockValueCount;
    private int[][] zigZagTable;
    private int[][] quantizeTable;



    public ScalarQuantization(int blockSize, int quality) {
        this.blockSize = blockSize;
        this.blockValueCount = blockSize * blockSize;
        zigZagTable = new int[blockValueCount][2];
        initZigZagTable(zigZagTable);


        quantizeTable = initQuantizeTable(quality);
    }







    // Sample jpeg quantization table as seen in slides
    private int[][] initQuantizeTable(int quality) {
        int[][] quantizeTable = {
                {16, 11, 10, 16, 24, 40, 51, 61},
                {12, 12, 14, 19, 26, 58, 60, 55},
                {14, 13, 16, 24, 40, 57, 69, 56},
                {14, 17, 22, 29, 51, 87, 80, 62},
                {18, 22, 37, 56, 68, 109, 103, 77},
                {24, 35, 55, 64, 81, 104, 113, 92},
                {49, 64, 78, 87, 103, 121, 120, 101},
                {72, 92, 95, 98, 112, 100, 103, 99}
        };

        return quantizeTable;

/*
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                quantizeTable[i][j] = (1 + ((1 + i + j) * quality));
            }
        }
*/

    }


    // Based on the Zig Zag algorithm found here
    // http://rosettacode.org/wiki/Zig_Zag
    private void initZigZagTable(int[][] zigZag) {
        int a = 1, b = 1;

        for (int i = 0; i < blockSize * blockSize; i++) {
            zigZag[i][0] = a - 1;
            zigZag[i][1] = b - 1;

            if ((a + b) % 2 == 0) {
                if (b < blockSize) {
                    b++;
                } else {
                    a += 2;
                }

                if (a > 1) {
                    a--;
                }
            } else {
                if (a < blockSize) {
                    a++;
                } else {
                    b += 2;
                }

                if (b > 1) {
                    b--;
                }
            }
        }
    }


    public int[] quantize(double[][] block) {
        int[] quantized = new int[blockValueCount];
        int row, col, result;


        for (int i = 0; i < blockValueCount; i++) {
            row = zigZagTable[i][0];
            col = zigZagTable[i][1];
            result = (int) Math.round(block[row][col] / quantizeTable[row][col]);

            quantized[i] = result;
        }

        int[] minimized = removeTrailingZeroes(quantized);
        return minimized;
    }


    private int[] removeTrailingZeroes(int[] array) {
        int i = array.length;

        // Backward traverses until non-zero
        while (i-- > 0 && array[i] == 0) {}
        int[] output = new int[i+1];
        System.arraycopy(array, 0, output, 0, i+1);

        return output;
    }


    public double[][] deQuantize(int[] quantized) {
        double[][] deQuantized = new double[blockSize][blockSize];
        int row, col, deQuantizedValue;

        for (int i = 0; i < blockValueCount; i++) {
            row = zigZagTable[i][0];
            col = zigZagTable[i][1];

            deQuantizedValue = quantized[i] * quantizeTable[row][col];
            deQuantized[row][col] = (Math.round(deQuantizedValue));
        }
        return deQuantized;
    }

}
