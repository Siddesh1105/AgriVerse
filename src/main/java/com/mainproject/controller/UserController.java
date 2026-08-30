package com.mainproject.controller;

import java.util.List;

import com.mainproject.dao.UserDAO;
import com.mainproject.model.User;

/**
 * Controller layer for User.
 */
public class UserController {

    private final UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    // =====================================================
    // SAVE USER
    // =====================================================

    public boolean saveUser(User user) {
        return userDAO.saveUser(user);
    }

    // =====================================================
    // GET USER BY EMAIL
    // =====================================================

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    // =====================================================
    // UPDATE PROFILE
    // =====================================================

    public boolean updateProfile(User user) {
        return userDAO.updateProfile(user);
    }

    // =====================================================
    // UPDATE PROFILE IMAGE
    // =====================================================

    public boolean updateProfileImage(
            String email,
            String imageUrl) {

        return userDAO.updateProfileImage(
                email,
                imageUrl
        );
    }

    // =====================================================
    // USER EXISTS
    // =====================================================

    public boolean userExists(String email) {
        return userDAO.userExists(email);
    }

    // =====================================================
    // GET ALL USERS
    // =====================================================

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // =====================================================
    // GET ALL FARMERS
    // =====================================================

    public List<User> getAllFarmers() {
        return userDAO.getAllFarmers();
    }

    // =====================================================
    // UPDATE FARMER VERIFICATION
    // =====================================================

    public boolean updateFarmerVerification(
            String email,
            String status,
            String rejectionReason) {

        return userDAO.updateFarmerVerification(
                email,
                status,
                rejectionReason
        );
    }
}