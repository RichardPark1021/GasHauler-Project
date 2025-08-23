package controller;

import controller.Marker;
import model.MarkerDB;
import model.HintDB;

/**
 * Class: Hints
 * Author: Sanaa Boddie
 * Version: 1.1
 * Course: ITEC 3860 FALL 2024
 * Written: November 6, 2024
 *
 * This class is the Hint class handling business logic for the Hint class
 */

public class Hints  {
    private int hintID;
    private String description;

    /**
     * Method: Hints
     * Purpose: Constructor to initialize Hint with a unique ID
     *
     * @throws GameException
     */
    public Hints(String dbName) throws GameException {
        HintDB hdb = new HintDB(dbName);
        hintID = hdb.getNextHintID();
    }

    public Hints getHint(String dbName, int id) throws GameException {
        HintDB hdb = new HintDB(dbName);
        return hdb.getHint(id);
    }

    public int getHintID() {
        return hintID;
    }

    public void setHintID(int hintID) {
        this.hintID = hintID;
    }

    /**
     * Method: getDescription
     * Purpose: Gets the description of the hint
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method: setDescription
     * Purpose: Sets the description of the hint
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method: toString
     * Purpose: Provides a string representation of the Hints object
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Hints{" +
                "hintID=" + hintID +
                ", description='" + description + '\'' +
                '}';
    }
}
