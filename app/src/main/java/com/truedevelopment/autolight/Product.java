package com.truedevelopment.autolight;

public class Product {

   private String nickname;
   private String ProductID;

    public Product() {
    }

    public Product(String nickname, String ProductID) {
        this.nickname = nickname;
        this.ProductID = ProductID;
    }

    public String getnickname() {
        return nickname;
    }

    public void setnickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String ProductID) {
        this.ProductID = ProductID;
    }
}
