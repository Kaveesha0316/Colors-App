package DTO;


public class Order_item_DTO {

    public Order_item_DTO() {
    }

    private int id;


    private String qty;


    private Double price;


    private Order_DTO order;


    private Product_DTO product;


    private Order_status_DTO order_status;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Order_DTO getOrder() {
        return order;
    }

    public void setOrder(Order_DTO order) {
        this.order = order;
    }

    public Product_DTO getProduct() {
        return product;
    }

    public void setProduct(Product_DTO product) {
        this.product = product;
    }

    public Order_status_DTO getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Order_status_DTO order_status) {
        this.order_status = order_status;
    }
}
