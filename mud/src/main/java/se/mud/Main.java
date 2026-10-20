package se.mud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import Core.CommandParser;

public class Main {

    // Let users connect to port 9999
    private static final int PORT = 9999;

    public static void main(String[] args) {
        System.out.println(" Starting server on " + PORT + "...");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Waiting for a connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("🔌 New connection from " + clientSocket.getRemoteSocketAddress());
                
                // Handle each client in a seperate thread
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Handle communication from clients
    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private CommandParser cmd = new CommandParser();

        public ClientHandler(Socket socket) {this.socket = socket;}

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // Welcome message
                out.println("=================================================");
                out.println(" Welcome to my first MUD! ");
                out.println("=================================================");
                out.println("> ");
                out.flush();

                // Start loop
                String inputLine;
                while ((inputLine = in.readLine()) != null) {

                    if (inputLine.equalsIgnoreCase("exit")) {
                        out.println("Exit!");
                        break;
                    }
                    
                    cmd.Input(inputLine);
                    
                    // Prompt
                    System.out.print("> ");
                    System.out.flush();
                }
            } catch (IOException e) {
                System.out.println("Connection broken by client.");
            } finally {
                try {
                    socket.close();
                    System.out.println("Connection closed.");
                } catch (IOException e) {
                    System.err.println("Could not close connection: " + e.getMessage());
                }
            }
        }

    }
}