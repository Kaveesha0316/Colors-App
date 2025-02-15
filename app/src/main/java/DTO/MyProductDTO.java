package DTO;

public class MyProductDTO {
    private Integer id;
    private String name;
    private String imgpath;
    private Double price;
    private Integer avqty;
    private Integer selqty;
    private Double profit;
    private Integer status;

    private String imgpath2;
    private String imgpath3;
    private String description;
    private String category;

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

    public MyProductDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAvqty() {
        return avqty;
    }

    public void setAvqty(Integer avqty) {
        this.avqty = avqty;
    }

    public Integer getSelqty() {
        return selqty;
    }

    public void setSelqty(Integer selqty) {
        this.selqty = selqty;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
