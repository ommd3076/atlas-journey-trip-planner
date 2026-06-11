
package algorithms;
import java.sql.*; import java.util.*; import db.DBConnection; import model.Edge; import model.PathResult;

public class DijkstraService {
    private Map<String, List<Edge>> graph = new HashMap<>();
    private Set<String> nodes = new LinkedHashSet<>();
    
    public DijkstraService() { loadGraph(); }
    
    private void loadGraph() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM edges")) {
            while (rs.next()) {
                Edge e = new Edge(rs.getInt("id"), rs.getString("fromNode"), rs.getString("toNode"), rs.getInt("weight"));
                graph.computeIfAbsent(e.getFromNode(), k -> new ArrayList<>()).add(e);
                nodes.add(e.getFromNode()); nodes.add(e.getToNode());
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public List<String> getAllNodes() { return new ArrayList<>(nodes); }
    
    public PathResult findShortestPath(String start, String end) {
        if (!nodes.contains(start) || !nodes.contains(end)) return new PathResult(null, -1, start, end, false);
        if (start.equals(end)) return new PathResult(Arrays.asList(start), 0, start, end, true);
        
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        for (String n : nodes) dist.put(n, Integer.MAX_VALUE);
        dist.put(start, 0);
        
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(start);
        
        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (!visited.add(u)) continue;
            if (u.equals(end)) break;
            for (Edge e : graph.getOrDefault(u, new ArrayList<>())) {
                String v = e.getToNode();
                if (visited.contains(v)) continue;
                int newDist = dist.get(u) + e.getWeight();
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    pq.add(v);
                }
            }
        }
        
        if (!visited.contains(end) || dist.get(end) == Integer.MAX_VALUE) return new PathResult(null, -1, start, end, false);
        List<String> path = new ArrayList<>();
        String curr = end;
        while (curr != null) { path.add(0, curr); curr = prev.get(curr); }
        return new PathResult(path, dist.get(end), start, end, true);
    }
}
