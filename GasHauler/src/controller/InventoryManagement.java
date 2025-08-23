package controller;

import model.InventoryManagerDB;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryManagement {
    private InventoryManagerDB db;
    private final int BASE_CAPACITY = 3;
    private final int EXTENDED_CAPACITY = 5;
    private final int EXTENSION_ITEM_ID = 19;
    private Scanner in;
    private int roomID;



    public InventoryManagement(String dbURL) throws SQLException, ClassNotFoundException, GameException {
        this.db = new InventoryManagerDB(dbURL);
    }

    /**
     * Retrieves the maximum capacity based on whether the player has the extension item.
     * @return maximum inventory capacity
     * @throws SQLException if database access error occurs
     */
    public int getMaxCapacity() throws SQLException {
        List<Integer> items = db.getPlayerInventoryItems();
        return items.contains(EXTENSION_ITEM_ID) ? EXTENDED_CAPACITY : BASE_CAPACITY;
    }

    /**
     * Picks up an item if inventory is not full.
     * @param itemID ID of the item to pick up
     * @param itemName Name of the item
     * @return message indicating success or failure
     * @throws SQLException if database access error occurs
     */
    public String pickUpItem(int itemID, String itemName) throws SQLException {
        List<Integer> items = db.getPlayerInventoryItems();
        String room = roomInvWithOtherNames(roomID).toLowerCase();
        if (!room.contains(itemName.toLowerCase())) {
            return "This item does not exist in the room.";
        }
        if (items.contains(itemID)) {
            return "You already have " + itemName + " in your inventory.";
        }
        if (items.size() >= getMaxCapacity()) {
            return "You can't carry any more items.";
        }
        db.addItemToInventory(itemID);
        return itemName + " Added to the inventory.";
    }

    /**
     * Drops an item from the player's inventory.
     * @param itemID ID of the item to drop
     * @param itemName Name of the item
     * @return message indicating the item was dropped
     * @throws SQLException if database access error occurs
     */
    public String dropItem(int itemID, String itemName) throws SQLException {
        List<Integer> items = db.getPlayerInventoryItems();
        if (!items.contains(itemID)) {
            return "You cant drop what you dont have!";
        }else {
            db.removeItemFromInventory(itemID);
            return "You dropped " + itemName;
        }
    }

    /**
     * Displays the current inventory items and maximum capacity.
     * @return formatted inventory list with capacity info
     * @throws SQLException if database access error occurs
     */
    public String displayInventory() throws SQLException {
        List<Integer> items = db.getPlayerInventoryItems();
        int capacity = getMaxCapacity();
        StringBuilder inventory = new StringBuilder("Inventory Capacity: " + items.size() + "/" + capacity + "\n");
        List<String> itemNames = db.getItemNamesFromIDs();
        for (String itemName : itemNames) {
            inventory.append(itemName).append("\n");
        }

        return inventory.toString();
    }

    /**
     * Retrieves the name of an item based on its ID.
     * @param itemID ID of the item
     * @return item name
     * @throws SQLException if database access error occurs
     */
    private String getItemName(int itemID) throws SQLException {
        return db.getItemNameByID(itemID); // Fetch item name from database
    }

    /**
     * Gets descriptions for a room based on the current room and inventory state.
     * @param roomID the ID of the room
     * @return list of descriptions for the room
     * @throws SQLException if database access error occurs
     */
    public String getRoomDescription(int roomID) throws SQLException {
        return db.getRoomDescription(roomID);
    }
    public String handleUse(String input, int roomID, String dbName) throws SQLException, GameException, ClassNotFoundException {
        String regex = "(?i)^Use\\s+([^\\s]+)\\s+on\\s+(.*)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        String itemName = matcher.group(1).trim();
        String target = matcher.group(3).trim();
        Items items = new Items(dbName);
        int itemID = items.getItemByName(dbName, itemName);
        return getUse(itemID, target, roomID);

    }
    public String getUse(int itemID, String marker, int activation) throws SQLException, ClassNotFoundException {
        List<Integer> items = db.getPlayerInventoryItems();
        if (!items.contains(itemID)) {
            return "You can't use what you don't have.";
        }
        return db.getUse(itemID, marker, activation);
    }

    public String defaultUse(int roomID) throws SQLException {
        in = new Scanner(System.in);
        String input;
        System.out.println("Would you like to use an item in the room, or your inventory?");
        input = in.nextLine();
        String inv = null;
        if (input.equalsIgnoreCase("inventory") || input.equalsIgnoreCase("backpack") || input.equalsIgnoreCase("pack")) {
            inv = displayInventory();
        }
        if (input.equalsIgnoreCase("room")) {
            inv = displayRoomInventory(roomID);
        }
        return inv;
    }

    public String displayRoomInventory(int roomID) throws SQLException {
        String layout = "Inventory for current room:\n";

        switch (roomID) {
            case 18:
                return layout + "Power Cell";
            case 17:
                return layout + "Mag Boots\nRailgun";
            case 13:
                return layout + "Fuel Injector\nSpace Suit";
            case 5:
                return layout + "Terrariums Book\nFlashlight\nAxe\nTech Clothes";
            case 3:
                return layout + "Rag\nMop\nCleaning Spray";
            case 20:
                return layout + "Flask";
            default:
                return layout + "No inventory available for this room.";
        }
    }
    public String roomInvWithOtherNames(int roomID) throws SQLException {
            switch (roomID) {
            case 18:
                return "Power Cell, Cell";
            case 17:
                return "Mag Boots,Magnetic Boots,Boots, Railgun,Gun";
            case 13:
                return  "Fuel Injector,Injector, Spacesuit, Suit";
            case 5:
                return "Terrariums Book, Terrariums, Flashlight, Tube, Axe, Ax, Maintenance Tech Clothes, Tech Clothes, Clothes";
            case 3:
                return "Rag, Cloth, Mop, Cleaning Spray,Spray, Spray Bottle,Cleaning Spray Bottle";
            case 20:
                return "Flask";
            default:
                return  "No inventory available";
        }
    }
public boolean validateUse(int roomID, int itemID) throws SQLException {
    switch (roomID) {
        case 3:
            switch (itemID) {
                case 2: case 3: case 4:
                    return true;
                default:
                    return false;
            }
        case 5:
            switch (itemID) {
                case 1: case 15: case 18: case 19: case 5:
                    return true;
                default:
                    return false;
            }
        case 6:
            switch (itemID) {
                case 9:
                    return true;
                default:
                    return false;
            }
        case 8:
            switch (itemID) {
                case 17:
                    return true;
                default:
                    return false;
            }
        case 9:
            switch (itemID) {
                case 13:
                    return true;
                default:
                    return false;
            }
        case 11:
            switch (itemID) {
                case 18: case 6: case 11:
                    return true;
                default:
                    return false;
            }
        case 13:
            switch (itemID) {
                case 1: case 10: case 7:
                    return true;
                default:
                    return false;
            }
        case 14:
            switch (itemID) {
                case 1: case 10:
                    return true;
                default:
                    return false;
            }
        case 17:
            switch (itemID) {
                case 18: case 4: case 6: case 8:
                    return true;
                default:
                    return false;
            }
        case 18:
            switch (itemID) {
                case 11:
                    return true;
                default:
                    return false;
            }
        case 20:
            switch (itemID) {
                case 14:
                    return true;
                default:
                    return false;
            }
        case 22:
            switch (itemID) {
                case 8: case 7:
                    return true;
                default:
                    return false;
            }
        default:
            return false;
    }
}
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
    /**
     * Closes the database connection when done.
     * @throws SQLException if a database access error occurs
     */
    public void close() throws SQLException {
        db.close();
    }
}
