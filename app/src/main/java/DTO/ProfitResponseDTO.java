package DTO;

public class ProfitResponseDTO {
    private String date;
    private Integer profit;

    public ProfitResponseDTO(String date, Integer profit) {
        this.date = date;
        this.profit = profit;
    }

    public String getDate() {
        return date;
    }

    public Integer getProfit() {
        return profit;
    }
}
