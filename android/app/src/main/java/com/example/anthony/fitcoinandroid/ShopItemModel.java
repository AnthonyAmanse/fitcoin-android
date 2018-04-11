package com.example.anthony.fitcoinandroid;

public class ShopItemModel {

    String sellerId;
    String productId;
    String productName;
    int quantityLeft;
    int price;

    public ShopItemModel(String sellerId, String productId, String productName, int quantityLeft, int price) {
        this.sellerId = sellerId;
        this.productId = productId;
        this.productName = productName;
        this.quantityLeft = quantityLeft;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantityLeft() {
        return quantityLeft;
    }

    public int getPrice() {
        return price;
    }
}
