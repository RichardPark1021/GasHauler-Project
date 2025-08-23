package model;

import controller.GameException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Class : GameDBCreate
 * @author: Richard Park, David Flores
 * @version: 1.1
 * Course: ITEC 3860 Fall 2024
 * Written: October 31, 2024
 * This class creates the Game db if it doesn't exist
 * Purpose: Creates the DB for GasHauler
 */
public class GameDBCreate {
    private final DB sdb; // Using the updated DB class
    private final String dbName;
    public GameDBCreate(String dbName) throws SQLException, ClassNotFoundException {
        this.dbName = dbName;
        this.sdb = new DB(dbName);
    }
    /**
     * Method: buildTables
     * Purpose: Build all tables
     *
     * @throws GameException
     */
    public void buildTables() throws GameException {
        buildPlayer();
        buildRoom();
        buildSequence();
        buildCommand();
        buildEvent();
        buildDefaultCommand();
        buildTransitions();
        buildAchievement();
        buildHints();
        buildItems();
        buildMarker();
        buildInvMgmnt();
        buildRoomInv();
        buildPlayerInv();
        buildUseItems();
    }
    /**
     * Method: buildPlayer
     * Purpose: Build the player table
     * @throws GameException
     */
    public void buildPlayer() throws GameException {
        String sql = "CREATE TABLE IF NOT EXISTS Player (" +
                "playerID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " + // Ensure unique usernames
                "password TEXT NOT NULL, " +
                "securityQuestion TEXT NOT NULL, " +
                "securityAnswer TEXT NOT NULL, " +
                "currentRoom INTEGER NOT NULL, " +
                "inventoryCapacity INTEGER NOT NULL, " +
                "score INTEGER NOT NULL, " +
                "inventoryID INTEGER NOT NULL, " +
                "roomID INTEGER NOT NULL)";
        try {
            sdb.updateDB(sql); // Use the updateDB method from the DB class
        } catch (SQLException ex) {
            throw new GameException("Error creating table Player: " + ex.getMessage());
        }
    }
    /**
     * Method: buildRoom
     * Purpose: Build the Room table and load data
     *
     * @return void
     * @throws SQLException
     */
    public void buildRoom() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/Rooms.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Room");
        }
    }
    /**
     * Method: buildEvent
     * Purpose: Build the Event table and load data
     *
     * @return void
     * @throws SQLException
     */
    public void buildEvent() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/Event.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Event");
        }
    }
    /**
     * Method: buildRoom
     * Purpose: Build the Room table and load data
     *
     * @return void
     * @throws SQLException
     */
    public void buildCommand() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/Command.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Command");
        }
    }
    /**
     * Method: buildRoom
     * Purpose: Build the Room table and load data
     *
     * @return void
     * @throws SQLException
     */
    public void buildDefaultCommand() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/DefaultCommand.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Default Command");
        }
    }
    /**
     * Method: buildSequence
     * Purpose: Build the Sequence table and load data
     *
     * @return void
     * @throws SQLException
     */
    public void buildSequence() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/Sequence.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Sequence");
        }
    }
    /**
     * Method: buildAchievement
     * Purpose: Build the Achievement table and load data
     *
     * @return void
     * @throws SQLException
     */
    public void buildAchievement() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/Achievement.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Achievement");
        }
    }
    /**
     * Method: buildMarker
     * Purpose: Build the Marker table and load data
     *
     * @return void
     * @throws SQLException
     */
    public void buildMarker() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/Marker.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Marker");
        }
    }
    /**
     * Method: buildHints
     * Purpose: Build the Hints table and load data
     *
     * @return void
     * @throws SQLException
     */
    public void buildHints() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/Hints.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Hints");
        }
    }
    /**
     * Method: buildItems
     * Purpose: Build the Items table and load data
     *
     * @return void
     * @throws SQLException
     */
    public void buildItems() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/Items.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Items");
        }
    }

    public void buildTransitions() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/transitions.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Transition");
        }
    }

    public void buildInvMgmnt() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/InvMgmnt.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Inventory Manager");
        }
    }

    public void buildRoomInv() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/RoomInv.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Room Inventory");
        }
    }

    public void buildPlayerInv() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/PlayerInv.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table Player Inventory");
        }
    }

    public void buildUseItems() throws GameException {
        try {
            FileReader fr;
            try {
                fr = new FileReader("src/useItem.txt");
                Scanner inFile = new Scanner(fr);
                while (inFile.hasNextLine()) {
                    String sql = inFile.nextLine();
                    if(!sql.isEmpty()) {
                        sdb.updateDB(sql);
                    }
                }
                inFile.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            throw new GameException("Error creating table use Items");
        }
    }
    /**
     * Method: insertPlayer
     * Purpose: Insert new player record to the player table
     * @param username
     * @param password
     * @param securityQuestion
     * @param securityAnswer
     * @throws GameException
     */
    public void insertPlayer(String username, String password, String securityQuestion, String securityAnswer) throws GameException {
        String sql = "INSERT INTO Player(username, password, securityQuestion, securityAnswer, currentRoom, inventoryCapacity, score, inventoryID, roomID) " +
                "VALUES(?, ?, ?, ?, 1, 3, 0, 1, 1)";
        try {
            sdb.updateDB(sql, username, password, securityQuestion, securityAnswer); // Pass parameters safely
        } catch (SQLException ex) {
            throw new GameException("Error inserting player: " + ex.getMessage());
        }
    }
    /**
     * Method: close
     * Purpose: Close the database connection
     * @throws SQLException
     */
    public void close() throws SQLException {
        if (sdb != null) {
            sdb.close();
        }
    }
}