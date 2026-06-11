
package algorithms;
import java.sql.*; import java.util.*; import db.DBConnection; import model.Attraction;

public class HeapSortService {
    public List<Attraction> getAllAttractions() {
        List<Attraction> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM attractions")) {
            while (rs.next()) list.add(new Attraction(rs.getInt("id"), rs.getString("name"), rs.getInt("cost"), rs.getInt("value"), rs.getInt("rating"), rs.getString("location"), rs.getString("description")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public List<Attraction> sortByScore(List<Attraction> items) {
        if (items == null || items.size() <= 1) return items;
        Attraction[] arr = items.toArray(new Attraction[0]);
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
        for (int i = n - 1; i > 0; i--) {
            Attraction temp = arr[0]; arr[0] = arr[i]; arr[i] = temp;
            heapify(arr, i, 0);
        }
        List<Attraction> res = new ArrayList<>();
        for (int i = 0; i < n; i++) res.add(arr[i]);
        return res;
    }

    private int compare(Attraction a, Attraction b) {
        int scoreCmp = Integer.compare(a.getScore(), b.getScore());
        if (scoreCmp != 0) return scoreCmp;
        return a.getName().compareTo(b.getName());
    }
    
    private void heapify(Attraction[] arr, int n, int i) {
        int largest = i; int l = 2 * i + 1; int r = 2 * i + 2;
        if (l < n && compare(arr[l], arr[largest]) < 0) largest = l;
        if (r < n && compare(arr[r], arr[largest]) < 0) largest = r;
        if (largest != i) {
            Attraction swap = arr[i]; arr[i] = arr[largest]; arr[largest] = swap;
            heapify(arr, n, largest);
        }
    }
    
    public List<Attraction> getSortedFromDB() { return sortByScore(getAllAttractions()); }
}
