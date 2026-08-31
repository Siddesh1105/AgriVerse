package com.mainproject.controller;

import com.mainproject.dao.EquipmentRentalDAO;
import com.mainproject.model.EquipmentRental;
import java.util.List;

public class EquipmentRentalController {

    private final EquipmentRentalDAO rentalDAO;

    public EquipmentRentalController() {
        rentalDAO = new EquipmentRentalDAO();
    }

    public boolean createRental(EquipmentRental rental) {
        return rentalDAO.createRental(rental);
    }

    public EquipmentRental getRentalById(String rentalId) {
        return rentalDAO.getRentalById(rentalId);
    }

    public List<EquipmentRental> getRentalsByFarmer(String farmerEmail) {
        return rentalDAO.getRentalsByFarmer(farmerEmail);
    }

    public List<EquipmentRental> getRentalsByBuyer(String buyerEmail) {
        return rentalDAO.getRentalsByBuyer(buyerEmail);
    }

    public boolean acceptRental(String rentalId) {
        return rentalDAO.updateRentalStatus(rentalId, "accepted");
    }

    public boolean rejectRental(String rentalId) {
        return rentalDAO.updateRentalStatus(rentalId, "rejected");
    }

    public boolean completeRental(String rentalId) {
        return rentalDAO.updateRentalStatus(rentalId, "completed");
    }

    public boolean completePayment(String rentalId, String paymentId, String razorpayOrderId, String paymentMethod) {
        return rentalDAO.completePayment(rentalId, paymentId, razorpayOrderId, paymentMethod);
    }

    public boolean completePayment(String rentalId, String paymentId, String paymentMethod) {
        return rentalDAO.completePayment(rentalId, paymentId, paymentMethod);
    }

    public boolean updateRentalStatus(String rentalId, String status) {
        return rentalDAO.updateRentalStatus(rentalId, status);
    }

    public boolean deleteRental(String rentalId) {
        return rentalDAO.deleteRental(rentalId);
    }
}
