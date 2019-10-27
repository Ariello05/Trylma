package Logic;

public class Point<K> {
    private double px, py;
    private K key;

    public Point(K key, double px, double py) {
        this.key = key;
        this.px = px;
        this.py = py;
    }

    public double getPx() {
        return px;
    }

    public double getPxCord() {
        return px * GameSettings.HEX_DIAMETER + GameSettings.SCREEN_SIZE / 2;
    }

    public double getPy() {
        return py;
    }

    public double getPyCord() {
        return py * (GameSettings.HEX_DIAMETER - (GameSettings.Y_OFFSET - GameSettings.VISUAL_OFFSET)) + GameSettings.SCREEN_SIZE / 2;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public boolean equals(Point<K> p) {
        return (px == p.getPx() && py == p.getPy());
    }

    public boolean contains(double x, double y) {
        double tx = (x - getPxCord() - GameSettings.PIECE_DIAMETER / 2) / (GameSettings.PIECE_DIAMETER / 2);
        double ty = (y - getPyCord() - GameSettings.PIECE_DIAMETER / 2) / (GameSettings.PIECE_DIAMETER / 2);

        if (tx*tx + ty*ty <=1.0) {
            return true;
        }
        else{
            return false;
        }
    }

    public String toString() {
        return ("(" + px + "," + py + ")");
    }
}
