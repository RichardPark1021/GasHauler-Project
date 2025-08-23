package model;

import controller.GameException;
import controller.Sequence;

import javax.sql.rowset.serial.SerialException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class: SequenceDB
 * @author: Erika Attidzah
 * @version: 1.0
 * Course: ITEC 3860 FALL 2024
 * Written: 2 Nov, 2024
 *
 * This class manages the interaction with the Sequence table in the database to determine
 * if a command has any associated sequences that need to be unlocked.
 */

public class SequenceDB {
    SQLiteDB sdb;
    CommandDB commandDB;
    private String dbName;
    public SequenceDB(String dbName) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        commandDB = new CommandDB(dbName);
    }

    /**
     * Method: checkAndUpdates
     * Purpose: check if there is any sequence for the following command, if there is, it unlocks it
     * @param commandID, the ID of the command that has a sequence
     * @throws SQLException
     */
    public void checkAndUpdate(int commandID) throws SQLException {
        String sql = "SELECT unlockCommand FROM Sequence WHERE commandID = ?";

        try(PreparedStatement statement = sdb.conn.prepareStatement(sql)){
            statement.setInt(1, commandID); //Link commandID to the query
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count > 0){
                    int unlockCommand = resultSet.getInt("unlockCommand");
                    commandDB.update(sdb.conn, unlockCommand);
                }
            }
        }
    }

    /**
     * Method: checkAndLock
     * Purpose: check if there is any sequence for the following command, if there is, it unlocks it
     * @param commandID, the ID of the command that has a sequence
     * @throws SQLException
     */
    public void checkAndLock(int commandID) throws SQLException {
        String sql = "SELECT unlockCommand FROM Sequence WHERE commandID = ?";

        try (PreparedStatement statement = sdb.conn.prepareStatement(sql)) {
            statement.setInt(1, commandID); // Link commandID to the query
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int unlockCommand = resultSet.getInt("unlockCommand");
                // Lock the command in the sequence
                commandDB.lockCommand(sdb.conn, unlockCommand);
                // Recursively lock any commands unlocked by this command
                checkAndLock(unlockCommand);
            }
        }
    }
}
