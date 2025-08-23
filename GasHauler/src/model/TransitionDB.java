package model;

import controller.GameException;
import controller.Transition;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: TransitionDB
 * This class handles all the database access for the Transition class.
 */
public class TransitionDB {
    private String dbName;

    public TransitionDB(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Retrieves the destination room ID based on commandID and roomID.
     *
     * @param commandID - ID of the command to match
     * @param roomID - ID of the current room
     * @return the destination room ID if found, otherwise -1
     * @throws GameException if there's an error accessing the database
     */
    public int getDestinationRoom(int commandID, int roomID) throws GameException {
        int destinationID = -1;
        String sql = "SELECT destinationID FROM Transition WHERE commandID = ? AND roomID = ?";

        try (SQLiteDB sdb = new SQLiteDB(dbName)) {
            ResultSet rs = sdb.queryDB(sql, String.valueOf(commandID), String.valueOf(roomID));

            if (rs.next()) {
                destinationID = rs.getInt("destinationID");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Unable to retrieve transition for command " + commandID + " in room " + roomID + ".\n" + e.getMessage());
        }
        return destinationID;
    }

    /**
     * Retrieves all transitions in the database as a list.
     *
     * @return a List of all transitions
     * @throws GameException if there's an error accessing the database
     */
    public List<Transition> getAllTransitions() throws GameException {
        List<Transition> transitions = new ArrayList<>();
        String sql = "SELECT * FROM Transition";

        try (SQLiteDB sdb = new SQLiteDB(dbName)) {
            ResultSet rs = sdb.queryDB(sql);

            while (rs.next()) {
                Transition transition = new Transition(
                        rs.getInt("transitionID"),
                        rs.getInt("commandID"),
                        rs.getInt("roomID"),
                        rs.getInt("destinationID")
                );
                transitions.add(transition);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Unable to retrieve all transitions.\n" + e.getMessage());
        }
        return transitions;
    }
}
