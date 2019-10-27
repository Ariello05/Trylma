package Logic;

import javafx.scene.paint.Color;

public class GameSettings {
    public static final int SCREEN_SIZE = 750;
    public static final int BOARD_RADIUS = 5;
    public static final int HEX_DIAMETER = (SCREEN_SIZE*2/3)/(BOARD_RADIUS + 2*(BOARD_RADIUS-1));
    public static final int PIECE_DIAMETER = HEX_DIAMETER*4/5;
    public static final double Y_OFFSET = ((double) HEX_DIAMETER/2)*(2-Math.sqrt(3));
    public static final double VISUAL_OFFSET = ((double) HEX_DIAMETER/2) * Math.sqrt(2)/9;

    public static final boolean MOVE_ASSISTANCE = true;
    public static int NUM_HUMAN_PLAYERS = 1;
    public static final Color[] PLAYERS = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PINK, Color.GRAY};
    public static final String[] PLAYER_NAMES = {"Red", "Blue", "Green", "Yellow", "Pink", "Gray"};
}
