package Protocols;


/**  PROTOCOL:
 *  Other.Client -> Other.Server           Other.Server -> Other.Client
 *  ----------------           ----------------
 *  MOVE <src> <dst> (x,y,z)   WELCOME PLAYER<int>  (player number)
 *  EXIT                       PLAYER_MOVED: <src> <dst> (x,y,z)
 *                             MESSAGE <text>
 *
 * */
public class StandardServerProtocol implements  ServerProtocol {
    private String message;

    public StandardServerProtocol(){
        message = new String();
    }

    @Override
    public Protocol.ClientToServerType interpretClientMessage(String message) {
        if(message.startsWith("MOVE:")) {
            this.message = message.substring(6);
            return Protocol.ClientToServerType.MOVE;
        }
        else if(message.startsWith("EXIT")){
            this.message = "EXIT";
            return Protocol.ClientToServerType.EXIT;
        }
        return Protocol.ClientToServerType.EXIT;
    }

    @Override
    public String getPreparedMessage() {
        if(message == null)
            message = new String("NULL");
        return message;
    }

    @Override
    public String createMessageToClient(Protocol.ServerToClientType type, String message) {
        switch (type){

            case WELCOME:
                return "WELCOME PLAYER: " + message;
            case PLAYER_MOVED:
                return "MOVE: " + message;
            case MESSAGE:
                return "MESSAGE_FROM_SERVER: " + message;
            case YOUR_TURN:
                return "YOUR_TURN";
            case YOUR_INDEX:
                return "YOUR_INDEX: " + message;
            default:
                    return "NULL";
        }
    }
}
