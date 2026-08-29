package com.mainproject.controller;

import com.mainproject.dao.WishlistDAO;
import com.mainproject.model.WishlistItem;

import java.util.List;

public class WishlistController {

    private final WishlistDAO wishlistDAO;

    public WishlistController() {

        wishlistDAO =
                new WishlistDAO();
    }

    // =====================================================
    // ADD
    // =====================================================

    public boolean addToWishlist(
            WishlistItem item) {

        return wishlistDAO.addToWishlist(
                item
        );
    }

    // =====================================================
    // GET
    // =====================================================

    public List<WishlistItem> getWishlist(
            String buyerEmail) {

        return wishlistDAO.getWishlist(
                buyerEmail
        );
    }

    // =====================================================
    // CHECK
    // =====================================================

    public boolean isInWishlist(
            String buyerEmail,
            String productId) {

        return wishlistDAO.isInWishlist(
                buyerEmail,
                productId
        );
    }

    // =====================================================
    // REMOVE
    // =====================================================

    public boolean removeFromWishlist(
            String wishlistId) {

        return wishlistDAO.removeFromWishlist(
                wishlistId
        );
    }

    // =====================================================
    // REMOVE PRODUCT
    // =====================================================

    public boolean removeProductFromWishlist(
            String buyerEmail,
            String productId) {

        return wishlistDAO
                .removeProductFromWishlist(
                        buyerEmail,
                        productId
                );
    }
}