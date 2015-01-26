

public abstract class Node implements Comparable<Node> {
    private final int frequency;

    public Node(int frequency) {
        this.frequency = frequency;
    }

    public int compareTo(Node other) {
        if (frequency < other.getFrequency()) {
            return -1;
        } else if (frequency > other.getFrequency()) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getFrequency() {
        return frequency;
    }
}