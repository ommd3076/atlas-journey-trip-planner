
package model;
public class Edge {
    private int id; private String fromNode; private String toNode; private int weight;
    public Edge() {}
    public Edge(int id, String from, String to, int w) { this.id=id; this.fromNode=from; this.toNode=to; this.weight=w; }
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getFromNode() { return fromNode; } public void setFromNode(String fromNode) { this.fromNode = fromNode; }
    public String getToNode() { return toNode; } public void setToNode(String toNode) { this.toNode = toNode; }
    public int getWeight() { return weight; } public void setWeight(int weight) { this.weight = weight; }
}
