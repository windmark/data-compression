public class ScalarQuantization {
    private final int tileSize;
    private final int tileValueCount;
    private int[][] zigZagTable;
    private int[][] quantizeTable;


    public ScalarQuantization(int tileSize) {
        this.tileSize = tileSize;
        this.tileValueCount = tileSize * tileSize;

        zigZagTable = initZigZagTable();
        quantizeTable = initQuantizeTable();
    }


    // Sample jpeg quantization table as seen in slides
    private int[][] initQuantizeTable() {
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
    }


    // Based on the Zig Zag algorithm found here
    // http://rosettacode.org/wiki/Zig_Zag
    private int[][] initZigZagTable() {
        int[][] zigZag = new int[tileValueCount][2];
        int a = 1, b = 1;

        for (int i = 0; i < tileSize * tileSize; i++) {
            zigZag[i][0] = a - 1;
            zigZag[i][1] = b - 1;

            if ((a + b) % 2 == 0) {
                if (b < tileSize) {
                    b++;
                } else {
                    a += 2;
                }
                if (a > 1) {
                    a--;
                }
            } else {
                if (a < tileSize) {
                    a++;
                } else {
                    b += 2;
                }
                if (b > 1) {
                    b--;
                }
            }
        }
        return zigZag;
    }


    public int[] quantize(double[][] block) {
        int[] quantized = new int[tileValueCount];
        int row, col, result;

        for (int i = 0; i < tileValueCount; i++) {
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

        while (i-- > 0 && array[i] == 0) {
            // Backward traverses until non-zero
        }
        int[] output = new int[i+1];
        System.arraycopy(array, 0, output, 0, i+1);

        return output;
    }


    public double[][] deQuantize(int[] quantized) {
        double[][] deQuantized = new double[tileSize][tileSize];
        int row, col, deQuantizedValue;

        for (int i = 0; i < tileValueCount; i++) {
            row = zigZagTable[i][0];
            col = zigZagTable[i][1];

            deQuantizedValue = quantized[i] * quantizeTable[row][col];
            deQuantized[row][col] = deQuantizedValue;
        }
        return deQuantized;
    }
}
