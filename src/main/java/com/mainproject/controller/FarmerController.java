package com.mainproject.controller;

import com.mainproject.dao.UserDAO;
import com.mainproject.model.User;

import java.util.ArrayList;
import java.util.List;

public class FarmerController {

    private final UserDAO userDAO;

    public FarmerController() {
        userDAO = new UserDAO();
    }

    // =====================================================
    // GET ALL FARMERS
    // =====================================================

    public List<User> getAllFarmers() {

        try {

            List<User> users =
                    userDAO.getAllUsers();

            List<User> farmers =
                    new ArrayList<>();

            if (users == null) {
                return farmers;
            }

            for (User user : users) {

                if (user == null) {
                    continue;
                }

                /*
                 * Check the user's role.
                 *
                 * Adjust this if your User model
                 * uses a different role field.
                 */

                String role =
                        user.getRole();

                if (role != null &&
                        role.equalsIgnoreCase(
                                "farmer"
                        )) {

                    farmers.add(user);
                }
            }

            return farmers;

        } catch (Exception e) {

            System.out.println(
                    "Error loading farmers:"
            );

            e.printStackTrace();

            return new ArrayList<>();
        }
    }
}