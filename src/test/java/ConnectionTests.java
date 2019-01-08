

import Logic.HexCell;
import Logic.Latticing;
import Logic.Piece;
import Other.Client;
import Other.Server;
import Other.ServerCreator;
import Other.ServerState;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.rmi.server.ExportException;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ConnectionTests {

    @Test
    public void create() throws Exception {

        ServerCreator sc;
        Server server;
        Client client;

        sc = Mockito.mock(ServerCreator.class);
        //sc = new ServerCreator();
        server = new Server(sc);
        client = new Client(null);

        //when(sc.setLogMessage(Matchers.any(String.class),server))

        server.setNewGame(9007,"LOCALHOST",1);
        server.start();
        assertEquals(sc.getConnectedAmount(), 0);
        client.connectToServer("LOCALHOST",9007);
        //assertEquals(sc.getConnectedAmount(), 1);
        assertFalse(server.isClosed());

        assertEquals(server.getSetIp(),"LOCALHOST");
        assertEquals(server.getSocketsSize(), 1);
        //assertEquals(sc.getState(), ServerState.RUNNING);
        server.close();
        //assertEquals(sc.getState(), ServerState.EXITED);
    }

    @Test (expected = Exception.class)
    public void testWrongArgument() throws  Exception
    {
        Client c = new Client(null);
        c.connectToServer("LOCALHOST", 999999);
    }

    @Test (expected = Exception.class)
    public void testWrongData() throws Exception
    {
        Client c = new Client();
        c.receive("MOVE: (-1,0,2)(0,0,0)");
    }
}
