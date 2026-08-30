package com.mainproject.controller;

import com.mainproject.dao.ProductOrderDAO;
import com.mainproject.model.Product;
import com.mainproject.model.ProductOrder;

import java.util.List;

public class ProductOrderController {

    private final ProductOrderDAO orderDAO;
    private final ProductController productController;

    public ProductOrderController() {

        orderDAO = new ProductOrderDAO();
        productController = new ProductController();
    }


    // =====================================================
    // CREATE ORDER
    // =====================================================

    public boolean createOrder(ProductOrder order) {

        if (order == null) {
            return false;
        }

        Product product =
                productController.getProductById(
                        order.getProductId()
                );

        if (product == null) {

            System.out.println(
                    "Product not found!"
            );

            return false;
        }

        // Check product availability

        if (product.getStatus() == null
                || !product.getStatus()
                .equalsIgnoreCase("active")) {

            System.out.println(
                    "Product is not available!"
            );

            return false;
        }

        // Check stock

        if (order.getQuantity() <= 0
                || order.getQuantity()
                > product.getStock()) {

            System.out.println(
                    "Invalid quantity or insufficient stock!"
            );

            return false;
        }

        // Automatically fill product information

        order.setProductName(product.getName());
        order.setProductImageUrl(product.getImageUrl());
        order.setCategory(product.getCategory());
        order.setUnit(product.getUnit());

        order.setFarmerEmail(
                product.getFarmerEmail()
        );

        order.setPricePerUnit(
                product.getPrice()
        );

        order.setTotalAmount(
                product.getPrice()
                        * order.getQuantity()
        );

        order.setStatus("pending");

        return orderDAO.createOrder(order);
    }


    // =====================================================
    // GET FARMER ORDERS
    // =====================================================

    public List<ProductOrder> getOrdersByFarmer(
            String farmerEmail) {

        return orderDAO.getOrdersByFarmer(
                farmerEmail
        );
    }


    // =====================================================
    // GET BUYER ORDERS
    // =====================================================

    public List<ProductOrder> getOrdersByBuyer(
            String buyerEmail) {

        return orderDAO.getOrdersByBuyer(
                buyerEmail
        );
    }


    // =====================================================
    // ACCEPT ORDER
    // REDUCE STOCK
    // =====================================================

    public boolean acceptOrder(String orderId) {

        ProductOrder order =
                orderDAO.getOrderById(orderId);

        if (order == null) {
            return false;
        }

        Product product =
                productController.getProductById(
                        order.getProductId()
                );

        if (product == null) {
            return false;
        }

        // Check stock again before accepting

        if (product.getStock()
                < order.getQuantity()) {

            System.out.println(
                    "Not enough stock available!"
            );

            return false;
        }

        double remainingStock =
                product.getStock()
                        - order.getQuantity();

        boolean stockUpdated =
                productController.updateStock(
                        product.getProductId(),
                        remainingStock
                );

        if (!stockUpdated) {
            return false;
        }

        boolean statusUpdated =
                orderDAO.updateOrderStatus(
                        orderId,
                        "accepted"
                );

        if (statusUpdated) {

            // Automatically mark unavailable
            // when stock becomes zero

            if (remainingStock <= 0) {

                productController
                        .updateProductStatus(
                                product.getProductId(),
                                "inactive"
                        );
            }

            return true;
        }

        return false;
    }


    // =====================================================
    // REJECT ORDER
    // =====================================================

    public boolean rejectOrder(String orderId) {

        return orderDAO.updateOrderStatus(
                orderId,
                "rejected"
        );
    }


    // =====================================================
    // MARK AS SHIPPED
    // =====================================================

    public boolean shipOrder(String orderId) {

        return orderDAO.updateOrderStatus(
                orderId,
                "shipped"
        );
    }


    // =====================================================
    // MARK AS DELIVERED
    // =====================================================

    public boolean deliverOrder(String orderId) {

        return orderDAO.updateOrderStatus(
                orderId,
                "delivered"
        );
    }


    public boolean completePayment(String orderId, String paymentId, String razorpayOrderId, String paymentMethod) {
        return orderDAO.completePayment(orderId, paymentId, razorpayOrderId, paymentMethod);
    }

    public boolean completePayment(String orderId, String paymentId, String paymentMethod) {
        return orderDAO.completePayment(orderId, paymentId, paymentMethod);
    }
}
