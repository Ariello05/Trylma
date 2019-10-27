package Other;

import java.io.BufferedReader;

/**
 * Listens for server input
 */
public class ServerListener extends Thread{
    private Client client;
    private BufferedReader in;
    private boolean run;

    ServerListener(BufferedReader in, Client client){
        this.in = in;
        this.client = client;
        run = true;
    }

    void stopListener(){
        run = false;
    }

    @Override
    public void run(){
        String response;
        while(run){
            try {
                response = in.readLine();
                client.receive(response);
            }catch(Exception e){
                client.critical_error();
            }
        }
    }

}
