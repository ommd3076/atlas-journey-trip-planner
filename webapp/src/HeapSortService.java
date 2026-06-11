import java.sql.*;
import java.util.*;

public class HeapSortService {

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

    private int score(Attraction a) {
        return a.rating * 10 + a.value;
    }

    private int compare(Attraction a, Attraction b) {
        int scoreCmp = Integer.compare(score(a), score(b));
        if (scoreCmp != 0) return scoreCmp;
        return a.name.compareTo(b.name);
    }

    public List<Attraction> sortByScore(List<Attraction> items) {
        if (items == null || items.size() <= 1) return items;

        Attraction[] arr = items.toArray(new Attraction[0]);
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            Attraction temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0);
        }

        List<Attraction> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(arr[i]);
        }
        return result;
    }

    private void heapify(Attraction[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && compare(arr[left], arr[largest]) < 0) largest = left;
        if (right < n && compare(arr[right], arr[largest]) < 0) largest = right;

        if (largest != i) {
            Attraction swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest);
        }
    }

    public List<Attraction> getSortedFromDB() {
        return sortByScore(getAllAttractions());
    }
}
