package com.mainproject.model;

import java.util.Date;

public class BuyerCartItem {

    private String cartItemId;
    private String buyerEmail;

    private String productId;
    private String productName;
    private String farmerEmail;

    private String unit;
    private double price;
    private double quantity;
    private double totalPrice;

    private String imageUrl;
    private Date updatedAt;

    public BuyerCartItem() {
    }

    public BuyerCartItem(
            String cartItemId,
            String buyerEmail,
            String productId,
            String productName,
            String farmerEmail,
            String unit,
            double price,
            double quantity,
            String imageUrl) {

        this.cartItemId = cartItemId;
        this.buyerEmail = buyerEmail;
        this.productId = productId;
        this.productName = productName;
        this.farmerEmail = farmerEmail;
        this.unit = unit;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        calculateTotal();
    }

    public void calculateTotal() {
        totalPrice = price * quantity;
    }

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        calculateTotal();
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
        calculateTotal();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
