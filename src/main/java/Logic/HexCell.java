package Logic;

import java.util.ArrayList;

public class HexCell<K> {
    /** Data held in Logic.HexCell */
    private K key;
    /** (x,y,z) coordinates. x+y+z=0 at all times */
    private int x, y, z;
    /** Neighboring HexCells. Char1 -> increase Logic.Dimension, Char2 -> decrease */
    private HexCell<K> xy, xz, yx, yz, zx, zy;
    /** For iteration. Notes whether the node has been visited */
    private boolean visited;

    /**
     * Constructs Logic.HexCell
     * @param key data held in node
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public HexCell(K key, int x, int y, int z) {
        /** Error Handling: x, y, and z must add up to 0 */
        if(x+y+z != 0) throw new IllegalArgumentException();

        /** Initialization */
        this.key = key;
        this.x = x;
        this.y = y;
        this.z = z;
        visited = false;
    }

    /**
     *
     * @param str string like (x,y,z)
     * @throws IllegalArgumentException
     */
    public HexCell(String str) {
        int i = str.indexOf('(');
        int j = str.indexOf(',');
        key = null;
        String helper = str.substring(i+1,j);
        x = Integer.parseInt(helper);
        i = j;
        j = str.indexOf(',', i+1);
        helper = str.substring(i+1,j);
        y = Integer.parseInt(helper);
        i = j;
        j = str.indexOf(')', i+1);
        helper = str.substring(i+1,j);
        z = Integer.parseInt(helper);

        /** Error Handling: x, y, and z must add up to 0 */
        if(x+y+z != 0) throw new IllegalArgumentException();
        visited = false;
    }

    /**
     * Sets key
     * @param key new key
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Returns current key
     * @return key
     */
    public K getKey() {
        return key;
    }

    /**
     * Returns x coordinate
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns y coordinate
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns z coordinate
     * @return z coordinate
     */
    public int getZ() {
        return z;
    }

    /**
     * Returns if the node has been visited
     * @return visited field
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Sets the visited field to the given boolean parameter
     * @param toVisit whether or not the node has been visited
     */
    public void visit(boolean toVisit) {
        visited = toVisit;
    }

    /**
     * Adds a neighbor to this node given node and direction
     * @param n node to add to neighbors
     * @param inc direction increasing from this node
     * @param dec direction decreasing from this node
     */
    public void addNeighbor(HexCell<K> n, Dimension inc, Dimension dec) {
        /** Error Handling: Must have two separate dimensions to locate the nodes*/
        if(inc == dec) throw new IllegalArgumentException();

        else if(inc == Dimension.X && dec == Dimension.Y) xy = n;
        else if(inc == Dimension.X && dec == Dimension.Z) xz = n;
        else if(inc == Dimension.Y && dec == Dimension.X) yx = n;
        else if(inc == Dimension.Y && dec == Dimension.Z) yz = n;
        else if(inc == Dimension.Z && dec == Dimension.X) zx = n;
        else if(inc == Dimension.Z && dec == Dimension.Y) zy = n;
    }

    /**
     * Returns the neighboring node given an increasing and decreasing dimension.
     * @param inc increasing dimension
     * @param dec decreasing dimension
     * @return neighboring node
     */
    public HexCell<K> getNeighbor(Dimension inc, Dimension dec) {
        if(inc == Dimension.X && dec == Dimension.Y) return xy;
        else if(inc == Dimension.X && dec == Dimension.Z) return xz;
        else if(inc == Dimension.Y && dec == Dimension.X) return yx;
        else if(inc == Dimension.Y && dec == Dimension.Z) return yz;
        else if(inc == Dimension.Z && dec == Dimension.X) return zx;
        else if(inc == Dimension.Z && dec == Dimension.Y) return zy;

        /** Error Handling: Must have two separate dimensions to locate the nodes */
        else throw new IllegalArgumentException();
    }

    /**
     * Return list of neighboring HexCells
     * @return ArrayList of neighbors
     */
    public ArrayList<HexCell<K>> getNeighbors() {
        ArrayList<HexCell<K>> result = new ArrayList<HexCell<K>>();
        if(xy != null) result.add(xy);
        if(xz != null) result.add(xz);
        if(yx != null) result.add(yx);
        if(yz != null) result.add(yz);
        if(zx != null) result.add(zx);
        if(zy != null) result.add(zy);
        return result;
    }

    /**
     * Determines if the passed node is adjacent to this node
     * @param n possible neighbor
     * @return true if n is a neighbor, false otherwise
     */
    public boolean isNeighbor(HexCell<K> n) {
        int dx, dy, dz;
        dx = x - n.getX();
        dy = y - n.getY();
        dz = z - n.getZ();

        return ((dx==1|dy==1|dz==1)&&(dx==-1|dy==-1|dz==-1));
    }

    /**
     * Determines if two HexCells have the same coordinates
     * @param n other Logic.HexCell
     * @return true if the two are equal, false otherwise
     */
    public boolean equals(HexCell<K> n) {
        if(n == null) return false;
        return (x == n.getX() && y == n.getY() && z == n.getZ());
    }

    /**
     * Returns a 2-D (x,y) point to represent this node
     * @return Logic.Point object with the Logic.HexCell and its 2-D (x,y) coordinate
     */
    public Point<HexCell<K>> pointConversion() {
        double dx = (double) x;
        double dy = (double) y;
        double dz = (double) z;
        return new Point<HexCell<K>>(this, -1*(dx-dy)/2, dz);
    }


    public String toString() {
        return ("(" + x + "," + y + "," + z + ")");
    }
}
