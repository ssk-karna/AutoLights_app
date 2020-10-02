package com.truedevelopment.autolight;

public class Product {

    String deviceName,productID;

    public Product() {
    }

    public Product(String deviceName, String productID) {
        this.deviceName = deviceName;
        this.productID = productID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
