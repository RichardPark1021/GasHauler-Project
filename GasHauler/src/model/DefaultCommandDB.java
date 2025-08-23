package model;

import controller.DefaultCommand;
import controller.GameException;

import javax.sql.rowset.serial.SerialException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Class: DefaultCommandDB
 * @author: Erika Attidzah
 * @version: 1.0
 * Course: ITEC 3860 FALL 2024
 * Written: 4 Nov, 2024
 *
 * This class creates a list of all the defaults commands with arguments from the database
 */

public class DefaultCommandDB {
    SQLiteDB sdb;
    private List<DefaultCommand> defaultCommandList;
    private String dbName;

    public DefaultCommandDB(String dbName) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        defaultCommandList = new ArrayList<>();
    }

    /**
     * Method: getDefaultsCommands
     * Purpose: get all the commands that needs an argument after
     * @return a List of DefaultCommand
     * @throws SQLException
     */
    public List<DefaultCommand> getAllDefaultsCommand() throws SQLException{
        String sql = "SELECT * FROM DefaultCommand";

        try(PreparedStatement statement = sdb.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                int defaultID = resultSet.getInt("defaultID");
                String name = resultSet.getString("name");
                String defaultAction = resultSet.getString("defaultAction");
                DefaultCommand defaultCommandDB = new DefaultCommand(defaultID, name, defaultAction);
                defaultCommandList.add(defaultCommandDB);
            }
        }
        return defaultCommandList;
    }
}
