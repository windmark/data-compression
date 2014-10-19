package com.windmark.marcus;

import java.util.ArrayList;
import java.util.List;

public class HuffmanCode {

    private ArrayList<List<Integer>> codeTable;

    public HuffmanCode(BinaryTree tree, int size) {
        assert tree != null;
        codeTable = new ArrayList<List<Integer>>();

        // to be able to insert out of order
        for (int i = 0; i < size; i++)
            codeTable.add(null);

        buildCodes(tree, new ArrayList<Integer>());
    }


    public void buildCodes(BinaryTree tree, ArrayList<Integer> prefix) {
        if (tree instanceof Node) {
            Node node = (Node) tree;

            // left
            prefix.add(0);
            buildCodes(node.left, prefix);
            prefix.remove(prefix.size() - 1);

            // right
            prefix.add(1);
            buildCodes(node.right, prefix);
            prefix.remove(prefix.size() - 1);

        } else if (tree instanceof Leaf) {
            Leaf leaf = (Leaf) tree;
            codeTable.set(leaf.value, new ArrayList<Integer>(prefix));

        } else {
            throw new AssertionError("Illegal type of node");
        }
    }

    public ArrayList<List<Integer>> get() {
        return codeTable;
    }

}
