

public class InternalNode extends Node {
    private Node leftChild;
    private Node rightChild;

    public InternalNode(Node leftChild, Node rightChild) {
        super(-1);

        if (leftChild == null || rightChild == null) {
            throw new NullPointerException("Left or right child can't be null in an internal node");
        }
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public InternalNode(Node leftChild, Node rightChild, int frequency) {
        super(frequency);

        if (leftChild == null || rightChild == null) {
            throw new NullPointerException("Left or right child can't be null in an internal node");
        }
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

}
