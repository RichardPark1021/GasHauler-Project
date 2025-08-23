
package model;

import controller.GameException;
import controller.Player;


import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  Class : PlayerDB
 * @author Richard Park, David Flores
 * Version 1.2
 * Course: ITEC 3860, Fall 2024
 * Written: November 2, 2024
 * This class handles all database access for the Player class
 */
public class PlayerDB {

    private String dbName;

    public PlayerDB(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Method: getNextPlayerID
     * Purpose: Gets the id for the next player
     * @return int
     */
    public int getNextPlayerID() throws GameException {
        int next = 0;
        try {
            SQLiteDB sdb = null;
            try {
                sdb = new SQLiteDB(dbName);
                next = sdb.getMaxValue("playerID", "Player") + 1;
            }
            catch(ClassNotFoundException | SerialException e) {
                throw new GameException("Unable to get next Player ID.");
            }
            sdb.close();
        }
        catch(SQLException ex) {
            throw new GameException("Next player ID not found.");
        }
        return next;
    }

    /**
     * Method: getPlayer
     * Purpose: handles db interactions to retrieve a single Player
     * @param id
     * @return Player
     * @throws GameException
     */
    public Player getPlayer(int id) throws GameException {
        Player player;
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            player = new Player(dbName);
            String sql = "SELECT * FROM Player WHERE playerID = " + id;
            ResultSet rs = sdb.queryDB(sql);
            if(rs.next()) {
                player.setPlayerID(rs.getInt("playerID"));
                player.setUsername(rs.getString("username"));
                player.setPassword(rs.getString("password"));
                player.setSecurityQuestion(rs.getString("securityQuestion"));
                player.setSecurityAnswer(rs.getString("securityAnswer"));
                player.setScore(rs.getInt("score"));
                player.setCurrentRoom(rs.getInt("currentRoom"));
                player.setInventoryCapacity(rs.getInt("inventoryCapacity"));
                player.setInventoryID(rs.getInt("inventoryID"));
                player.setRoomID(rs.getInt("roomID"));
            }
            else {
                throw new SQLException("Player " + id + " not found.");
            }
            sdb.close();
        }
        catch(SQLException | ClassNotFoundException ex) {
            throw new GameException("Player with player number " + id + " not found.");
        }
        return player;
    }

    public void createPlayerDatabase(String playerName) throws GameException {
        String dbName = "src/" + playerName + ".db";
        File dbFile = new File(dbName);

        if (dbFile.exists()) {
            throw new GameException("Database already exists for player: " + playerName);
        }

        try (SQLiteDB sdb = new SQLiteDB(dbName)) {
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS Player (
                    playerID INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    securityQuestion TEXT,
                    securityAnswer TEXT
                );
            """;
            sdb.updateDB(createTableSQL);
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Error creating player database for " + playerName, e);
        }
    }
    /**
     public int getNextPlayerID() throws GameException {
     try (SQLiteDB sdb = new SQLiteDB()) {
     return sdb.getMaxValue("playerID", "Player") + 1;
     } catch (SQLException | ClassNotFoundException e) {
     throw new GameException("Unable to get next Player ID.", e);
     }
     }
     */
    public boolean playerAccountExists(String username, String password) throws GameException {
        try (SQLiteDB sdb = new SQLiteDB(dbName)) {
            String sql = "SELECT * FROM Player WHERE username = ? AND password = ?";
            ResultSet rs = sdb.queryDB(sql, username, password);
            boolean exists = rs.next();
            rs.close();
            return exists;
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Error checking player account in " + dbName, e);
        }
    }

    public String[] getPlayerRecoveryData(String username) throws GameException {
        try (SQLiteDB sdb = new SQLiteDB(dbName)) {
            String sql = "SELECT securityQuestion, securityAnswer, password FROM Player WHERE username = ?";
            ResultSet rs = sdb.queryDB(sql, username);
            if (rs.next()) {
                return new String[] {
                        rs.getString("securityQuestion"),
                        rs.getString("securityAnswer"),
                        rs.getString("password")
                };
            } else {
                throw new GameException("Username does not exist in " + dbName);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Error retrieving recovery data for " + username, e);
        }
    }

    public boolean checkExistingPlayer(String username) throws GameException {
        try (SQLiteDB sdb = new SQLiteDB()) {
            String sql = "SELECT * FROM Player WHERE username = ?";
            ResultSet rs = sdb.queryDB(sql, username);
            return !rs.next();
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("An error occurred while checking for username", e);
        }
    }

    /**
     * Method: updatePlayer
     * This method updates player's currentRoom
     */
    public void updatePlayer(Player player) throws GameException {
        String sql = "UPDATE Player SET currentRoom = '" + player.getCurrentRoom() + "' WHERE playerID = " + player.getPlayerID();
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
}