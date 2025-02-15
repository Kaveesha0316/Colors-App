package model;

public class MyProduct {
    private String id;
    private String name;
    private String price;
    private String av_qty;
    private String sl_qty;
    private String profit;
    private String imgpath;
    private Integer status;

    private String imgpath2;
    private String imgpath3;
    private String description;
    private String category;


    public MyProduct(String id, String name, String price, String av_qty, String sl_qty, String profit, String imgpath, Integer status, String imgpath2, String imgpath3, String description, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.av_qty = av_qty;
        this.sl_qty = sl_qty;
        this.profit = profit;
        this.imgpath = imgpath;
        this.status = status;
        this.imgpath2 = imgpath2;
        this.imgpath3 = imgpath3;
        this.description = description;
        this.category = category;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }
}
