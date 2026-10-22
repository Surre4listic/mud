package se.mud.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {
    private static final int PORT = 4000;
    // list of active players
    private static CopyOnWriteArrayList<Player> activePlayers = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        
        System.out.println("test1");
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            Player player = new Player(clientSocket);
            activePlayers.add(player);
            new Thread(player).start();
        }
    }

    // broadcast text only to same room
    public static void room(int roomID, String text, Player sender) {
        for (Player player : activePlayers) {
            if (player.getRoomID() == roomID && player != sender) {
                player.sendText(text);
            }
        }
    }

    // broadcast text to whole world
    public static void world(String text, Player sender) {
        for (Player player : activePlayers) {
            if (player != sender) {
                player.sendText(text);
            }
        }
    }

    // remove player from active players
    public static void removePlayer(Player player) {
        activePlayers.remove(player);
    }
}