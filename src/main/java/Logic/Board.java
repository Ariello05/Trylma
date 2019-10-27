package Logic;

import GUI.Main;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Board {

    /** Logic.Board data srtucture */
    private Latticing<Piece> latticeBoard;
    /** Array of Players */
    private Color[] players;
    /** Logic.Board radius */
    private int radius;
    /** Win Locations */
    ArrayList<ArrayList<HexCell<Piece>>> winLocs;


    public Board(int radius, Color[] players) {
        if (radius < 2) throw new IllegalArgumentException();
        if(players.length != 2 && players.length != 4 && players.length != 6) throw new IllegalArgumentException();

        latticeBoard = new Latticing<Piece>();
        this.players = players;
        this.radius = radius;

        winLocs = new ArrayList<ArrayList<HexCell<Piece>>>();
        for (int i = 0; i < players.length; i++) {
            winLocs.add(new ArrayList<HexCell<Piece>>());
        }

        createCenter(latticeBoard);
        createHomes(latticeBoard);
    }

    /**
     * Creates new latticeBoard and fills it
     */
    public void regenerateBoard(){
        latticeBoard = new Latticing<Piece>();

        createCenter(latticeBoard);
        createHomes(latticeBoard);
    }

    /**
     *
     * @param latticeBoard lattice to be filled with standard pieces
     */
    public void populateLattice(Latticing<Piece> latticeBoard){
        createCenter(latticeBoard);
        createHomes(latticeBoard);
    }

    /**
     *
     * @param latticeBoard new logic for Board
     */
    public void setNewLatticeBoard(Latticing<Piece> latticeBoard){
        this.latticeBoard = latticeBoard;
    }

    /**
     *
     * @return used Latticing
     */
    public Latticing<Piece> getLatticeBoard(){
        return latticeBoard;
    }

    /**
     * Creates center area of the board
     *
     */
    private void createCenter(Latticing<Piece> lattice) {
        for (int x = -1 * (radius-1); x <= radius - 1; x++) {
            for (int y = -1 * (radius - 1); y <= radius - 1; y++) {
                if (-1 * (x + y) >= -1 * (radius - 1) && -1 * (x + y) <= radius - 1) {
                    lattice.insert(new HexCell<Piece>(null, x, y, -1 * (x + y)));
                }
            }
        }
    }

    /**
     * Creates triangle areas of the board
     *
     */
    private void createHomes(Latticing<Piece> lattice) {

        /** x */
        for(int x = radius; x <= 2*(radius-1); x++) {
            for(int z = -1*(radius-1); z < -1*Math.abs(x-radius); z++) {
                if(players.length == 6) {
                    HexCell<Piece> n = new HexCell<Piece>(new Piece(players[5]), x, -1*(x+z), z);
                    winLocs.get(4).add(n);
                    lattice.insert(n);
                }
                else
                    lattice.insert(new HexCell<Piece>(null, x, -1*(x+z), z));
            }
        }
    /** -x */
        for(int x = -1*radius; x >= -2*(radius-1); x--) {
            for(int z = radius-1; z > Math.abs(x+radius); z--) {
                if(players.length == 6) {
                    HexCell<Piece> n = new HexCell<Piece>(new Piece(players[4]), x, -1*(x+z), z);
                    winLocs.get(5).add(n);
                    lattice.insert(n);
                }
                else
                    lattice.insert(new HexCell<Piece>(null, x, -1*(x+z), z));
            }
        }
        /** y */
        for(int y = radius; y <= 2*(radius-1); y++) {
            for(int x = -1*(radius-1); x < -1*Math.abs(y-radius); x++) {
                if(players.length != 2) {
                    HexCell<Piece> n = new HexCell<Piece>(new Piece(players[3]), x, y, -1*(x+y));
                    winLocs.get(2).add(n);
                    lattice.insert(n);
                }
                else
                    lattice.insert(new HexCell<Piece>(null, x, y, -1*(x+y)));
            }
        }
        /** -y */
        for(int y = -1*radius; y >= -2*(radius-1); y--) {
            for(int x = radius-1; x > Math.abs(y+radius); x--) {
                if(players.length != 2) {
                    HexCell<Piece> n = new HexCell<Piece>(new Piece(players[2]), x, y, -1*(x+y));
                    winLocs.get(3).add(n);
                    lattice.insert(n);
                }
                else
                    lattice.insert(new HexCell<Piece>(null, x, y, -1*(x+y)));
            }
        }
        /** z */
        for(int z = radius; z <= 2*(radius-1); z++) {
            for(int y = -1*(radius-1); y < -1*Math.abs(z-radius); y++) {
                HexCell<Piece> n = new HexCell<Piece>(new Piece(players[1]), -1*(y+z), y, z);
                winLocs.get(0).add(n);
                lattice.insert(n);
            }
        }
        /** -z */
        for(int z = -1*radius; z >= -2*(radius-1); z--) {
            for(int y = radius-1; y > Math.abs(z+radius); y--) {
                HexCell<Piece> n = new HexCell<Piece>(new Piece(players[0]), -1*(y+z), y, z);
                winLocs.get(1).add(n);
                lattice.insert(n);
            }
        }
    }

    public boolean moveFromString(String src, String dst) {
        HexCell<Piece> a,b;

        try {
            int beg = 1;
            int end = src.indexOf(',');
            int x = Integer.parseInt(src.substring(beg, end));
            beg = end + 1;
            end = src.indexOf(',', beg);
            int y = Integer.parseInt(src.substring(beg, end));
            beg = end + 1;
            end = src.indexOf(')', beg);
            int z = Integer.parseInt(src.substring(beg, end));
            a = getLatticeBoard().get(x, y, z);

            beg = 1;
            end = dst.indexOf(',');
            x = Integer.parseInt(dst.substring(beg, end));
            beg = end + 1;
            end = dst.indexOf(',', beg);
            y = Integer.parseInt(dst.substring(beg, end));
            beg = end + 1;
            end = dst.indexOf(')', beg);
            z = Integer.parseInt(dst.substring(beg, end));
            b = getLatticeBoard().get(x, y, z);


            return this.move(a,b);

        } catch (NumberFormatException e) {
            System.out.println("Zla konwersja!");
        }

        System.out.println("Couldn`t move despite server message");
        return false;

    }

    public boolean move(HexCell<Piece> src, HexCell<Piece> dst) {

        if(!isValidMove(src, dst)) return false;
        //System.out.println("BOARD A MOVE WAS MADE FROM " + src + " TO: " + dst + "WITHING CLIENT: " + main.getClient().getIndex());
        latticeBoard.flipNodes(src, dst);
        return true;
    }

    public boolean isValidMove(HexCell<Piece> src, HexCell<Piece> dst) {
        if(src == null || dst == null) throw new IllegalArgumentException();
        /** Must move to an empty space. Must move a non-empty piece */
        if(src.getKey() == null || dst.getKey() != null) return false;

        /** Searches through valid move list */
        for(HexCell<Piece> n : getValidMoves(src)) {
            if(dst.equals(n)) return true;
        }
        /** Returns false if move not found */
        return false;
    }

    private ArrayList<HexCell<Piece>> getValidMoves(HexCell<Piece> src) {
        ArrayList<HexCell<Piece>> validNodes = new ArrayList<HexCell<Piece>>();
        src.visit(true);

        /** Add valid immediate moves */
        for(HexCell<Piece> p : src.getNeighbors()) {
            if(p.getKey() == null && getDistance(p, src) == 1)
                validNodes.add(p);
        }
        /** Add valid jumps */
        validNodes.addAll(getValidJumps(src, validNodes));
        /** Return list */
        latticeBoard.resetVisited();
        return validNodes;
    }

    private ArrayList<HexCell<Piece>> getValidJumps(HexCell<Piece> src, ArrayList<HexCell<Piece>> validNodes) {

        /** Find possible jumps and take them */
        for(HexCell<Piece> nbr : src.getNeighbors()) {
            if(nbr.getKey() != null) {
                /** Possible jump */
                HexCell<Piece> jmp = getJumpNode(src, nbr);
                if(jmp != null && jmp.getKey() == null && !jmp.isVisited()) {
                    /** Add to possible jump list */
                    validNodes.add(jmp);
                    jmp.visit(true);
                    /** Recursively check for jumps from the jmp position */
                    validNodes.addAll(getValidJumps(jmp, validNodes));
                }
            }
        }
        return validNodes;
    }

    private HexCell<Piece> getJumpNode(HexCell<Piece> src, HexCell<Piece> toJump) {
        /** Error Handling */
        if(src == null || toJump.getKey() == null) throw new IllegalArgumentException();
        if(getDistance(src, toJump) != 1) throw new IllegalArgumentException();


        /** Returns node next in line */
        return latticeBoard.get(toJump.getX() + (toJump.getX() - src.getX()),
                toJump.getY() + (toJump.getY() - src.getY()),
                toJump.getZ() + (toJump.getZ() - src.getZ()));
    }

    public int won() {
        for(int playerIndex = 0; playerIndex < players.length; playerIndex++) {
            if(won(playerIndex)) return playerIndex;
        }
        return -1;
    }

    /**
     * Helper method for won(). Determines if a single player has won the game.
     * A player has won if both (1) every winLoc contains a piece and (2) over
     * half of those pieces are the player's.
     *
     * @param playerIndex index of the player from the players array
     * @return true if the given player has won, false otherwise
     */
    private boolean won(int playerIndex) {
        /** Number of player's pieces in winLocs */
        double playerCount = 0;

        /** For each winLoc... */
        for(HexCell<Piece> n : winLocs.get(playerIndex)) {
            /** If any of the winLocs are empty, the player hasn't won */
            if(n.getKey() == null) return false;
            /** If there is a player's piece in this winLoc, increment count */
            if(n.getKey().getColor().equals(players[playerIndex].darker()))
                playerCount++;
        }

        /** If more then 50% of the winLocs are player's pieces, that player wins */
        if((playerCount / (double) winLocs.get(playerIndex).size()) > 0.5) return true;
        else return false;
    }

    public int size() {
        return latticeBoard.getAllNodes().size();
    }

    public int getDistance(HexCell<Piece> n1, HexCell<Piece> n2) {
        return latticeBoard.getDistance(n1, n2);
    }

    /**
     * Gets a list of all of the (x,y) coordinates of a given player's pieces
     * @param playerIndex index of player in player array
     * @return list of (x,y) coordinates of player's pieces
     */
    public ArrayList<Point<HexCell<Piece>>> getPlayerPoints(int playerIndex) {
        ArrayList<Point<HexCell<Piece>>> pts = new ArrayList<Point<HexCell<Piece>>>();
        for(HexCell<Piece> n : latticeBoard.getAllNodes()) {
            if(n.getKey() != null && n.getKey().getPlayer() == players[playerIndex])
                pts.add(n.pointConversion());
        }
        return pts;
    }

    /**
     * Finds the nearest point without a piece in it on the board.
     * @param point center point to search around
     * @return nearest open point, or null if board is full.
     */
    public Point<HexCell<Piece>> getNearestOpenPoint(Point<HexCell<Piece>> point) {

        /** Error Handling: Logic.Point and its key must be non-null */
        if(point == null || point.getKey() == null) throw new IllegalArgumentException();

        int shortestDistance = radius*2;
        Point<HexCell<Piece>> nearestPoint = null;

        for(Point<HexCell<Piece>> p : getPoints()) {
            if(p.getKey().getKey() == null && getDistance(p.getKey(), point.getKey()) < shortestDistance) {
                shortestDistance = getDistance(p.getKey(), point.getKey());
                nearestPoint = p;
            }
        }
        return nearestPoint;
    }

    public ArrayList<Point<HexCell<Piece>>> getPoints() {
        ArrayList<Point<HexCell<Piece>>> pts = new ArrayList<Point<HexCell<Piece>>>();
        for(HexCell<Piece> n : latticeBoard.getAllNodes()) {
            pts.add(n.pointConversion());
        }
        return pts;
    }

    /**
     * Returns winLocs for a specific player
     * @param playerIndex index of player in player array
     * @return list of winLocs for a given player
     */
    public ArrayList<HexCell<Piece>> getWinLocs(int playerIndex) {
        return winLocs.get(playerIndex);
    }


}

