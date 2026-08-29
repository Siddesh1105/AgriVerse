package com.mainproject.view.buyer;

import com.mainproject.controller.FarmerController;
import com.mainproject.model.User;
import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class Farmers {

    private final BuyerDashboard mainController;

    private final FarmerController farmerController =
            new FarmerController();

    public Farmers(BuyerDashboard controller) {
        this.mainController = controller;
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root = new VBox(20);

        root.setPadding(
                new Insets(25, 30, 25, 30)
        );

        root.setStyle(
                "-fx-background-color:#F8FAFC;"
        );

        // =================================================
        // HEADER
        // =================================================

        Label title =
                new Label("👨‍🌾 Farmers");

        title.setStyle(
                "-fx-font-size:25px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label subtitle =
                new Label(
                        "Explore farmers and their products."
                );

        subtitle.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#64748B;"
        );

        VBox header =
                new VBox(5);

        header.getChildren().addAll(
                title,
                subtitle
        );

        // =================================================
        // SEARCH
        // =================================================

        TextField search =
                new TextField();

        search.setPromptText(
                "Search farmers..."
        );

        search.setPrefHeight(40);

        search.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#CBD5E1;" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-padding:8 12;"
        );

        // =================================================
        // FARMER GRID
        // =================================================

        GridPane grid =
                new GridPane();

        grid.setHgap(18);
        grid.setVgap(18);

        // =================================================
        // LOAD FARMERS
        // =================================================

        List<User> farmers =
                farmerController.getAllFarmers();

        // =================================================
        // DEBUG FARMERS
        // =================================================

        System.out.println(
                "===================================="
        );

        System.out.println(
                "Farmers loaded for Buyer:"
        );

        if (farmers != null) {

            System.out.println(
                    "Total Farmers: "
                            + farmers.size()
            );

            for (User farmer : farmers) {

                if (farmer == null) {
                    continue;
                }

                System.out.println(
                        "------------------------------------"
                );

                System.out.println(
                        "Farmer Name: "
                                + farmer.getFullName()
                );

                System.out.println(
                        "Farmer Email: "
                                + farmer.getEmail()
                );

                System.out.println(
                        "Farmer Role: "
                                + farmer.getRole()
                );
            }

        } else {

            System.out.println(
                    "Farmer list is NULL."
            );
        }

        System.out.println(
                "===================================="
        );

        // =================================================
        // DISPLAY FARMERS
        // =================================================

        populateFarmers(
                grid,
                farmers,
                ""
        );

        // =================================================
        // SEARCH
        // =================================================

        search.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    String query =
                            newValue == null
                                    ? ""
                                    : newValue
                                            .trim()
                                            .toLowerCase();

                    populateFarmers(
                            grid,
                            farmers,
                            query
                    );
                }
        );

        // =================================================
        // ROOT
        // =================================================

        root.getChildren().addAll(
                header,
                search,
                grid
        );

        // =================================================
        // LANGUAGE
        // =================================================

        LanguageManager.apply(root);

        // =================================================
        // SCROLL
        // =================================================

        ScrollPane scroll =
                new ScrollPane(root);

        scroll.setFitToWidth(true);

        scroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;"
        );

        return scroll;
    }

    // =====================================================
    // POPULATE FARMERS
    // =====================================================

    private void populateFarmers(
            GridPane grid,
            List<User> farmers,
            String query) {

        grid.getChildren().clear();

        if (farmers == null ||
                farmers.isEmpty()) {

            showEmptyMessage(
                    grid,
                    "👨‍🌾",
                    "No farmers found."
            );

            return;
        }

        int column = 0;
        int row = 0;

        int displayedCount = 0;

        for (User farmer : farmers) {

            if (farmer == null) {
                continue;
            }

            String farmerName =
                    getUserName(farmer);

            String farmerEmail =
                    safe(farmer.getEmail());

            String searchableText =
                    (
                            farmerName
                                    + " "
                                    + farmerEmail
                    ).toLowerCase();

            if (!query.isEmpty() &&
                    !searchableText.contains(query)) {

                continue;
            }

            VBox card =
                    createFarmerCard(
                            farmer
                    );

            grid.add(
                    card,
                    column,
                    row
            );

            displayedCount++;

            column++;

            if (column == 3) {

                column = 0;
                row++;
            }
        }

        if (displayedCount == 0) {

            showEmptyMessage(
                    grid,
                    "🔍",
                    "No farmers match your search."
            );
        }
    }

    // =====================================================
    // FARMER CARD
    // =====================================================

    private VBox createFarmerCard(
            User farmer) {

        VBox card =
                new VBox(10);

        card.setPrefWidth(300);

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;"
        );

        // =================================================
        // ICON
        // =================================================

        Label icon =
                new Label("👨‍🌾");

        icon.setStyle(
                "-fx-font-size:38px;"
        );

        // =================================================
        // NAME
        // =================================================

        String farmerName =
                getUserName(farmer);

        Label name =
                new Label(
                        farmerName
                                + " ✔ Verified Farmer"
                );

        name.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#166534;"
        );

        name.setWrapText(true);

        // =================================================
        // EMAIL
        // =================================================

        String farmerEmail =
                safe(
                        farmer.getEmail()
                );

        Label email =
                new Label(
                        "📧 "
                                + (
                                farmerEmail.isEmpty()
                                        ? "Email not available"
                                        : farmerEmail
                        )
                );

        email.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#64748B;"
        );

        email.setWrapText(true);

        // =================================================
        // MOBILE
        // =================================================

        Label phone =
                new Label();

        String mobile =
                safe(
                        farmer.getMobileNumber()
                );

        if (!mobile.isEmpty()) {

            phone.setText(
                    "📱 " + mobile
            );

        } else {

            phone.setText(
                    "📱 Contact not available"
            );
        }

        phone.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#64748B;"
        );

        // =================================================
        // ROLE
        // =================================================

        Label role =
                new Label(
                        "🌱 Farmer"
                );

        role.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#64748B;"
        );

        // =================================================
        // VIEW PROFILE
        // =================================================

        Button view =
                new Button(
                        "View Profile"
                );

        view.setMaxWidth(
                Double.MAX_VALUE
        );

        view.setStyle(
                "-fx-background-color:#117864;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-padding:9;" +
                "-fx-cursor:hand;"
        );

        view.setOnAction(e -> {

            /*
             * =================================================
             * GET EMAIL FROM SELECTED FARMER
             * =================================================
             */

            String selectedEmail =
                    safe(
                            farmer.getEmail()
                    );

            String selectedName =
                    getUserName(farmer);

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "SELECTED FARMER"
            );

            System.out.println(
                    "Name  : "
                            + selectedName
            );

            System.out.println(
                    "Email : "
                            + selectedEmail
            );

            System.out.println(
                    "Role  : "
                            + farmer.getRole()
            );

            System.out.println(
                    "===================================="
            );

            // =================================================
            // EMAIL VALIDATION
            // =================================================

            if (selectedEmail.isEmpty()) {

                Alert alert =
                        new Alert(
                                Alert.AlertType.ERROR
                        );

                alert.setTitle(
                        "Farmer Information Error"
                );

                alert.setHeaderText(
                        "Farmer Email Missing"
                );

                alert.setContentText(
                        "The selected farmer does not have " +
                        "an email in the User object."
                );

                alert.showAndWait();

                return;
            }

            // =================================================
            // OPEN FARMER PROFILE
            // =================================================

            mainController.setView(
                    new FarmerProfile(
                            mainController,
                            selectedEmail,
                            selectedName
                    ).getView()
            );
        });

        // =================================================
        // CARD CONTENT
        // =================================================

        card.getChildren().addAll(
                icon,
                name,
                email,
                phone,
                role,
                view
        );

        return card;
    }

    // =====================================================
    // GET USER NAME
    // =====================================================

    private String getUserName(
            User user) {

        if (user == null) {
            return "Farmer";
        }

        String fullName =
                safe(
                        user.getFullName()
                ).trim();

        if (!fullName.isEmpty()) {
            return fullName;
        }

        return "Farmer";
    }

    // =====================================================
    // EMPTY MESSAGE
    // =====================================================

    private void showEmptyMessage(
            GridPane grid,
            String iconText,
            String messageText) {

        VBox empty =
                new VBox(10);

        empty.setAlignment(
                Pos.CENTER
        );

        empty.setPadding(
                new Insets(40)
        );

        Label icon =
                new Label(iconText);

        icon.setStyle(
                "-fx-font-size:42px;"
        );

        Label message =
                new Label(messageText);

        message.setStyle(
                "-fx-font-size:16px;" +
                "-fx-text-fill:#64748B;"
        );

        empty.getChildren().addAll(
                icon,
                message
        );

        grid.add(
                empty,
                0,
                0
        );
    }

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }
}