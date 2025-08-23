package controller;

import model.EventDB;

/**
 * Class: Event
 * @author: Erika Attidzah
 * @version: 1.0
 * Course: ITEC 3860 FALL 2024
 * Written: 2 Nov, 2024
 *
 * This class creates an Event Object
 */

public class Event {
    private int eventID;
    private int commandID;
    private String description;

    public Event(int eventID, int commandID, String description) {
        this.eventID = eventID;
        this.commandID = commandID;
        this.description = description;
    }

    public int getEventID() {
        return eventID;
    }

    public int getCommandID() {
        return commandID;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString(){
        return getDescription();
    }
}
