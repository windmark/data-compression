import java.io.InputStream;
import java.io.IOException;


public class BitInputStream {
    private InputStream inputStream;
    private int nextBits;
    private int bitsRemaining;
    private boolean isEndOfStream;

    public BitInputStream(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream can't be null");
        }
        this.inputStream = inputStream;
        isEndOfStream = false;
        bitsRemaining = 0;
    }

    public int read() {
        if (isEndOfStream) {
            return -1;
        }
        if (bitsRemaining == 0) {
            try {
                nextBits = inputStream.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nextBits == -1) {
                isEndOfStream = true;
                return -1;
            }
            bitsRemaining = 8;
        }
        bitsRemaining--;
        return (nextBits >>> bitsRemaining) & 1;
    }

    public void close() throws IOException {
        inputStream.close();
    }
}
