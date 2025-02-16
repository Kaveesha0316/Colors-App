package model;

public class Order {
    private String name;
    private String qty;
    private String price;
    private String date;
    private String status;
    private String imageUrl;


    public Order(String name, String qty, String price, String date, String status, String imageUrl) {
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.date = date;
        this.status = status;
        this.imageUrl = imageUrl;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
