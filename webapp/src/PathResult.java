import java.util.List;

public class PathResult {
    public List<String> path;
    public int totalDistance;
    public String start;
    public String end;
    public boolean found;

    public PathResult(List<String> path, int totalDistance, String start, String end, boolean found) {
        this.path = path; this.totalDistance = totalDistance; this.start = start; this.end = end; this.found = found;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"found\":").append(found);
        sb.append(",\"start\":\"").append(start).append("\"");
        sb.append(",\"end\":\"").append(end).append("\"");
        sb.append(",\"totalDistance\":").append(totalDistance);
        sb.append(",\"path\":[");
        if (path != null) {
            for (int i = 0; i < path.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append("\"").append(path.get(i)).append("\"");
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}
