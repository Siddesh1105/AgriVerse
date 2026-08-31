package com.mainproject.controller;

import com.mainproject.dao.BuyerCartDAO;
import com.mainproject.model.BuyerCartItem;

import java.util.List;

public class BuyerCartController {

    private final BuyerCartDAO cartDAO;

    public BuyerCartController() {
        cartDAO = new BuyerCartDAO();
    }

    public boolean addToCart(BuyerCartItem item) {
        return cartDAO.addToCart(item);
    }

    public List<BuyerCartItem> getCartItems(String buyerEmail) {
        return cartDAO.getCartItems(buyerEmail);
    }

    public boolean updateQuantity(
            String cartItemId,
            double quantity) {
        return cartDAO.updateQuantity(cartItemId, quantity);
    }

    public boolean removeFromCart(String cartItemId) {
        return cartDAO.removeFromCart(cartItemId);
    }

    public boolean clearCart(String buyerEmail) {
        return cartDAO.clearCart(buyerEmail);
    }
}
