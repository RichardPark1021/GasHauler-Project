package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryManagerDB {
    private SQLiteDB sdb;
    private String dbName;

    public InventoryManagerDB(String dbName) throws SQLException, ClassNotFoundException {
        this.dbName = dbName;
        sdb = new SQLiteDB(dbName);
    }
    public String getUse(int ID, String marker, int activation) throws SQLException, ClassNotFoundException {
        String query = "SELECT description FROM useItem WHERE itemID = ? AND markerName LIKE ? AND activation = ?";
        try (PreparedStatement stmt = sdb.getConnection().prepareStatement(query)) {
            stmt.setInt(1, ID);
            stmt.setString(2, "%" + marker + "%");
            stmt.setInt(3, activation);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("description");
            }
        }
        return null;
    }

    // Method to get all item IDs from the player's inventory
    public List<Integer> getPlayerInventoryItems() throws SQLException {
        List<Integer> playerItems = new ArrayList<>();
        String query = "SELECT itemID FROM PlayerInv";
        try (PreparedStatement stmt = sdb.getConnection().prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                playerItems.add(rs.getInt("itemID"));
            }
        }
        return playerItems;
    }

    public List<String> getItemNamesFromIDs() throws SQLException {
        List<Integer> itemIDs = getPlayerInventoryItems();
        List<String> itemNames = new ArrayList<>();
        String query = "SELECT singleName FROM Items WHERE itemID = ?";

        try (PreparedStatement stmt = sdb.getConnection().prepareStatement(query)) {
            for (int itemID : itemIDs) {
                stmt.setInt(1, itemID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        itemNames.add(rs.getString("singleName"));
                    } else {
                        itemNames.add("Unknown Item (ID: " + itemID + ")");
                    }
                }
            }
        }
            return itemNames;
        }
    // Method to get the name of an item by its ID
    public String getItemNameByID(int itemID) throws SQLException {
        String query = "SELECT ItemName FROM Items WHERE ItemID = ?";
        try (PreparedStatement stmt = sdb.getConnection().prepareStatement(query)) {
            stmt.setInt(1, itemID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("name") : "Unknown Item";
        }
    }

    /**
     * Method: getItemIDByName
     * Purpose: gets itemID by name from item database
     * @param itemName
     * @return
     * @throws SQLException
     */
    public int getItemIDByName(String itemName) throws SQLException {
        String query = "SELECT itemID FROM Items WHERE name LIKE ?";
        try (PreparedStatement stmt = sdb.getConnection().prepareStatement(query)) {
            // Use wildcards to search for the item name within the name column
            stmt.setString(1, "%" + itemName + "%");
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("itemID") : -1; // Return -1 if no item is found
        }
    }

    // Retrieve the Room's Marker ID from RoomInv table
    public Integer getRoomMarkerID(int roomID) throws SQLException {
        String query = "SELECT markerID FROM RoomInv WHERE RoomID = ?";
        try (PreparedStatement stmt = sdb.getConnection().prepareStatement(query)) {
            stmt.setInt(1, roomID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("MarkerID");
                }
            }
        }
        return null;
    }

    public int getMarkerActivation(int markerID) throws SQLException {
        String query = "SELECT active FROM Marker WHERE markerID = ?";
        try (PreparedStatement stmt = sdb.getConnection().prepareStatement(query)) {
            stmt.setInt(1, markerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("active");
                }
            }
        }
        return 0;
    }
    public String getRoomDescription(int roomID) throws SQLException {

        List<Integer> playerItems = getPlayerInventoryItems();

        List<List<Integer>> roomItemSets = getItemIDsForRoom(roomID);

        List<Integer> matchedItems = compareInventoryWithItems(playerItems, roomItemSets);

        Integer markerID = getRoomMarkerID(roomID);

        int activation = getMarkerActivation(markerID);

        // Debugging output to check values
       // System.out.println("Debug: RoomID = " + roomID + ", MarkerID = " + markerID + ", Activation = " + activation + ", MatchedItems = " + matchedItems);

        return getRoomDescription(roomID, matchedItems.get(0), matchedItems.get(1), matchedItems.get(2), markerID, activation);
    }

    public String getRoomDescription(int roomID, int item1, int item2, int item3, Integer markerID, int markerActive) throws SQLException {
        String query = "SELECT description FROM InvMgmnt WHERE RoomID = ? AND itemID = ? AND secondItemID = ? AND thirdItemID = ? AND markerID = ? AND markerActive = ?";
        try (PreparedStatement stmt = sdb.getConnection().prepareStatement(query)) {
            stmt.setInt(1, roomID);
            stmt.setInt(2, item1);
            stmt.setInt(3, item2);
            stmt.setInt(4, item3);
            stmt.setInt(5, markerID);
            stmt.setInt(6, markerActive);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("description");
            }
        }
        return null;
    }
    public List<List<Integer>> getItemIDsForRoom(int roomID) throws SQLException {
        String query = "SELECT itemID, secondItemID, thirdItemID FROM InvMgmnt WHERE RoomID = ?";
        List<List<Integer>> allItemIDs = new ArrayList<>();

        try (PreparedStatement stmt = sdb.getConnection().prepareStatement(query)) {
            stmt.setInt(1, roomID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                List<Integer> itemIDs = new ArrayList<>();
                itemIDs.add(rs.getInt("itemID"));
                itemIDs.add(rs.getInt("secondItemID"));
                itemIDs.add(rs.getInt("thirdItemID"));
                allItemIDs.add(itemIDs);
            }
        }
        return allItemIDs;
    }

    public List<Integer> compareInventoryWithItems(List<Integer> playerItems, List<List<Integer>> roomItemIDs) {
        List<Integer> matchedItems = new ArrayList<>();
        if (playerItems.isEmpty()) {
            matchedItems.add(0);
            matchedItems.add(0);
            matchedItems.add(0);
            return matchedItems;
        }
        boolean matchFoundInAnyRoom = false;

        for (List<Integer> roomItems : roomItemIDs) {
            if (roomItems.get(0) == 0) {
                continue;
            }
            List<Integer> currentMatchedItems = new ArrayList<>();
            for (int i = 0; i < roomItems.size(); i++) {
                Integer roomItem = roomItems.get(i);
                boolean matchFound = false;

                for (Integer playerItem : playerItems) {
                    if (roomItem.equals(playerItem)) {
                        currentMatchedItems.add(playerItem);
                        matchFound = true;
                        break;
                    }
                }
                if (!matchFound) {
                    currentMatchedItems.add(0);
                }
            }

            if (currentMatchedItems.get(0) != 0) {
                matchedItems = currentMatchedItems;
                matchFoundInAnyRoom = true;
                break;
            }
        }
        if (!matchFoundInAnyRoom) {
            matchedItems.add(0);
            matchedItems.add(0);
            matchedItems.add(0);
        }
        return matchedItems;
    }


    public void addItemToInventory(int itemID) throws SQLException {
        String query = "INSERT INTO PlayerInv (ItemID) VALUES (?)";
        try (PreparedStatement stmt = sdb.getConnection().prepareStatement(query)) {
            stmt.setInt(1, itemID);
            stmt.executeUpdate();
        }
    }

    public void removeItemFromInventory(int itemID) throws SQLException {
        String query = "DELETE FROM PlayerInv WHERE ItemID = ?";
        try (PreparedStatement stmt = sdb.getConnection().prepareStatement(query)) {
            stmt.setInt(1, itemID);
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (sdb.getConnection() != null) sdb.getConnection().close();
    }
}
