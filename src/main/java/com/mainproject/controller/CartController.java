package com.mainproject.controller;

import java.util.*;
import com.mainproject.model.CartItem;
import com.mainproject.dao.CartDAO;

/** Controller layer for CartItem. */
public class CartController {

    private final CartDAO cartDAO;

    public CartController() {
        this.cartDAO = new CartDAO();
    }

    public boolean addToCart(
            CartItem item) {
        return cartDAO.addToCart(item);
    }

    public List<CartItem> getCartItems(
            String farmerEmail) {
        return cartDAO.getCartItems(farmerEmail);
    }

    public boolean updateRentalDays(
            String cartItemId,
            int rentalDays) {
        return cartDAO.updateRentalDays(cartItemId, rentalDays);
    }

    public boolean removeFromCart(
            String cartItemId) {
        return cartDAO.removeFromCart(cartItemId);
    }

}