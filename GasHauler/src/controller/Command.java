package controller;

import model.CommandDB;

import java.util.ArrayList;

/**
 * Class: Command
 * @author: Erika Attidzah
 * @version: 1.0
 * Course: ITEC 3860 FALL 2024
 * Written: 2 Nov, 2024
 *
 * This class creates a Command Object
 */
public class Command {
    private int commandID;
    private int roomID;
    private int defaultID;
    private String name;
    private boolean unlocked;
    public Command(int commandID, int roomID, int defaultID, String name, boolean unlocked){
        this.commandID = commandID;
        this.roomID = roomID;
        this.defaultID = defaultID;
        this.name = name;
        this.unlocked = unlocked;
    }
    public int getCommandID() {
        return commandID;
    }
    public int getRoomID() {
        return roomID;
    }
    public int getDefaultID() {
        return defaultID;
    }
    public String getName() {
        return name;
    }
    public boolean isUnlocked() {
        return unlocked;
    }
    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    @Override
    public String toString() {
        return "CommandID " + commandID + " roomID " + roomID + " defaultID " + defaultID + " is unlocked " + isUnlocked();
    }
}
