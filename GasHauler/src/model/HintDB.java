package model;
import controller.Marker;
import controller.Hints;
import controller.GameException;

import javax.sql.rowset.serial.SerialException;
import java.sql.ResultSet;
import java.sql.SQLException;
public class HintDB {
    private String dbName;

    public HintDB(String dbName) {
        this.dbName = dbName;
    }

    public int getNextHintID() throws GameException {
        int next = 0;
        try {
            SQLiteDB sdb = null;
            try {
                sdb = new SQLiteDB(dbName);
                next = sdb.getMaxValue("hintID", "Hints") + 1;
            }
            catch(ClassNotFoundException | SerialException e) {
                throw new GameException("Unable to get next Hint ID.");
            }
            sdb.close();
        }
        catch(SQLException ex) {
            throw new GameException("Next hint ID not found.");
        }
        return next;
    }

    /**
     * Method: getHint
     * Purpose: Retrieves a hint from the database by markerID
     * @param id
     * @return Hints
     * @throws GameException
     */
    public Hints getHint(int id) throws GameException {
        Hints hint;
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            hint = new Hints(dbName);
            String sql = "SELECT * FROM Hints WHERE hintID = " + id;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                hint.setHintID(rs.getInt("hintID"));
                hint.setDescription(rs.getString("description"));
            } else {
                throw new SQLException("Hint with ID " + id + " not found.");
            }
            sdb.close();
        } catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Hint with ID " + id + " not found.");
        }
        return hint;
    }

    /**
     * Method: updateHint
     * Purpose: Updates an existing hint record in the database
     * @param hint
     * @throws GameException
     */
    public void updateHint(Hints hint) throws GameException {
        String sql = "UPDATE Hints SET " +
                "description = '" + hint.getDescription() + "'" +
                " WHERE markerID = "; //+ hint.getMarkerID();
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            sdb.updateDB(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
}
