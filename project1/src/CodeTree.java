import java.util.ArrayList;
import java.util.List;


public class CodeTree {
    private InnerNode root;
    private ArrayList<List<Integer>> codeList;

    public CodeTree(InnerNode root, int codeLength) {
        if (root == null) {
            throw new NullPointerException("Root node can't be null");
        }
        this.root = root;
        codeList = new ArrayList<List<Integer>>();

        // to be able to insert out of order
        for (int i = 0; i < codeLength; i++) {
            codeList.add(null);
        }
        generateCodeList(root, new ArrayList<Integer>());
    }

    private void generateCodeList(Node node, ArrayList<Integer> prefix) {
        if (node instanceof InnerNode) {
            InnerNode innerNode = (InnerNode) node;

            prefix.add(0);
            generateCodeList(innerNode.getLeftChild(), prefix);
            prefix.remove(prefix.size() - 1);

            prefix.add(1);
            generateCodeList(innerNode.getRightChild(), prefix);
            prefix.remove(prefix.size() - 1);

        } else if (node instanceof Leaf) {
            Leaf leaf = (Leaf) node;
            codeList.set(leaf.getValue(), new ArrayList<Integer>(prefix));

        } else {
            throw new IllegalArgumentException("Illegal node type");
        }
    }

    public List<Integer> getCode(int index) {
        if (index < 0 || index > codeList.size()) {
            throw new IllegalArgumentException("Index out of bounds");
        } else if (codeList.get(index) == null) {
            throw new IllegalArgumentException("Index has no code");
        } else {
            return codeList.get(index);
        }
    }

    public InnerNode getRoot() {
        return root;
    }
}
