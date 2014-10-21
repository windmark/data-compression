package com.windmark.marcus;

import java.util.ArrayList;
import java.util.List;

public class CodeTree {

    private ArrayList<List<Integer>> codeTable;

    public CodeTree(BinaryTree tree, int size) {
        assert tree != null;
        codeTable = new ArrayList<List<Integer>>();

        // to be able to insert out of order
        for (int i = 0; i < size; i++) {
            codeTable.add(null);
        }

        buildCode(tree, new ArrayList<Integer>());
    }


    public void buildCode(BinaryTree tree, ArrayList<Integer> prefix) {
        if (tree instanceof Node) {
            Node node = (Node) tree;

            prefix.add(0);
            buildCode(node.left, prefix);
            prefix.remove(prefix.size() - 1);

            prefix.add(1);
            buildCode(node.right, prefix);
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
    public List<Integer> getValueCode(int index) { return codeTable.get(index); }

}
