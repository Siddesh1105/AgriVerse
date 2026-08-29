package com.mainproject.view.buyer;

import com.mainproject.controller.DashboardController;
import com.mainproject.model.Order;
import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardOverview {

    private final BuyerDashboard mainController;
    private final DashboardController dashboardController;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DashboardOverview(BuyerDashboard controller) {

        this.mainController = controller;

        this.dashboardController =
                new DashboardController();
    }


    // =====================================================
    // MAIN VIEW
    // =====================================================

    public Node getView() {

        String buyerEmail =
                mainController.getBuyerEmail();

        String buyerName =
                mainController.getBuyerName();


        // =================================================
        // LOAD REAL DATA
        // =================================================

        List<Order> buyerOrders =
                dashboardController.getBuyerOrders(
                        buyerEmail
                );


        int totalOrders =
                buyerOrders.size();


        int pendingOrders =
                dashboardController.getPendingOrders(
                        buyerEmail
                );


        int completedOrders =
                dashboardController.getCompletedOrders(
                        buyerEmail
                );


        int cartItemCount =
                dashboardController.getCartItemCount(
                        buyerEmail
                );


        // =================================================
        // ROOT
        // =================================================

        VBox root =
                new VBox(20);

        root.setPadding(
                new Insets(
                        25,
                        30,
                        25,
                        30
                )
        );

        root.setStyle(
                "-fx-background-color:#F8FAFC;"
        );


        // =================================================
        // HEADER
        // =================================================

        BorderPane header =
                new BorderPane();


        VBox greetingBox =
                new VBox(4);


        String displayName =
                (buyerName == null ||
                        buyerName.trim().isEmpty())
                        ? "Buyer"
                        : buyerName;


        Label lblGreeting =
                new Label(
                        "Good Morning, "
                                + displayName
                                + " 👋"
                );

        lblGreeting.setStyle(
                "-fx-font-size:22px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#1E293B;"
        );


        Label lblWelcome =
                new Label(
                        "Welcome back to AgriLink"
                );

        lblWelcome.setStyle(
                "-fx-font-size:15px;" +
                        "-fx-text-fill:#64748B;"
        );


        greetingBox.getChildren().addAll(
                lblGreeting,
                lblWelcome
        );


        // =================================================
        // TOP ACTIONS
        // =================================================

        HBox topActions =
                new HBox(12);

        topActions.setAlignment(
                Pos.CENTER_RIGHT
        );


        Button btnCart =
                new Button(
                        "🛒 Cart ("
                                + cartItemCount
                                + ")"
                );

        btnCart.setStyle(
                "-fx-background-color:#FFFFFF;" +
                        "-fx-border-color:#E2E8F0;" +
                        "-fx-border-radius:8;" +
                        "-fx-background-radius:8;" +
                        "-fx-padding:10 16;" +
                        "-fx-font-weight:bold;" +
                        "-fx-cursor:hand;"
        );


        btnCart.setOnAction(
                e -> mainController.setView(
                        new ShoppingCart(
                                mainController
                        ).getView()
                )
        );


        Label lblUser =
                new Label(
                        "👤 "
                                + displayName
                                + " (Buyer)"
                );

        lblUser.setStyle(
                "-fx-font-weight:bold;" +
                        "-fx-text-fill:#166534;" +
                        "-fx-font-size:13px;"
        );


        topActions.getChildren().addAll(
                btnCart,
                lblUser
        );


        header.setLeft(
                greetingBox
        );

        header.setRight(
                topActions
        );


        root.getChildren().add(
                header
        );


        // =================================================
        // DASHBOARD STATISTICS
        // =================================================

        HBox metrics =
                new HBox(15);

        metrics.setAlignment(
                Pos.CENTER
        );


        VBox totalCard =
                createMetricCard(
                        "Total Orders",
                        String.valueOf(totalOrders),
                        "All your orders",
                        "#166534"
                );


        VBox pendingCard =
                createMetricCard(
                        "Pending Orders",
                        String.valueOf(pendingOrders),
                        "Orders in progress",
                        "#CA8A04"
                );


        VBox completedCard =
                createMetricCard(
                        "Completed Orders",
                        String.valueOf(completedOrders),
                        "Successfully completed",
                        "#16A34A"
                );


        VBox cartCard =
                createMetricCard(
                        "Cart Items",
                        String.valueOf(cartItemCount),
                        "Items ready to buy",
                        "#2563EB"
                );


        totalCard.setOnMouseClicked(
                e -> mainController.navigateTo(
                        "My Orders"
                )
        );


        pendingCard.setOnMouseClicked(
                e -> mainController.navigateTo(
                        "My Orders"
                )
        );


        completedCard.setOnMouseClicked(
                e -> mainController.navigateTo(
                        "My Orders"
                )
        );


        cartCard.setOnMouseClicked(
                e -> mainController.setView(
                        new ShoppingCart(
                                mainController
                        ).getView()
                )
        );


        HBox.setHgrow(
                totalCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                pendingCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                completedCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                cartCard,
                Priority.ALWAYS
        );


        metrics.getChildren().addAll(
                totalCard,
                pendingCard,
                completedCard,
                cartCard
        );


        root.getChildren().add(
                metrics
        );


        // =================================================
        // RECENT ORDERS
        // LIVE MARKETPLACE REMOVED
        // =================================================

        VBox recentOrdersBox =
                createRecentOrdersSection(
                        buyerOrders
                );

        recentOrdersBox.setMaxWidth(
                Double.MAX_VALUE
        );


        root.getChildren().add(
                recentOrdersBox
        );


        // =================================================
        // BOTTOM ROW
        // =================================================

        HBox bottomRow =
                new HBox(20);

        bottomRow.setAlignment(
                Pos.TOP_CENTER
        );


        VBox recommendationsBox =
                createRecommendationsSection();


        VBox aiBox =
                createAiPromoSection();


        recommendationsBox.setMaxWidth(
                Double.MAX_VALUE
        );

        aiBox.setMaxWidth(
                Double.MAX_VALUE
        );


        HBox.setHgrow(
                recommendationsBox,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                aiBox,
                Priority.ALWAYS
        );


        bottomRow.getChildren().addAll(
                recommendationsBox,
                aiBox
        );


        root.getChildren().add(
                bottomRow
        );


        // =================================================
        // EMPTY ORDER MESSAGE
        // =================================================

        if (buyerOrders.isEmpty()) {

            Label emptyMessage =
                    new Label(
                            "You haven't placed any orders yet. Start exploring the marketplace!"
                    );

            emptyMessage.setStyle(
                    "-fx-text-fill:#64748B;" +
                            "-fx-font-size:13px;"
            );

            root.getChildren().add(
                    emptyMessage
            );
        }


        // =================================================
        // SCROLL PANE
        // =================================================

        ScrollPane scrollPane =
                new ScrollPane(root);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background:transparent;" +
                        "-fx-background-color:transparent;" +
                        "-fx-border-color:transparent;"
        );


        LanguageManager.apply(scrollPane);

        return scrollPane;
    }


    // =====================================================
    // METRIC CARD
    // =====================================================

    private VBox createMetricCard(
            String title,
            String value,
            String subtitle,
            String colorHex) {

        VBox card =
                new VBox(5);

        card.setPadding(
                new Insets(18)
        );

        card.setPrefHeight(145);

        card.setPrefWidth(220);

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setStyle(
                "-fx-background-color:#FFFFFF;" +
                        "-fx-border-color:#E2E8F0;" +
                        "-fx-border-radius:10;" +
                        "-fx-background-radius:10;" +
                        "-fx-cursor:hand;"
        );


        Label lblTitle =
                new Label(title);

        lblTitle.setStyle(
                "-fx-text-fill:#64748B;" +
                        "-fx-font-size:13px;"
        );


        Region spacer =
                new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );


        Label lblValue =
                new Label(value);

        lblValue.setStyle(
                "-fx-font-size:30px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:"
                        + colorHex
                        + ";"
        );


        Label lblSubtitle =
                new Label(subtitle);

        lblSubtitle.setStyle(
                "-fx-text-fill:#94A3B8;" +
                        "-fx-font-size:12px;"
        );


        card.getChildren().addAll(
                lblTitle,
                spacer,
                lblValue,
                lblSubtitle
        );


        return card;
    }


    // =====================================================
    // RECENT ORDERS
    // =====================================================

    private VBox createRecentOrdersSection(
            List<Order> orders) {

        VBox box =
                createSectionBox();


        box.setPrefHeight(250);


        BorderPane titleBar =
                new BorderPane();


        Label title =
                new Label(
                        "Recent Orders 📦"
                );

        title.setStyle(
                "-fx-font-weight:bold;" +
                        "-fx-font-size:16px;" +
                        "-fx-text-fill:#1E293B;"
        );


        Hyperlink viewAll =
                new Hyperlink(
                        "View All"
                );


        viewAll.setOnAction(
                e -> mainController.navigateTo(
                        "My Orders"
                )
        );


        titleBar.setLeft(
                title
        );

        titleBar.setRight(
                viewAll
        );


        VBox orderList =
                new VBox(10);


        if (orders == null ||
                orders.isEmpty()) {

            Label empty =
                    new Label(
                            "No orders found yet."
                    );

            empty.setStyle(
                    "-fx-text-fill:#64748B;" +
                            "-fx-font-size:13px;" +
                            "-fx-padding:15 0 15 0;"
            );

            orderList.getChildren().add(
                    empty
            );

        } else {

            int limit =
                    Math.min(
                            4,
                            orders.size()
                    );


            for (int i = 0;
                 i < limit;
                 i++) {

                Order order =
                        orders.get(i);

                if (order != null) {

                    orderList.getChildren().add(
                            createOrderRow(
                                    order
                            )
                    );
                }
            }
        }


        box.getChildren().addAll(
                titleBar,
                orderList
        );


        return box;
    }


    // =====================================================
    // ORDER ROW
    // =====================================================

    private HBox createOrderRow(
            Order order) {

        HBox row =
                new HBox(15);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPadding(
                new Insets(10, 0, 10, 0)
        );

        row.setStyle(
                "-fx-border-color:transparent transparent #F1F5F9 transparent;"
        );


        VBox details =
                new VBox(4);


        String orderId =
                order.getOrderId();

        if (orderId == null ||
                orderId.trim().isEmpty()) {

            orderId = "Unknown";
        }


        String shortOrderId =
                orderId.length() > 8
                        ? orderId.substring(0, 8)
                        : orderId;


        Label lblOrder =
                new Label(
                        "Order #" + shortOrderId
                );

        lblOrder.setStyle(
                "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#334155;"
        );


        int itemCount =
                order.getItems() == null
                        ? 0
                        : order.getItems().size();


        Label lblItems =
                new Label(
                        itemCount == 1
                                ? "1 item"
                                : itemCount + " items"
                );

        lblItems.setStyle(
                "-fx-font-size:12px;" +
                        "-fx-text-fill:#64748B;"
        );


        details.getChildren().addAll(
                lblOrder,
                lblItems
        );


        HBox.setHgrow(
                details,
                Priority.ALWAYS
        );


        VBox amountBox =
                new VBox(4);

        amountBox.setAlignment(
                Pos.CENTER_RIGHT
        );


        Label lblAmount =
                new Label(
                        formatCurrency(
                                order.getTotalAmount()
                        )
                );

        lblAmount.setStyle(
                "-fx-font-size:14px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#1E293B;"
        );


        String status =
                getDisplayStatus(
                        order.getStatus()
                );


        Label lblStatus =
                new Label(status);

        lblStatus.setStyle(
                "-fx-font-size:12px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:"
                        + getStatusColor(status)
                        + ";"
        );


        amountBox.getChildren().addAll(
                lblAmount,
                lblStatus
        );


        row.getChildren().addAll(
                details,
                amountBox
        );


        return row;
    }


    // =====================================================
    // FORMAT CURRENCY
    // =====================================================

    private String formatCurrency(
            double amount) {

        NumberFormat formatter =
                NumberFormat.getNumberInstance(
                        new Locale("en", "IN")
                );

        formatter.setMinimumFractionDigits(0);

        formatter.setMaximumFractionDigits(2);

        return "₹"
                + formatter.format(amount);
    }


    // =====================================================
    // DISPLAY STATUS
    // =====================================================

    private String getDisplayStatus(
            String status) {

        if (status == null ||
                status.trim().isEmpty()) {

            return "Pending";
        }

        return status;
    }


    // =====================================================
    // STATUS COLOR
    // =====================================================

    private String getStatusColor(
            String status) {

        if (status == null) {

            return "#CA8A04";
        }


        if (status.equalsIgnoreCase("Completed") ||
                status.equalsIgnoreCase("Delivered")) {

            return "#16A34A";
        }


        if (status.equalsIgnoreCase("Accepted")) {

            return "#2563EB";
        }


        if (status.equalsIgnoreCase("Processing")) {

            return "#CA8A04";
        }


        return "#CA8A04";
    }


    // =====================================================
    // RECOMMENDED PRODUCTS
    // =====================================================

    private VBox createRecommendationsSection() {

        VBox box =
                createSectionBox();


        Label title =
                new Label(
                        "Recommended for You ⭐"
                );

        title.setStyle(
                "-fx-font-weight:bold;" +
                        "-fx-font-size:16px;" +
                        "-fx-text-fill:#1E293B;"
        );


        Label description =
                new Label(
                        "Explore fresh products selected for you."
                );

        description.setWrapText(true);

        description.setStyle(
                "-fx-text-fill:#64748B;" +
                        "-fx-font-size:13px;"
        );


        Button btnExplore =
                new Button(
                        "Explore Marketplace"
                );

        btnExplore.setStyle(
                "-fx-background-color:#F0FDF4;" +
                        "-fx-text-fill:#166534;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:9 15;" +
                        "-fx-background-radius:8;" +
                        "-fx-cursor:hand;"
        );


        btnExplore.setOnAction(
                e -> mainController.navigateTo(
                        "Marketplace"
                )
        );


        box.getChildren().addAll(
                title,
                description,
                btnExplore
        );


        return box;
    }


    // =====================================================
    // AI SMART RECOMMENDATIONS
    // =====================================================

    private VBox createAiPromoSection() {

        VBox box =
                createSectionBox();


        Label title =
                new Label(
                        "🤖 AI Smart Recommendations"
                );

        title.setStyle(
                "-fx-font-weight:bold;" +
                        "-fx-font-size:16px;" +
                        "-fx-text-fill:#1E293B;"
        );


        Label description =
                new Label(
                        "Get personalized crop and product recommendations using AI."
                );

        description.setWrapText(true);

        description.setStyle(
                "-fx-text-fill:#64748B;" +
                        "-fx-font-size:13px;"
        );


        Button btnTry =
                new Button(
                        "Try AI Recommendations"
                );

        btnTry.setStyle(
                "-fx-background-color:#166534;" +
                        "-fx-text-fill:white;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:9 15;" +
                        "-fx-background-radius:8;" +
                        "-fx-cursor:hand;"
        );


        btnTry.setOnAction(
                e -> mainController.navigateTo(
                        "AI Recommendations"
                )
        );


        box.getChildren().addAll(
                title,
                description,
                btnTry
        );


        return box;
    }


    // =====================================================
    // COMMON SECTION BOX
    // =====================================================

    private VBox createSectionBox() {

        VBox box =
                new VBox(15);

        box.setPadding(
                new Insets(20)
        );

        box.setPrefHeight(190);

        box.setMaxWidth(
                Double.MAX_VALUE
        );

        box.setStyle(
                "-fx-background-color:#FFFFFF;" +
                        "-fx-border-color:#E2E8F0;" +
                        "-fx-border-radius:10;" +
                        "-fx-background-radius:10;"
        );

        return box;
    }
}