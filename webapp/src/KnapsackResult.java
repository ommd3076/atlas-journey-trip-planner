import java.util.List;

public class KnapsackResult {
    public List<Attraction> selectedItems;
    public List<Attraction> skippedItems;
    public int totalValue;
    public int totalCost;
    public int budget;
    public int remainingBudget;

    public KnapsackResult(List<Attraction> selectedItems, int totalValue, int totalCost, int budget, List<Attraction> skippedItems) {
        this.selectedItems = selectedItems;
        this.totalValue = totalValue;
        this.totalCost = totalCost;
        this.budget = budget;
        this.remainingBudget = budget - totalCost;
        this.skippedItems = skippedItems;
    }

    public String toJson() {
        return "{\"totalValue\":" + totalValue + ",\"totalCost\":" + totalCost
            + ",\"budget\":" + budget + ",\"remainingBudget\":" + remainingBudget
            + ",\"selected\":" + Attraction.toJsonArray(selectedItems)
            + ",\"skipped\":" + Attraction.toJsonArray(skippedItems) + "}";
    }
}
