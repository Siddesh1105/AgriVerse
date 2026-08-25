package com.mainproject.view.farmer;

import com.mainproject.util.LanguageManager;

import com.mainproject.controller.CartController;
import com.mainproject.model.CartItem;


import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final String farmerEmail;
    private final String farmerName;

    private final Runnable continueShopping;
    private final Runnable checkoutAction;

    private final CartController cartController =
            new CartController();

    private final List<CartItem> cartItems =
            new ArrayList<>();

    private VBox itemsContainer;

    private Label itemCountLabel;

    private Label totalLabel;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Cart(
            String farmerEmail,
            String farmerName,
            Runnable continueShopping,
            Runnable checkoutAction) {

        this.farmerEmail =
                farmerEmail;

        this.farmerName =
                farmerName;

        this.continueShopping =
                continueShopping;

        this.checkoutAction =
                checkoutAction;

        loadCart();
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root =
                new VBox(18);

        root.setPadding(
                new Insets(20));

        root.setStyle(
                "-fx-background-color:#F4FBF7;");

        // =================================================
        // HEADER
        // =================================================

        HBox header =
                new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT);

        Label title =
                new Label(
                        "My Cart");

        title.setStyle(
                "-fx-font-size:30px;"
                        + "-fx-font-weight:800;"
                        + "-fx-text-fill:#17202A;");

        HBox.setHgrow(
                title,
                Priority.ALWAYS);

        itemCountLabel =
                new Label();

        itemCountLabel.setStyle(
                "-fx-font-size:16px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#117864;");

        header.getChildren()
                .addAll(
                        title,
                        itemCountLabel);

        // =================================================
        // SUBTITLE
        // =================================================

        Label subtitle =
                new Label(
                        "Review the equipment you want to rent.");

        subtitle.setStyle(
                "-fx-font-size:15px;"
                        + "-fx-text-fill:#566573;");

        // =================================================
        // ITEMS
        // =================================================

        itemsContainer =
                new VBox(14);

        itemsContainer.setPadding(
                new Insets(5));

        ScrollPane scrollPane =
                new ScrollPane(
                        itemsContainer);

        scrollPane.setFitToWidth(
                true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setStyle(
                "-fx-background-color:transparent;"
                        + "-fx-border-color:transparent;");

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS);

        // =================================================
        // TOTAL
        // =================================================

        totalLabel =
                new Label();

        totalLabel.setStyle(
                "-fx-font-size:23px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#117864;");

        // =================================================
        // CONTINUE SHOPPING
        // =================================================

        Button continueButton =
                new Button(
                        "← Continue Shopping");

        continueButton.setPrefHeight(
                44);

        continueButton.setPrefWidth(
                230);

        continueButton.setStyle(
                "-fx-background-color:white;"
                        + "-fx-text-fill:#117864;"
                        + "-fx-font-weight:bold;"
                        + "-fx-border-color:#A2D9CE;"
                        + "-fx-border-radius:9px;"
                        + "-fx-background-radius:9px;"
                        + "-fx-cursor:hand;");

        continueButton.setOnAction(
                e -> {

                    if (continueShopping != null) {

                        continueShopping.run();
                    }
                });

        // =================================================
        // CHECKOUT
        // =================================================

        Button checkoutButton =
                new Button(
                        "Proceed to Checkout");

        checkoutButton.setPrefHeight(
                44);

        checkoutButton.setPrefWidth(
                245);

        checkoutButton.setStyle(
                "-fx-background-color:#117864;"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:9px;"
                        + "-fx-cursor:hand;");

        checkoutButton.setOnAction(
                e -> {

                    if (cartItems.isEmpty()) {

                        showAlert(
                                Alert.AlertType.WARNING,
                                "Empty Cart",
                                "Your cart is empty.");

                        return;
                    }

                    if (checkoutAction != null) {

                        checkoutAction.run();
                    }
                });

        HBox bottomButtons =
                new HBox(
                        12,
                        continueButton,
                        checkoutButton);

        bottomButtons.setAlignment(
                Pos.CENTER_RIGHT);

        VBox bottom =
                new VBox(
                        12,
                        totalLabel,
                        bottomButtons);

        bottom.setAlignment(
                Pos.CENTER_RIGHT);

        // =================================================
        // ROOT
        // =================================================

        root.getChildren()
                .addAll(
                        header,
                        subtitle,
                        scrollPane,
                        bottom);

        refreshView();

        LanguageManager.apply(root);
        return root;
    }

    // =====================================================
    // LOAD CART FROM FIRESTORE
    // =====================================================

    private void loadCart() {

        cartItems.clear();

        if (farmerEmail == null
                || farmerEmail
                .trim()
                .isEmpty()) {

            System.out.println(
                    "Farmer email is empty.");

            return;
        }

        List<CartItem> loaded =
                cartController.getCartItems(
                        farmerEmail);

        if (loaded != null) {

            cartItems.addAll(
                    loaded);
        }

        System.out.println(
                "Cart loaded for: "
                        + farmerEmail);

        System.out.println(
                "Cart item count: "
                        + cartItems.size());
    }

    // =====================================================
    // REFRESH CART
    // =====================================================

    private void refreshView() {

        if (itemsContainer == null) {

            return;
        }

        itemsContainer
                .getChildren()
                .clear();

        itemCountLabel.setText(
                "Items: "
                        + cartItems.size());

        if (cartItems.isEmpty()) {

            VBox emptyBox =
                    new VBox(10);

            emptyBox.setAlignment(
                    Pos.CENTER);

            emptyBox.setPadding(
                    new Insets(60));

            Label icon =
                    new Label("🛒");

            icon.setStyle(
                    "-fx-font-size:42px;");

            Label message =
                    new Label(
                            "Your cart is empty.");

            message.setStyle(
                    "-fx-font-size:18px;"
                            + "-fx-font-weight:bold;"
                            + "-fx-text-fill:#566573;");

            emptyBox.getChildren()
                    .addAll(
                            icon,
                            message);

            itemsContainer
                    .getChildren()
                    .add(
                            emptyBox);

            totalLabel.setText(
                    "Total: ₹0");

            return;
        }

        for (CartItem item :
                cartItems) {

            itemsContainer
                    .getChildren()
                    .add(
                            createCartCard(
                                    item));
        }

        updateTotal();
    }

    // =====================================================
    // CREATE CART CARD
    // =====================================================

    private HBox createCartCard(
            CartItem item) {

        HBox card =
                new HBox(18);

        card.setAlignment(
                Pos.CENTER_LEFT);

        card.setPadding(
                new Insets(12));

        card.setPrefHeight(
                150);

        card.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:14px;"
                        + "-fx-border-color:#A2D9CE;"
                        + "-fx-border-radius:14px;");

        // =================================================
        // IMAGE
        // =================================================

        VBox imageBox =
                new VBox();

        imageBox.setPrefWidth(
                190);

        imageBox.setPrefHeight(
                125);

        imageBox.setAlignment(
                Pos.CENTER);

        imageBox.setStyle(
                "-fx-background-color:#F1FAF6;"
                        + "-fx-background-radius:10px;");

        if (item.getImageUrl() != null
                && !item.getImageUrl()
                .trim()
                .isEmpty()) {

            try {

                Image image =
                        new Image(
                                item.getImageUrl(),
                                180,
                                120,
                                true,
                                true,
                                true);

                ImageView imageView =
                        new ImageView(
                                image);

                imageView.setFitWidth(
                        180);

                imageView.setFitHeight(
                        120);

                imageView.setPreserveRatio(
                        true);

                imageBox.getChildren()
                        .add(
                                imageView);

            } catch (Exception e) {

                addImagePlaceholder(
                        imageBox);
            }

        } else {

            addImagePlaceholder(
                    imageBox);
        }

        // =================================================
        // DETAILS
        // =================================================

        VBox details =
                new VBox(7);

        HBox.setHgrow(
                details,
                Priority.ALWAYS);

        Label name =
                new Label(
                        safe(
                                item.getEquipmentName(),
                                "Equipment"));

        name.setStyle(
                "-fx-font-size:19px;"
                        + "-fx-font-weight:800;"
                        + "-fx-text-fill:#17202A;");

        Label category =
                new Label(
                        safe(
                                item.getCategory(),
                                "Equipment"));

        category.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-text-fill:#566573;");

        Label location =
                new Label(
                        "Location: "
                                + safe(
                                        item.getLocation(),
                                        "-"));

        location.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-text-fill:#566573;");

        Label price =
                new Label(
                        "₹"
                                + formatPrice(
                                        item.getPricePerDay())
                                + " / day");

        price.setStyle(
                "-fx-font-size:16px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#117864;");

        details.getChildren()
                .addAll(
                        name,
                        category,
                        location,
                        price);

        // =================================================
        // RENTAL DAYS
        // =================================================

        VBox rentalBox =
                new VBox(6);

        rentalBox.setAlignment(
                Pos.CENTER);

        Label rentalTitle =
                new Label(
                        "Rental Days");

        rentalTitle.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-text-fill:#566573;");

        HBox quantityBox =
                new HBox(8);

        quantityBox.setAlignment(
                Pos.CENTER);

        Button minus =
                new Button("-");

        minus.setPrefWidth(
                42);

        minus.setPrefHeight(
                38);

        styleOutlineButton(
                minus);

        Label daysLabel =
                new Label(
                        String.valueOf(
                                item.getRentalDays()));

        daysLabel.setMinWidth(
                35);

        daysLabel.setAlignment(
                Pos.CENTER);

        daysLabel.setStyle(
                "-fx-font-size:16px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#17202A;");

        Button plus =
                new Button("+");

        plus.setPrefWidth(
                42);

        plus.setPrefHeight(
                38);

        styleOutlineButton(
                plus);

        // =================================================
        // MINUS
        // =================================================

        minus.setOnAction(
                e -> {

                    int currentDays =
                            item.getRentalDays();

                    if (currentDays <= 1) {

                        return;
                    }

                    updateRentalDays(
                            item,
                            currentDays - 1);
                });

        // =================================================
        // PLUS
        // =================================================

        plus.setOnAction(
                e -> {

                    int currentDays =
                            item.getRentalDays();

                    updateRentalDays(
                            item,
                            currentDays + 1);
                });

        quantityBox
                .getChildren()
                .addAll(
                        minus,
                        daysLabel,
                        plus);

        rentalBox
                .getChildren()
                .addAll(
                        rentalTitle,
                        quantityBox);

        // =================================================
        // ITEM TOTAL
        // =================================================

        VBox totalBox =
                new VBox(5);

        totalBox.setAlignment(
                Pos.CENTER_RIGHT);

        Label itemTotalTitle =
                new Label(
                        "Total");

        itemTotalTitle.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-text-fill:#566573;");

        Label itemTotal =
                new Label(
                        "₹"
                                + formatPrice(
                                        item.getTotalPrice()));

        itemTotal.setStyle(
                "-fx-font-size:18px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#117864;");

        // =================================================
        // REMOVE
        // =================================================

        Button remove =
                new Button(
                        "Remove");

        remove.setStyle(
                "-fx-background-color:transparent;"
                        + "-fx-text-fill:#C0392B;"
                        + "-fx-font-weight:bold;"
                        + "-fx-cursor:hand;");

        remove.setOnAction(
                e -> {

                    boolean success =
                            cartController.removeFromCart(
                                    item.getCartItemId());

                    if (success) {

                        cartItems.remove(
                                item);

                        refreshView();
                    } else {

                        showAlert(
                                Alert.AlertType.ERROR,
                                "Remove Failed",
                                "Unable to remove item from cart.");
                    }
                });

        totalBox
                .getChildren()
                .addAll(
                        itemTotalTitle,
                        itemTotal,
                        remove);

        // =================================================
        // CARD
        // =================================================

        card.getChildren()
                .addAll(
                        imageBox,
                        details,
                        rentalBox,
                        totalBox);

        return card;
    }

    // =====================================================
    // UPDATE RENTAL DAYS
    // =====================================================

    private void updateRentalDays(
            CartItem item,
            int newDays) {

        if (newDays < 1) {

            newDays = 1;
        }

        boolean success =
                cartController.updateRentalDays(
                        item.getCartItemId(),
                        newDays);

        if (!success) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Update Failed",
                    "Unable to update rental days.");

            return;
        }

        /*
         * Update local object.
         */

        item.setRentalDays(
                newDays);

        item.calculateTotal();

        /*
         * Rebuild the card so:
         *
         * Rental Days
         * Item Total
         * Grand Total
         *
         * all update immediately.
         */

        refreshView();
    }

    // =====================================================
    // UPDATE GRAND TOTAL
    // =====================================================

    private void updateTotal() {

        double total = 0;

        for (CartItem item :
                cartItems) {

            /*
             * Total =
             * price per day × rental days
             */

            item.calculateTotal();

            total +=
                    item.getTotalPrice();
        }

        totalLabel.setText(
                "Total: ₹"
                        + formatPrice(
                                total));
    }

    // =====================================================
    // IMAGE PLACEHOLDER
    // =====================================================

    private void addImagePlaceholder(
            VBox imageBox) {

        Label imageLabel =
                new Label("🚜");

        imageLabel.setStyle(
                "-fx-font-size:42px;");

        imageBox.getChildren()
                .add(
                        imageLabel);
    }

    // =====================================================
    // BUTTON STYLE
    // =====================================================

    private void styleOutlineButton(
            Button button) {

        button.setStyle(
                "-fx-background-color:white;"
                        + "-fx-text-fill:#117864;"
                        + "-fx-font-size:16px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-border-color:#A2D9CE;"
                        + "-fx-border-radius:8px;"
                        + "-fx-background-radius:8px;"
                        + "-fx-cursor:hand;");
    }

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(
            String value,
            String fallback) {

        return value == null
                || value.trim().isEmpty()
                ? fallback
                : value;
    }

    // =====================================================
    // FORMAT PRICE
    // =====================================================

    private String formatPrice(
            double value) {

        if (value == (long) value) {

            return String.format(
                    "%d",
                    (long) value);
        }

        return String.format(
                "%.2f",
                value);
    }

    // =====================================================
    // ALERT
    // =====================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(type);

        alert.setTitle(
                title);

        alert.setHeaderText(
                null);

        alert.setContentText(
                message);

        alert.showAndWait();
    }
}