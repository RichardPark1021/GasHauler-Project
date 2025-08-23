package controller;

import model.GameDBCreate;

import java.sql.SQLException;

/**
 * Class : CreateFilesController.java
 * @author: Richard Park
 * @version: 1.0
 * Course: ITEC 3860, Fall 2024
 * Written: October 31, 2024
 * This class calls into the model to build the database if it doesn't exist.
 */
public class CreateFilesController {

    /**
     * Method: createFile
     * Purpose: Create the database for GameDB using default settings.
     * @throws GameException if an error occurs while creating the database.
     */
    public void createFile() throws GameException, SQLException, ClassNotFoundException {
        createFile("default_game_db");
    }

    /**
     * Method: createFile
     * Purpose: Create the database for GameDB with a specified name.
     * @param dbName the name of the database to create.
     * @throws GameException if an error occurs while creating the database.
     */
    public void createFile(String dbName) throws GameException, SQLException, ClassNotFoundException {
        if (dbName == null || dbName.trim().isEmpty()) {
            throw new GameException("Database name cannot be null or empty.");
        }

        GameDBCreate sdb = new GameDBCreate(dbName);
        try {
            sdb.buildTables();
            System.out.println("Database created successfully: " + dbName);
        } catch (GameException e) {
            throw new GameException("Error creating database: " + e.getMessage(), e);
        }
    }
}