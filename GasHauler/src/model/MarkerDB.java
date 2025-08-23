package model;

import controller.Marker;
import controller.GameException;

import javax.sql.rowset.serial.SerialException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class: MarkerDB
 * @author Sanaa Boddie
 * @version 1.0
 * Course: ITEC 3860 FALL 2024
 * Written: November 5, 2024
 *
 * This class handles all the database access for the Marker class
 */
public class MarkerDB {
    private String dbName;

    public MarkerDB(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Method: getNextMarkerID
     * Purpose: Gets the next marker ID based on a provided ID (optional overload method)
     *
     * @return int
     */
    public int getNextMarkerID() {
        int next = 0;
        try {
            SQLiteDB sdb = null;
            try {
                sdb = new SQLiteDB(dbName);
                next = sdb.getMaxValue("markerID", "Marker") + 1;
            } catch (ClassNotFoundException | SerialException e) {
                throw new GameException("Unable to get next Marker ID.");
            }
            sdb.close();
        } catch (SQLException | GameException ex) {
            try {
                throw new GameException("Next marker ID not found.");
            } catch (GameException e) {
                throw new RuntimeException(e);
            }
        }
        return next;
    }

    /**
     * Method: getMarker
     * Purpose: Retrieves a marker from the database by markerName
     * @param markerName
     * @return Marker
     * @throws GameException
     */
    public Marker getMarker(String markerName) throws GameException {
        Marker marker;
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            marker = new Marker(dbName);
            String sql = "SELECT * FROM Marker WHERE name = '" + markerName + "'";
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                marker.setMarkerID(rs.getInt("markerID"));
                marker.setName(rs.getString("name"));
                marker.setActive(rs.getInt("active"));
            } else {
                throw new SQLException("Marker " + markerName + " not found.");
            }
            sdb.close();
        } catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Marker with name " + markerName + " not found.");
        }
        return marker;
    }

    /**
     * Method: updateMarker
     * Purpose: Updates an existing marker record in the database
     * @param markerName
     * @throws GameException
     */
    public void updateMarker(String markerName) throws GameException {
        String sql = "UPDATE Marker SET " +
                "active = 1 WHERE name = '" + markerName + "'";
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            sdb.updateDB(sql);
            sdb.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    public void markerOff(String markerName) throws GameException {
        String sql = "UPDATE Marker SET " +
                "active = 0 WHERE name = '" + markerName + "'";
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            sdb.updateDB(sql);
            sdb.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }


    /**
     * Method: getMarkerByID
     * Purpose: Retrieves a marker from the database by markerID
     * @param markerID
     * @return Marker
     * @throws GameException
     */
    public Marker getMarkerByID(int markerID) throws GameException {
        Marker marker = null;
        String sql = "SELECT * FROM Marker WHERE markerID = ?";
        try (SQLiteDB sdb = new SQLiteDB(dbName);
             PreparedStatement stmt = sdb.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, markerID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    marker = new Marker(dbName);
                    marker.setMarkerID(rs.getInt("markerID"));
                    marker.setName(rs.getString("name"));
                    marker.setActive(rs.getInt("active"));
                } else {
                    throw new GameException("Marker with ID '" + markerID + "' not found.");
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Error retrieving marker with ID '" + markerID + "': " + ex.getMessage(), ex);
        }
        return marker;
    }
}
