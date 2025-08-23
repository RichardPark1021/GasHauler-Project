package controller;

import model.MarkerDB;
import model.PlayerDB;

/**
 * Class : Marker
 * @author: Sanaa Boddie
 * @version: 1.0
 * Course: ITEC 3860 Fall 2024
 * Written: November 5, 2024
 * Purpose: This class is the Marker class handling business logic for the Marker class
 */

public class Marker {
    public int markerID;
    private int active;
    private String name;

    /**
     * Method: Marker
     * Purpose: Constructor to initialize Marker with a unique ID
     * @throws GameException
     */
    public Marker(String dbName) throws GameException {
        MarkerDB mdb = new MarkerDB(dbName);
        markerID = mdb.getNextMarkerID();
    }

    /**
     * Method: getMarker
     * Purpose: Gets a marker from the marker table
     *
     * @param id
     * @return Marker
     * @throws GameException
     */
    public Marker getMarker(String dbName, String markerName) throws GameException {
        MarkerDB mdb = new MarkerDB(dbName);
        return mdb.getMarker(markerName);
    }

    /**
     * Method: getMarkerID
     * Purpose: Gets the marker ID
     * @return markerID
     */
    public int getMarkerID() {
        return markerID;
    }

    /**
     * Method: setMarkerID
     * Purpose: Sets the marker ID
     * @param markerID
     */
    public void setMarkerID(int markerID) {
        this.markerID = markerID;
    }

    /**
     * Method: isActive
     * Purpose: Gets the active status of the marker
     * @return active
     */
    public int getActive() {
        return active;
    }

    /**
     * Method: setActive
     * Purpose: Sets the active status of the marker
     * @param active
     */
    public void setActive(int active) {
        this.active = active;
    }

    /**
     * Method: getName
     * Purpose: Gets the name of the marker
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Method: setName
     * Purpose: Sets the name of the marker
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

//    public void updateMarker(String dbName) throws GameException {
//        MarkerDB mdb = new MarkerDB(dbName);
//        mdb.updateMarker(this);
//    }

    /**
     * Method: toString
     * Purpose: Provides a string representation of the Marker object
     * @return String
     */
    @Override
    public String toString() {
        return "Marker{" +
                "markerID=" + markerID +
                ", active=" + active +
                ", name='" + name + '\'' +
                '}';
    }
}
