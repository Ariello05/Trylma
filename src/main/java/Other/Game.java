package Other;

import Logic.Board;
import Logic.GameSettings;
import Logic.HexCell;
import Logic.Piece;
import Protocols.Protocol;
import Protocols.ServerProtocol;
import Protocols.StandardServerProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

class Game {

    private Board board;
    private ServerBots bots;

    Game(){
        protocol = new StandardServerProtocol();
        players = new ArrayList<>();
        bots = new ServerBots(this);
        //gameOrder = 0;
    }

    public Board getBoard(){
        return board;
    }

    public void createNewGame()
    {
        System.out.println("NEW GAME WAS CREATED ON SERVER");
        board = new Board(GameSettings.BOARD_RADIUS, GameSettings.PLAYERS);
        //gameOrder = 0;
    }

    public void stopGame(){
        for( Player p : players){
            p.close();
        }
    }

    /**
     *
     * @return true if there are more than 1 threads running
     */
    public boolean hasRunningSockets(){
        if(players.size() >= 1)
            return true;

        return false;
    }

    /**
     * @param al  arrayList of threads to control
     */
    public void setPlayersArray(ArrayList<Player>al){
        players = al;
    }

    public void updatePlayers(HexCell<Piece> src, HexCell<Piece> dst){
        for (Player p : players) {
                p.otherPlayerMoved(src, dst);
        }
    }

    /**
     * The current player.
     */
    //Player currentPlayer;
    ArrayList<Player> players;
    ServerProtocol protocol;
    //int gameOrder;

    /**
     * Called by the player threads when a player tries to make a
     * move.  This method checks to see if the move is legal: that
     * is, the player requesting the move must be the current player
     * and the square in which she is trying to move must not already
     * be occupied.  If the move is legal the game state is updated
     */
    public synchronized boolean legalMove(HexCell<Piece> src, HexCell<Piece> dst, Player player) {

        //for (Player p : players) {//     if(p != player)//        p.otherPlayerMoved(src, dst);//}

        //if (player == currentPlayer ) {
            if(board.moveFromString(src.toString(), dst.toString())) {

                /*
                if(board.won() != -1)
                {
                    for (Player p : players) {
                        if(p != currentPlayer)
                            p.otherPlayerWon();
                    }
                }*/

                for (Player p : players) {
                    if(p != player)
                        p.otherPlayerMoved(src, dst);
                }

                int or = player.order;
                or++;
                if(or >= players.size()){
                    bots.setCurrentPlayerIndex(or);
                    while(bots.getCurrentPlayerIndex() != 0) {
                        bots.runComputerPlayer();
                    }
                    or = 0;
                }

                players.get(or).startTurn();
                return true;
            }

        return false;

      // return true;
    }

    /**
     * Thread for communication with the
     * client the player has a socket with its input and output
     * streams.  Since only text is being communicated we use a
     * reader and a writer.
     */
    class Player extends Thread {
        //Player next;
        int order;
        Socket socket;
        BufferedReader input;
        PrintWriter output;
        boolean on;

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, int order) {
            this.socket = socket;
            this.order = order;
            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println(protocol.createMessageToClient(Protocol.ServerToClientType.WELCOME, Integer.toString(order)));
                //output.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {

                System.out.println("Player died: " + e);
                players.remove(this);
            }
        }

        public void startTurn(){
            output.println(protocol.createMessageToClient(Protocol.ServerToClientType.YOUR_TURN,""));
        }

        /**
         * Closes the thread and thus socket
         */
        public void close(){
           // try {
                output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE,"Socket was called to close"));
               //this.socket.close(); // SOCKET WILL BE CLOSED WHEN THREAD ENDS HIS JOB
                this.on = false;
            //} //catch (IOException e) {
                //e.printStackTrace();
            //}
        }

        public int getOrder(){
            return order;
        }


        /**
         * We sent updated info from board to other clients
         */
        public void otherPlayerMoved(HexCell<Piece>src, HexCell<Piece> dst) {
            output.println(protocol.createMessageToClient(Protocol.ServerToClientType.PLAYER_MOVED, src.toString() + " " + dst.toString()));
        }

        /**
         * The run method of this thread.
         */
        public void run() {
            try {
                // The thread is only started after everyone connects.
                output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "All players connected"));
                output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "you are number: " + Integer.toString(order) ));

                output.println(protocol.createMessageToClient(Protocol.ServerToClientType.YOUR_INDEX, Integer.toString(order)));

                // Tell the first player that it is her turn.
                if (order == 0) {
                    output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "Your Move"));
                    output.println(protocol.createMessageToClient(Protocol.ServerToClientType.YOUR_TURN, ""));

                }

                // output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "End"));

                // Repeatedly get commands from the client and process them.
                on = true;
                while (on) {
                    String command = input.readLine();
                    switch(protocol.interpretClientMessage(command)){
                        case EXIT:

                            output.println("EXIT");
                            on = false;
                            break;
                        case MOVE:

                            String prep = protocol.getPreparedMessage();

                            try {
                                HexCell<Piece> src = new HexCell<Piece>(prep);
                                prep = prep.substring(prep.indexOf(')') + 1 );
                                HexCell<Piece> dst = new HexCell<Piece>(prep);

                                if (legalMove(src, dst, this)) {
                                    output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "Valid move"));
                                } else {
                                    output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "Invalid move"));
                                }
                            }
                            catch (IllegalArgumentException e){
                                output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "Can`t convert src && dst"));
                            }
                            break;

                        default:
                            break;
                    }
                }

            } catch (Exception e) {
                players.remove(this);
                e.printStackTrace();
            } finally {
                try {socket.close();} catch (IOException e) { players.remove(this);}
            }
        }
    }

}
