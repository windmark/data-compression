


public class ScalarQuantization {
    private final int blockSize;
    private int[][] zigZag;


    public ScalarQuantization(int blockSize) {
        this.blockSize = blockSize;
        zigZag = new int[blockSize * blockSize][2];
        initZigZag(zigZag);
    }





    // Based on the Zig Zag algorithm found here
    // http://rosettacode.org/wiki/Zig_Zag
    public void initZigZag(int[][] zigZag) {
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

}
