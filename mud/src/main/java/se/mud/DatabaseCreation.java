package se.mud;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreation {

    public static void Check() {
        // Get a connection from our singleton DatabaseConnection instance and prepare the SQL statement
        try (Connection conn = DatabaseHandler.getInstance().getConnection();
            Statement stmt = conn.createStatement()) {
            
            String query = """
                CREATE TABLE IF NOT EXISTS players (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(60) NOT NULL,
                experience INT DEFAULT 0,
                strength INT DEFAULT 10,
                dexterity INT DEFAULT 10,
                intelligence INT DEFAULT 10,
                room_id INT DEFAULT 1)
            """;
            stmt.execute(query);
            System.out.println("Database check complete: 'players' table is ready.");
            
        } catch (SQLException e) {
            System.err.println("Error creating player table: " + e.getMessage());
        }
    }
    
}
