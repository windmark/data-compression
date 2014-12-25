import java.io.*;

public class SignalToNoiseRatio {
    private static final int IMAGE_WIDTH = 1600;
    private static final int IMAGE_HEIGHT = 1200;


    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: java SignalToNoiseRatio original_file decoded_file");
            System.exit(1);
            return;
        }

        File originalFile = new File(args[0]);
        File decodedFile = new File(args[1]);

        BufferedInputStream originalStream = new BufferedInputStream(new FileInputStream(originalFile));
        BufferedInputStream decodedStream = new BufferedInputStream(new FileInputStream(decodedFile));

        int[][] originalImage = imageToMatrix(originalStream);
        int[][] decodedImage = imageToMatrix(decodedStream);

        double SNR = signalNoiseRatio(originalImage, decodedImage);
        System.out.println(SNR);

        originalStream.close();
        decodedStream.close();
    }


    private static double signalNoiseRatio(int[][] originalImage, int[][] decodedImage) {
        double origSum = 0, diffSum = 0;
        double SNR, origMse, diffMse;
        int pixelCount = IMAGE_WIDTH * IMAGE_HEIGHT;

        for (int i = 0; i < IMAGE_WIDTH; ++i) {
            for (int j = 0; j < IMAGE_HEIGHT; ++j) {
                int p1 = originalImage[i][j];
                int p2 = decodedImage[i][j];

                origSum += p1 * p1;

                int err = p1 - p2;
                diffSum += (err * err);
            }
        }
        origMse = origSum / pixelCount;
        diffMse = diffSum / (IMAGE_HEIGHT * IMAGE_WIDTH);
        SNR = 10 * Math.log10(origMse / diffMse);
        return SNR;
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
