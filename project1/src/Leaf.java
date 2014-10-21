

public class Leaf extends Node {
    private int value;

    public Leaf(int value) {
        super(-1);

        if (this.value < 0) {
            throw new IllegalArgumentException("Illegal value");
        }
        this.value = value;
    }

    public Leaf(int value, int frequency) {
        super(frequency);

        if (value < 0)
            throw new IllegalArgumentException("Illegal value");
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}