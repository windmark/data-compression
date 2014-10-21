import java.util.PriorityQueue;
import java.util.Queue;


public class CodeFrequency {
    private int[] table;

    public CodeFrequency(int size) {
        table = new int[size];
        for (int i = 0; i < size; i++) {
            table[i] = 1;
        }
    }

    public void increment(int index) {
        if (index < 0 || index >= table.length) {
            throw new IllegalArgumentException("Index out of range");
        }
        table[index]++;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.length; i++)
            sb.append(String.format("%d\t%d%n", i, table[i]));
        return sb.toString();
    }


    public CodeTree buildCodeTree() {
        PriorityQueue<NodeWithFrequency> codeQueue = new PriorityQueue<NodeWithFrequency>();

        for (int i = 0; i < table.length; i++) {
            if (table[i] > 0) {
                codeQueue.add(new NodeWithFrequency(new Leaf(i), i, table[i]));
            }
        }

        // Pad with zero-frequency symbols until queue has at least 2 items
        for (int i = 0; i < table.length && codeQueue.size() < 2; i++) {
            if (i >= table.length || table[i] == 0)
                codeQueue.add(new NodeWithFrequency(new Leaf(i), i, 0));
        }
        if (codeQueue.size() < 2)
            throw new AssertionError();

        // Repeatedly tie together two nodes with the lowest frequency
        while (codeQueue.size() > 1) {
            NodeWithFrequency nf1 = codeQueue.remove();
            NodeWithFrequency nf2 = codeQueue.remove();
            codeQueue.add(new NodeWithFrequency(
                    new InternalNode(nf1.node, nf2.node),
                    Math.min(nf1.lowestSymbol, nf2.lowestSymbol),
                    nf1.frequency + nf2.frequency));
        }

        // Return the remaining node
        return new CodeTree((InternalNode)codeQueue.remove().node, table.length);
    }



    private static class NodeWithFrequency implements Comparable<NodeWithFrequency> {
        public final Node node;
        public final int lowestSymbol;
        public final long frequency;

        public NodeWithFrequency(Node node, int lowestSymbol, long freq) {
            this.node = node;
            this.lowestSymbol = lowestSymbol;
            this.frequency = freq;
        }

        public int compareTo(NodeWithFrequency other) {
            if (frequency < other.frequency || lowestSymbol < other.lowestSymbol) {
                return -1;
            } else if (frequency > other.frequency || lowestSymbol > other.lowestSymbol) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
