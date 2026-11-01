
/*
The entry point. It has only one job now: boot up, listen on the port, and spin off player threads.
*/

package se.mud;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import se.mud.core.Utl;

public class MUDServer {
    private static final int PORT = 4000;

    public static void main(String[] args) {
        Utl.print("Starting MUD on port " + PORT + "...");
        // Start the server socket and listen for incoming connections
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Stops here until a new client connection is received
                Socket clientSocket = serverSocket.accept();
                // Instantiate a new player thread for each connection
                Player player = new Player(clientSocket);
                new Thread(player).start();
            }
        } catch (IOException e) {
            Utl.debug("Server error: " + e.getMessage());
        }
    }
}