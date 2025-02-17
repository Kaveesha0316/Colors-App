package DTO;

public class CategoryProfitDTO {
    private String category;
    private int profit;

    public CategoryProfitDTO(String category, int profit) {
        this.category = category;
        this.profit = profit;
    }

    public String getCategory() {
        return category;
    }

    public int getProfit() {
        return profit;
    }
}
