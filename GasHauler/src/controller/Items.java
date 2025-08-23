package controller;

import model.ItemDB;

/**
 * Class: Items
 * Author: Sanaa Boddie
 * Version: 1.1
 * Course: ITEC 3860 FALL 2024
 * Written: November 7, 2024
 *
 * This class is the Item class handling business logic for the Item class
 */

public class Items {
    private int itemID;
    private String name;
    private int roomID;
    private int inventoryID;

    public Items(String dbName) throws GameException {
        ItemDB idb = new ItemDB(dbName);
        itemID = idb.getNextItemID();
    }

    public Items getItem(String dbName, int id) throws GameException {
        ItemDB idb = new ItemDB(dbName);
        return idb.getItem(id);
    }
    public int getItemByName(String dbName, String itemName) throws GameException {
        ItemDB idb = new ItemDB(dbName);
        return idb.getItemByName(itemName);
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    /**
     * Method: getName
     * Purpose: Gets the name  of the item
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Method: setName
     * Purpose: Sets the name of the item
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public int getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    /**
     * Method: toString
     * Purpose: Provides a string representation of the Item object
     * @return String
     */
    @Override
    public String toString() {
        return "Items{" +
                "itemID='" + itemID + '\'' +
                ", name='" + name + '\'' +
                ", roomID=" + roomID +
                ", inventoryID=" + inventoryID +
                '}';
    }


}
