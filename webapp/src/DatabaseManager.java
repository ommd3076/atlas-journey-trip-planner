import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:webapp/trip.db";
    private static Connection connection;

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    public static void initializeSchema() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS attractions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, " +
                "cost INTEGER NOT NULL, value INTEGER NOT NULL, rating INTEGER NOT NULL, " +
                "location TEXT NOT NULL, description TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS edges (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, fromNode TEXT NOT NULL, " +
                "toNode TEXT NOT NULL, weight INTEGER NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE NOT NULL, password TEXT NOT NULL)");
            System.out.println("[DB] Schema initialized.");
        } catch (SQLException e) {
            System.err.println("[DB] Schema error: " + e.getMessage());
        }
    }
}
