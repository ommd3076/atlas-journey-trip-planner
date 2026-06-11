import java.sql.*;
import java.util.*;

/**
 * 0-1 Knapsack Algorithm using Dynamic Programming.
 * Time Complexity: O(n * W) where n = number of items, W = budget
 * Space Complexity: O(n * W) for DP table
 */
public class KnapsackService {

    public List<Attraction> getAllAttractions() {
        List<Attraction> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM attractions ORDER BY id")) {
            while (rs.next()) {
                list.add(new Attraction(rs.getInt("id"), rs.getString("name"), rs.getInt("cost"),
                    rs.getInt("value"), rs.getInt("rating"), rs.getString("location"), rs.getString("description")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Solves the 0-1 Knapsack problem.
     * Builds a DP table where dp[i][w] = max value using first i items with budget w.
     * Backtracks to find which items were selected.
     */
    public KnapsackResult solve(List<Attraction> items, int budget) {
        int n = items.size();
        if (n == 0 || budget <= 0) {
            return new KnapsackResult(new ArrayList<>(), 0, 0, budget, new ArrayList<>(items));
        }

        int W = budget;
        int[][] dp = new int[n + 1][W + 1];

        // Build DP table: for each item and each budget capacity
        for (int i = 1; i <= n; i++) {
            Attraction a = items.get(i - 1);
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i - 1][w]; // exclude item
                if (a.cost <= w) {
                    // include item if it gives better value
                    dp[i][w] = Math.max(dp[i][w], dp[i - 1][w - a.cost] + a.value);
                }
            }
        }

        // Backtrack to find selected items
        List<Attraction> selected = new ArrayList<>();
        int w = W;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                Attraction a = items.get(i - 1);
                selected.add(a);
                w -= a.cost;
            }
        }

        // Skipped items are those not in selected
        List<Attraction> skipped = new ArrayList<>();
        for (Attraction a : items) {
            if (!selected.contains(a)) skipped.add(a);
        }

        int totalCost = 0;
        for (Attraction a : selected) totalCost += a.cost;

        return new KnapsackResult(selected, dp[n][W], totalCost, budget, skipped);
    }

    public KnapsackResult solveFromDB(int budget) {
        return solve(getAllAttractions(), budget);
    }
}
