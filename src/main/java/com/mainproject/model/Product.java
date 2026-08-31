package com.mainproject.model;

public class Product {

    private String productId;
    private String farmerEmail;

    private String name;
    private String unit;
    private String category;
    private String variety;
    private String description;
    private String harvestDate;

    private double price;
    private double stock;

    private String status;

    private String imageUrl;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // Required by Firestore
    // =====================================================

    public Product() {
    }

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Product(
            String productId,
            String farmerEmail,
            String name,
            String unit,
            String category,
            String variety,
            String description,
            String harvestDate,
            double price,
            double stock,
            String status,
            String imageUrl) {

        this.productId = productId;
        this.farmerEmail = farmerEmail;
        this.name = name;
        this.unit = unit;
        this.category = category;
        this.variety = variety;
        this.description = description;
        this.harvestDate = harvestDate;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    // =====================================================
    // PRODUCT ID
    // =====================================================

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    // =====================================================
    // FARMER EMAIL
    // =====================================================

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }

    // =====================================================
    // NAME
    // =====================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // =====================================================
    // UNIT
    // =====================================================

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // =====================================================
    // CATEGORY
    // =====================================================

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // =====================================================
    // VARIETY
    // =====================================================

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    // =====================================================
    // DESCRIPTION
    // =====================================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // =====================================================
    // HARVEST DATE
    // =====================================================

    public String getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(String harvestDate) {
        this.harvestDate = harvestDate;
    }

    // =====================================================
    // PRICE
    // =====================================================

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // =====================================================
    // STOCK
    // =====================================================

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    // =====================================================
    // STATUS
    // =====================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =====================================================
    // IMAGE URL
    // =====================================================

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // =====================================================
    // TO STRING
    // =====================================================

    @Override
    public String toString() {

        return "Product{" +
                "productId='" + productId + '\'' +
                ", farmerEmail='" + farmerEmail + '\'' +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", category='" + category + '\'' +
                ", variety='" + variety + '\'' +
                ", description='" + description + '\'' +
                ", harvestDate='" + harvestDate + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", status='" + status + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}