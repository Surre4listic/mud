package se.mud.core;

public class CommandMethods {

    Player player;

    public CommandMethods(Player player) {
        this.player = player;
        System.out.println("test");
    }

    @CommandAnnontation (commands={"help"}, desc="help")
    public String help(String input) {
        Utl.debug("help recieved: " + input + ".");
        return "";
    }

    
    @CommandAnnontation (commands={"move", "run"}, desc="move")
    public String move(String input) {
        Utl.debug("Move recieved: " + input + ".");
        return "";
    }

    
    @CommandAnnontation (commands={"say", "speak"}, desc="Speak to anyone in the room.")
    public String speach(String input) {
        Utl.debug("speach recieved recieved: " + input + ".");
        if (input != null && !input.isBlank()) {
            Broadcast.room(player.getRoomID(), input, player);
        } else {
            
        }
        return "";
    }
    
}
