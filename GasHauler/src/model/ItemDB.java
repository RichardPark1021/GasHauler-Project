package model;

import controller.Items;
import controller.GameException;
import controller.Marker;

import javax.sql.rowset.serial.SerialException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class: ItemDB
 * Author: Sanaa Boddie
 * Version: 1.1
 * Course: ITEC 3860 FALL 2024
 * Written: November 5, 2024
 *
 * This class handles all the database access for the Item class.
 */
public class ItemDB {
    private String dbName;

    public ItemDB(String dbName) {
        this.dbName = dbName;
    }

    public int getNextItemID() throws GameException {
        int next = 0;
        try {
            SQLiteDB sdb = null;
            try {
                sdb = new SQLiteDB(dbName);
                next = sdb.getMaxValue("itemID", "Items") + 1;
            }
            catch(ClassNotFoundException | SerialException e) {
                throw new GameException("Unable to get next Item ID.");
            }
            sdb.close();
        }
        catch(SQLException ex) {
            throw new GameException("Next Item ID not found.");
        }
        return next;
    }

    /**
     * Method: getItem
     * Purpose: Retrieves an item from the database by itemID
     * @param id
     * @return Item
     * @throws GameException
     */

    public Items getItem(int id) throws GameException {
        Items item = null;
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            item = new Items(dbName);
            String sql = "SELECT * FROM Items WHERE itemID = " + id;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                item.setItemID(rs.getInt("itemID"));
                item.setName(rs.getString("name"));
                item.setRoomID(rs.getInt("roomID"));
                item.setInventoryID(rs.getInt("inventoryID"));
            } else {
               // throw new SQLException("Item " + id + " not found.");
                item = null;
            }
            sdb.close();
        } catch (SQLException | ClassNotFoundException ex) {
           // throw new GameException("Item with name " + id + " not found.");
        }
        return item;
    }

    /**
     * Method: getItemIDByName
     * Purpose: Retrieves the itemID based on the item's name.
     * @param itemName The name of the item.
     * @return itemID The ID of the item.
     * @throws GameException If the item with the specified name is not found.
     */
    public int getItemByName(String itemName) throws GameException {
        int itemID = -1;
        try {
            SQLiteDB sdb = new SQLiteDB(dbName);
            String sql = "SELECT itemID FROM Items WHERE name LIKE '%" + itemName + "%'";
            ResultSet rs = sdb.queryDB(sql);
            //  throw new SQLException("Item with name containing '" + itemName + "' not found.");
            if (rs.next()) {
                itemID = rs.getInt("itemID");
            } else itemID = -1;
            sdb.close();
        } catch (SQLException | ClassNotFoundException e) {
           // throw new GameException("Error retrieving item ID for name: " + itemName + " - " + e.getMessage());
        }
        return itemID;
    }

    /**
     * Method: updateItem
     * Purpose: Updates an existing item record in the database
     * @param item
     * @throws GameException
     */
    public void updateItem(Items item) throws GameException {
        String sql = "UPDATE Items SET " +
                "inventoryID = " + item.getInventoryID() + ", " +
                "roomID = " + item.getRoomID() + ", " +
                "WHERE itemID = " + item.getItemID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
            sdb.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
}
