package DTO;



import java.util.Date;


public class Product_DTO {

    public Product_DTO(){}


    private int id;


    private String name;


    private Double price;


    private String description;


    private Integer qty;


    private Date created_at;


    private Category_DTO category;

    private User_DTO user;


    private Status_DTO status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Category_DTO getCategory() {
        return category;
    }

    public void setCategory(Category_DTO category) {
        this.category = category;
    }

    public User_DTO getUser() {
        return user;
    }

    public void setUser(User_DTO user) {
        this.user = user;
    }

    public Status_DTO getStatus() {
        return status;
    }

    public void setStatus(Status_DTO status) {
        this.status = status;
    }
}
