package com.windmark.marcus;


class BinaryTree implements Comparable<BinaryTree> {
    public final int frequency;

    public BinaryTree(int freq) {
        frequency = freq;
    }


    public int compareTo(BinaryTree tree) {
        return frequency - tree.frequency;
    }
}

class Node extends BinaryTree {
    public final BinaryTree left, right;

    public Node(BinaryTree l, BinaryTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}

class Leaf extends BinaryTree {
    public final int value;

    public Leaf(int frequency, int val) {
        super(frequency);
        value = val;
    }
}
