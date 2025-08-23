package model;

import controller.GameException;
import controller.Room;

import javax.sql.rowset.serial.SerialException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  Class: RoomDB
 * @author Richard Park
 * Version 1.0
 * Course: ITEC 3860, Fall 2024
 * Written: November 4, 2024
 * This class handles all database access for the Room class
 */
public class RoomDB {
    private String dbName;

    public RoomDB(String dbName) {
        this.dbName = dbName;
    }
    /**
     * Method: getNextRoomID
     * Purpose: Gets the id for the next room
     * @return int
     */
    public int getNextRoomID() throws GameException {
        int next = 0;
        try {
            SQLiteDB sdb = null;
            try {
                sdb = new SQLiteDB(dbName);
                next = sdb.getMaxValue("roomID", "Room") + 1;
            }
            catch(ClassNotFoundException | SerialException e) {
                throw new GameException("Unable to get next Room ID.");
            }
            sdb.close();
        }
        catch(SQLException ex) {
            throw new GameException("Next room ID not found.");
        }
        return next;
    }

    /**
     * Method: getRoom
     * Purpose: handles db interactions to retrieve a single Room
     * @param id
     * @return Room
     * @throws GameException
     */
    public Room getRoom(int id) throws GameException {
        Room room;
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            room = new Room(dbName);
            String sql = "SELECT * FROM Room WHERE roomID = " + id;
            ResultSet rs = sdb.queryDB(sql);
            if(rs.next()) {
                room.setRoomID(rs.getInt("roomID"));
                room.setName(rs.getString("name"));
                room.setFloor(rs.getString("floor"));
                room.setDescription(rs.getString("description"));
                room.setSecondDescription(rs.getString("secondDescription"));
                room.setThirdDescription(rs.getString("thirdDescription"));
                room.setVisited(rs.getInt("visited"));
                room.setHintID(rs.getInt("hintID"));
            }
            else {
                throw new SQLException("Room " + id + " not found.");
            }
            sdb.close();
        }
        catch(SQLException | ClassNotFoundException ex) {
            throw new GameException("Room with room number " + id + " not found.");
        }
        return room;
    }

    /**
     * Method: updateRoom
     * Purpose: This method updates room's visited
     * @param room
     * @throws GameException
     */
    public void updateRoom(Room room) throws GameException {
        String sql = "UPDATE Room SET visited = '" + room.getVisited() + "' WHERE roomID = " + room.getRoomID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
}
