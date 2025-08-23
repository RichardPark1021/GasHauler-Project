package model;

import controller.Achievement;
import controller.GameException;

import javax.sql.rowset.serial.SerialException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class: AchievementDB
 * @author Sanaa Boddie
 * @version 1.0
 * Course: ITEC 3860 FALL 2024
 * Written: November 4, 2024
 *
 * This class handles all the database access for the Achievement class
 */
public class AchievementDB {
    private String dbName;

    public AchievementDB(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Method: getNextAchievementID
     * Purpose: Gets the id for the next achievement
     * @return int
     */
    public int getNextAchievementID() throws GameException {
        int next = 0;
        try{
            SQLiteDB sdb = null;
            try{
                sdb = new SQLiteDB(dbName);
                next = sdb.getMaxValue("achievementID", "Achievement") + 1;
            }catch(ClassNotFoundException | SerialException e){
                throw new GameException("Unable to get the next achievement ID");
            }
            sdb.close();
        }catch(SQLException ex){
            throw new GameException("Unable to find achievement ID");
        }
        return next;
    }

    /**
     * Method: getAchievement
     * Purpose: handles db interactions to retrieve an achievement
     * @param id
     * @return Achievement
     * @throws GameException
     */
    public Achievement getAchievement(int id) throws GameException {
        Achievement achievement;
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            achievement = new Achievement(dbName);
            String sql = "SELECT * FROM Achievement WHERE achievementID = " + id;
            ResultSet rs = sdb.queryDB(sql);
            if(rs.next()) {
                achievement.setAchievementID(rs.getInt("achievementID"));
                achievement.setName(rs.getString("name"));
                achievement.setDescription(rs.getString("description"));
                achievement.setScoreValue(rs.getInt("scoreValue"));
                achievement.setCompleted(rs.getInt("completed"));
            }
            else {
                throw new SQLException("Achievement " + id + " not found.");
            }
            sdb.close();
        }
        catch(SQLException | ClassNotFoundException ex) {
            throw new GameException("Achievement with achievement number " + id + " not found.");
        }
        return achievement;
    }

    /**
     * Method: updateAchievement
     * This method updates the achievement
     */
    public void updateAchievement(String name) throws GameException {
        String sql = "UPDATE Achievement SET " +
                "completed = 1 WHERE name = '" + name + "'";
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            sdb.updateDB(sql);
            sdb.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

}
