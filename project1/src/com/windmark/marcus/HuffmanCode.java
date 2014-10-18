package com.windmark.marcus;

import java.util.*;

abstract class BinaryTree implements Comparable<BinaryTree> {
    public final int frequency;

    public BinaryTree(int freq) {
        frequency = freq;
    }

    public int compareTo(BinaryTree tree) {
        return frequency - tree.frequency;
    }
}

class TreeNode extends BinaryTree {
    public final BinaryTree left, right;

    public TreeNode(BinaryTree l, BinaryTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}

class TreeLeaf extends BinaryTree {
    public final int value;

    public TreeLeaf(int frequency, int val) {
        super(frequency);
        value = val;
    }
}



public class HuffmanCode {

    public static BinaryTree buildTree(FrequencyTable frequencies) {
        PriorityQueue<BinaryTree> codewordTree = new PriorityQueue<BinaryTree>();

        for (int i = 0; i < frequencies.length(); i++) {
            if (frequencies.get(i) > 0)
                codewordTree.add(new TreeLeaf(frequencies.get(i), i));
        }

        assert codewordTree.size() > 0;

        while (codewordTree.size() > 1) {
            BinaryTree x = codewordTree.poll();
            BinaryTree y = codewordTree.poll();

            codewordTree.add(new TreeNode(x, y));
        }
        return codewordTree.poll();
    }

    public static void printCodes(BinaryTree tree, StringBuffer prefix) {
        assert tree != null;
        if (tree instanceof TreeLeaf) {
            TreeLeaf leaf = (TreeLeaf)tree;

            System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);

        } else if (tree instanceof TreeNode) {
            TreeNode node = (TreeNode) tree;

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