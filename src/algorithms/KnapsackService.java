
package algorithms;
import java.sql.*; import java.util.*; import db.DBConnection; import model.Attraction; import model.KnapsackResult;

public class KnapsackService {
    public List<Attraction> getAllAttractions() {
        List<Attraction> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM attractions")) {
            while (rs.next()) list.add(new Attraction(rs.getInt("id"), rs.getString("name"), rs.getInt("cost"), rs.getInt("value"), rs.getInt("rating"), rs.getString("location"), rs.getString("description")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public KnapsackResult solveKnapsack(List<Attraction> items, int budget) {
        int n = items.size();
        if (n == 0 || budget <= 0) return new KnapsackResult(new ArrayList<>(), 0, 0, budget);
        int W = budget;
        int[][] dp = new int[n + 1][W + 1];
        for (int i = 1; i <= n; i++) {
            Attraction a = items.get(i - 1);
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i - 1][w];
                if (a.getCost() <= w) dp[i][w] = Math.max(dp[i][w], dp[i - 1][w - a.getCost()] + a.getValue());
            }
        }
        List<Attraction> selected = new ArrayList<>();
        int w = W, totalCost = 0;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                Attraction a = items.get(i - 1);
                selected.add(a); totalCost += a.getCost(); w -= a.getCost();
            }
        }
        return new KnapsackResult(selected, dp[n][W], totalCost, budget);
    }
    
    public KnapsackResult solveKnapsackFromDB(int budget) { return solveKnapsack(getAllAttractions(), budget); }
}
