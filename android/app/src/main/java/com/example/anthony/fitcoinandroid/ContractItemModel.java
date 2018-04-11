package com.example.anthony.fitcoinandroid;

public class ContractItemModel {
//    let id: String
//    let sellerId: String
//    let userId: String
//    let productId: String
//    let productName: String
//    let quantity: Int
//    let cost: Int
//    let state: String
    String contractId;
    String sellerId;
    String userId;
    String productId;
    String productName;
    int quantity;
    int totalPrice;
    String state;

    public ContractItemModel(String contractId, String sellerId, String userId, String productId, String productName, int quantity, int totalPrice, String state) {
        this.contractId = contractId;
        this.sellerId = sellerId;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.state = state;
    }

    public String getContractId() {
        return contractId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
