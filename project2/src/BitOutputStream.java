import java.io.IOException;
import java.io.OutputStream;


public class BitOutputStream {
    private OutputStream outputStream;
    private int currentByte;
    private int numBitsInCurrentByte;

    public BitOutputStream(OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("Input stream can't be null");
        }
        this.outputStream = outputStream;
        currentByte = 0;
        numBitsInCurrentByte = 0;
    }

    public void write(int bit) throws IOException {
        if (!(bit == 0 || bit == 1)) {
            throw new IllegalArgumentException("Bit must be either 0 or 1");
        }

        currentByte = (currentByte << 1) | bit;
        numBitsInCurrentByte++;
        if (numBitsInCurrentByte == 8) {
            outputStream.write(currentByte);
            numBitsInCurrentByte = 0;
        }
    }


    public void close() throws IOException {
        while (numBitsInCurrentByte != 0) {
            write(0);
        }
        outputStream.close();
    }
}
