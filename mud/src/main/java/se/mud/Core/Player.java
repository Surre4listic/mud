package se.mud.core;

import java.io.*;
import java.net.*;

public class Player implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String name;
    private int roomID = 0;
    private CommandParser cmd = new CommandParser(this);

    public Player(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Login Phase
            out.println("Welcome! Enter your character name:");
            this.name = in.readLine();
            out.println("Welcome, " + name + "!");
            
            // broadcast connection to anyone in same room
            Broadcast.room(roomID, name + " materializes out of thin air.", this);

            // loop
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equalsIgnoreCase("quit")) {
                    break;
                }
                cmd.Parse(inputLine);

            }
        } catch (IOException e) {
            System.out.println(name + " disconnected unexpectedly.");
        } finally {
            cleanup();
        }
    }

    // print text to this player
    public void sendText(String text) {
        if (out != null) out.println(text);
    }

    public int getRoomID() { return roomID; }

    private void cleanup() {
        Broadcast.removePlayer(this);
        Broadcast.room(roomID, name + " has left the realm.", this);
        try { socket.close(); } catch (IOException e) {}
    }
}