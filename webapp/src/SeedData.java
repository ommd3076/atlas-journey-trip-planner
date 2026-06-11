import java.sql.*;

public class SeedData {
    public static void seedIfNeeded() {
        if (count("attractions") == 0) seedAttractions();
        if (count("edges") == 0) seedEdges();
        if (count("users") == 0) seedUsers();
    }

    private static int count(String table) {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { return 0; }
    }

    private static void seedAttractions() {
        String sql = "INSERT INTO attractions (name, cost, value, rating, location, description) VALUES (?,?,?,?,?,?)";
        Object[][] data = {
            {"Grand Museum", 150, 95, 5, "Museum District", "World-class art and history museum"},
            {"Central Gardens", 50, 80, 4, "Central Park", "Botanical gardens with walking trails"},
            {"Harbor Walk", 30, 65, 4, "Golden Coast", "Scenic waterfront promenade"},
            {"Heritage Walk", 80, 90, 5, "Heritage Quarter", "Historic architecture guided tour"},
            {"Sunset Peak", 25, 70, 4, "Sunset Point", "Panoramic city viewpoint"},
            {"Innovation Hub", 100, 85, 4, "Innovation District", "Interactive tech and science museum"},
            {"Market Bazaar", 40, 60, 3, "Market Square", "Local artisan market and food stalls"},
            {"Lake Cruise", 90, 75, 4, "Lake Gardens", "Scenic boat tour on Crescent Lake"},
            {"Old Town Alley", 60, 70, 4, "Old Town", "Charming historic alleyway cafes"},
            {"City View Deck", 70, 78, 4, "City Center", "360-degree observation deck"},
            {"Science Museum", 180, 92, 5, "Innovation District", "Hands-on science exhibitions"},
            {"Sunset Beach", 20, 50, 3, "Golden Coast", "Relaxing beach with sunset views"}
        };
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Object[] r : data) {
                ps.setString(1, (String)r[0]); ps.setInt(2, (Integer)r[1]); ps.setInt(3, (Integer)r[2]);
                ps.setInt(4, (Integer)r[3]); ps.setString(5, (String)r[4]); ps.setString(6, (String)r[5]);
                ps.executeUpdate();
            }
            System.out.println("[DB] Seeded 12 attractions.");
        } catch (SQLException e) { System.err.println("[DB] Seed error: " + e.getMessage()); }
    }

    private static void seedEdges() {
        String sql = "INSERT INTO edges (fromNode, toNode, weight) VALUES (?,?,?)";
        Object[][] data = {
            {"North Gate","Central Park","14"},{"Central Park","North Gate","14"},
            {"Central Park","Park Entrance","9"},{"Park Entrance","Central Park","9"},
            {"Park Entrance","City Center","13"},{"City Center","Park Entrance","13"},
            {"City Center","Main Junction","6"},{"Main Junction","City Center","6"},
            {"Main Junction","Market Square","11"},{"Market Square","Main Junction","11"},
            {"Market Square","Old Town","15"},{"Old Town","Market Square","15"},
            {"Old Town","South Bridge","18"},{"South Bridge","Old Town","18"},
            {"West End","Innovation District","8"},{"Innovation District","West End","8"},
            {"Innovation District","Museum Circle","13"},{"Museum Circle","Innovation District","13"},
            {"Museum Circle","Museum District","5"},{"Museum District","Museum Circle","5"},
            {"Museum Circle","Park Entrance","10"},{"Park Entrance","Museum Circle","10"},
            {"City Center","Heritage Quarter","18"},{"Heritage Quarter","City Center","18"},
            {"Heritage Quarter","Heritage Cross","8"},{"Heritage Cross","Heritage Quarter","8"},
            {"Heritage Cross","Old Gate","15"},{"Old Gate","Heritage Cross","15"},
            {"Old Gate","Old Town","12"},{"Old Town","Old Gate","12"},
            {"Sunset Point","West End","13"},{"West End","Sunset Point","13"},
            {"Sunset Point","Market Square","20"},{"Market Square","Sunset Point","20"},
            {"Market Square","Market Hub","6"},{"Market Hub","Market Square","6"},
            {"Market Hub","Main Junction","10"},{"Main Junction","Market Hub","10"},
            {"Main Junction","Heritage Quarter","13"},{"Heritage Quarter","Main Junction","13"},
            {"Heritage Quarter","Lake Gardens","15"},{"Lake Gardens","Heritage Quarter","15"},
            {"Lake Gardens","East Crossing","10"},{"East Crossing","Lake Gardens","10"},
            {"Lake Gardens","Heritage Cross","12"},{"Heritage Cross","Lake Gardens","12"},
            {"East Crossing","Golden Coast","20"},{"Golden Coast","East Crossing","20"},
            {"Golden Coast","Old Gate","15"},{"Old Gate","Golden Coast","15"},
            {"Old Gate","South Bridge","12"},{"South Bridge","Old Gate","12"},
            {"Innovation District","Main Junction","15"},{"Main Junction","Innovation District","15"},
            {"Market Hub","Old Town","13"},{"Old Town","Market Hub","13"},
            {"Heritage Cross","Old Town","8"},{"Old Town","Heritage Cross","8"},
            {"North Gate","Park Entrance","18"},{"Park Entrance","North Gate","18"},
            {"West End","Museum Circle","15"},{"Museum Circle","West End","15"},
            {"Main Junction","Heritage Cross","10"},{"Heritage Cross","Main Junction","10"},
            {"City Center","Museum District","20"},{"Museum District","City Center","20"}
        };
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Object[] r : data) {
                ps.setString(1, (String)r[0]); ps.setString(2, (String)r[1]); ps.setInt(3, Integer.parseInt((String)r[2]));
                ps.executeUpdate();
            }
            System.out.println("[DB] Seeded " + data.length + " edges.");
        } catch (SQLException e) { System.err.println("[DB] Seed error: " + e.getMessage()); }
    }

    private static void seedUsers() {
        String sql = "INSERT INTO users (username, password) VALUES (?,?)";
        Object[][] data = {{"admin","admin"}, {"demo","demo"}, {"traveler","traveler"}};
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Object[] r : data) {
                ps.setString(1, (String)r[0]); ps.setString(2, (String)r[1]); ps.executeUpdate();
            }
            System.out.println("[DB] Seeded 3 users.");
        } catch (SQLException e) { System.err.println("[DB] Seed error: " + e.getMessage()); }
    }

    public static void resetData() {
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM attractions"); stmt.execute("DELETE FROM edges"); stmt.execute("DELETE FROM users");
            seedAttractions(); seedEdges(); seedUsers();
            System.out.println("[DB] Data reset complete.");
        } catch (SQLException e) { System.err.println("[DB] Reset error: " + e.getMessage()); }
    }
}
