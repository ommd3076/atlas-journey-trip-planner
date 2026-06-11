import java.sql.*;
import java.util.*;

/**
 * Dijkstra's Shortest Path Algorithm using Priority Queue.
 * Time Complexity: O((V + E) log V) where V = vertices, E = edges
 * Space Complexity: O(V) for distance and predecessor maps
 */
public class DijkstraService {

    public List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM edges")) {
            while (rs.next()) {
                edges.add(new Edge(rs.getInt("id"), rs.getString("fromNode"),
                    rs.getString("toNode"), rs.getInt("weight")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return edges;
    }

    public List<String> getAllNodes() {
        Set<String> nodes = new LinkedHashSet<>();
        for (Edge e : getAllEdges()) {
            nodes.add(e.fromNode);
            nodes.add(e.toNode);
        }
        return new ArrayList<>(nodes);
    }

    /**
     * Finds the shortest path from start to end using Dijkstra's algorithm.
     * Uses a min-priority queue to always expand the closest unvisited node.
     * Returns the path, total distance, and success status.
     */
    public PathResult findShortestPath(String start, String end) {
        List<Edge> edges = getAllEdges();
        Set<String> allNodes = new HashSet<>();
        Map<String, List<Edge>> graph = new HashMap<>();

        for (Edge e : edges) {
            graph.computeIfAbsent(e.fromNode, k -> new ArrayList<>()).add(e);
            allNodes.add(e.fromNode);
            allNodes.add(e.toNode);
        }

        if (!allNodes.contains(start) || !allNodes.contains(end)) {
            return new PathResult(null, -1, start, end, false);
        }
        if (start.equals(end)) {
            return new PathResult(Arrays.asList(start), 0, start, end, true);
        }

        // Distance map and predecessor map for path reconstruction
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        for (String node : allNodes) dist.put(node, Integer.MAX_VALUE);
        dist.put(start, 0);

        // Min-priority queue ordered by distance
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(d -> dist.get(d)));
        pq.add(start);

        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (u.equals(end)) break;

            for (Edge e : graph.getOrDefault(u, new ArrayList<>())) {
                String v = e.toNode;
                int newDist = dist.get(u) + e.weight;
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    pq.add(v);
                }
            }
        }

        if (dist.get(end) == Integer.MAX_VALUE) {
            return new PathResult(null, -1, start, end, false);
        }

        // Reconstruct path by walking backwards from end to start
        List<String> path = new ArrayList<>();
        String curr = end;
        while (curr != null) {
            path.add(0, curr);
            curr = prev.get(curr);
        }

        return new PathResult(path, dist.get(end), start, end, true);
    }
}
