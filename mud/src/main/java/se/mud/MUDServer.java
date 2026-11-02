
/*
The entry point. It has only one job now: boot up, listen on the port, and spin off player threads.
*/

package se.mud;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import se.mud.Other.Utl;

public class MUDServer {
    private static final int PORT = 4000;

    public static void main(String[] args) {
        Utl.print("Starting MUD on port " + PORT + "...");
        // Start the server socket and listen for incoming connections
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            // Initialize the database
            // Todo: Uncomment this when the database creation is complete

            // Continuously accept new client connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Instantiate a new player thread for each connection
                ClientConnections player = new ClientConnections(clientSocket);
                new Thread(player).start();
            }
        } catch (IOException e) {
            Utl.debug("Server error: " + e.getMessage());
        }
    }
}