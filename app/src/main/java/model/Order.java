package model;

public class Order {
    private String name;
    private String qty;
    private String price;
    private String date;
    private String status;


    public Order(String namae, String qty, String price, String date, String status) {
        this.name = namae;
        this.qty = qty;
        this.price = price;
        this.date = date;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNamae() {
        return name;
    }

    public void setNamae(String namae) {
        this.name = namae;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
