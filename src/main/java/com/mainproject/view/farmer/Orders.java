package com.mainproject.view.farmer;

import com.mainproject.controller.OrderController;
import com.mainproject.controller.NotificationController;
import com.mainproject.model.Order;
import com.mainproject.model.OrderItem;
import com.mainproject.model.Notification;
import com.mainproject.view.common.ReviewDialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Orders {

    // =====================================================
    // VARIABLES
    // =====================================================

    private final String farmerEmail;

    private final OrderController orderController =
            new OrderController();

    private final NotificationController notificationController =
            new NotificationController();

    private VBox ordersContainer;

    private String currentFilter = "All";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Orders(String farmerEmail) {

        this.farmerEmail = farmerEmail;
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root = new VBox(18);

        root.setPadding(new Insets(20));

        root.setStyle(
                "-fx-background-color: transparent;"
        );

        // =================================================
        // HEADER
        // =================================================

        Label title = new Label("📦 Customer Orders");

        title.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1B2631;"
        );

        Label subtitle = new Label(
                "Manage orders for your products."
        );

        subtitle.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#566573;"
        );

        VBox header = new VBox(5);

        header.getChildren().addAll(
                title,
                subtitle
        );

        // =================================================
        // FILTER BUTTONS
        // =================================================

        FlowPane filters = new FlowPane();

        filters.setHgap(10);
        filters.setVgap(10);

        String[] filterNames = {
                "All",
                "Pending",
                "Accepted",
                "Processing",
                "Completed",
                "Rejected"
        };

        for (String filter : filterNames) {

            Button button =
                    createFilterButton(filter);

            filters.getChildren().add(button);
        }

        // =================================================
        // ORDERS CONTAINER
        // =================================================

        ordersContainer = new VBox(15);

        ordersContainer.setFillWidth(true);

        // =================================================
        // ROOT
        // =================================================

        root.getChildren().addAll(
                header,
                filters,
                ordersContainer
        );

        // =================================================
        // LOAD ORDERS
        // =================================================

        loadOrders();

        ScrollPane scrollPane =
                new ScrollPane(root);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background:transparent;" +
                "-fx-background-color:transparent;"
        );

        return scrollPane;
    }

    // =====================================================
    // CREATE FILTER BUTTON
    // =====================================================

    private Button createFilterButton(
            String filter) {

        Button button =
                new Button(filter);

        updateFilterButtonStyle(
                button,
                filter.equals(currentFilter)
        );

        button.setOnAction(e -> {

            currentFilter = filter;

            loadOrders();
        });

        return button;
    }

    // =====================================================
    // FILTER BUTTON STYLE
    // =====================================================

    private void updateFilterButtonStyle(
            Button button,
            boolean active) {

        if (active) {

            button.setStyle(
                    "-fx-background-color:#117864;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-weight:bold;" +
                    "-fx-background-radius:20;" +
                    "-fx-padding:7 16;" +
                    "-fx-cursor:hand;"
            );

        } else {

            button.setStyle(
                    "-fx-background-color:white;" +
                    "-fx-text-fill:#1B2631;" +
                    "-fx-border-color:#A2D9CE;" +
                    "-fx-border-radius:20;" +
                    "-fx-background-radius:20;" +
                    "-fx-padding:7 16;" +
                    "-fx-cursor:hand;"
            );
        }
    }

    // =====================================================
    // LOAD ORDERS
    // =====================================================

    private void loadOrders() {

        ordersContainer.getChildren().clear();

        System.out.println(
                "===================================="
        );

        System.out.println(
                "LOADING FARMER ORDERS"
        );

        System.out.println(
                "Farmer Email: "
                        + farmerEmail
        );

        System.out.println(
                "===================================="
        );

        List<Order> orders;

        try {

            orders =
                    orderController.getFarmerOrders(
                            farmerEmail
                    );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Unable to load orders."
            );

            return;
        }

        // =================================================
        // EMPTY
        // =================================================

        if (orders == null ||
                orders.isEmpty()) {

            showEmptyOrders();

            return;
        }

        boolean foundOrders = false;

        // =================================================
        // DISPLAY ORDERS
        // =================================================

        for (Order order : orders) {

            if (order == null) {
                continue;
            }

            List<OrderItem> farmerItems =
                    getFarmerItems(order);

            if (farmerItems.isEmpty()) {
                continue;
            }

            // Filter order items

            List<OrderItem> filteredItems =
                    filterItems(farmerItems);

            if (filteredItems.isEmpty()) {
                continue;
            }

            foundOrders = true;

            ordersContainer.getChildren().add(
                    createOrderCard(
                            order,
                            filteredItems
                    )
            );
        }

        // =================================================
        // NOTHING AFTER FILTER
        // =================================================

        if (!foundOrders) {

            Label empty =
                    new Label(
                            "No "
                                    + currentFilter.toLowerCase()
                                    + " orders found."
                    );

            empty.setStyle(
                    "-fx-font-size:16px;" +
                    "-fx-text-fill:#64748B;"
            );

            ordersContainer.getChildren().add(
                    empty
            );
        }
    }

    // =====================================================
    // GET ONLY FARMER PRODUCTS
    // =====================================================

    private List<OrderItem> getFarmerItems(
            Order order) {

        List<OrderItem> farmerItems =
                new ArrayList<>();

        if (order.getItems() == null) {
            return farmerItems;
        }

        for (OrderItem item :
                order.getItems()) {

            if (item == null) {
                continue;
            }

            if (item.getFarmerEmail() != null &&
                    item.getFarmerEmail()
                            .equalsIgnoreCase(
                                    farmerEmail
                            )) {

                farmerItems.add(item);
            }
        }

        return farmerItems;
    }

    // =====================================================
    // FILTER ITEMS
    // =====================================================

    private List<OrderItem> filterItems(
            List<OrderItem> items) {

        List<OrderItem> filtered =
                new ArrayList<>();

        for (OrderItem item : items) {

            if ("All".equalsIgnoreCase(
                    currentFilter)) {

                filtered.add(item);

            } else if (safe(item.getStatus())
                    .equalsIgnoreCase(
                            currentFilter
                    )) {

                filtered.add(item);
            }
        }

        return filtered;
    }

    // =====================================================
    // EMPTY ORDERS
    // =====================================================

    private void showEmptyOrders() {

        VBox emptyBox =
                new VBox(10);

        emptyBox.setAlignment(
                Pos.CENTER
        );

        emptyBox.setPadding(
                new Insets(50)
        );

        Label icon =
                new Label("📦");

        icon.setStyle(
                "-fx-font-size:45px;"
        );

        Label message =
                new Label(
                        "No customer orders yet."
                );

        message.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#64748B;"
        );

        Label sub =
                new Label(
                        "Orders for your products will appear here."
                );

        sub.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#94A3B8;"
        );

        emptyBox.getChildren().addAll(
                icon,
                message,
                sub
        );

        ordersContainer.getChildren().add(
                emptyBox
        );
    }

    // =====================================================
    // CREATE ORDER CARD
    // =====================================================

    private VBox createOrderCard(
            Order order,
            List<OrderItem> farmerItems) {

        VBox card =
                new VBox(14);

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;"
        );

        // =================================================
        // ORDER HEADER
        // =================================================

        HBox orderHeader =
                new HBox();

        orderHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox orderInfo =
                new VBox(5);

        Label orderId =
                new Label(
                        "Order #"
                                + safe(
                                order.getOrderId()
                        )
                );

        orderId.setStyle(
                "-fx-font-size:17px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1B2631;"
        );

        String dateText = "";

        if (order.getOrderDate() != null) {

            dateText =
                    new SimpleDateFormat(
                            "dd MMM yyyy, hh:mm a"
                    ).format(
                            order.getOrderDate()
                    );
        }

        Label date =
                new Label(
                        "📅 " + dateText
                );

        date.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#64748B;"
        );

        orderInfo.getChildren().addAll(
                orderId,
                date
        );

        HBox.setHgrow(
                orderInfo,
                Priority.ALWAYS
        );

        orderHeader.getChildren().add(
                orderInfo
        );

        // =================================================
        // BUYER INFORMATION
        // =================================================

        VBox buyerBox =
                new VBox(5);

        buyerBox.setPadding(
                new Insets(12)
        );

        buyerBox.setStyle(
                "-fx-background-color:#F8FAFC;" +
                "-fx-background-radius:8;"
        );

        Label buyerTitle =
                new Label("👤 Customer Details");

        buyerTitle.setStyle(
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#334155;"
        );

        Label buyerName =
                new Label(
                        "Name: "
                                + safe(
                                order.getBuyerName()
                        )
                );

        Label buyerEmail =
                new Label(
                        "Email: "
                                + safe(
                                order.getBuyerEmail()
                        )
                );

        buyerName.setStyle(
                "-fx-text-fill:#475569;"
        );

        buyerEmail.setStyle(
                "-fx-text-fill:#475569;"
        );

        buyerBox.getChildren().addAll(
                buyerTitle,
                buyerName,
                buyerEmail
        );

        // =================================================
        // PRODUCTS TITLE
        // =================================================

        Label productsTitle =
                new Label("🥬 Your Products");

        productsTitle.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1B2631;"
        );

        // =================================================
        // PRODUCT ITEMS
        // =================================================

        VBox productsBox =
                new VBox(10);

        double farmerTotal = 0;

        for (OrderItem item : farmerItems) {

            farmerTotal +=
                    item.getTotalPrice();

            productsBox.getChildren().add(
                    createProductRow(
                            order,
                            item
                    )
            );
        }

        // =================================================
        // PAYMENT INFORMATION
        // =================================================

        VBox paymentBox = createPaymentBox(order);

        // =================================================
        // FARMER TOTAL
        // =================================================

        Label total =
                new Label(
                        "Your Order Amount: ₹"
                                + format(
                                farmerTotal
                        )
                );

        total.setStyle(
                "-fx-font-size:17px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#117864;"
        );

        // =================================================
        // CARD
        // =================================================

        card.getChildren().addAll(
                orderHeader,
                buyerBox,
                new Separator(),
                productsTitle,
                productsBox,
                new Separator(),
                paymentBox,
                new Separator(),
                total
        );

        return card;
    }


    // =====================================================
    // PAYMENT INFORMATION
    // =====================================================

    private VBox createPaymentBox(Order order) {

        VBox paymentBox = new VBox(6);
        paymentBox.setPadding(new Insets(12));
        paymentBox.setStyle(
                "-fx-background-color:#F8FAFC;" +
                "-fx-background-radius:8;"
        );

        Label paymentTitle = new Label("💳 Payment Details");
        paymentTitle.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#334155;"
        );

        String paymentStatus = safe(order.getPaymentStatus());
        String paymentMethod = safe(order.getPaymentMethod());
        String paymentId = safe(order.getPaymentId());

        if (paymentStatus.isEmpty()) {
            paymentStatus = "Pending";
        }

        Label statusLabel = new Label(
                "Payment Status: " + formatPaymentStatus(paymentStatus)
        );

        boolean paid = paymentStatus.equalsIgnoreCase("paid")
                || paymentStatus.equalsIgnoreCase("completed")
                || paymentStatus.equalsIgnoreCase("success");

        statusLabel.setStyle(
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" + (paid ? "#117864;" : "#D68910;")
        );

        Label methodLabel = new Label(
                "Payment Method: " +
                        (paymentMethod.isEmpty() ? "Not selected" : paymentMethod)
        );
        methodLabel.setStyle("-fx-text-fill:#475569;");

        paymentBox.getChildren().addAll(
                paymentTitle,
                statusLabel,
                methodLabel
        );

        if (!paymentId.isEmpty()) {
            Label paymentIdLabel = new Label(
                    "Payment ID: " + paymentId
            );
            paymentIdLabel.setStyle("-fx-text-fill:#64748B;");
            paymentBox.getChildren().add(paymentIdLabel);
        }

        if (order.getPaymentDate() != null) {
            String paidDate = new SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a"
            ).format(order.getPaymentDate());

            Label paymentDateLabel = new Label(
                    "Payment Date: " + paidDate
            );
            paymentDateLabel.setStyle("-fx-text-fill:#64748B;");
            paymentBox.getChildren().add(paymentDateLabel);
        }

        return paymentBox;
    }

    private String formatPaymentStatus(String status) {
        if (status.equalsIgnoreCase("paid")) {
            return "PAID ✅";
        }
        if (status.equalsIgnoreCase("pending")) {
            return "PENDING ⏳";
        }
        if (status.equalsIgnoreCase("cash on delivery")
                || status.equalsIgnoreCase("pending_cod")) {
            return "CASH ON DELIVERY";
        }
        if (status.equalsIgnoreCase("failed")) {
            return "FAILED ❌";
        }
        return status.toUpperCase();
    }

    // =====================================================
    // CREATE PRODUCT ROW
    // =====================================================

    private VBox createProductRow(
            Order order,
            OrderItem item) {

        VBox row =
                new VBox(10);

        row.setPadding(
                new Insets(14)
        );

        row.setStyle(
                "-fx-background-color:#FAFAFA;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;"
        );

        // =================================================
        // PRODUCT HEADER
        // =================================================

        HBox top =
                new HBox();

        top.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox details =
                new VBox(5);

        Label name =
                new Label(
                        "🥬 "
                                + safe(
                                item.getProductName()
                        )
                );

        name.setStyle(
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1B2631;"
        );

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

        Label price =
                new Label(
                        "₹"
                                + format(
                                item.getPrice()
                        )
                                + " / "
                                + safe(
                                item.getUnit()
                        )
                );

        Label amount =
                new Label(
                        "Amount: ₹"
                                + format(
                                item.getTotalPrice()
                        )
                );

        quantity.setStyle(
                "-fx-text-fill:#64748B;"
        );

        price.setStyle(
                "-fx-text-fill:#64748B;"
        );

        amount.setStyle(
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#117864;"
        );

        details.getChildren().addAll(
                name,
                quantity,
                price,
                amount
        );

        HBox.setHgrow(
                details,
                Priority.ALWAYS
        );

        Label status =
                createStatusLabel(
                        item.getStatus()
                );

        top.getChildren().addAll(
                details,
                status
        );

        // =================================================
        // ACTION BUTTONS
        // =================================================

        HBox actions =
                new HBox(8);

        actions.setAlignment(
                Pos.CENTER_LEFT
        );

        String currentStatus =
                safe(item.getStatus());

        // -------------------------------------------------
        // ACCEPT
        // -------------------------------------------------

        if (currentStatus.equalsIgnoreCase("Pending") ||
                currentStatus.isEmpty()) {

            Button accept =
                    createActionButton(
                            "✅ Accept",
                            "#117864"
                    );

            accept.setOnAction(e ->
                    updateItemStatus(
                            order,
                            item,
                            "Accepted"
                    )
            );

            Button reject =
                    createActionButton(
                            "❌ Reject",
                            "#C0392B"
                    );

            reject.setOnAction(e ->
                    updateItemStatus(
                            order,
                            item,
                            "Rejected"
                    )
            );

            actions.getChildren().addAll(
                    accept,
                    reject
            );
        }

        // -------------------------------------------------
        // PROCESSING
        // -------------------------------------------------

        else if (currentStatus.equalsIgnoreCase(
                "Accepted")) {

            Button processing =
                    createActionButton(
                            "🔄 Start Processing",
                            "#D68910"
                    );

            processing.setOnAction(e ->
                    updateItemStatus(
                            order,
                            item,
                            "Processing"
                    )
            );

            actions.getChildren().add(
                    processing
            );
        }

        // -------------------------------------------------
        // COMPLETE
        // -------------------------------------------------

        else if (currentStatus.equalsIgnoreCase(
                "Processing")) {

            Button complete =
                    createActionButton(
                            "📦 Complete",
                            "#117864"
                    );

            complete.setOnAction(e ->
                    updateItemStatus(
                            order,
                            item,
                            "Completed"
                    )
            );

            actions.getChildren().add(
                    complete
            );
        }

        // -------------------------------------------------
        // FINAL STATUS
        // -------------------------------------------------

        else if (currentStatus.equalsIgnoreCase(
                "Completed")) {

            Label completed =
                    new Label(
                            "✅ This product order is completed."
                    );

            completed.setStyle(
                    "-fx-text-fill:#117864;" +
                    "-fx-font-weight:bold;"
            );

            actions.getChildren().add(
                    completed
            );

            Button reviewBuyer = createActionButton(
                    "⭐ Review Buyer",
                    "#117864"
            );
            reviewBuyer.setOnAction(e -> ReviewDialog.show(
                    farmerEmail, farmerEmail, "FARMER",
                    safe(order.getBuyerEmail()), safe(order.getBuyerName()), "BUYER",
                    safe(order.getOrderId()) + "_" + safe(item.getProductId()),
                    "PRODUCT_ORDER"
            ));
            actions.getChildren().add(reviewBuyer);

        } else if (currentStatus.equalsIgnoreCase(
                "Rejected")) {

            Label rejected =
                    new Label(
                            "❌ This product order was rejected."
                    );

            rejected.setStyle(
                    "-fx-text-fill:#C0392B;" +
                    "-fx-font-weight:bold;"
            );

            actions.getChildren().add(
                    rejected
            );
        }

        row.getChildren().addAll(
                top,
                actions
        );

        return row;
    }

    // =====================================================
    // CREATE ACTION BUTTON
    // =====================================================

    private Button createActionButton(
            String text,
            String color) {

        Button button =
                new Button(text);

        button.setStyle(
                "-fx-background-color:"
                        + color
                        + ";" +
                        "-fx-text-fill:white;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:7;" +
                        "-fx-padding:8 14;" +
                        "-fx-cursor:hand;"
        );

        return button;
    }

    // =====================================================
    // CREATE STATUS LABEL
    // =====================================================

    private Label createStatusLabel(
            String statusText) {

        String status =
                safe(statusText);

        if (status.isEmpty()) {
            status = "Pending";
        }

        Label label =
                new Label(status);

        String background = "#FCF3CF";
        String textColor = "#B7950B";

        if (status.equalsIgnoreCase(
                "Accepted")) {

            background = "#D6EAF8";
            textColor = "#2471A3";

        } else if (status.equalsIgnoreCase(
                "Processing")) {

            background = "#FDEBD0";
            textColor = "#D68910";

        } else if (status.equalsIgnoreCase(
                "Completed")) {

            background = "#D4EFDF";
            textColor = "#117864";

        } else if (status.equalsIgnoreCase(
                "Rejected")) {

            background = "#FADBD8";
            textColor = "#C0392B";
        }

        label.setStyle(
                "-fx-background-color:"
                        + background
                        + ";" +
                        "-fx-text-fill:"
                        + textColor
                        + ";" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:5 10;" +
                        "-fx-background-radius:15;"
        );

        return label;
    }

    // =====================================================
    // UPDATE ITEM STATUS
    // =====================================================

    private void updateItemStatus(
            Order order,
            OrderItem item,
            String newStatus) {

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Update Order"
        );

        confirmation.setHeaderText(
                "Change product order status?"
        );

        confirmation.setContentText(
                item.getProductName()
                        + "\n\nNew Status: "
                        + newStatus
        );

        confirmation.showAndWait()
                .ifPresent(button -> {

                    if (button.getButtonData()
                            .isDefaultButton()) {

                        boolean success =
                                orderController
                                        .updateOrderItemStatus(
                                                order.getOrderId(),
                                                item.getProductId(),
                                                farmerEmail,
                                                newStatus
                                        );

                        if (success) {

                            // =================================================
                            // CREATE BUYER NOTIFICATION
                            // =================================================

                            createBuyerNotification(
                                    order,
                                    item,
                                    newStatus
                            );

                            showSuccess(
                                    "Order updated successfully!\n\n"
                                            + item.getProductName()
                                            + " → "
                                            + newStatus
                            );

                            // Reload Firestore data
                            loadOrders();

                        } else {

                            showError(
                                    "Unable to update the order."
                            );
                        }
                    }
                });
    }

    // =====================================================
    // CREATE BUYER NOTIFICATION
    // =====================================================

    private void createBuyerNotification(
            Order order,
            OrderItem item,
            String status) {

        try {

            String buyerEmail =
                    order.getBuyerEmail();

            if (buyerEmail == null ||
                    buyerEmail.trim().isEmpty()) {

                System.out.println(
                        "Buyer email not found. Notification not created."
                );

                return;
            }

            String title;
            String message;
            String type;

            switch (safe(status).toLowerCase()) {

                case "accepted":

                    title = "✅ Order Accepted";

                    message =
                            "Your order for "
                                    + safe(item.getProductName())
                                    + " has been accepted by the farmer.";

                    type = "ORDER_ACCEPTED";

                    break;

                case "processing":

                    title = "🔄 Order Processing";

                    message =
                            "Your order for "
                                    + safe(item.getProductName())
                                    + " is now being processed by the farmer.";

                    type = "ORDER_PROCESSING";

                    break;

                case "completed":

                    title = "📦 Order Completed";

                    message =
                            "Your order for "
                                    + safe(item.getProductName())
                                    + " has been completed successfully.";

                    type = "ORDER_COMPLETED";

                    break;

                case "rejected":

                    title = "❌ Order Rejected";

                    message =
                            "Unfortunately, your order for "
                                    + safe(item.getProductName())
                                    + " was rejected by the farmer.";

                    type = "ORDER_REJECTED";

                    break;

                default:

                    title = "📦 Order Status Updated";

                    message =
                            "Your order for "
                                    + safe(item.getProductName())
                                    + " has been updated to "
                                    + safe(status)
                                    + ".";

                    type = "ORDER_UPDATE";

                    break;
            }

            Notification notification =
                    new Notification(
                            buyerEmail,
                            title,
                            message,
                            type
                    );

            boolean saved =
                    notificationController.addNotification(
                            notification
                    );

            System.out.println(
                    "Buyer notification created: " + saved
            );

        } catch (Exception e) {

            // Order status is already updated successfully.
            // Notification failure must not break the order flow.

            System.out.println(
                    "Error while creating buyer notification."
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // SUCCESS ALERT
    // =====================================================

    private void showSuccess(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Success"
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    // =====================================================
    // ERROR ALERT
    // =====================================================

    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Error"
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
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
}