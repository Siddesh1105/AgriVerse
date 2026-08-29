package com.mainproject.view.buyer;

import com.mainproject.controller.OrderController;
import com.mainproject.model.Order;
import com.mainproject.model.OrderItem;
import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyOrders {

    private final BuyerDashboard navigator;

    private final OrderController orderController =
            new OrderController();

    private VBox ordersList;

    private String selectedFilter = "All";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public MyOrders(BuyerDashboard navigator) {

        this.navigator = navigator;
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        BorderPane mainRoot = new BorderPane();

        mainRoot.setPadding(
                new Insets(25, 30, 25, 30)
        );

        // =====================================================
        // HEADER
        // =====================================================

        Label title = new Label("📦 My Orders");

        title.setStyle(
                "-fx-font-size:26px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label subtitle = new Label(
                "Track and manage your product orders"
        );

        subtitle.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#64748B;"
        );

        VBox titleBox = new VBox(5);

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        Button refreshButton =
                new Button("↻ Refresh");

        refreshButton.setStyle(
                "-fx-background-color:#117864;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:9 18;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        refreshButton.setOnAction(e ->
                loadOrders()
        );

        HBox header = new HBox();

        header.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(
                titleBox,
                Priority.ALWAYS
        );

        header.getChildren().addAll(
                titleBox,
                refreshButton
        );

        // =====================================================
        // FILTER BUTTONS
        // =====================================================

        HBox filters = new HBox(10);

        filters.setPadding(
                new Insets(20, 0, 15, 0)
        );

        Button allButton =
                createFilterButton("All");

        Button pendingButton =
                createFilterButton("Pending");

        Button confirmedButton =
                createFilterButton("Confirmed");

        Button processingButton =
                createFilterButton("Processing");

        Button completedButton =
                createFilterButton("Completed");

        Button cancelledButton =
                createFilterButton("Cancelled");

        filters.getChildren().addAll(
                allButton,
                pendingButton,
                confirmedButton,
                processingButton,
                completedButton,
                cancelledButton
        );

        // =====================================================
        // ORDERS LIST
        // =====================================================

        ordersList = new VBox(15);

        ScrollPane scrollPane =
                new ScrollPane(ordersList);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;"
        );

        VBox topSection = new VBox();

        topSection.getChildren().addAll(
                header,
                filters
        );

        mainRoot.setTop(topSection);
        mainRoot.setCenter(scrollPane);

        // =====================================================
        // LOAD ORDERS
        // =====================================================

        loadOrders();

        LanguageManager.apply(mainRoot);

        return mainRoot;
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
                filter.equals(selectedFilter)
        );

        button.setOnAction(e -> {

            selectedFilter = filter;

            loadOrders();

        });

        return button;
    }

    // =====================================================
    // FILTER BUTTON STYLE
    // =====================================================

    private void updateFilterButtonStyle(
            Button button,
            boolean selected) {

        if (selected) {

            button.setStyle(
                    "-fx-background-color:#117864;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:8 16;" +
                    "-fx-background-radius:20;" +
                    "-fx-cursor:hand;"
            );

        } else {

            button.setStyle(
                    "-fx-background-color:#F1F5F9;" +
                    "-fx-text-fill:#475569;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:8 16;" +
                    "-fx-background-radius:20;" +
                    "-fx-cursor:hand;"
            );
        }
    }

    // =====================================================
    // LOAD ORDERS
    // =====================================================

    private void loadOrders() {

        if (ordersList == null) {
            return;
        }

        ordersList.getChildren().clear();

        String buyerEmail =
                navigator.getBuyerEmail();

        List<Order> orders =
                orderController.getBuyerOrders(
                        buyerEmail
                );

        if (orders == null) {

            orders = new ArrayList<>();
        }

        List<Order> filteredOrders =
                new ArrayList<>();

        for (Order order : orders) {

            if (order == null) {
                continue;
            }

            if (selectedFilter.equalsIgnoreCase("All")) {

                filteredOrders.add(order);

            } else if (
                    safe(order.getStatus())
                            .equalsIgnoreCase(selectedFilter)
            ) {

                filteredOrders.add(order);
            }
        }

        // =====================================================
        // EMPTY ORDERS
        // =====================================================

        if (filteredOrders.isEmpty()) {

            VBox emptyBox = new VBox(12);

            emptyBox.setAlignment(
                    Pos.CENTER
            );

            emptyBox.setPadding(
                    new Insets(80)
            );

            Label icon =
                    new Label("📦");

            icon.setStyle(
                    "-fx-font-size:50px;"
            );

            Label emptyTitle =
                    new Label(
                            "No orders found"
                    );

            emptyTitle.setStyle(
                    "-fx-font-size:20px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-text-fill:#334155;"
            );

            Label message =
                    new Label(
                            selectedFilter.equals("All")
                                    ? "You haven't placed any orders yet."
                                    : "No "
                                    + selectedFilter.toLowerCase()
                                    + " orders found."
                    );

            message.setStyle(
                    "-fx-font-size:14px;" +
                    "-fx-text-fill:#64748B;"
            );

            Button marketplaceButton =
                    new Button(
                            "🛒 Go to Marketplace"
                    );

            marketplaceButton.setStyle(
                    "-fx-background-color:#117864;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:10 20;" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;"
            );

            marketplaceButton.setOnAction(e ->
                    navigator.setView(
                            new LiveMarketplace(navigator)
                                    .getView()
                    )
            );

            emptyBox.getChildren().addAll(
                    icon,
                    emptyTitle,
                    message,
                    marketplaceButton
            );

            ordersList.getChildren().add(
                    emptyBox
            );

            return;
        }

        // =====================================================
        // DISPLAY ORDERS
        // =====================================================

        for (Order order : filteredOrders) {

            ordersList.getChildren().add(
                    createOrderCard(order)
            );
        }
    }

    // =====================================================
    // ORDER CARD
    // =====================================================

    private VBox createOrderCard(
            Order order) {

        VBox card =
                new VBox(15);

        card.setPadding(
                new Insets(20)
        );

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-width:1;" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;"
        );

        // =====================================================
        // HEADER
        // =====================================================

        String date = "";

        if (order.getOrderDate() != null) {

            date =
                    new SimpleDateFormat(
                            "dd MMM yyyy, hh:mm a"
                    ).format(
                            order.getOrderDate()
                    );
        }

        Label orderId =
                new Label(
                        "Order #"
                                + safe(
                                order.getOrderId()
                        )
                );

        orderId.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label dateLabel =
                new Label(
                        "📅 " + date
                );

        dateLabel.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#64748B;"
        );

        VBox headerInfo =
                new VBox(6);

        headerInfo.getChildren().addAll(
                orderId,
                dateLabel
        );

        Label status =
                createStatusBadge(
                        safe(order.getStatus())
                );

        HBox header =
                new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                headerInfo,
                Priority.ALWAYS
        );

        header.getChildren().addAll(
                headerInfo,
                status
        );

        // =====================================================
        // ITEMS
        // =====================================================

        VBox itemList =
                new VBox(8);

        if (order.getItems() != null) {

            for (OrderItem item :
                    order.getItems()) {

                if (item == null) {
                    continue;
                }

                HBox itemRow =
                        new HBox();

                itemRow.setAlignment(
                        Pos.CENTER_LEFT
                );

                Label itemName =
                        new Label(
                                "🛒 "
                                        + safe(
                                        item.getProductName()
                                )
                                        + " × "
                                        + format(
                                        item.getQuantity()
                                )
                                        + " "
                                        + safe(
                                        item.getUnit()
                                )
                );

                itemName.setStyle(
                        "-fx-font-size:15px;" +
                        "-fx-text-fill:#334155;"
                );

                Label itemPrice =
                        new Label(
                                "₹"
                                        + format(
                                        item.getTotalPrice()
                                )
                );

                itemPrice.setStyle(
                        "-fx-font-size:15px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#117864;"
                );

                HBox.setHgrow(
                        itemName,
                        Priority.ALWAYS
                );

                itemRow.getChildren().addAll(
                        itemName,
                        itemPrice
                );

                itemList.getChildren().add(
                        itemRow
                );
            }
        }

        // =====================================================
        // TOTAL
        // =====================================================

        Label totalLabel =
                new Label(
                        "Total Amount"
                );

        totalLabel.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#64748B;"
        );

        Label totalAmount =
                new Label(
                        "₹"
                                + format(
                                order.getTotalAmount()
                        )
                );

        totalAmount.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#117864;"
        );

        VBox totalBox =
                new VBox(4);

        totalBox.getChildren().addAll(
                totalLabel,
                totalAmount
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        Button viewButton =
                new Button(
                        "👁 View Details"
                );

        viewButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#117864;" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:#117864;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:9 16;" +
                "-fx-cursor:hand;"
        );

        viewButton.setOnAction(e ->
                showOrderDetails(order)
        );

        HBox actions =
                new HBox(10);

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        actions.getChildren().add(
                viewButton
        );

        // =====================================================
        // CANCEL PENDING ORDER
        // =====================================================

        if (safe(order.getStatus())
                .equalsIgnoreCase("Pending")) {

            Button cancelButton =
                    new Button(
                            "Cancel Order"
                    );

            cancelButton.setStyle(
                    "-fx-background-color:#FEF2F2;" +
                    "-fx-text-fill:#DC2626;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:9 16;" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;"
            );

            cancelButton.setOnAction(e ->
                    cancelOrder(order)
            );

            actions.getChildren().add(
                    cancelButton
            );
        }

        HBox footer =
                new HBox();

        footer.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                totalBox,
                Priority.ALWAYS
        );

        footer.getChildren().addAll(
                totalBox,
                actions
        );

        card.getChildren().addAll(
                header,
                new Separator(),
                itemList,
                new Separator(),
                footer
        );

        return card;
    }

    // =====================================================
    // STATUS BADGE
    // =====================================================

    private Label createStatusBadge(
            String statusText) {

        Label status =
                new Label(
                        statusText.isEmpty()
                                ? "Unknown"
                                : statusText
        );

        status.setStyle(
                "-fx-font-weight:bold;" +
                "-fx-padding:7 14;" +
                "-fx-background-radius:20;"
        );

        if (statusText.equalsIgnoreCase("Pending")) {

            status.setStyle(
                    "-fx-background-color:#FEF3C7;" +
                    "-fx-text-fill:#B45309;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:7 14;" +
                    "-fx-background-radius:20;"
            );

        } else if (
                statusText.equalsIgnoreCase("Confirmed")
                        || statusText.equalsIgnoreCase("Processing")
        ) {

            status.setStyle(
                    "-fx-background-color:#DBEAFE;" +
                    "-fx-text-fill:#1D4ED8;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:7 14;" +
                    "-fx-background-radius:20;"
            );

        } else if (
                statusText.equalsIgnoreCase("Completed")
        ) {

            status.setStyle(
                    "-fx-background-color:#DCFCE7;" +
                    "-fx-text-fill:#15803D;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:7 14;" +
                    "-fx-background-radius:20;"
            );

        } else if (
                statusText.equalsIgnoreCase("Cancelled")
        ) {

            status.setStyle(
                    "-fx-background-color:#FEE2E2;" +
                    "-fx-text-fill:#DC2626;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:7 14;" +
                    "-fx-background-radius:20;"
            );
        }

        return status;
    }

    // =====================================================
    // ORDER DETAILS
    // =====================================================

    private void showOrderDetails(
            Order order) {

        Dialog<Void> dialog =
                new Dialog<>();

        dialog.setTitle("Order Details");

        dialog.setHeaderText(
                "Order #"
                        + safe(
                        order.getOrderId()
                )
        );

        ButtonType closeButton =
                new ButtonType(
                        "Close",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .add(closeButton);

        VBox content =
                new VBox(12);

        content.setPadding(
                new Insets(10)
        );

        Label status =
                new Label(
                        "Status: "
                                + safe(
                                order.getStatus()
                        )
                );

        Label address =
                new Label(
                        "📍 Delivery Address:\n"
                                + safe(
                                order.getDeliveryAddress()
                        )
                );

        Label payment =
                new Label(
                        "💳 Payment Method: "
                                + safe(
                                order.getPaymentMethod()
                        )
                );

        Label subtotal =
                new Label(
                        "Subtotal: ₹"
                                + format(
                                order.getSubtotal()
                        )
                );

        Label delivery =
                new Label(
                        "Delivery Charge: ₹"
                                + format(
                                order.getDeliveryCharge()
                        )
                );

        Label total =
                new Label(
                        "Total Amount: ₹"
                                + format(
                                order.getTotalAmount()
                        )
                );

        total.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#117864;"
        );

        content.getChildren().addAll(
                status,
                new Separator(),
                address,
                payment,
                new Separator(),
                subtotal,
                delivery,
                total
        );

        dialog.getDialogPane()
                .setContent(content);

        dialog.showAndWait();
    }

    // =====================================================
    // CANCEL ORDER
    // =====================================================

    private void cancelOrder(
            Order order) {

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Cancel Order"
        );

        confirmation.setHeaderText(
                "Do you want to cancel this order?"
        );

        confirmation.setContentText(
                "Order #"
                        + safe(
                        order.getOrderId()
                )
        );

        ButtonType yes =
                new ButtonType(
                        "Yes, Cancel"
                );

        ButtonType no =
                new ButtonType(
                        "No",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        confirmation.getButtonTypes()
                .setAll(yes, no);

        confirmation.showAndWait()
                .ifPresent(result -> {

                    if (result == yes) {

                        boolean success =
                                orderController.updateStatus(
                                        order.getOrderId(),
                                        "Cancelled"
                                );

                        if (success) {

                            new Alert(
                                    Alert.AlertType.INFORMATION,
                                    "Order cancelled successfully."
                            ).showAndWait();

                            loadOrders();

                        } else {

                            new Alert(
                                    Alert.AlertType.ERROR,
                                    "Unable to cancel the order."
                            ).showAndWait();
                        }
                    }
                });
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