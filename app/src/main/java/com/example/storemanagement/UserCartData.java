package com.example.storemanagement;

import java.util.ArrayList;
import java.util.List;

public class UserCartData {
    ArrayList<ProductModel> productModel;
//    String userEmail;

    public ArrayList<ProductModel> getProductModel() {
        return productModel;
    }

    public void setProductModel(ArrayList<ProductModel> productModel) {
        this.productModel = productModel;
    }

//    public String getUserEmail() {
//        return userEmail;
//    }
//
//    public void setUserEmail(String userEmail) {
//        this.userEmail = userEmail;
//    }

    public UserCartData(ArrayList<ProductModel> productModel) {
        this.productModel = productModel;
    }

    public UserCartData(){

    }
}
