package com.mainproject.dao;

import com.mainproject.model.EquipmentRental;
import com.mainproject.model.Order;
import com.mainproject.model.OrderItem;
import com.mainproject.model.ProductOrder;
import java.util.List;

/** Read-only earnings calculations based only on already paid transactions. */
public class FarmerEarningsDAO {
    private final EquipmentRentalDAO rentalDAO = new EquipmentRentalDAO();
    private final ProductOrderDAO productOrderDAO = new ProductOrderDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public double getRentalEarnings(String farmerEmail) {
        double total = 0;
        for (EquipmentRental rental : rentalDAO.getRentalsByFarmer(farmerEmail)) {
            if (rental != null && "paid".equalsIgnoreCase(rental.getPaymentStatus())) total += rental.getTotalAmount();
        }
        return total;
    }

    public int getPaidRentalCount(String farmerEmail) {
        int count = 0;
        for (EquipmentRental rental : rentalDAO.getRentalsByFarmer(farmerEmail)) {
            if (rental != null && "paid".equalsIgnoreCase(rental.getPaymentStatus())) count++;
        }
        return count;
    }

    /** Earnings from paid single-product orders. */
    public double getDirectProductEarnings(String farmerEmail) {
        double total = 0;
        for (ProductOrder order : productOrderDAO.getOrdersByFarmer(farmerEmail)) {
            if (order != null && "paid".equalsIgnoreCase(order.getPaymentStatus())) total += order.getTotalAmount();
        }
        return total;
    }

    /** Earnings from paid multi-item/cart orders, counting only this farmer's items. */
    public double getCartProductEarnings(String farmerEmail) {
        double total = 0;
        for (Order order : orderDAO.getFarmerOrders(farmerEmail)) {
            if (order == null || !"paid".equalsIgnoreCase(order.getPaymentStatus()) || order.getItems() == null) continue;
            for (OrderItem item : order.getItems()) {
                if (item != null && item.getFarmerEmail() != null
                        && item.getFarmerEmail().equalsIgnoreCase(farmerEmail)) {
                    double amount = item.getTotalPrice();
                    if (amount <= 0) amount = item.getPrice() * item.getQuantity();
                    total += amount;
                }
            }
        }
        return total;
    }

    public double getProductEarnings(String farmerEmail) {
        return getDirectProductEarnings(farmerEmail) + getCartProductEarnings(farmerEmail);
    }

    public int getPaidProductCount(String farmerEmail) {
        int count = 0;
        for (ProductOrder order : productOrderDAO.getOrdersByFarmer(farmerEmail)) {
            if (order != null && "paid".equalsIgnoreCase(order.getPaymentStatus())) count++;
        }
        for (Order order : orderDAO.getFarmerOrders(farmerEmail)) {
            if (order == null || !"paid".equalsIgnoreCase(order.getPaymentStatus()) || order.getItems() == null) continue;
            for (OrderItem item : order.getItems()) {
                if (item != null && item.getFarmerEmail() != null && item.getFarmerEmail().equalsIgnoreCase(farmerEmail)) count++;
            }
        }
        return count;
    }

    public double getTotalEarnings(String farmerEmail) {
        return getProductEarnings(farmerEmail) + getRentalEarnings(farmerEmail);
    }
}
