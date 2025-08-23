package model;

import controller.Event;
import controller.GameException;

import javax.sql.rowset.serial.SerialException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class: EventDB
 * @author: Erika Attidzah
 * @version: 1.0
 * Course: ITEC 3860 FALL 2024
 * Written: 2 Nov, 2024
 *
 * This class is responsible for managing events in relation to commands
 */

public class EventDB {
    SQLiteDB sdb;
    private String dbName;

    public EventDB(String dbName) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
    }

    /**
     * Method: getEvent
     * Purpose: Retrieves the corresponding Event for the commandID
     * @param commandID, the ID of the command
     * @return an Event object
     * @throws SQLException
     */
    public Event getEvent(int commandID) throws SQLException {
        String sql = "SELECT eventID, commandID, description FROM Event WHERE commandID = ?";
        try(PreparedStatement statement = sdb.getConnection().prepareStatement(sql)){
            statement.setInt(1, commandID);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                int eventID = resultSet.getInt("eventID");
                String description = resultSet.getString("description");
                Event event = new Event(eventID, commandID, description);
                return event;
            } else{
                return null;
            }
        }
    }
}
