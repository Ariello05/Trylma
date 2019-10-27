import Logic.HexCell;
import Logic.Piece;

import Logic.Point;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class HexCellTest {

    HexCell<Piece> a;
    HexCell<Piece> b;
    HexCell<Piece> c;

    @Before
    public void create(){
        a = new HexCell<>(null,1,0,-1);
        b = new HexCell<>(null, 0,0,0);
        c = new HexCell<>(null,10, 0,-10);
    }

    @Test
    public void createFromString(){
        String str = "(10,0,-10)";
        HexCell<Piece> cp = new HexCell<Piece>(str);

        assertEquals(cp.toString(), "(10,0,-10)");
    }

    @Test
    public void checkNeigbour(){
        assertTrue(a.isNeighbor(b));
        assertFalse(a.isNeighbor(c));

    }

    @Test
    public void check2D(){
        Point<HexCell<Piece>> p = a.pointConversion();
        assertEquals(p.getKey(),a);
        assertEquals(p.getPx(),-1.0/2.0);
        assertEquals(p.getPy(),-1.0);
    }
}
