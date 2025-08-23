package controller;
/**
 * Class: Transition
 * This class represents a transition between rooms in the game, identified by a transition ID,
 * a command ID, a room ID, and a destination ID.
 */
public class Transition {
    private int transitionID;
    private int commandID;
    private int roomID;
    private int destinationID;

    public Transition(int transitionID, int commandID, int roomID, int destinationID) {
        this.transitionID = transitionID;
        this.commandID = commandID;
        this.roomID = roomID;
        this.destinationID = destinationID;
    }

    public int getTransitionID() {
        return transitionID;
    }

    public void setTransitionID(int transitionID) {
        this.transitionID = transitionID;
    }

    public int getCommandID() {
        return commandID;
    }

    public void setCommandID(int commandID) {
        this.commandID = commandID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(int destinationID) {
        this.destinationID = destinationID;
    }
}
