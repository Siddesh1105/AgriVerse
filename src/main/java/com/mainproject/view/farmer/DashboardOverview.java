package com.mainproject.view.farmer;

import com.mainproject.controller.ProductController;
import com.mainproject.controller.OrderController;
import com.mainproject.controller.EquipmentRentalController;
import com.mainproject.model.Order;
import com.mainproject.model.OrderItem;
import com.mainproject.model.EquipmentRental;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class DashboardOverview {

    private final FarmerDashboard navigator;

    private final ProductController productController;
    private final OrderController orderController;
    private final EquipmentRentalController equipmentRentalController;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DashboardOverview(
            FarmerDashboard navigator) {

        this.navigator = navigator;

        this.productController = new ProductController();
        this.orderController = new OrderController();
        this.equipmentRentalController = new EquipmentRentalController();
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root = new VBox(18);

        root.setPadding(
                new Insets(5));

        // =================================================
        // GET LOGGED-IN FARMER
        // =================================================

        String farmerEmail = navigator.getFarmerEmail();

        String farmerName = navigator.getFarmerName();

        // =================================================
        // PRODUCT STATISTICS
        // =================================================

        int totalProducts = 0;

        int activeProducts = 0;

        try {

            if (farmerEmail != null
                    && !farmerEmail.trim().isEmpty()) {

                totalProducts = productController
                        .getProductCountByOwner(
                                farmerEmail);

                activeProducts = productController
                        .getActiveProductCountByOwner(
                                farmerEmail);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error loading dashboard product statistics:");

            e.printStackTrace();
        }

        // =================================================
        // HEADER
        // =================================================

        HBox header = new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);

        String displayName = farmerName == null
                || farmerName.trim().isEmpty()
                        ? "Farmer"
                        : farmerName;

        Label title = new Label(
                "Good Morning, "
                        + displayName
                        + " 👋");

        title.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: 800;" +
                        "-fx-text-fill: #1B2631;");

        Label subtitle = new Label(
                "Here's what's happening on your farm today.");

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #566573;");

        titleBox.getChildren().addAll(
                title,
                subtitle);

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Button addProductBtn = new Button(
                "+ Add Product");

        addProductBtn.setStyle(
                "-fx-background-color: #117864;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 8 16;" +
                        "-fx-cursor: hand;");

        addProductBtn.setOnAction(
                e -> navigator.navigateTo(
                        "AddProduct"));

        header.getChildren().addAll(
                titleBox,
                spacer,
                addProductBtn);

        // =================================================
        // 4 KEY STATISTICS
        // =================================================

        HBox statsRow = new HBox(14);

        statsRow.getChildren().addAll(

                createStatCard(
                        "Total Products",
                        String.valueOf(
                                totalProducts)),

                createStatCard(
                        "Active Products",
                        String.valueOf(
                                activeProducts)),

                createStatCard(
                        "Pending Orders",
                        "0"),

                createStatCard(
                        "Completed Orders",
                        "0"));

        // =================================================
        // ACTIVE EARNINGS
        // =================================================
        /*
         * Earnings are calculated from real successful payments:
         *
         * 1. Product orders:
         *    Only PAID orders are counted and only the logged-in
         *    farmer's own product items are included.
         *
         * 2. Equipment rentals:
         *    Only PAID rentals are counted where the logged-in
         *    farmer is the equipment owner.
         *
         * The values are loaded fresh whenever the dashboard opens,
         * so no existing order or rental functionality is changed.
         */

        double totalEarnings = 0;
        double monthlyEarnings = 0;

        try {

            totalEarnings = getTotalEarnings(farmerEmail);
            monthlyEarnings = getMonthlyEarnings(farmerEmail);

        } catch (Exception e) {

            System.out.println(
                    "Error loading farmer earnings:");

            e.printStackTrace();
        }

        HBox earningsRow = new HBox(14);

        VBox earnMonth = createEarningCard(
                "Earnings (This Month)",
                formatCurrency(monthlyEarnings));

        VBox earnTotal = createEarningCard(
                "Total Earnings",
                formatCurrency(totalEarnings));

        HBox.setHgrow(
                earnMonth,
                Priority.ALWAYS);

        HBox.setHgrow(
                earnTotal,
                Priority.ALWAYS);

        earningsRow.getChildren().addAll(
                earnMonth,
                earnTotal);

        // =================================================
        // QUICK ACCESS ROW 1
        // =================================================

        HBox gridRow1 = new HBox(14);

        gridRow1.getChildren().addAll(

                createActionCard(
                        "Marketplace",
                        "Sell your products",
                        "🏪",
                        () -> navigator.navigateTo(
                                "Marketplace")),

                createActionCard(
                        "Equipment Rental",
                        "Rent or list equipment",
                        "🚜",
                        () -> navigator.navigateTo(
                                "Equipment Rental")),

                createActionCard(
                        "Crop Prices",
                        "Check latest prices",
                        "📈",
                        () -> navigator.navigateTo(
                                "Crop Prices")));

        // =================================================
        // QUICK ACCESS ROW 2
        // =================================================

        HBox gridRow2 = new HBox(14);

        gridRow2.getChildren().addAll(

                createActionCard(
                        "Weather",
                        "Current weather info",
                        "⛅",
                        () -> navigator.navigateTo(
                                "Weather")),

                createActionCard(
                        "AI Recommendations",
                        "Smart farm insights",
                        "✨",
                        () -> navigator.navigateTo(
                                "AI Recommendations")),

                createActionCard(
                        "Notifications",
                        "View all alerts",
                        "🔔",
                        () -> navigator.navigateTo(
                                "Notifications")));

        // =================================================
        // ADD TO ROOT
        // =================================================

        root.getChildren().addAll(
                header,
                statsRow,
                earningsRow,
                gridRow1,
                gridRow2);

        // =================================================
        // SCROLL
        // =================================================

        ScrollPane scroll = new ScrollPane(
                root);

        scroll.setFitToWidth(
                true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;");

        return scroll;
    }

    // =====================================================
    // ACTIVE EARNINGS CALCULATION
    // =====================================================

    private double getTotalEarnings(
            String farmerEmail) {

        return getProductEarnings(
                farmerEmail,
                false)
                + getRentalEarnings(
                        farmerEmail,
                        false);
    }

    private double getMonthlyEarnings(
            String farmerEmail) {

        return getProductEarnings(
                farmerEmail,
                true)
                + getRentalEarnings(
                        farmerEmail,
                        true);
    }

    /**
     * Calculates earnings from marketplace product payments.
     *
     * A single order can contain products from multiple farmers,
     * so only items owned by the logged-in farmer are counted.
     * Delivery charges are intentionally not added to farmer earnings.
     */
    private double getProductEarnings(
            String farmerEmail,
            boolean currentMonthOnly) {

        if (farmerEmail == null
                || farmerEmail.trim().isEmpty()) {

            return 0;
        }

        double earnings = 0;

        try {

            List<Order> orders =
                    orderController.getFarmerOrders(
                            farmerEmail.trim());

            for (Order order : orders) {

                if (order == null
                        || !"paid".equalsIgnoreCase(
                                safe(order.getPaymentStatus()))) {

                    continue;
                }

                Date paymentDate =
                        order.getPaymentDate() != null
                                ? order.getPaymentDate()
                                : order.getOrderDate();

                if (currentMonthOnly
                        && !isInCurrentMonth(paymentDate)) {

                    continue;
                }

                List<OrderItem> items =
                        order.getItems();

                if (items == null) {
                    continue;
                }

                for (OrderItem item : items) {

                    if (item == null
                            || item.getFarmerEmail() == null
                            || !item.getFarmerEmail()
                                    .trim()
                                    .equalsIgnoreCase(
                                            farmerEmail.trim())) {

                        continue;
                    }

                    /*
                     * Rejected items are not treated as earnings.
                     * All other paid items are included so the
                     * existing order workflow remains unchanged.
                     */
                    if ("rejected".equalsIgnoreCase(
                            safe(item.getStatus()))) {

                        continue;
                    }

                    double itemAmount =
                            item.getTotalPrice();

                    /*
                     * Older Firestore records may not have
                     * totalPrice saved. Calculate it safely.
                     */
                    if (itemAmount <= 0) {

                        itemAmount =
                                item.getPrice()
                                        * item.getQuantity();
                    }

                    earnings +=
                            Math.max(0, itemAmount);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error calculating product earnings:");

            e.printStackTrace();
        }

        return earnings;
    }

    /**
     * Calculates earnings from successful equipment rental payments
     * where this farmer owns the equipment.
     */
    private double getRentalEarnings(
            String farmerEmail,
            boolean currentMonthOnly) {

        if (farmerEmail == null
                || farmerEmail.trim().isEmpty()) {

            return 0;
        }

        double earnings = 0;

        try {

            List<EquipmentRental> rentals =
                    equipmentRentalController
                            .getRentalsByFarmer(
                                    farmerEmail.trim());

            for (EquipmentRental rental : rentals) {

                if (rental == null
                        || !"paid".equalsIgnoreCase(
                                safe(rental.getPaymentStatus()))) {

                    continue;
                }

                Date paymentDate =
                        rental.getPaymentDate() != null
                                ? rental.getPaymentDate()
                                : rental.getCreatedAt();

                if (currentMonthOnly
                        && !isInCurrentMonth(paymentDate)) {

                    continue;
                }

                earnings +=
                        Math.max(
                                0,
                                rental.getTotalAmount());
            }

        } catch (Exception e) {

            System.out.println(
                    "Error calculating rental earnings:");

            e.printStackTrace();
        }

        return earnings;
    }

    private boolean isInCurrentMonth(
            Date date) {

        if (date == null) {
            return false;
        }

        Calendar now =
                Calendar.getInstance();

        Calendar value =
                Calendar.getInstance();

        value.setTime(date);

        return now.get(Calendar.YEAR)
                == value.get(Calendar.YEAR)
                && now.get(Calendar.MONTH)
                == value.get(Calendar.MONTH);
    }

    private String safe(String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private String formatCurrency(
            double amount) {

        if (Math.abs(amount
                - Math.rint(amount)) < 0.000001) {

            return "₹"
                    + String.format(
                            "%,.0f",
                            amount);
        }

        return "₹"
                + String.format(
                        "%,.2f",
                        amount);
    }

    // =====================================================
    // STAT CARD
    // =====================================================

    private VBox createStatCard(
            String title,
            String count) {

        VBox card = new VBox(4);

        card.setPadding(
                new Insets(
                        14,
                        16,
                        14,
                        16));

        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 12px;" +
                        "-fx-border-width: 1px;");

        HBox.setHgrow(
                card,
                Priority.ALWAYS);

        Label tLbl = new Label(
                title);

        tLbl.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #566573;");

        Label cLbl = new Label(
                count);

        cLbl.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: 800;" +
                        "-fx-text-fill: #1B2631;");

        card.getChildren().addAll(
                tLbl,
                cLbl);

        return card;
    }

    // =====================================================
    // EARNING CARD
    // =====================================================

    private VBox createEarningCard(
            String title,
            String amount) {

        VBox card = new VBox(6);

        card.setPadding(
                new Insets(
                        16,
                        20,
                        16,
                        20));

        card.setStyle(
                "-fx-background-color: #D4EFDF;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 12px;");

        Label t = new Label(
                title);

        t.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #117864;");

        Label a = new Label(
                amount);

        a.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: 900;" +
                        "-fx-text-fill: #117864;");

        card.getChildren().addAll(
                t,
                a);

        return card;
    }

    // =====================================================
    // ACTION CARD
    // =====================================================

    private VBox createActionCard(
            String title,
            String subtitle,
            String icon,
            Runnable action) {

        VBox card = new VBox(6);

        card.setPadding(
                new Insets(16));

        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 12px;" +
                        "-fx-cursor: hand;");

        HBox.setHgrow(
                card,
                Priority.ALWAYS);

        Label iLbl = new Label(
                icon);

        iLbl.setStyle(
                "-fx-font-size: 24px;");

        Label tLbl = new Label(
                title);

        tLbl.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1B2631;");

        Label sLbl = new Label(
                subtitle);

        sLbl.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #566573;");

        card.getChildren().addAll(
                iLbl,
                tLbl,
                sLbl);

        card.setOnMouseClicked(
                e -> action.run());

        return card;
    }
}