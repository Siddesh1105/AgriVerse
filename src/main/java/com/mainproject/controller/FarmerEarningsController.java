package com.mainproject.controller;

import com.mainproject.dao.FarmerEarningsDAO;

public class FarmerEarningsController {
    private final FarmerEarningsDAO dao = new FarmerEarningsDAO();
    public double getRentalEarnings(String farmerEmail) { return dao.getRentalEarnings(farmerEmail); }
    public int getPaidRentalCount(String farmerEmail) { return dao.getPaidRentalCount(farmerEmail); }
    public double getProductEarnings(String farmerEmail) { return dao.getProductEarnings(farmerEmail); }
    public int getPaidProductCount(String farmerEmail) { return dao.getPaidProductCount(farmerEmail); }
    public double getTotalEarnings(String farmerEmail) { return dao.getTotalEarnings(farmerEmail); }
}
