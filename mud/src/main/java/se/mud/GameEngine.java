/*
This component tracks global game state (like active players) and acts as the central coordinator.
*/

package se.mud;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import se.mud.Other.Utl;

public class GameEngine {
    private static final Set<ClientConnections> activePlayers = Collections.synchronizedSet(new HashSet<>());

    public static void registerPlayer(ClientConnections player) {
        activePlayers.add(player);
        broadcast("* " + player.getName() + " has entered the world. *", player);
        Utl.debug(player.getName() + " joined the game.");
    }

    public static void removePlayer(ClientConnections player) {
        if (activePlayers.remove(player)) {
            broadcast("* " + player.getName() + " has left the world. *", player);
            Utl.debug(player.getName() + " left the game.");
        }
    }

    public static void broadcast(String message, ClientConnections excludePlayer) {
        synchronized (activePlayers) {
            for (ClientConnections player : activePlayers) {
                if (player != excludePlayer) {
                    player.sendMessage(message);
                }
            }
        }
    }
}
