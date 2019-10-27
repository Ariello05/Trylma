import Logic.*;

import Logic.Dimension;
import Logic.Point;
import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.scene.paint.Color;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.util.ArrayList;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LatticeTest {

    @Test
    public void createFromString(){
        String str = "(10,0,-10)";
        HexCell<Piece> cp = new HexCell<Piece>(str);

        assertEquals(cp.toString(), "(10,0,-10)");
    }

    @Test
    public void testLattice(){

        Latticing<Piece> latt = new Latticing<>();
        latt.insert(new HexCell<Piece>(null,1,0,-1));

        assertTrue(latt.containsNode(1,0,-1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWrongArgument() throws  IllegalArgumentException
    {
        Latticing<Piece> latt = new Latticing<>();
        latt.insert(new HexCell<Piece>(null,-1,0,-1));
    }

    @Test
    public void checkFlip() {
        Latticing<Piece> latt = new Latticing<>();
        HexCell<Piece> a = new HexCell<>(null,1,0,-1);
        HexCell<Piece> b = new HexCell<>(new Piece(Color.RED),-2,0,2);

        latt.flipNodes(a,b);
        assertNull(b.getKey());
        assertNotNull(a.getKey());
    }

    @Test
    public void checkDimensionalInsert(){
        Latticing<Piece> latt = new Latticing<>();
        HexCell<Piece> a = new HexCell<>(null,1,0,-1);
        latt.insert(a);
        latt.insert(a, Dimension.X,Dimension.Z);
        assertNotNull(latt.get(2,0,-2));
        assertTrue(latt.containsNode(a));
        assertFalse(latt.containsNode(0,0,0));
        assertEquals(latt.getDistance(a,new HexCell<Piece>(null,2,0,-2)),1);

    }



}
