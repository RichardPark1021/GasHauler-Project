package model;

import java.io.File;
import java.sql.*;

/**Class: SQLiteDB
 * @author Richard Park, David Flores
 * Version 1.2
 * Course: ITEC 3860, Fall 2024
 * Written: November 2, 2024
 * This class – creates the wrapper around SQLite specific initializations.
 */
public class SQLiteDB extends DB implements AutoCloseable {
    public SQLiteDB() throws SQLException, ClassNotFoundException {
        super();
        // Calls the DB constructor
    }

    public SQLiteDB(String dbName) throws SQLException, ClassNotFoundException {
        super(dbName); // Calls the DB constructor with dbName
        initializeDB(dbName);
    }

    /** private void initializeDB(String dbName) throws SQLException, ClassNotFoundException {
     sJdbc = "jdbc:sqlite";
     sDriverName = "org.sqlite.JDBC";
     Class.forName(sDriverName);
     sDbUrl = (dbName == null) ? sJdbc : sJdbc + ":" + dbName;
     conn = DriverManager.getConnection(sDbUrl);
     }
     */
    private void initializeDB(String dbName) throws SQLException, ClassNotFoundException {
        sJdbc = "jdbc:sqlite";
        sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);
        File dbFile = new File(dbName);
        if (!dbFile.exists()) {
            throw new SQLException("Database file not found at: " + dbName);
        }

        sDbUrl = sJdbc + ":" + dbName;
        conn = DriverManager.getConnection(sDbUrl);
    }
    public Connection getConnection() {
        return conn;
    }

    public ResultSet queryDB(String sql, String... parameters) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            pstmt.setString(i + 1, parameters[i]);
        }
        return pstmt.executeQuery();
    }

    public int updateDB(String sql, String... parameters) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            pstmt.setString(i + 1, parameters[i]);
        }
        return pstmt.executeUpdate();
    }

    @Override
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}