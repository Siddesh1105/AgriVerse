package com.mainproject.controller;

import java.util.*;
import com.mainproject.model.Product;
import com.mainproject.dao.ProductDAO;

/** Controller layer for Product. */
public class ProductController {

    private final ProductDAO productDAO;

    public ProductController() {
        this.productDAO = new ProductDAO();
    }

    public boolean addProduct(Product product) {
        return productDAO.addProduct(product);
    }

    public boolean addProduct(
            Product product,
            String farmerEmail) {
        return productDAO.addProduct(product, farmerEmail);
    }

    public Product getProductById(
            String productId) {
        return productDAO.getProductById(productId);
    }

    public List<Product> getFarmerProducts(
            String farmerEmail) {
        return productDAO.getFarmerProducts(farmerEmail);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public int getProductCountByOwner(
            String farmerEmail) {
        return productDAO.getProductCountByOwner(farmerEmail);
    }

    public int getActiveProductCountByOwner(
            String farmerEmail) {
        return productDAO.getActiveProductCountByOwner(farmerEmail);
    }

    public boolean updateProduct(
            Product product) {
        return productDAO.updateProduct(product);
    }

    public boolean updateProductImage(
            String productId,
            String imageUrl) {
        return productDAO.updateProductImage(productId, imageUrl);
    }

    public boolean updateProductStatus(
            String productId,
            String status) {
        return productDAO.updateProductStatus(productId, status);
    }

    public boolean updateStock(
            String productId,
            double stock) {
        return productDAO.updateStock(productId, stock);
    }

    public boolean deleteProduct(
            String productId) {
        return productDAO.deleteProduct(productId);
    }

    public boolean productExists(
            String productId) {
        return productDAO.productExists(productId);
    }

}