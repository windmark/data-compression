package com.windmark.marcus;

import java.util.*;

abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; // the frequency of this tree
    public HuffmanTree(int freq) { frequency = freq; }

    // compares on the frequency
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}

class HuffmanLeaf extends HuffmanTree {
    //public final char value; // the character this leaf represents
    public final int value;

    public HuffmanLeaf(int freq, int val) {
        super(freq);
        value = val;
    }
}

class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; // subtrees

    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}

public class HuffmanCode {

    public static HuffmanTree buildTree(int[] frequencies) {
        PriorityQueue<HuffmanTree> codewordTree = new PriorityQueue<HuffmanTree>();

        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0)
                codewordTree.add(new HuffmanLeaf(frequencies[i], i));
        }

        // to not continue if is are no frequency > 0
        assert codewordTree.size() > 0;

        while (codewordTree.size() > 1) {
            // the two nodes with least frequency
            HuffmanTree a = codewordTree.poll();
            HuffmanTree b = codewordTree.poll();

            codewordTree.add(new HuffmanNode(a, b));
        }
        return codewordTree.poll();
    }

    public static void printCodes(HuffmanTree tree, StringBuffer prefix) {
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;

            System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);

        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode) tree;

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