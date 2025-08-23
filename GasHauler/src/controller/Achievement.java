package controller;


import model.AchievementDB;

/**
 * Class : Achievement
 * @author: Sanaa Boddie
 * @version: 1.0
 * Course: ITEC 3860 Fall 2024
 * Written: November 11, 2024
 * Purpose: This class is the Achievement class handling business logic for the Player class
 */

public class Achievement {
    private int achievementID;
    private String name;
    private String description;
    private int scoreValue;
    private int completed;

    /**
     * Method: Achievement
     * Purpose: Constructor
     * @throws GameException
     */
    public Achievement(String dbName) throws GameException {
        AchievementDB adb = new AchievementDB(dbName);
        achievementID = adb.getNextAchievementID();
    }

    /**
     * Method: getAchievement
     * Purpose: Gets a achievement from the achievement table
     *
     * @param id
     * @return Achievement
     * @throws GameException
     */
    public Achievement getAchievement(String dbName, int id) throws GameException {
        AchievementDB adb = new AchievementDB(dbName);
        return adb.getAchievement(id);
    }


    /**
     * Method: getAchievementID
     * Purpose: Gets the achievement ID
     * @return achievementID
     */
    public int getAchievementID(){
        return achievementID;
    }

    /**
     * Method: setAchievementID
     * Purpose: Sets the achievement ID
     * @param achievementID
     */
    public void setAchievementID(int achievementID){
        this.achievementID = achievementID;
    }

    /**
     * Method: getName
     * Purpose: Gets the name of the achievement
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * Method: setName
     * Purpose: Sets the name of the achievement
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Method: getDescription
     * Purpose: Gets the description of the achievement
     * @return description
     */
    public String getDescription(){
        return description;
    }

    /**
     * Method: setDescription
     * Purpose: Sets the description of the achievement
     * @param description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Method: getScoreValue
     * Purpose: Gets the score value of the achievement
     * @return scoreValue
     */
    public int getScoreValue(){
        return scoreValue;
    }

    /**
     * Method: setScoreValue
     * Purpose: Sets the score value of the achievement
     * @param scoreValue
     */
    public void setScoreValue(int scoreValue){
        this.scoreValue = scoreValue;
    }


    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    /**
     * Method: toString
     * Purpose: Create toString
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Achievement{" +
                "achievementID=" + achievementID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", scoreValue=" + scoreValue +
                ", completed=" + completed +
                '}';
    }
}
