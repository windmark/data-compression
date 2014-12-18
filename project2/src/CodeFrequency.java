import java.util.PriorityQueue;


public class CodeFrequency {
    private int[] table;

    public CodeFrequency(int size) {
        table = new int[size];
        for (int i = 0; i < size; i++) {
            table[i] = 1;
        }
    }

    public int getFrequency(int index) {
        return table[index];
    }

    public void increment(int index) {
        if (index < 0 || index >= table.length) {
            throw new IllegalArgumentException("Index out of range");
        }
        table[index]++;
    }

    public CodeTree generateCodeTree() {
        PriorityQueue<Node> codeQueue = new PriorityQueue<Node>();

        for (int i = 0; i < table.length; i++) {
            if (table[i] > 0) {
                codeQueue.add(new Leaf(i, table[i]));
            }
        }

        for (int i = 0; i < table.length && codeQueue.size() < 2; i++) {
            if (i >= table.length || table[i] == 0)
                codeQueue.add(new Leaf(i, 0));
        }

        while (codeQueue.size() > 1) {
            Node node1 = codeQueue.remove();
            Node node2 = codeQueue.remove();
            codeQueue.add(new InnerNode(node1, node2, node1.getFrequency() + node2.getFrequency()));
        }

        InnerNode root = (InnerNode) codeQueue.remove();
        CodeTree codeTree = new CodeTree(root, table.length);
        return codeTree;
    }
}
