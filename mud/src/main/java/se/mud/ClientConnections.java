/*
This class represents an individual player, holding their data, connection socket, and communication streams.
*/

package se.mud;

import java.io.BufferedReader;      // A wrapper for reading text from an input stream efficiently
import java.io.IOException;         // For handling IO exceptions
import java.io.InputStreamReader;   // For reading input from the player
import java.io.PrintWriter;         // For sending messages to the player
import java.net.Socket;             // Represents the player's connection to the server

import se.mud.Other.Player;
import se.mud.Other.CommandParser;
import se.mud.Other.Utl;

public class ClientConnections implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String name;
    private CommandParser commandParser;
    private Player player;

    // Constructor initializes the player with their socket connection
    public ClientConnections(Socket socket) {
        this.socket = socket;
    }

    // The main run method for the player thread, handling login and command processing
    @Override
    public void run() {
        try {
            // Initialize input and output streams for communication with the player
            out = new PrintWriter(socket.getOutputStream(), true);  // Our output stream to the player
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Our input stream from the player

            // Welcome the player and prompt for login or character creation, return player
            out.println("Welcome to just another MUD");

            // Loop through until we have ourself a player
            while (player == null) {
                out.println("Enter your character name or write 'new' to create one.");
                String input = in.readLine();
                // If input is new, start creating a new character
                if (Utl.equal(input, "new")) {
                    // Save name to input
                    out.println("Please choose a name: ");
                    input = in.readLine();
                    // Call playerCreate with input and the next input
                    out.println("Please choose a password: ");
                    player = DatabaseHandler.getInstance().playerCreate(input, in.readLine());
                    out.println("Welcome to the world " + player.getName() + "!");
                } else {
                    // We have recieved name and will call playerLogin with next input and save to player
                    out.println("Please input your password: ");
                    player = DatabaseHandler.getInstance().playerLogin(input, in.readLine());

                    // Could not find player, player is still null loop will continue
                    if (player == null) {
                        out.println("Incorrect name or password!");
                    // Player has been set so loop will end
                    } else {
                        out.println("Welcome back " + player.getName() + "!");
                    }
                }
            }

            // Register the player to the world
            GameEngine.registerPlayer(this);
            // Initialize the command parser for this player
            commandParser = new CommandParser(this);

            // Input loop to continuously read commands from the player
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equalsIgnoreCase("quit")) {
                    out.println("Goodbye!");
                    break;
                }
                // Send raw text to our decoupled parser
                commandParser.Input(inputLine);
            }
        } catch (IOException e) {
            System.out.println(name + " disconnected abruptly.");
        } finally {
            cleanup();
        }
    }

    // Placeholder method to check if a player exists in the database
    public boolean playerExists(String username) {
        return true; // Assume the player exists for now; implement actual database check later
    }

    // Placeholder method to handle player login with username and password
    public boolean login(String username, String password) {
        return true; // Username and password match in the database, login is successful
    }

    public boolean createCharacter() {
        return true; // Return true if character creation is successful 
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public String getName() {
        return name;
    }

    private void cleanup() {
        GameEngine.removePlayer(this);
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket for " + name + ": " + e.getMessage());
        }
    }


}
