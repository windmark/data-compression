package com.windmark.marcus;

import java.util.PriorityQueue;


public class HuffmanTree {

    private BinaryTree tree;

    public HuffmanTree(FrequencyTable frequencies) {
        buildTree(frequencies);
    }


    private void buildTree(FrequencyTable frequencies) {
        PriorityQueue<BinaryTree> codewordTree = new PriorityQueue<BinaryTree>();

        for (int i = 0; i < frequencies.length(); i++) {
            if (frequencies.get(i) > 0)
                codewordTree.add(new Leaf(frequencies.get(i), i));
        }

        assert codewordTree.size() > 0;

        while (codewordTree.size() > 1) {
            BinaryTree x = codewordTree.poll();
            BinaryTree y = codewordTree.poll();

            codewordTree.add(new Node(x, y));
        }
        tree = codewordTree.poll();
    }

    public BinaryTree getTree() {
        return tree;
    }




    public void printCodes(BinaryTree tree, StringBuffer prefix) {
        assert tree != null;
        if (tree instanceof Leaf) {
            Leaf leaf = (Leaf)tree;

            System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);

        } else if (tree instanceof Node) {
            Node node = (Node) tree;

            // traverse left
            prefix.append('0');
            printCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length() - 1);

            // traverse right
            prefix.append('1');
            printCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length()-1);
        }
    }
}