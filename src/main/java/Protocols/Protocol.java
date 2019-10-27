package Protocols;

/**  PROTOCOL:
        *  Other.Client -> Other.Server           Other.Server -> Other.Client
        *  ----------------           ----------------
        *  MOVE: <src> <dst> (x,y,z)   WELCOME PLAYER: <int>  (player number)
        *  EXIT                       PLAYER_MOVED: <src> <dst> (x,y,z)
        *                             MESSAGE: <text>
        *
        * */
public class Protocol {

    public enum ClientToServerType {MOVE, EXIT}
    public enum ServerToClientType {WELCOME, PLAYER_MOVED, MESSAGE, YOUR_TURN, YOUR_INDEX}

}
