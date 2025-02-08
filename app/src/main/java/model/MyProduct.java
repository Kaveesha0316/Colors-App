package model;

public class MyProduct {
    private String id;
    private String name;
    private String price;
    private String av_qty;
    private String sl_qty;
    private String profit;

    public MyProduct(String name, String price, String av_qty, String sl_qty, String profit,String id) {
        this.name = name;
        this.price = price;
        this.av_qty = av_qty;
        this.sl_qty = sl_qty;
        this.profit = profit;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAv_qty() {
        return av_qty;
    }

    public void setAv_qty(String av_qty) {
        this.av_qty = av_qty;
    }

    public String getSl_qty() {
        return sl_qty;
    }

    public void setSl_qty(String sl_qty) {
        this.sl_qty = sl_qty;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }
}
