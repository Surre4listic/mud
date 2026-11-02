/*
This class represents an individual player, holding their data, connection socket, and communication streams.
*/

package se.mud;

import java.io.BufferedReader;      // A wrapper for reading text from an input stream efficiently
import java.io.IOException;         // For handling IO exceptions
import java.io.InputStreamReader;   // For reading input from the player
import java.io.PrintWriter;         // For sending messages to the player
import java.net.Socket;             // Represents the player's connection to the server
import java.sql.Connection;

import se.mud.Other.CommandParser;
import se.mud.Other.Utl;

public class ClientConnections implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String name;
    private CommandParser commandParser;
    private Connection connection;
    private boolean loggedIn = false;

    // Constructor initializes the player with their socket connection
    public ClientConnections(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Initialize input and output streams for communication with the player
            out = new PrintWriter(socket.getOutputStream(), true);  // Our output stream to the player
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Our input stream from the player

            // Welcome message and prompt for character name
            out.println("Welcome to just another MUD!");

            // Todo: Check database comparison for player existence
            // Login loop to ensure the player is authenticated before proceeding
            do {
                out.println("Please enter your character name or type 'new' to create a new character:");
                String input = in.readLine();
                // Check if the player wants to create a new character or log in with an existing one
                if (Utl.equal(input, "new")) {
                    // Handle new character creation
                    out.println("Creating a new character. Please follow the prompts.");
                    if (createCharacter()) {
                        loggedIn = true;
                    }
                } else {
                    // Check if the player exists in the database
                    if (playerExists(input)) {
                        // Prompt for password and validate login
                        out.println("Please enter your password:");
                        while (!login(input, in.readLine())) {
                            out.println("Invalid password. Please try again.");
                        }
                        loggedIn = true;
                    } else {
                        out.println("Character not found. Please try again or type 'new' to create a new character.");
                    }
                }

            } while (!loggedIn);

            GameEngine.registerPlayer(this); // Register the player to the world
            commandParser = new CommandParser(this); // Initialize the command parser for this player

            out.println("Welcome, " + name + "! You are now logged in.");

            // Input loop to continuously read commands from the player
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                inputLine = inputLine.trim();
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
        // Hash the password using BCrypt
        // String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        // Store the username and hashed password in the database
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
