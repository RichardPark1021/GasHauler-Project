package controller;

import model.PlayerDB;

/**
 * Class : Player
 * @author: Richard Park
 * @version: 1.1
 * Course: ITEC 3860 Fall 2024
 * Written: November 4, 2024
 * Purpose: This class is the Player class handling business logic for the Player class
 */
public class Player {
    private int playerID;
    private String username;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private int score;
    private int currentRoom;
    private int inventoryCapacity;
    private int inventoryID;
    private int roomID;

    /**
     * Method: Player
     * Purpose: Constructor
     * @throws GameException
     */
    public Player(String dbName) throws GameException {
        PlayerDB pdb = new PlayerDB(dbName);
        playerID = pdb.getNextPlayerID();
    }

    /**
     * Method: getPlayer
     * Purpose: Gets a player from the Player table
     * @param id
     * @return Player
     * @throws GameException
     */
    public Player getPlayer(String dbName, int id) throws GameException {
        PlayerDB mdb = new PlayerDB(dbName);
        return mdb.getPlayer(id);
    }

    /**
     * Method: getPlayerID
     * Purpose: Gets playerID
     * @return int
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Method: setPlayerID
     * Purpose: Sets playerID
     * @param playerID
     */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Method: getUsername
     * Purpose: Gets username
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method: setUsername
     * Purpose: Sets username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Method: getPassword
     * Purpose: Gets password
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method: setPassword
     * Purpose: Sets password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method: getSecurityQuestion
     * Purpose: Gets securityQuestion
     * @return String
     */
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * Method: setSecurityQuestion
     * Purpose: Sets securityQuestion
     * @param securityQuestion
     */
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    /**
     * Method: getSecurityAnswer
     * Purpose: Gets securityAnswer
     * @return String
     */
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * Method: setSecurityAnswer
     * Purpose: Sets securityAnswer
     * @param securityAnswer
     */
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    /**
     * Method: getScore
     * Purpose: Gets score
     * @return int
     */
    public int getScore() {
        return score;
    }

    /**
     * Method: setScore
     * Purpose: Sets score
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Method: getCurrentRoom
     * Purpose: Gets currentRoom
     * @return int
     */
    public int getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Method: setCurrentRoom
     * Purpose: Sets currentRoom
     * @param currentRoom
     */
    public void setCurrentRoom(int currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Method: getInventoryCapacity
     * Purpose: Gets inventoryCapacity
     * @return int
     */
    public int getInventoryCapacity() {
        return inventoryCapacity;
    }

    /**
     * Method: setInventoryCapacity
     * Purpose: Sets inventoryCapacity
     * @param inventoryCapacity
     */
    public void setInventoryCapacity(int inventoryCapacity) {
        this.inventoryCapacity = inventoryCapacity;
    }

    /**
     * Method: getInventoryID
     * Purpose: Gets inventoryID
     * @return int
     */
    public int getInventoryID() {
        return inventoryID;
    }

    /**
     * Method: setInventoryID
     * Purpose: Sets inventoryID
     * @param inventoryID
     */
    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    /**
     * Method: getRoomID
     * Purpose: Gets roomID
     * @return int
     */
    public int getRoomID() {
        return roomID;
    }

    /**
     * Method: setRoomID
     * Purpose: Sets roomID
     * @param roomID
     */
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    /**
     * Method: updatePlayer
     * This method uses the PlayerDB in model to update the player
     */
    public void updatePlayer(String dbName) throws GameException {
        PlayerDB  pdb = new PlayerDB(dbName);
        pdb.updatePlayer(this);
    }

    /**
     * Method: toString
     * Purpose: Create toString
     * @return String
     */
    @Override
    public String toString() {
        return "Player{" +
                "playerID=" + playerID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", securityQuestion='" + securityQuestion + '\'' +
                ", securityAnswer='" + securityAnswer + '\'' +
                ", score=" + score +
                ", currentRoom=" + currentRoom +
                ", inventoryCapacity=" + inventoryCapacity +
                ", inventoryID=" + inventoryID +
                ", roomID=" + roomID +
                '}';
    }
}
