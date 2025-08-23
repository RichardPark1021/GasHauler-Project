package controller;

import java.io.File;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Class: GameController
 * @author Richard Park
 * @version: 1.0
 * Course: ITEC 3860, Fall 2024
 * Written: October 31, 2024
 * This class is the main controller for the program
 */
public class GameController {
    private Scanner sc;

    /**
     * Method: start
     * Purpose: Retrieves the database for a player. If the database doesn't exist, create it.
     * @param playerName The name of the player to create or access their database.
     * @throws GameException
     */
    public void start(String playerName) throws GameException, SQLException, ClassNotFoundException {
        String dbName = playerName + ".db";
        File dbFile = new File("src/" + dbName);

        if (!dbFile.exists()) {
            CreateFilesController cfc = new CreateFilesController();
            cfc.createFile(dbName);
        }
        // Initialize the game with the player's database.
        initializeGameDatabase(dbName);
    }

    /**
     * Method: initializeGameDatabase
     * Purpose: Initialize the game using the player's specific database.
     * @param dbName The name of the player's database.
     * @throws GameException
     */
    private void initializeGameDatabase(String dbName) throws GameException {
        System.out.println("Database " + dbName + " initialized for the player.");

    }
}