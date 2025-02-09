package model;

public class CustomerOrder {

    private String id;
    private String name;
    private String qty;
    private String price;
    private String date;
    private String cname;
    private String cmobile;
    private String ccity;
    private String caddrss;

    public CustomerOrder(String id, String name, String qty, String price, String date, String cname, String cmobile, String ccity, String caddrss) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.date = date;
        this.cname = cname;
        this.cmobile = cmobile;
        this.ccity = ccity;
        this.caddrss = caddrss;
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

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCmobile() {
        return cmobile;
    }

    public void setCmobile(String cmobile) {
        this.cmobile = cmobile;
    }

    public String getCcity() {
        return ccity;
    }

    public void setCcity(String ccity) {
        this.ccity = ccity;
    }

    public String getCaddrss() {
        return caddrss;
    }

    public void setCaddrss(String caddrss) {
        this.caddrss = caddrss;
    }
}
