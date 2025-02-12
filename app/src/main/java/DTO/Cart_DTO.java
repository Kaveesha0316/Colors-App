package DTO;


import java.util.Date;


public class Cart_DTO {
    public Cart_DTO() {
    }


    private int id;


    private String qty;


    private Date date;

    private User_DTO user;


    private Product_DTO product;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User_DTO getUser() {
        return user;
    }

    public void setUser(User_DTO user) {
        this.user = user;
    }

    public Product_DTO getProduct() {
        return product;
    }

    public void setProduct(Product_DTO product) {
        this.product = product;
    }
}
