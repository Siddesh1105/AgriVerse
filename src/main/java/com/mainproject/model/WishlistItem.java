package com.mainproject.model;

import java.util.Date;

public class WishlistItem {

    private String wishlistId;

    private String buyerEmail;

    private String productId;
    private String productName;
    private String farmerEmail;

    private String unit;
    private String category;

    private double price;
    private double stock;

    private String imageUrl;

    private Date createdAt;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // Required by Firestore
    // =====================================================

    public WishlistItem() {
    }

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public WishlistItem(
            String buyerEmail,
            Product product) {

        this.buyerEmail = buyerEmail;

        if (product != null) {

            this.productId =
                    product.getProductId();

            this.productName =
                    product.getName();

            this.farmerEmail =
                    product.getFarmerEmail();

            this.unit =
                    product.getUnit();

            this.category =
                    product.getCategory();

            this.price =
                    product.getPrice();

            this.stock =
                    product.getStock();

            this.imageUrl =
                    product.getImageUrl();
        }

        this.createdAt =
                new Date();
    }

    // =====================================================
    // WISHLIST ID
    // =====================================================

    public String getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(
            String wishlistId) {

        this.wishlistId =
                wishlistId;
    }

    // =====================================================
    // BUYER EMAIL
    // =====================================================

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(
            String buyerEmail) {

        this.buyerEmail =
                buyerEmail;
    }

    // =====================================================
    // PRODUCT ID
    // =====================================================

    public String getProductId() {
        return productId;
    }

    public void setProductId(
            String productId) {

        this.productId =
                productId;
    }

    // =====================================================
    // PRODUCT NAME
    // =====================================================

    public String getProductName() {
        return productName;
    }

    public void setProductName(
            String productName) {

        this.productName =
                productName;
    }

    // =====================================================
    // FARMER EMAIL
    // =====================================================

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(
            String farmerEmail) {

        this.farmerEmail =
                farmerEmail;
    }

    // =====================================================
    // UNIT
    // =====================================================

    public String getUnit() {
        return unit;
    }

    public void setUnit(
            String unit) {

        this.unit =
                unit;
    }

    // =====================================================
    // CATEGORY
    // =====================================================

    public String getCategory() {
        return category;
    }

    public void setCategory(
            String category) {

        this.category =
                category;
    }

    // =====================================================
    // PRICE
    // =====================================================

    public double getPrice() {
        return price;
    }

    public void setPrice(
            double price) {

        this.price =
                price;
    }

    // =====================================================
    // STOCK
    // =====================================================

    public double getStock() {
        return stock;
    }

    public void setStock(
            double stock) {

        this.stock =
                stock;
    }

    // =====================================================
    // IMAGE URL
    // =====================================================

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(
            String imageUrl) {

        this.imageUrl =
                imageUrl;
    }

    // =====================================================
    // CREATED AT
    // =====================================================

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            Date createdAt) {

        this.createdAt =
                createdAt;
    }
}