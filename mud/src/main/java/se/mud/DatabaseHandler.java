package se.mud;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import se.mud.Other.Player;
import se.mud.Other.Utl; 

public class DatabaseHandler {

    // HikariDataSource handles connection pooling and is thread-safe
    private HikariDataSource dataSource;

    // Private constructor to prevent instantiation from outside
    private DatabaseHandler() {
        
        try {
            // Configure HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/mud?useSSL=false&serverTimezone=UTC");
            config.setUsername("dbuser");
            config.setPassword("dbpass");
            
            // Optimization: HikariCP settings for performance and resource management
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
        private static final DatabaseHandler INSTANCE = new DatabaseHandler();
    }

    // Global access point to get the singleton instance of DatabaseConnection
    public static DatabaseHandler getInstance() {
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


    public boolean playerExist(String playerName) {
        String query = "SELECT name FROM players WHERE name = ?";
                Utl.debug("1");
        // Get a connection from our singleton DatabaseConnection instance and prepare the SQL statement
        try (Connection conn = DatabaseHandler.getInstance().getConnection();
                    PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the player name parameter in the SQL query
            stmt.setString(1, playerName);
                Utl.debug("2");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Utl.debug("3");
                    return true;
                }
                return false;
            } catch (SQLException e) {
                Utl.debug("4");
                return false;
            }
        } catch (SQLException e) {
                Utl.debug("5");
            return false;
        }
    }


    
    public Player playerCreate(String playerName, String password) {
        Utl.debug("playerCreate: " + playerName + ". Password: " + password + ".");
        if (playerExist(playerName)) {
            Utl.debug("playerCreate: already exist");
            return null;
        } else {
            String query = "INSERT INTO players (name, password, experience, strength, dexterity, intelligence, room_id) VALUES(?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseHandler.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setString(1, playerName);
                    stmt.setString(2, Utl.hashPassword(password));
                    stmt.setInt(3, 0);
                    stmt.setInt(4, 0);
                    stmt.setInt(5, 0);
                    stmt.setInt(6, 0);
                    stmt.setInt(7, 0);
                    stmt.executeUpdate();

                    Utl.debug("works");

                    return new Player(playerName, 0, 0, 0, 0, 0);
                
            } catch (SQLException e) {
                System.err.println("Error loading player from database: " + e.getMessage());
            }
            return null;
        }
    }

    // Check player login credentials and return a player object if correct
    public Player playerLogin(String playerName, String password) {
        Utl.debug("playerLogin: " + playerName + ". Password: " + password + ".");
        String query = "SELECT name, password, experience, strength, dexterity, intelligence, room_id FROM players WHERE name = ?";
        
        // Get a connection from our singleton DatabaseConnection instance and prepare the SQL statement
        try (Connection conn = DatabaseHandler.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the player name parameter in the SQL query
            stmt.setString(1, playerName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                // If a player is found and the password matches that hashed password
                if (rs.next() && Utl.checkPassword(password, rs.getString("password"))) {
                    // Retrieve player data from the result set
                    int experience = rs.getInt("experience");
                    int strength = rs.getInt("strength");
                    int dexterity = rs.getInt("dexterity");
                    int intelligence = rs.getInt("intelligence");
                    int room_id = rs.getInt("room_id");
                    // Create and return a new Player object with the retrieved data
                    return new Player(playerName, experience, strength, dexterity, intelligence, room_id);
                }
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error loading player from database: " + e.getMessage());
        }
        
        return null; // Player not found or error occurred
    }
}