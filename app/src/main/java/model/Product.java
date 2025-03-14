package model;

public class Product {

    private String id;
    private String name;
    private String price;
    private Integer qty;
    private Integer status;
    private String description;
    private String category;
    private String imgpath1;
    private String imgpath2;
    private String imgpath3;

    public Product(String id, String name, String price, Integer qty, Integer status, String description, String category, String imgpath1, String imgpath2, String imgpath3) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.status = status;
        this.description = description;
        this.category = category;
        this.imgpath1 = imgpath1;
        this.imgpath2 = imgpath2;
        this.imgpath3 = imgpath3;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgpath1() {
        return imgpath1;
    }

    public void setImgpath1(String imgpath1) {
        this.imgpath1 = imgpath1;
    }

    public String getImgpath2() {
        return imgpath2;
    }

    public void setImgpath2(String imgpath2) {
        this.imgpath2 = imgpath2;
    }

    public String getImgpath3() {
        return imgpath3;
    }

    public void setImgpath3(String imgpath3) {
        this.imgpath3 = imgpath3;
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
}
