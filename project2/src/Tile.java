public class Tile {
    private double[][] tile;
    private double meanValue;


    public Tile(double[][] tile, double meanValue) {
        this.tile = tile;
        this.meanValue = meanValue;
    }

    public double[][] getTile() {
        return tile;
    }

    public double getMeanValue() {
        return meanValue;
    }
}
