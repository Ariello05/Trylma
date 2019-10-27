import Protocols.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProtocolTests {

    @Test
    public void commandsTest(){
        ClientProtocol cp = new StandardClientProtocol();
        ServerProtocol sp = new StandardServerProtocol();

        String s = cp.createMessageToServer(Protocol.ClientToServerType.MOVE,"(0,0,1)(-1,0,0)");
        sp.interpretClientMessage(s);
        assertEquals(sp.getPreparedMessage(), "(0,0,1)(-1,0,0)");

        s = cp.createMessageToServer(Protocol.ClientToServerType.EXIT, ":)");
        assertEquals(sp.interpretClientMessage(s), Protocol.ClientToServerType.EXIT);

        s = sp.createMessageToClient(Protocol.ServerToClientType.MESSAGE, ":O");
        cp.interpretServerMessage(s);
        assertEquals(cp.getPreparedMessage(),":O");

        s = sp.createMessageToClient(Protocol.ServerToClientType.PLAYER_MOVED, ":X");
        cp.interpretServerMessage(s);
        assertEquals(cp.getPreparedMessage(),":X");
    }
}
