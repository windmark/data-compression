import java.io.IOException;
import java.io.EOFException;


public class CodeReader {
    private BitInputStream inputStream;
    private CodeTree codeTree;

    public CodeReader(BitInputStream inputStream, CodeTree codeTree) {
        if (inputStream == null) {
            throw new NullPointerException("BitInputStream can't be null");
        }
        if (codeTree == null) {
            throw new IllegalArgumentException("Code tree can't be null");
        }
        this.inputStream = inputStream;
        this.codeTree = codeTree;
    }

    public int read() throws IOException {
        InnerNode currentNode = codeTree.getRoot();

        while (true) {
            int bit = inputStream.read();
            if (bit == -1) {
                return -1; // EOS REACHED
            }
            Node nextNode;

            if (bit == 0) {
                nextNode = currentNode.getLeftChild();
            } else if (bit == 1) {
                nextNode = currentNode.getRightChild();
            } else {
                throw new Error("Bit other than 0 or 1 not permitted.");
            }

            if (nextNode instanceof Leaf) {
                return ((Leaf) nextNode).getValue();
            } else if (nextNode instanceof InnerNode) {
                currentNode = (InnerNode) nextNode;
            } else {
                throw new Error("Node other than Leaf or InnerNode not permitted");
            }
        }
    }

    public CodeTree getCodeTree() {
        return codeTree;
    }

    public void setCodeTree(CodeTree newCodeTree) {
        if (codeTree == null) {
            throw new IllegalArgumentException("Code tree can't be null");
        }
        codeTree = newCodeTree;
    }
}
