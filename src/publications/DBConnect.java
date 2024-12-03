package publications;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    public static Connection connect() {
        Connection conn = null;
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Establish a connection to the database
            String URL = "jdbc:sqlite:src/project.db"; // Ensure this path is correct
            conn = DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        return conn;
    }

    public static void testConnection() {
        try (Connection conn = connect()) {
            System.out.println("Testing Database connection...");
            if (conn != null) {
                System.out.println("Connection successful!\n");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}