package se.mud;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import se.mud.Other.Player;
import se.mud.Other.Utl; 

public class DatabaseConnection {

    // HikariDataSource handles connection pooling and is thread-safe
    private HikariDataSource dataSource;

    // Private constructor to prevent instantiation from outside
    private DatabaseConnection() {
        try {
            // Configure HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/mud_database?useSSL=false&serverTimezone=UTC");
            config.setUsername("dbuser");
            config.setPassword("dbpass");
            
            // Optimization: HikariCP inställningar för bättre prestanda
            config.setMaximumPoolSize(10); // Maximum number of connections to database
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            
            // Initialize the data source
            this.dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            System.err.println("Could not initialize database pool: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    // A private static class that holds the instance.
    // This is not loaded into memory until someone calls getInstance().
    private static class Holder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }

    // Global access point to get the singleton instance of DatabaseConnection
    public static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }

    // Public method to get a connection from the pool
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    // Close the pool when the application is shutting down
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public Player loadPlayer(String playerName, String password) {
        String query = "SELECT name, pass, hp, level, room_id FROM players WHERE name = ?";
        
        // Get a connection from our singleton DatabaseConnection instance and prepare the SQL statement
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
        
            PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the player name parameter in the SQL query
            stmt.setString(1, playerName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                // If a player is found and the password matches that hashed password
                if (rs.next() && Utl.checkPassword(password, rs.getString("pass"))) {
                    // Retrieve player data from the result set
                    int hp = rs.getInt("hp");
                    int level = rs.getInt("level");
                    int roomId = rs.getInt("room_id");
                    // Create and return a new Player object with the retrieved data
                    return new Player(playerName, hp, level, roomId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading player from database: " + e.getMessage());
        }
        
        return null; // Player not found or error occurred
    }
}