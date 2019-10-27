import Logic.*;

import Logic.Dimension;
import Logic.Point;
import Other.ServerCreator;
import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.scene.paint.Color;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.util.ArrayList;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BoardTest {

    @Test
    public void checkCreation()
    {
        Latticing<Piece> latt;
        latt = Mockito.mock(Latticing.class);

        Board b = new Board(GameSettings.BOARD_RADIUS,GameSettings.PLAYERS);
        b.populateLattice(latt);

        Mockito.verify(latt,times(10*6+24+18+12+6+1)).insert(Matchers.any(HexCell.class));

    }

    @Test (expected = IllegalArgumentException.class)
    public void checkMoves(){
        Board board = new Board(GameSettings.BOARD_RADIUS,GameSettings.PLAYERS);
        HexCell<Piece> a = new HexCell<>(null,-15,0,15);
        HexCell<Piece> b = new HexCell<>(null,-14,0,14);
        assertFalse(board.move(a,b));
        board.move(null,null);
    }


}
