import java.io.IOException;


public final class HuffmanDecoder {

    private BitInputStream input;

    // Must be initialized before calling read().
    // The code tree can be changed after each value decoded, as long as the encoder and decoder have the same code tree at the same time.
    public CodeTree codeTree;



    public HuffmanDecoder(BitInputStream in) {
        if (in == null)
            throw new NullPointerException("Argument is null");
        input = in;
    }



    public int read() throws IOException {
        if (codeTree == null)
            throw new NullPointerException("Code tree is null");

        InternalNode currentNode = codeTree.getRoot();
        while (true) {
            int temp = input.readNoEof();
            Node nextNode;
            if      (temp == 0) nextNode = currentNode.getLeftChild();
            else if (temp == 1) nextNode = currentNode.getRightChild();
            else throw new AssertionError();

            if (nextNode instanceof Leaf)
                return ((Leaf)nextNode).getValue();
            else if (nextNode instanceof InternalNode)
                currentNode = (InternalNode)nextNode;
            else
                throw new AssertionError();
        }
    }

}
