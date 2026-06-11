public class Edge {
    public int id;
    public String fromNode;
    public String toNode;
    public int weight;

    public Edge() {}

    public Edge(int id, String fromNode, String toNode, int weight) {
        this.id = id; this.fromNode = fromNode; this.toNode = toNode; this.weight = weight;
    }

    public String toJson() {
        return "{\"id\":" + id + ",\"from\":\"" + esc(fromNode) + "\",\"to\":\"" + esc(toNode) + "\",\"weight\":" + weight + "}";
    }

    public static String toJsonArray(java.util.List<Edge> edges) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < edges.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(edges.get(i).toJson());
        }
        sb.append("]");
        return sb.toString();
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
