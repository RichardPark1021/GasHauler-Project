package controller;

import model.RoomDB;

/**
 * Class : Room
 * @author: Richard Park
 * @version: 1.0
 * Course: ITEC 3860 Fall 2024
 * Written: November 4, 2024
 * Purpose: This class is the Room class handling business logic for the Room class
 */
public class Room {
    private int roomID;
    private String name;
    private String floor;
    private String description;
    private String secondDescription;
    private String thirdDescription;
    private int visited;
    private int hintID;

    /**
     * Method: Room
     * Purpose: Constructor
     * @throws GameException
     */
    public Room(String dbName) throws GameException {
        RoomDB rdb = new RoomDB(dbName);
        roomID = rdb.getNextRoomID();
    }

    /**
     * Method: getRoom
     * Purpose: Gets a room from the Room table
     * @param id
     * @return Player
     * @throws GameException
     */
    public Room getRoom(String dbName, int id) throws GameException {
        RoomDB rdb = new RoomDB(dbName);
        return rdb.getRoom(id);
    }

    /**
     * Method: getRoomID
     * Purpose: gets roomID
     * @return int
     */
    public int getRoomID() {
        return roomID;
    }

    /**
     * Method: setRoomID
     * Purpose: sets roomID
     * @param roomID
     */
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    /**
     * Method: getName
     * Purpose: gets name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Method: setName
     * Purpose: sets name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method: getFloor
     * Purpose: gets floor
     * @return String
     */
    public String getFloor() {
        return floor;
    }

    /**
     * Method: setFloor
     * Purpose: sets floor
     * @param floor
     */
    public void setFloor(String floor) {
        this.floor = floor;
    }

    /**
     * Method: getDescription
     * Purpose: gets description
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method: setDescription
     * Purpose: sets Description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method: getSecondDescription
     * Purpose: gets secondDescription
     * @return String
     */
    public String getSecondDescription() {
        return secondDescription;
    }

    /**
     * Method: setSecondDescription
     * Purpose: sets secondDescription
     * @param secondDescription
     */
    public void setSecondDescription(String secondDescription) {
        this.secondDescription = secondDescription;
    }

    /**
     * Method: getThirdDescription
     * Purpose: gets thirdDescription
     * @return
     */
    public String getThirdDescription() {
        return thirdDescription;
    }

    /**
     * Method: setThirdDescription
     * Purpose: sets thirdDescription
     * @param thirdDescription
     */
    public void setThirdDescription(String thirdDescription) {
        this.thirdDescription = thirdDescription;
    }

    /**
     * Method: getVisited
     * Purpose: gets visited
     * @return int
     */
    public int getVisited() {
        return visited;
    }

    /**
     * Method: setVisited
     * Purpose: sets visited
     * @param visited
     */
    public void setVisited(int visited) {
        this.visited = visited;
    }

    /**
     * Method: getHintID
     * Purpose: gets hintID
     * @return int
     */
    public int getHintID() {
        return hintID;
    }

    /**
     * Method: setHintID
     * Purpose: sets hintID
     * @param hintID
     */
    public void setHintID(int hintID) {
        this.hintID = hintID;
    }



    /**
     * Method: toString
     * @return String
     */
    @Override
    public String toString() {
        return "Room{" +
                "roomID=" + roomID +
                ", name='" + name + '\'' +
                ", floor='" + floor + '\'' +
                ", description='" + description + '\'' +
                ", secondDescription='" + secondDescription + '\'' +
                ", visited=" + visited +
                ", hintID=" + hintID +
                '}';
    }
}
