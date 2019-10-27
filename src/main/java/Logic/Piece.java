package Logic;

import javafx.scene.paint.Color;

public class Piece {
    private Color color;
    private boolean isHighlighted;

    public Piece(Color color) {
        isHighlighted = false;
        this.color = color;
    }

    public Color getColor() {
        if (isHighlighted) {
            return color;
        } else {
            return color.darker();
        }
    }

    public Color getPlayer() {
        return color;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void highlight(boolean highlighted) {
        isHighlighted = highlighted;
    }
}
