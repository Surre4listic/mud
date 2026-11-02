package se.mud.Other;

public class Player {

    private String name;
    private int hp;
    private int level;
    private int roomId;

    public Player(String name, int hp, int level, int roomId) {
        this.name = name;
        this.hp = hp;
        this.level = level;
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getLevel() {
        return level;
    }

    public int getRoomId() {
        return roomId;
    }

    
}
