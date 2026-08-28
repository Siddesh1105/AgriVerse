package com.mainproject.view.buyer;

import com.mainproject.controller.BuyerCartController;
import com.mainproject.controller.OrderController;
import com.mainproject.controller.NotificationController;
import com.mainproject.model.BuyerCartItem;
import com.mainproject.model.Order;
import com.mainproject.model.OrderItem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Checkout {

    private final BuyerDashboard navigator;

    private final BuyerCartController cartController =
            new BuyerCartController();

    private final OrderController orderController =
            new OrderController();

    private final NotificationController notificationController =
            new NotificationController();

    private final VBox itemsBox =
            new VBox(12);

    private final Label subtotalLabel =
            new Label();

    private final Label deliveryLabel =
            new Label();

    private final Label totalLabel =
            new Label();

    private final TextArea addressField =
            new TextArea();

    private final ToggleGroup paymentGroup =
            new ToggleGroup();

    private double subtotal = 0;
    private double deliveryCharge = 0;
    private double totalAmount = 0;

    public Checkout(BuyerDashboard navigator) {
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

        // =====================================================
        // HEADER
        // =====================================================

        Label title = new Label("🛒 Checkout");

        title.setStyle(
                "-fx-font-size:28px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label subtitle =
                new Label(
                        "Review your order and complete checkout"
                );

        subtitle.setStyle(
                "-fx-font-size:15px;" +
                "-fx-text-fill:#64748B;"
        );

        VBox titleBox = new VBox(5);
        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        Button backButton =
                new Button("← Back to Cart");

        backButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#CBD5E1;" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-padding:10 16;" +
                "-fx-font-size:14px;" +
                "-fx-cursor:hand;"
        );

        backButton.setOnAction(e ->
                navigator.setView(
                        new ShoppingCart(navigator).getView()
                )
        );

        HBox header = new HBox();

        header.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(
                titleBox,
                Priority.ALWAYS
        );

        header.getChildren().addAll(
                titleBox,
                backButton
        );

        root.setTop(header);

        // =====================================================
        // LEFT SIDE
        // =====================================================

        VBox leftContent = new VBox(20);

        leftContent.setPadding(
                new Insets(20, 20, 20, 0)
        );

        VBox orderSection =
                createOrderSection();

        VBox addressSection =
                createAddressSection();

        VBox paymentSection =
                createPaymentSection();

        leftContent.getChildren().addAll(
                orderSection,
                addressSection,
                paymentSection
        );

        ScrollPane leftScroll =
                new ScrollPane(leftContent);

        leftScroll.setFitToWidth(true);

        leftScroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;" +
                "-fx-border-color:transparent;"
        );

        // =====================================================
        // RIGHT SIDE - SUMMARY
        // =====================================================

        VBox summary =
                createOrderSummary();

        summary.setPrefWidth(350);
        summary.setMinWidth(320);

        BorderPane.setMargin(
                summary,
                new Insets(20, 0, 20, 25)
        );

        // =====================================================
        // CENTER
        // =====================================================

        root.setCenter(leftScroll);
        root.setRight(summary);

        // =====================================================
        // LOAD CART
        // =====================================================

        loadOrderItems();

        return root;
    }

    // =====================================================
    // ORDER ITEMS SECTION
    // =====================================================

    private VBox createOrderSection() {

        VBox section = new VBox(12);

        section.setPadding(new Insets(22));

        section.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;"
        );

        Label heading =
                new Label("📦 Order Items");

        heading.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        section.getChildren().addAll(
                heading,
                new Separator(),
                itemsBox
        );

        return section;
    }

    // =====================================================
    // LOAD CART ITEMS
    // =====================================================

    private void loadOrderItems() {

        itemsBox.getChildren().clear();

        List<BuyerCartItem> items =
                cartController.getCartItems(
                        navigator.getBuyerEmail()
                );

        subtotal = 0;

        if (items == null || items.isEmpty()) {

            Label empty =
                    new Label("Your cart is empty.");

            empty.setStyle(
                    "-fx-font-size:16px;" +
                    "-fx-text-fill:#64748B;"
            );

            itemsBox.getChildren().add(empty);

            updateSummary();

            return;
        }

        for (BuyerCartItem item : items) {

            if (item == null) {
                continue;
            }

            subtotal += item.getTotalPrice();

            itemsBox.getChildren().add(
                    createOrderItem(item)
            );
        }

        updateSummary();
    }

    // =====================================================
    // CREATE ORDER ITEM
    // =====================================================

    private VBox createOrderItem(
            BuyerCartItem item) {

        VBox card = new VBox(8);

        card.setPadding(new Insets(15));

        card.setStyle(
                "-fx-background-color:#F8FAFC;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;"
        );

        // Product Name

        Label productName =
                new Label(
                        safe(item.getProductName())
                );

        productName.setStyle(
                "-fx-font-size:17px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        // Farmer

        Label farmer =
                new Label(
                        "🌾 Farmer: "
                                + safe(
                                item.getFarmerEmail()
                        )
                );

        farmer.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#64748B;"
        );

        // Quantity

        Label quantity =
                new Label(
                        "Quantity: "
                                + format(
                                item.getQuantity()
                        )
                                + " "
                                + safe(
                                item.getUnit()
                        )
                );

        quantity.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#475569;"
        );

        // Price

        Label price =
                new Label(
                        "₹"
                                + format(item.getPrice())
                                + " / "
                                + safe(item.getUnit())
                );

        price.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#475569;"
        );

        Label amount =
                new Label(
                        "₹"
                                + format(
                                item.getTotalPrice()
                        )
                );

        amount.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#117864;"
        );

        HBox bottomRow = new HBox();

        HBox.setHgrow(
                price,
                Priority.ALWAYS
        );

        bottomRow.setAlignment(
                Pos.CENTER_LEFT
        );

        bottomRow.getChildren().addAll(
                price,
                amount
        );

        card.getChildren().addAll(
                productName,
                farmer,
                quantity,
                bottomRow
        );

        return card;
    }

    // =====================================================
    // DELIVERY ADDRESS
    // =====================================================

    private VBox createAddressSection() {

        VBox section = new VBox(12);

        section.setPadding(new Insets(22));

        section.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;"
        );

        Label heading =
                new Label("📍 Delivery Address");

        heading.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label instruction =
                new Label(
                        "Enter your complete delivery address"
                );

        instruction.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#64748B;"
        );

        addressField.setPromptText(
                "House / Building\nStreet / Area\nCity, District, State - PIN Code"
        );

        addressField.setPrefRowCount(4);

        addressField.setWrapText(true);

        addressField.setStyle(
                "-fx-font-size:15px;" +
                "-fx-background-radius:8;" +
                "-fx-border-radius:8;" +
                "-fx-border-color:#CBD5E1;" +
                "-fx-background-color:white;"
        );

        section.getChildren().addAll(
                heading,
                instruction,
                addressField
        );

        return section;
    }

    // =====================================================
    // PAYMENT SECTION
    // =====================================================

    private VBox createPaymentSection() {

        VBox section = new VBox(14);

        section.setPadding(new Insets(22));

        section.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;"
        );

        Label heading =
                new Label("💳 Payment Method");

        heading.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label instruction =
                new Label(
                        "Choose your preferred payment method"
                );

        instruction.setStyle(
                "-fx-font-size:15px;" +
                "-fx-text-fill:#64748B;"
        );

        Separator separator =
                new Separator();

        // =================================================
        // PAYMENT OPTIONS
        // =================================================

        RadioButton upi =
                new RadioButton();

        upi.setToggleGroup(paymentGroup);
        upi.setUserData("UPI");
        upi.setSelected(true);

        VBox upiText = new VBox(3);

        Label upiTitle =
                new Label("📱 UPI Payment");

        upiTitle.setStyle(
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label upiDescription =
                new Label(
                        "Pay using Google Pay, PhonePe, Paytm or other UPI apps"
                );

        upiDescription.setWrapText(true);

        upiDescription.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#64748B;"
        );

        upiText.getChildren().addAll(
                upiTitle,
                upiDescription
        );

        HBox upiOption =
                createPaymentOption(
                        upi,
                        upiText
                );


        // CARD

        RadioButton card =
                new RadioButton();

        card.setToggleGroup(paymentGroup);
        card.setUserData("CARD");

        VBox cardText = new VBox(3);

        Label cardTitle =
                new Label("💳 Debit / Credit Card");

        cardTitle.setStyle(
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label cardDescription =
                new Label(
                        "Pay securely using your debit or credit card"
                );

        cardDescription.setWrapText(true);

        cardDescription.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#64748B;"
        );

        cardText.getChildren().addAll(
                cardTitle,
                cardDescription
        );

        HBox cardOption =
                createPaymentOption(
                        card,
                        cardText
                );


        // CASH ON DELIVERY

        RadioButton cod =
                new RadioButton();

        cod.setToggleGroup(paymentGroup);
        cod.setUserData("COD");

        VBox codText = new VBox(3);

        Label codTitle =
                new Label("💵 Cash on Delivery");

        codTitle.setStyle(
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label codDescription =
                new Label(
                        "Pay cash when your order is delivered"
                );

        codDescription.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#64748B;"
        );

        codText.getChildren().addAll(
                codTitle,
                codDescription
        );

        HBox codOption =
                createPaymentOption(
                        cod,
                        codText
                );


        section.getChildren().addAll(
                heading,
                instruction,
                separator,
                upiOption,
                cardOption,
                codOption
        );

        return section;
    }

    // =====================================================
    // PAYMENT OPTION ROW
    // =====================================================

    private HBox createPaymentOption(
            RadioButton radioButton,
            VBox content) {

        HBox row = new HBox(15);

        row.setAlignment(Pos.CENTER_LEFT);

        row.setPadding(
                new Insets(14)
        );

        row.setMinHeight(75);

        row.setStyle(
                "-fx-background-color:#F8FAFC;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;" +
                "-fx-cursor:hand;"
        );

        HBox.setHgrow(
                content,
                Priority.ALWAYS
        );

        radioButton.setStyle(
                "-fx-font-size:16px;"
        );

        row.getChildren().addAll(
                radioButton,
                content
        );

        // Clicking anywhere selects the payment option

        row.setOnMouseClicked(e ->
                radioButton.setSelected(true)
        );

        return row;
    }

    // =====================================================
    // ORDER SUMMARY
    // =====================================================

    private VBox createOrderSummary() {

        VBox summary = new VBox(18);

        summary.setPadding(new Insets(25));

        summary.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;"
        );

        Label heading =
                new Label("💰 Order Summary");

        heading.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        subtotalLabel.setStyle(
                "-fx-font-size:16px;" +
                "-fx-text-fill:#334155;"
        );

        deliveryLabel.setStyle(
                "-fx-font-size:16px;" +
                "-fx-text-fill:#334155;"
        );

        totalLabel.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#117864;"
        );

        Button placeOrder =
                new Button("✓ Place Order");

        placeOrder.setMaxWidth(
                Double.MAX_VALUE
        );

        placeOrder.setPrefHeight(55);

        placeOrder.setStyle(
                "-fx-background-color:#117864;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:17px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:10;" +
                "-fx-cursor:hand;"
        );

        placeOrder.setOnAction(e ->
                placeOrder()
        );

        summary.getChildren().addAll(
                heading,
                new Separator(),
                subtotalLabel,
                deliveryLabel,
                new Separator(),
                totalLabel,
                placeOrder
        );

        return summary;
    }

    // =====================================================
    // UPDATE SUMMARY
    // =====================================================

    private void updateSummary() {

        deliveryCharge =
                subtotal > 0 ? 40 : 0;

        totalAmount =
                subtotal + deliveryCharge;

        subtotalLabel.setText(
                "Subtotal: ₹"
                        + format(subtotal)
        );

        deliveryLabel.setText(
                "Delivery: ₹"
                        + format(deliveryCharge)
        );

        totalLabel.setText(
                "Total: ₹"
                        + format(totalAmount)
        );
    }

    // =====================================================
    // PLACE ORDER
    // =====================================================

    private void placeOrder() {

        // =================================================
        // VALIDATE ADDRESS
        // =================================================

        String address =
                addressField.getText();

        if (address == null ||
                address.trim().isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Delivery Address Required",
                    "Please enter your complete delivery address."
            );

            return;
        }

        // =================================================
        // GET CART
        // =================================================

        List<BuyerCartItem> cartItems =
                cartController.getCartItems(
                        navigator.getBuyerEmail()
                );

        if (cartItems == null ||
                cartItems.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Cart Empty",
                    "Your shopping cart is empty."
            );

            return;
        }

        // =================================================
        // CREATE ORDER
        // =================================================

        Order order =
                new Order();

        order.setBuyerEmail(
                navigator.getBuyerEmail()
        );

        order.setBuyerName(
                getBuyerName()
        );

        order.setDeliveryAddress(
                address.trim()
        );

        // =================================================
        // PAYMENT METHOD
        // =================================================

        String paymentMethod = "UPI";

        if (paymentGroup.getSelectedToggle() != null &&
                paymentGroup.getSelectedToggle().getUserData() != null) {

            paymentMethod =
                    paymentGroup
                            .getSelectedToggle()
                            .getUserData()
                            .toString();
        }

        order.setPaymentMethod(
                paymentMethod
        );

        // =================================================
        // CREATE ORDER ITEMS
        // =================================================

        List<OrderItem> orderItems =
                new ArrayList<>();

        for (BuyerCartItem cartItem : cartItems) {

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setProductId(
                    cartItem.getProductId()
            );

            orderItem.setProductName(
                    cartItem.getProductName()
            );

            orderItem.setFarmerEmail(
                    cartItem.getFarmerEmail()
            );

            orderItem.setUnit(
                    cartItem.getUnit()
            );

            orderItem.setPrice(
                    cartItem.getPrice()
            );

            orderItem.setQuantity(
                    cartItem.getQuantity()
            );

            orderItem.setTotalPrice(
                    cartItem.getTotalPrice()
            );

            // IMPORTANT:
            // Farmer will manage this status

            orderItem.setStatus(
                    "Pending"
            );

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);

        // =================================================
        // TOTALS
        // =================================================

        order.calculateTotals();

        order.setStatus(
                "Pending"
        );

        order.setOrderDate(
                new Date()
        );

        // =================================================
        // SAVE ORDER
        // =================================================

        String orderId =
                orderController.placeOrder(order);

        if (orderId == null ||
                orderId.trim().isEmpty()) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Order Failed",
                    "Unable to place your order. Please try again."
            );

            return;
        }

        // =================================================
        // CREATE BUYER NOTIFICATION
        // =================================================

        try {

            com.mainproject.model.Notification notification =
                    new com.mainproject.model.Notification(
                            navigator.getBuyerEmail(),
                            "🛒 Order Placed Successfully",
                            "Your order has been placed successfully and is waiting for farmer confirmation.",
                            "ORDER"
                    );

            boolean notificationAdded =
                    notificationController.addNotification(
                            notification
                    );

            System.out.println(
                    "Buyer notification created: "
                            + notificationAdded
            );

        } catch (Exception ex) {

            // The order is already successfully placed.
            // Notification failure must not cancel the checkout.
            ex.printStackTrace();

            System.out.println(
                    "Notification could not be created."
            );
        }

        // =================================================
        // REMOVE ITEMS FROM CART
        // =================================================

        for (BuyerCartItem item : cartItems) {

            if (item.getCartItemId() != null) {

                cartController.removeFromCart(
                        item.getCartItemId()
                );
            }
        }

        // =================================================
        // SUCCESS
        // =================================================

        showAlert(
                Alert.AlertType.INFORMATION,
                "Order Placed Successfully 🎉",
                "Your order has been placed successfully!\n\n"
                        + "Order ID: "
                        + orderId
                        + "\n\n"
                        + "Payment Method: "
                        + paymentMethod
                        + "\n\n"
                        + "Total Amount: ₹"
                        + format(order.getTotalAmount())
        );

        // =================================================
        // GO TO MY ORDERS
        // =================================================

        navigator.setView(
                new MyOrders(navigator).getView()
        );
    }

    // =====================================================
    // BUYER NAME
    // =====================================================

    private String getBuyerName() {

        String email =
                navigator.getBuyerEmail();

        if (email == null ||
                email.isEmpty()) {

            return "Buyer";
        }

        if (email.contains("@")) {

            String name =
                    email.substring(
                            0,
                            email.indexOf("@")
                    );

            return name;
        }

        return email;
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

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(String value) {

        return value == null
                ? ""
                : value;
    }

    // =====================================================
    // FORMAT NUMBER
    // =====================================================

    private String format(double value) {

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
}