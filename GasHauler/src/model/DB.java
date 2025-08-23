package model;

import java.sql.*;

/**
 * Class : DB
 * @author Richard Park, David Flores
 * Version 1.2
 * Course: ITEC 3860, Fall 2024
 * Written: November 2, 2024
 * This class controls basic DB functionality
 * Purpose: Has Query and Update DB
 */
public class DB {
    protected String sJdbc;
    protected String sDriverName;
    protected Connection conn;
    protected String sDbUrl;
    protected int timeout = 5;

    public DB() throws SQLException, ClassNotFoundException {
        this(null);  // Default to no dbName (for temporary databases or specific subclass handling)
    }

    public DB(String dbName) throws SQLException, ClassNotFoundException {
        initializeDB(dbName);
    }

    private void initializeDB(String dbName) throws SQLException, ClassNotFoundException {
        sJdbc = "jdbc:sqlite";
        sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);
        // Default to "Cara.db" if dbName is null
        sDbUrl = (dbName == null) ? sJdbc + ":Cara.db" : sJdbc + ":" + dbName;
        conn = DriverManager.getConnection(sDbUrl);
    }

    /**
     * Method: getMaxValue
     * Purpose: Gets max value for a particular field in a particular table
     * @param columnName
     * @param table
     * @return int
     */
    public int getMaxValue(String columnName, String table) {
        int max = 0;
        try {
            Statement stmt = conn.createStatement();
            String sql = "Select MAX(" + columnName + ") from " + table;
            ResultSet rs = stmt.executeQuery(sql);
            max = rs.getInt(1);
        } catch (SQLException sqe) {
            System.out.println(sqe.getMessage());
        }
        return max;
    }

    public ResultSet queryDB(String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setQueryTimeout(timeout);

        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }

        return pstmt.executeQuery();
    }

    public boolean updateDB(String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setQueryTimeout(timeout);

        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }

        return pstmt.executeUpdate() > 0;
    }

    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}