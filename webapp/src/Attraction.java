public class Attraction {
    public int id;
    public String name;
    public int cost;
    public int value;
    public int rating;
    public String location;
    public String description;

    public Attraction() {}

    public Attraction(int id, String name, int cost, int value, int rating, String location, String description) {
        this.id = id; this.name = name; this.cost = cost; this.value = value;
        this.rating = rating; this.location = location; this.description = description;
    }

    public String toJson() {
        return "{\"id\":" + id + ",\"name\":\"" + esc(name) + "\",\"cost\":" + cost
            + ",\"value\":" + value + ",\"rating\":" + rating
            + ",\"location\":\"" + esc(location) + "\",\"description\":\"" + esc(description) + "\"}";
    }

    public static String toJsonArray(java.util.List<Attraction> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(list.get(i).toJson());
        }
        sb.append("]");
        return sb.toString();
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
