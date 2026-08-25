package com.mainproject.controller;

import java.util.*;
import com.mainproject.model.User;
import com.mainproject.dao.UserDAO;

/** Controller layer for User. */
public class UserController {

    private final UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    public boolean saveUser(User user) {
        return userDAO.saveUser(user);
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public boolean updateProfile(User user) {
        return userDAO.updateProfile(user);
    }

    public boolean updateProfileImage(
            String email,
            String imageUrl) {
        return userDAO.updateProfileImage(email, imageUrl);
    }

    public boolean userExists(String email) {
        return userDAO.userExists(email);
    }

}