import java.io.IOException;
import java.util.List;

public class CodeWriter {
    private BitOutputStream outputStream;
    private CodeTree codeTree;

    public CodeWriter(BitOutputStream outputStream, CodeTree codeTree) {
        if (outputStream == null) {
            throw new IllegalArgumentException("BitOutputStream can't be null");
        }
        if (codeTree == null) {
            throw new IllegalArgumentException("Code tree can't be null");
        }
        this.outputStream = outputStream;
        this.codeTree = codeTree;
    }

    public void write(int value) {
        List<Integer> codeBits = codeTree.getCode(value);
        try {
            for (int bit : codeBits) {
                outputStream.write(bit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCodeTree(CodeTree newCodeTree) {
        if (codeTree == null) {
            throw new IllegalArgumentException("Code tree can't be null");
        }
        codeTree = newCodeTree;
    }

}
