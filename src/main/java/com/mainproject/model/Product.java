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

    // Cloudinary image URL
    private String imageUrl;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // Required by Firestore
    // =====================================================

    public Product() {
    }

    // =====================================================
    // FULL CONSTRUCTOR
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
    // GETTERS
    // =====================================================

    public String getProductId() {
        return productId;
    }

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getCategory() {
        return category;
    }

    public String getVariety() {
        return variety;
    }

    public String getDescription() {
        return description;
    }

    public String getHarvestDate() {
        return harvestDate;
    }

    public double getPrice() {
        return price;
    }

    public double getStock() {
        return stock;
    }

    public String getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHarvestDate(String harvestDate) {
        this.harvestDate = harvestDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =====================================================
    // IMAGE URL SETTER
    // =====================================================

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