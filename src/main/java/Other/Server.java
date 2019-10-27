package Other;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;

import Logic.*;

/**
 * A server for a network multi-player game.  Modified and
 *
 *  PROTOCOL:
 *  Other.Client -> Other.Server           Other.Server -> Other.Client
 *  ----------------           ----------------
 *  MOVE <src> <dst> (x,y,z)   WELCOME PLAYER<int>  (player number)
 *  EXIT                       PLAYER_MOVED: <src> <dst> (x,y,z)
 *                             MESSAGE <text>
 *
 *
 * A second change is that it allows an unlimited number of pairs of
 * players to play.
 */
public class Server extends Thread {

    private ServerCreator sh;
    private Game game;
    private int port;
    private String ip;
    private int players;
    private ServerSocket listener;
    private Random random;

    private boolean isOn;

    public Server(ServerCreator handler) {
        sh = handler;
        isOn = false;
        players = 0;
        random = new Random();
    }

    /**
     * @throws ServerNotInitializedException if thread was not started
     */
    public boolean isClosed() throws ServerNotInitializedException {
        if(!isOn)
            throw new ServerNotInitializedException("Socket is not set, invoke start()");

        return listener.isClosed();
    }

    public int getSocketsSize(){
        return players;
    }

    final public String getSetIp(){
        return ip;
    }

    /**
     * @throws ServerNotInitializedException if thread was not started
     */
    public Game getRunningGame() throws ServerNotInitializedException {
        if(!isOn)
            throw new ServerNotInitializedException("Game is not set, invoke start()");
        return game;
    }

    /**
     * @throws ServerNotInitializedException if thread was not started
     */
    public ServerSocket getServerSocket() throws ServerNotInitializedException {
        if(!isOn)
            throw new ServerNotInitializedException("Socket is not set, invoke start()");

        return listener;
    }


    /**
     * @throws ServerNotInitializedException if thread was not started
     */
    public void close() throws ServerNotInitializedException {
      if(!isOn)
            throw new ServerNotInitializedException("Socket is not set, invoke start()");

        sh.setState(ServerState.OFFLINE);

        if(listener == null)
            return;

        try {
            endGame();

            if (!listener.isClosed())
                listener.close();
        }catch(Exception e){
            sh.setLogMessage("WARNING2: " + e.getMessage() + "\n close failure...",this);
        }
    }

    /**
     * Forces game to stop
     * @throws ServerNotInitializedException if thread was not started
     */
    public void endGame() throws ServerNotInitializedException {
        if(!isOn)
            throw new ServerNotInitializedException("Socket is not set, invoke start()");

        if(game != null) {
            game.stopGame();
            sh.setState(ServerState.WAITING);
            sh.setLogMessage("Game was stopped\n Set new game!",this);
        }
    }

    /**
     * Runs the application. Pairs up clients that connect.
     */

    /**
     * @param port    [0-59999]
     * @param ip      {x.x.x.x}
     * @param players {1,2,3,6}
     */
    public void setNewGame(int port, String ip, int players) {
        this.port = port;
        this.ip = ip;
        this.players = players;
    }


    public void run() {
        isOn = true;
        try {
            listener = new ServerSocket(port, 0, InetAddress.getByName(ip));

            sh.setLogMessage("Server is waiting for all players\n\t to join\n",this);
            try {
                GameSettings.NUM_HUMAN_PLAYERS = players;
                ArrayList<Game.Player> ordered_players = new ArrayList<>();
                ArrayList<Game.Player> game_players = new ArrayList<>();
                ArrayList<Integer>nums = new ArrayList<>();

                game = new Game();

                for (int i = 0; i < players; ++i) {
                    int order = random.nextInt(players);
                    while(nums.contains(order)){
                        order = random.nextInt(players);
                    }
                    nums.add(order);

                    Game.Player a = game.new Player(listener.accept(), order);
                    game_players.add(a);
                    sh.increment();
                }

                for (int i = 0; i < players; ++i) {
                    for (int j = 0; j < players; ++j) {
                        if(game_players.get(j).order == i){
                            ordered_players.add(game_players.get(j));
                        }
                    }
                }

                game.setPlayersArray(ordered_players);
                game.createNewGame();

                for (Game.Player p : ordered_players) {
                    p.start();
                }

                sh.setLogMessage("Server is running",this);
                sh.setState(ServerState.RUNNING);

                while(game.hasRunningSockets()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //
                    }
                }

            } finally {
                listener.close();
                sh.setLogMessage("Server closed action",this);
            }
        } catch(Exception e1) {
            sh.setLogMessage("ERROR1: " + e1.getMessage() + "\n Server exception", this);
        }


    }
}

