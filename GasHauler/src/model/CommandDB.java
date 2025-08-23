package model;

import controller.Command;
import controller.GameException;

import javax.sql.rowset.serial.SerialException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class: CommandDB
 * @author: Erika Attidzah
 * @version: 1.0
 * Course: ITEC 3860 FALL 2024
 * Written: 2 Nov, 2024
 *
 * This class is responsible for managing commands in relation to rooms
 */
public class CommandDB {
    SQLiteDB sdb;
    private Map<Integer, Command> roomCommands;
    private String dbName;

    public CommandDB(String dbName) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        roomCommands = new HashMap<>();
    }

    /**
     * Method: getAllCommands
     * Purpose: Retrieves the commands that are unlocked and associated with roomID from the database
     * @param roomID, the ID of the room the commands are in
     * @return a map containing commandID as keys, and the corresponding Command Object as values
     * @throws SQLException
     */
    public Map<Integer, Command> getAllCommands(int roomID){
        roomCommands.clear(); // clear previous commands for the room
        String sql = "SELECT * FROM Command WHERE roomID = ? AND unlocked = 1";
        try (PreparedStatement statement = sdb.getConnection().prepareStatement(sql)){
            statement.setInt(1, roomID); // link the roomID to the query
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                int commandID = resultSet.getInt("commandID");
                int defaultID = resultSet.getInt("defaultID");
                String name = resultSet.getString("name");
                boolean unlocked = resultSet.getBoolean("unlocked");
                Command command = new Command(commandID, roomID, defaultID, name, unlocked);
                roomCommands.put(commandID, command);
            }
        } catch(SQLException e){
            System.err.println("Error taking commands" + e.getMessage());

        }

        return roomCommands;
    }

    /**
     * Method: update
     * Purpose: update the Command table to unlock a command
     * @param commandID, the ID of the command that needs to be unlocked
     * @throws SQLException
     */
    public void update(Connection connection, int commandID) throws SQLException {
        String sql = "SELECT unlocked FROM Command WHERE commandID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, commandID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boolean unlocked = resultSet.getBoolean("unlocked");
                if (!unlocked) {
                    String update = "UPDATE Command SET unlocked = 1 WHERE commandID = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(update)) {
                        updateStatement.setInt(1, commandID);
                        updateStatement.executeUpdate();
                    }catch(SQLException s){
                        System.out.println("BUSYYYY");
                    }
                }
            }
        }
    }

    /**
     * Method: unlockOneCommand
     * Purpose: update one command to unlock it
     * @param commandID, the ID of the command that needs to be unlocked
     * @throws SQLException
     */
    public void unlockOneCommand(int commandID) throws SQLException{
        String sql = "SELECT unlocked FROM Command WHERE commandID = ?";
        try (PreparedStatement statement = sdb.getConnection().prepareStatement(sql)) {
            statement.setInt(1, commandID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boolean unlocked = resultSet.getBoolean("unlocked");
                if (!unlocked) {
                    String update = "UPDATE Command SET unlocked = 1 WHERE commandID = ?";
                    try (PreparedStatement updateStatement = sdb.getConnection().prepareStatement(update)) {
                        updateStatement.setInt(1, commandID);
                        updateStatement.executeUpdate();
                    }catch(SQLException s){
                        System.out.println("busy");
                    }
                }
            }
        }
    }

    /**
     * Method: lockCommand
     * Purpose: update one command to lock it
     * @param commandID, the ID of the command that needs to be unlocked
     * @param connection, one connection to not have a busy program
     * @throws SQLException
     */
    public void lockCommand(Connection connection, int commandID) throws SQLException {
        String sql = "SELECT unlocked FROM Command WHERE commandID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, commandID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boolean unlocked = resultSet.getBoolean("unlocked");
                if (unlocked) {
                    // Lock the command by setting unlocked to 0
                    String update = "UPDATE Command SET unlocked = 0 WHERE commandID = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(update)) {
                        updateStatement.setInt(1, commandID);
                        updateStatement.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println("Failed to lock the command.");
                    }
                }
            }
        }
    }

    /**
     * Method: lockOneCommand
     * Purpose: update one command to lock it
     * @param commandID, the ID of the command that needs to be locked
     * @throws SQLException
     */
    public void lockOneCommand(int commandID) throws SQLException {
        String sql = "SELECT unlocked FROM Command WHERE commandID = ?";
        try (PreparedStatement statement = sdb.getConnection().prepareStatement(sql)) {
            statement.setInt(1, commandID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boolean unlocked = resultSet.getBoolean("unlocked");
                if (!unlocked) {
                    String update = "UPDATE Command SET unlocked = 0 WHERE commandID = ?";
                    try (PreparedStatement updateStatement = sdb.getConnection().prepareStatement(update)) {
                        updateStatement.setInt(1, commandID);
                        updateStatement.executeUpdate();
                    }catch(SQLException s){
                        System.out.println("busy");
                    }
                }
            }
        }
    }


}
