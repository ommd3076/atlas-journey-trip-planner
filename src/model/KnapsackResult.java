
package model;
import java.util.List;
public class KnapsackResult {
    private List<Attraction> items; private int totalValue; private int totalCost; private int budget;
    public KnapsackResult(List<Attraction> items, int val, int cost, int budget) {
        this.items=items; this.totalValue=val; this.totalCost=cost; this.budget=budget;
    }
    public List<Attraction> getSelectedItems() { return items; }
    public int getTotalValue() { return totalValue; }
    public int getTotalCost() { return totalCost; }
    public int getBudget() { return budget; }
    public int getRemainingBudget() { return budget - totalCost; }
}
