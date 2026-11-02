package se.mud.Other;

public class Player {

    private String name;
    private int experience;
    private int strength;
    private int dexterity;
    private int intelligence;
    private int room_id;

    public Player(String name, int experience, int strength, int dexterity, int intelligence, int room_id) {
        this.name = name;
        this.experience = experience;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.room_id = room_id;
    }

    public String getName() {
        return name;
    }

    public int getExperience() {
        return experience;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getLevel() {
        return experience / 100; // Better calculation later, for now, 100 exp = 1 level
    }

    public int getRoomId() {
        return room_id;
    }

    
}
