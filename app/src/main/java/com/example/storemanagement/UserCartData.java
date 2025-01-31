package com.example.storemanagement;

public class UserCartData {
    ProductModel productModel;
    String userEmail;

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UserCartData(ProductModel productModel, String userEmail) {
        this.productModel = productModel;
        this.userEmail = userEmail;
    }

    public UserCartData(){

    }
}
