package model;

import java.io.Serializable;

public class Cart implements Serializable {
    private Integer id;
    private String qty;
    private String product_name;
    private Double product_price;
    private String product_imagepath;
    private Integer product_Id;

    public Cart(int id, String qty, String product_name, Double product_price, String product_imagepath, Integer product_Id) {
        this.id = id;
        this.qty = qty;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_imagepath = product_imagepath;
        this.product_Id = product_Id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_imagepath() {
        return product_imagepath;
    }

    public void setProduct_imagepath(String product_imagepath) {
        this.product_imagepath = product_imagepath;
    }

    public Integer getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(Integer product_Id) {
        this.product_Id = product_Id;
    }
}
