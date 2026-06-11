
package model;
public class Attraction {
    private int id; private String name; private int cost; private int value; private int rating; private String location; private String description;
    public Attraction() {}
    public Attraction(int id, String name, int cost, int value, int rating, String loc, String desc) {
        this.id=id; this.name=name; this.cost=cost; this.value=value; this.rating=rating; this.location=loc; this.description=desc;
    }
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public int getCost() { return cost; } public void setCost(int cost) { this.cost = cost; }
    public int getValue() { return value; } public void setValue(int value) { this.value = value; }
    public int getRating() { return rating; } public void setRating(int rating) { this.rating = rating; }
    public String getLocation() { return location; } public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public int getScore() { return rating * 10 + value; }
}
