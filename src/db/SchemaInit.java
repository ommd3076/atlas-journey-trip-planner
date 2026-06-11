
package db;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInit {
    public static void initSchema() {
        String sql1 = "CREATE TABLE IF NOT EXISTS attractions (" +
                      "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                      "name TEXT, cost INTEGER, value INTEGER, rating INTEGER, location TEXT, description TEXT)";
        String sql2 = "CREATE TABLE IF NOT EXISTS edges (" +
                      "id INTEGER PRIMARY KEY AUTOINCREMENT, fromNode TEXT, toNode TEXT, weight INTEGER)";
        String sql3 = "CREATE TABLE IF NOT EXISTS users (" +
                      "id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            System.out.println("Database schema initialized.");
        } catch (SQLException e) {
            System.err.println("Schema Error: " + e.getMessage());
        }
    }
}
