package Protocols;


/**  PROTOCOL:
 *  Other.Client -> Other.Server           Other.Server -> Other.Client
 *  ----------------           ----------------
 *  MOVE <src> <dst> (x,y,z)   WELCOME PLAYER<int>  (player number)
 *  EXIT                       PLAYER_MOVED: <src> <dst> (x,y,z)
 *                             MESSAGE <text>
 *
 * */
public class StandardClientProtocol implements  ClientProtocol {
    private String message;

    public StandardClientProtocol(){
        message = new String();
    }

    @Override
    public Protocol.ServerToClientType interpretServerMessage(String message) {
        if(message.startsWith("MESSAGE_FROM_SERVER: ")) {
            String starts = "MESSAGE_FROM_SERVER: ";
            this.message = message.substring(starts.length());
            return Protocol.ServerToClientType.MESSAGE;
        }
        else if(message.startsWith("WELCOME PLAYER: ")){
            this.message = "You are player number: " + message;
            return Protocol.ServerToClientType.WELCOME;
        }
        else if(message.startsWith("MOVE: ")){
            this.message = message.substring(6);
            return Protocol.ServerToClientType.PLAYER_MOVED;
        }
        else if(message.startsWith("YOUR_TURN")){
            this.message = message;
            return Protocol.ServerToClientType.YOUR_TURN;
        }
        else if(message.startsWith("YOUR_INDEX: ")){
            this.message = message.substring(12);
            return  Protocol.ServerToClientType.YOUR_INDEX;
        }
        this.message = "Unkown message";
        return Protocol.ServerToClientType.MESSAGE;
    }

    @Override
    public String getPreparedMessage() {
        return this.message;
    }

    @Override
    public String createMessageToServer(Protocol.ClientToServerType type, String Message) {
        switch (type){

            case MOVE:
                return "MOVE: " + Message;
            case EXIT:
                return "EXIT";
            default:
                return "NULL";
        }
    }
}
