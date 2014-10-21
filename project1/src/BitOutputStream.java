import java.io.IOException;
import java.io.OutputStream;


/**
 * A stream where bits can be written to. Because they are written to an underlying byte stream, the end of the stream is padded with 0's up to a multiple of 8 bits. The bits are written in big endian.
 */
public final class BitOutputStream {

    // Underlying byte stream to write to.
    private OutputStream output;

    // The accumulated bits for the current byte. Always in the range 0x00 to 0xFF.
    private int currentByte;

    // The number of accumulated bits in the current byte. Always between 0 and 7 (inclusive).
    private int numBitsInCurrentByte;



    // Creates a bit output stream based on the given byte output stream.
    public BitOutputStream(OutputStream out) {
        if (out == null)
            throw new NullPointerException("Argument is null");
        output = out;
        currentByte = 0;
        numBitsInCurrentByte = 0;
    }



    // Writes a bit to the stream. The specified bit must be 0 or 1.
    public void write(int b) throws IOException {
        if (!(b == 0 || b == 1))
            throw new IllegalArgumentException("Argument must be 0 or 1");
        currentByte = currentByte << 1 | b;
        numBitsInCurrentByte++;
        if (numBitsInCurrentByte == 8) {
            output.write(currentByte);
            numBitsInCurrentByte = 0;
        }
    }


    // Closes this stream and the underlying OutputStream. If called when this bit stream is not at a byte boundary,
    // then the minimum number of "0" bits (between 0 and 7 of them) are written as padding to reach the next byte boundary.
    public void close() throws IOException {
        while (numBitsInCurrentByte != 0)
            write(0);
        output.close();
    }

}
