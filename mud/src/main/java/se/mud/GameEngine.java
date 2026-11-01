/*
This component tracks global game state (like active players) and acts as the central coordinator.
*/

package se.mud;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import se.mud.core.Utl;

public class GameEngine {
    private static final Set<Player> activePlayers = Collections.synchronizedSet(new HashSet<>());

    public static void registerPlayer(Player player) {
        activePlayers.add(player);
        broadcast("* " + player.getName() + " has entered the world. *", player);
        Utl.debug(player.getName() + " joined the game.");
    }

    public static void removePlayer(Player player) {
        if (activePlayers.remove(player)) {
            broadcast("* " + player.getName() + " has left the world. *", player);
            Utl.debug(player.getName() + " left the game.");
        }
    }

    public static void broadcast(String message, Player excludePlayer) {
        synchronized (activePlayers) {
            for (Player player : activePlayers) {
                if (player != excludePlayer) {
                    player.sendMessage(message);
                }
            }
        }
    }
}
