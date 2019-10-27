package Other;

import Protocols.ClientProtocol;
import Protocols.Protocol;
import Protocols.StandardClientProtocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import Logic.*;
import GUI.Main;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
   // private String serverAddress;
   // private int port;

    private ClientProtocol protocol;
    private ServerListener listener;
    private Main main;

    private int controlIndex;
    private boolean validTurn;


    public Client(Main main){
        this.main = main;
        //serverAddress = "192.168.1.13";
        //serverAddress = "LOCALHOST";
        //port = 9898;
        controlIndex = 0;
        validTurn = false;
    }

    public int getControlIndex(){
        return controlIndex;
    }

    public boolean getValidTurn(){
        return  validTurn;
    }

    public void connectToServer(String serverAddress, int port)throws Exception{
        socket = new Socket(serverAddress, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        protocol = new StandardClientProtocol();

        listener = new ServerListener(in,this);
        listener.start();

       // out.println(protocol.createMessageToServer(Protocol.ClientToServerType.MOVE, "(3,0,-3)_(2,0,-2)"));
        //out.println(protocol.createMessageToServer(Protocol.ClientToServerType.EXIT," "));
    }

    /**
    */
    public void receive(String str)throws Exception{
        switch (protocol.interpretServerMessage(str)){

            case WELCOME:
                System.out.println(str);
                // TODO: Napis przywitania
                break;
            case YOUR_TURN:
                this.validTurn = true;
                break;
            case PLAYER_MOVED:
                String prep = protocol.getPreparedMessage();
                HexCell<Piece> src = new HexCell<Piece>(prep);
                prep = prep.substring(prep.indexOf(')') + 1);
                HexCell<Piece> dst = new HexCell<Piece>(prep);

                System.out.println("client: " + src.toString() + "\t" + dst.toString());

                //System.out.println("INDEX: " + index + " UPDATE THIS CLIENT");

                if (main.getBoard().moveFromString(src.toString(), dst.toString()) )
                {
                    //System.out.println("INDEX: " + index + " SUCCESFUL MOVE");

                }
                else {
                    //System.out.println("INDEX: " + index + " UNSUCCESFUL MOVE");

                }
                main.getArea().reDraw();
                break;

            case MESSAGE:
                System.out.println(str);
                // TODO: Wiadomosc np. Twoja tura
                break;
            case YOUR_INDEX:
                String prep2 = protocol.getPreparedMessage();
                int i = Integer.parseInt(prep2);
                controlIndex = i;
                main.getArea().setCurrentPlayerIndex(controlIndex);

                System.out.println("You are player number: " + prep2);
                break;

            default:
                    break;

        }
        //if(str.contains("End")){
        //    System.out.println("END RECEIVED:" + str);
        //   listener.stopListener();
        //}
    }

    public void critical_error(){
        out.println(protocol.createMessageToServer(Protocol.ClientToServerType.EXIT," "));
        listener.stopListener();
    }

    public void moveCommand(String str){
        validTurn = false;
        out.println(protocol.createMessageToServer(Protocol.ClientToServerType.MOVE, str));
    }
}
