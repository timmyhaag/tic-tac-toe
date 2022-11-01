import java.net.*;
import java.io.*;

public class Dispatcher {
    static ServerSocket port;

    public static void main (String[] args) {
        ServerThread serverThread;
        Socket toClient;
        ServerSocket server;
        try {
            server = new ServerSocket(7788);
            port = server;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        do {
            try {
                System.out.println("Waiting for a connection.");
                toClient = port.accept();

                System.out.println("A request has been received.");
                serverThread = new ServerThread(toClient);
                serverThread.start();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        } while(true);
    }
}
