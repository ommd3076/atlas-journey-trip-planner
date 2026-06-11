
package model;
import java.util.List;
public class PathResult {
    private List<String> path; private int dist; private String start; private String end; private boolean found;
    public PathResult(List<String> path, int dist, String start, String end, boolean found) {
        this.path=path; this.dist=dist; this.start=start; this.end=end; this.found=found;
    }
    public List<String> getPath() { return path; }
    public int getTotalDistance() { return dist; }
    public boolean isPathFound() { return found; }
    public String getFormattedPath() {
        if(path == null || path.isEmpty()) return "No path";
        return String.join(" -> ", path);
    }
}
