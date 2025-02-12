package DTO;



public class Product_image_DTO {

    public Product_image_DTO() {
    }


    private int id;

    private String image_path1;
    private String image_path2;

    private String image_path3;

    private Product_DTO product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_path1() {
        return image_path1;
    }

    public void setImage_path1(String image_path1) {
        this.image_path1 = image_path1;
    }

    public String getImage_path2() {
        return image_path2;
    }

    public void setImage_path2(String image_path2) {
        this.image_path2 = image_path2;
    }

    public String getImage_path3() {
        return image_path3;
    }

    public void setImage_path3(String image_path3) {
        this.image_path3 = image_path3;
    }

    public Product_DTO getProduct() {
        return product;
    }

    public void setProduct(Product_DTO product) {
        this.product = product;
    }
}
