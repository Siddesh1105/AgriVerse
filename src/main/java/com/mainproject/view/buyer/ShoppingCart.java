package com.mainproject.view.buyer;

import com.mainproject.controller.BuyerCartController;
import com.mainproject.model.BuyerCartItem;
import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class ShoppingCart {

    private final BuyerDashboard navigator;

    private final BuyerCartController cartController =
            new BuyerCartController();

    private final VBox itemsBox =
            new VBox(15);

    private final Label subtotalLabel =
            new Label();

    private final Label deliveryLabel =
            new Label();

    private final Label totalLabel =
            new Label();

    public ShoppingCart(BuyerDashboard navigator) {
        this.navigator = navigator;
    }

    // =====================================================
    // MAIN VIEW
    // =====================================================

    public Node getView() {

        BorderPane root = new BorderPane();

        root.setPadding(new Insets(25));
        root.setStyle(
                "-fx-background-color:#F8FAFC;"
        );

        // =================================================
        // HEADER
        // =================================================

        Label title =
                new Label("🛒 My Cart");

        title.setStyle(
                "-fx-font-size:26px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Button continueShopping =
                new Button("← Continue Shopping");

        continueShopping.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#CBD5E1;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-padding:8 15;" +
                "-fx-cursor:hand;"
        );

        continueShopping.setOnAction(e ->
                navigator.setView(
                        new LiveMarketplace(navigator).getView()
                )
        );

        HBox top =
                new HBox(18);

        top.setAlignment(Pos.CENTER_LEFT);

        top.getChildren().addAll(
                title,
                continueShopping
        );

        // =================================================
        // CART ITEMS
        // =================================================

        ScrollPane scroll =
                new ScrollPane(itemsBox);

        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;" +
                "-fx-border-color:transparent;"
        );

        itemsBox.setPadding(
                new Insets(5, 15, 20, 0)
        );

        // =================================================
        // ORDER SUMMARY
        // =================================================

        VBox summary =
                createSummary();

        root.setTop(top);
        root.setCenter(scroll);
        root.setRight(summary);

        BorderPane.setMargin(
                top,
                new Insets(0, 0, 20, 0)
        );

        BorderPane.setMargin(
                summary,
                new Insets(0, 0, 0, 25)
        );

        loadCart();

        LanguageManager.apply(root);

        return root;
    }

    // =====================================================
    // LOAD CART
    // =====================================================

    private void loadCart() {

        itemsBox.getChildren().clear();

        List<BuyerCartItem> items =
                cartController.getCartItems(
                        navigator.getBuyerEmail()
                );

        // =================================================
        // EMPTY CART
        // =================================================

        if (items == null || items.isEmpty()) {

            VBox emptyBox =
                    new VBox(15);

            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(50));

            Label icon =
                    new Label("🛒");

            icon.setStyle(
                    "-fx-font-size:50px;"
            );

            Label empty =
                    new Label("Your cart is empty");

            empty.setStyle(
                    "-fx-font-size:20px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-text-fill:#334155;"
            );

            Label message =
                    new Label(
                            "Browse the marketplace and add products to your cart."
                    );

            message.setStyle(
                    "-fx-font-size:14px;" +
                    "-fx-text-fill:#64748B;"
            );

            Button shopNow =
                    new Button("Continue Shopping");

            shopNow.setStyle(
                    "-fx-background-color:#117864;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:10 20;" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;"
            );

            shopNow.setOnAction(e ->
                    navigator.setView(
                            new LiveMarketplace(navigator).getView()
                    )
            );

            emptyBox.getChildren().addAll(
                    icon,
                    empty,
                    message,
                    shopNow
            );

            itemsBox.getChildren().add(
                    emptyBox
            );

            updateSummary(0);

            return;
        }

        // =================================================
        // LOAD PRODUCTS
        // =================================================

        double subtotal = 0;

        for (BuyerCartItem item : items) {

            if (item == null) {
                continue;
            }

            subtotal += item.getTotalPrice();

            itemsBox.getChildren().add(
                    createCartRow(item)
            );
        }

        updateSummary(subtotal);
    }

    // =====================================================
    // CART ITEM CARD
    // =====================================================

    private VBox createCartRow(
            BuyerCartItem item) {

        VBox card =
                new VBox(15);

        card.setPadding(
                new Insets(20)
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;"
        );

        // =================================================
        // PRODUCT INFORMATION
        // =================================================

        VBox productInfo =
                new VBox(8);

        Label productName =
                new Label(
                        safe(item.getProductName())
                );

        productName.setWrapText(true);

        productName.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label farmer =
                new Label(
                        "👨‍🌾 Farmer: "
                                + safe(item.getFarmerEmail())
                );

        farmer.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#64748B;"
        );

        Label price =
                new Label(
                        "💰 ₹"
                                + format(item.getPrice())
                                + " / "
                                + safe(item.getUnit())
                );

        price.setStyle(
                "-fx-font-size:15px;" +
                "-fx-text-fill:#475569;"
        );

        productInfo.getChildren().addAll(
                productName,
                farmer,
                price
        );

        // =================================================
        // TOP SECTION
        // =================================================

        HBox topRow =
                new HBox();

        topRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Label itemTotal =
                new Label(
                        "₹" +
                                format(
                                        item.getTotalPrice()
                                )
                );

        itemTotal.setStyle(
                "-fx-font-size:21px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#117864;"
        );

        topRow.getChildren().addAll(
                productInfo,
                spacer,
                itemTotal
        );

        // =================================================
        // QUANTITY SECTION
        // =================================================

        HBox bottomRow =
                new HBox(12);

        bottomRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label quantityLabel =
                new Label("Quantity:");

        quantityLabel.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#475569;"
        );

        int currentQuantity =
                Math.max(
                        1,
                        (int) Math.round(
                                item.getQuantity()
                        )
                );

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        1,
                        100000,
                        currentQuantity
                );

        Spinner<Integer> quantitySpinner =
                new Spinner<>();

        quantitySpinner.setValueFactory(
                valueFactory
        );

        quantitySpinner.setEditable(true);

        quantitySpinner.setPrefWidth(100);
        quantitySpinner.setPrefHeight(35);

        // =================================================
        // UPDATE QUANTITY
        // =================================================

        valueFactory.valueProperty().addListener(
                (obs, oldValue, newValue) -> {

                    if (newValue == null ||
                            oldValue == null ||
                            newValue.equals(oldValue)) {

                        return;
                    }

                    boolean success =
                            cartController.updateQuantity(
                                    item.getCartItemId(),
                                    newValue
                            );

                    if (success) {

                        loadCart();

                    } else {

                        showAlert(
                                Alert.AlertType.ERROR,
                                "Update Failed",
                                "Unable to update cart quantity."
                        );
                    }
                }
        );

        Region bottomSpacer =
                new Region();

        HBox.setHgrow(
                bottomSpacer,
                Priority.ALWAYS
        );

        Button remove =
                new Button("🗑 Remove");

        remove.setStyle(
                "-fx-background-color:#FEE2E2;" +
                "-fx-text-fill:#B91C1C;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:8 14;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        remove.setOnAction(e -> {

            boolean success =
                    cartController.removeFromCart(
                            item.getCartItemId()
                    );

            if (success) {

                loadCart();

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Remove Failed",
                        "Unable to remove this product from your cart."
                );
            }
        });

        bottomRow.getChildren().addAll(
                quantityLabel,
                quantitySpinner,
                bottomSpacer,
                remove
        );

        // =================================================
        // FINAL CARD
        // =================================================

        card.getChildren().addAll(
                topRow,
                new Separator(),
                bottomRow
        );

        return card;
    }

    // =====================================================
    // ORDER SUMMARY
    // =====================================================

    private VBox createSummary() {

        VBox summary =
                new VBox(16);

        summary.setPrefWidth(320);

        summary.setPadding(
                new Insets(25)
        );

        summary.setAlignment(
                Pos.TOP_LEFT
        );

        summary.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;"
        );

        Label heading =
                new Label("💰 Order Summary");

        heading.setStyle(
                "-fx-font-size:21px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Separator separator1 =
                new Separator();

        subtotalLabel.setStyle(
                "-fx-font-size:16px;" +
                "-fx-text-fill:#475569;"
        );

        deliveryLabel.setStyle(
                "-fx-font-size:16px;" +
                "-fx-text-fill:#475569;"
        );

        Separator separator2 =
                new Separator();

        totalLabel.setStyle(
                "-fx-font-size:23px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#117864;"
        );

        Button checkout =
                new Button("✓ Proceed to Checkout");

        checkout.setMaxWidth(
                Double.MAX_VALUE
        );

        checkout.setPrefHeight(50);

        checkout.setStyle(
                "-fx-background-color:#117864;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        checkout.setOnAction(e -> {

            List<BuyerCartItem> items =
                    cartController.getCartItems(
                            navigator.getBuyerEmail()
                    );

            if (items == null ||
                    items.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Cart Empty",
                        "Your cart is empty."
                );

                return;
            }

            navigator.setView(
                    new Checkout(navigator).getView()
            );
        });

        summary.getChildren().addAll(
                heading,
                separator1,
                subtotalLabel,
                deliveryLabel,
                separator2,
                totalLabel,
                checkout
        );

        return summary;
    }

    // =====================================================
    // UPDATE SUMMARY
    // =====================================================

    private void updateSummary(
            double subtotal) {

        double delivery =
                subtotal > 0 ? 40 : 0;

        double total =
                subtotal + delivery;

        subtotalLabel.setText(
                "Subtotal: ₹"
                        + format(subtotal)
        );

        deliveryLabel.setText(
                "Delivery: ₹"
                        + format(delivery)
        );

        totalLabel.setText(
                "Total: ₹"
                        + format(total)
        );
    }

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(
            String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }

    // =====================================================
    // FORMAT NUMBER
    // =====================================================

    private String format(
            double value) {

        if (value == Math.rint(value)) {

            return String.valueOf(
                    (long) value
            );
        }

        return String.format(
                "%.2f",
                value
        );
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

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}