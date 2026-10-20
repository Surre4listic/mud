package se.mud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import se.mud.Core.CommandParser;

public class Main {

    // Let users connect to port 9999
    private static final int PORT = 9999;

    public static void main(String[] args) {

        //
        // simulate player from own terminal
        //
        new Simulation().run();        

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

    //
    // simulate player from own terminal
    //
    private static class Simulation implements Runnable  {
        private CommandParser cmd = new CommandParser();
        Scanner s = new Scanner(System.in);
        String inputLine;

        @Override
            public void run() {

            System.out.println("Simulated player connection");

            // Start loop
            while ((inputLine = s.nextLine()) != null) {

                if (inputLine.equalsIgnoreCase("exit")) {
                    System.out.println("Exit!");
                    break;
                }
                
                cmd.Parse(inputLine);
                
                // Prompt
                System.out.print("> ");
                System.out.flush();
            }

            s.close();
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


                String inputLine;
                // Start loop
                while ((inputLine = in.readLine()) != null) {

                    if (inputLine.equalsIgnoreCase("exit")) {
                        out.println("Exit!");
                        break;
                    }
                    
                    cmd.Parse(inputLine);
                    
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