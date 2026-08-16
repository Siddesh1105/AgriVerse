package com.mainproject.view;

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

import java.util.ArrayList;
import java.util.List;

public class Notifications {

    private static final String GREEN = "#117864";
    private static final String BORDER = "#A2D9CE";
    private static final String TEXT = "#1B2631";
    private static final String SUB_TEXT = "#566573";

    private final List<AppNotification> notifications = new ArrayList<>();

    private VBox notificationContainer;

    private Label unreadLabel;

    private String selectedFilter = "All";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Notifications(FarmerDashboard farmerDashboard) {

        // IMPORTANT:
        // Load notifications when the page is created.
        loadNotifications();
    }

    // =========================================================
    // GET VIEW
    // =========================================================

    public Node getView() {

        VBox root = new VBox(16);

        root.setPadding(
                new Insets(10));

        // =====================================================
        // TITLE
        // =====================================================

        Label title = new Label("Notifications");

        title.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: 800;" +
                        "-fx-text-fill: " + TEXT + ";");

        Label subtitle = new Label(
                "Stay updated with your farm activities.");

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: " + SUB_TEXT + ";");

        VBox header = new VBox(
                3,
                title,
                subtitle);

        // =====================================================
        // ACTION BAR
        // =====================================================

        Button markAllButton = new Button(
                "✓ Mark all as read");

        markAllButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + GREEN + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 7px;" +
                        "-fx-background-radius: 7px;" +
                        "-fx-padding: 7 14;" +
                        "-fx-cursor: hand;");

        Button clearButton = new Button(
                "Clear all");

        clearButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #C0392B;" +
                        "-fx-border-color: #E6B0AA;" +
                        "-fx-border-radius: 7px;" +
                        "-fx-background-radius: 7px;" +
                        "-fx-padding: 7 14;" +
                        "-fx-cursor: hand;");

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        unreadLabel = new Label();

        unreadLabel.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + GREEN + ";");

        HBox actionBar = new HBox(
                10,
                markAllButton,
                clearButton,
                spacer,
                unreadLabel);

        actionBar.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // MARK ALL
        // =====================================================

        markAllButton.setOnAction(
                event -> {

                    for (AppNotification notification : notifications) {

                        notification.read = true;
                    }

                    refresh();
                });

        // =====================================================
        // CLEAR ALL
        // =====================================================

        clearButton.setOnAction(
                event -> {

                    notifications.clear();

                    refresh();
                });

        // =====================================================
        // FILTER BUTTONS
        // =====================================================

        Button allButton = createFilterButton("All");

        Button ordersButton = createFilterButton("Orders");

        Button systemButton = createFilterButton("System");

        Button weatherButton = createFilterButton("Weather");

        Button marketButton = createFilterButton("Market");

        HBox filters = new HBox(
                10,
                allButton,
                ordersButton,
                systemButton,
                weatherButton,
                marketButton);

        // =====================================================
        // FILTER ACTIONS
        // =====================================================

        allButton.setOnAction(
                e -> {

                    selectedFilter = "All";

                    setSelectedFilter(
                            allButton,
                            ordersButton,
                            systemButton,
                            weatherButton,
                            marketButton);

                    refresh();
                });

        ordersButton.setOnAction(
                e -> {

                    selectedFilter = "Orders";

                    setSelectedFilter(
                            ordersButton,
                            allButton,
                            systemButton,
                            weatherButton,
                            marketButton);

                    refresh();
                });

        systemButton.setOnAction(
                e -> {

                    selectedFilter = "System";

                    setSelectedFilter(
                            systemButton,
                            allButton,
                            ordersButton,
                            weatherButton,
                            marketButton);

                    refresh();
                });

        weatherButton.setOnAction(
                e -> {

                    selectedFilter = "Weather";

                    setSelectedFilter(
                            weatherButton,
                            allButton,
                            ordersButton,
                            systemButton,
                            marketButton);

                    refresh();
                });

        marketButton.setOnAction(
                e -> {

                    selectedFilter = "Market";

                    setSelectedFilter(
                            marketButton,
                            allButton,
                            ordersButton,
                            systemButton,
                            weatherButton);

                    refresh();
                });

        // =====================================================
        // NOTIFICATION CONTAINER
        // =====================================================

        notificationContainer = new VBox(10);

        notificationContainer.setFillWidth(
                true);

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scroll = new ScrollPane(
                notificationContainer);

        scroll.setFitToWidth(true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;");

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        // =====================================================
        // ROOT
        // =====================================================

        root.getChildren().addAll(
                header,
                actionBar,
                filters,
                scroll);

        // =====================================================
        // FIRST REFRESH
        // =====================================================

        refresh();

        return root;
    }

    // =========================================================
    // LOAD NOTIFICATIONS
    // =========================================================

    private void loadNotifications() {

        System.out.println(
                "Loading notifications...");

        notifications.add(
                new AppNotification(
                        "Orders",
                        "New order received for Tomato (50 kg)",
                        "2 min ago",
                        false));

        notifications.add(
                new AppNotification(
                        "System",
                        "Your product Onion is low in stock.",
                        "1 hour ago",
                        false));

        notifications.add(
                new AppNotification(
                        "Weather",
                        "Heavy rainfall expected tomorrow.",
                        "3 hours ago",
                        false));

        notifications.add(
                new AppNotification(
                        "Orders",
                        "Payment of ₹2,500 received for Order #ORD123",
                        "5 hours ago",
                        false));

        notifications.add(
                new AppNotification(
                        "Market",
                        "Market price for Wheat increased by 3%.",
                        "Yesterday",
                        true));

        System.out.println(
                "Notifications loaded: "
                        + notifications.size());
    }

    // =========================================================
    // REFRESH
    // =========================================================

    private void refresh() {

        notificationContainer
                .getChildren()
                .clear();

        int unreadCount = 0;

        int displayedCount = 0;

        for (AppNotification notification : notifications) {

            if (!notification.read) {

                unreadCount++;
            }

            // FILTER
            if (!selectedFilter.equals("All")
                    &&
                    !notification.type.equals(
                            selectedFilter)) {

                continue;
            }

            notificationContainer
                    .getChildren()
                    .add(
                            createNotificationCard(
                                    notification));

            displayedCount++;
        }

        unreadLabel.setText(
                unreadCount + " unread");

        // =====================================================
        // EMPTY MESSAGE
        // =====================================================

        if (displayedCount == 0) {

            Label empty = new Label(
                    "No notifications found.");

            empty.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: " + SUB_TEXT + ";" +
                            "-fx-padding: 30;");

            notificationContainer
                    .getChildren()
                    .add(empty);
        }

        System.out.println(
                "Displayed notifications: "
                        + displayedCount);
    }

    // =========================================================
    // CREATE CARD
    // =========================================================

    private HBox createNotificationCard(
            AppNotification notification) {

        HBox card = new HBox(16);

        card.setAlignment(
                Pos.CENTER_LEFT);

        card.setPadding(
                new Insets(16));

        card.setMaxWidth(
                Double.MAX_VALUE);

        String background = notification.read
                ? "#FFFFFF"
                : "#E8F8F5";

        card.setStyle(
                "-fx-background-color: "
                        + background
                        + ";" +
                        "-fx-border-color: "
                        + BORDER
                        + ";" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;");

        // =====================================================
        // ICON
        // =====================================================

        Label icon = new Label(
                getIcon(
                        notification.type));

        icon.setStyle(
                "-fx-font-size: 22px;");

        icon.setMinWidth(35);

        // =====================================================
        // MESSAGE
        // =====================================================

        VBox textBox = new VBox(4);

        HBox.setHgrow(
                textBox,
                Priority.ALWAYS);

        Label message = new Label(
                notification.message);

        message.setWrapText(
                true);

        message.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: "
                        +
                        (notification.read
                                ? "normal"
                                : "bold")
                        + ";" +
                        "-fx-text-fill: " + TEXT + ";");

        Label type = new Label(
                notification.type);

        type.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: " + GREEN + ";");

        textBox.getChildren().addAll(
                message,
                type);

        // =====================================================
        // TIME
        // =====================================================

        Label time = new Label(
                notification.time);

        time.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: " + SUB_TEXT + ";");

        // =====================================================
        // UNREAD
        // =====================================================

        Label dot = new Label(
                notification.read
                        ? ""
                        : "●");

        dot.setStyle(
                "-fx-text-fill: " + GREEN + ";" +
                        "-fx-font-size: 11px;");

        // =====================================================
        // CLICK
        // =====================================================

        card.setOnMouseClicked(
                event -> {

                    notification.read = true;

                    refresh();
                });

        card.getChildren().addAll(
                icon,
                textBox,
                time,
                dot);

        return card;
    }

    // =========================================================
    // ICON
    // =========================================================

    private String getIcon(
            String type) {

        switch (type) {

            case "Orders":
                return "📦";

            case "System":
                return "⚠";

            case "Weather":
                return "☁";

            case "Market":
                return "📈";

            default:
                return "🔔";
        }
    }

    // =========================================================
    // FILTER BUTTON
    // =========================================================

    private Button createFilterButton(
            String text) {

        Button button = new Button(text);

        if (text.equals(
                selectedFilter)) {

            setSelectedStyle(
                    button);

        } else {

            setNormalStyle(
                    button);
        }

        return button;
    }

    // =========================================================
    // SET SELECTED FILTER
    // =========================================================

    private void setSelectedFilter(
            Button selected,
            Button... others) {

        setSelectedStyle(
                selected);

        for (Button button : others) {

            setNormalStyle(
                    button);
        }
    }

    // =========================================================
    // SELECTED STYLE
    // =========================================================

    private void setSelectedStyle(
            Button button) {

        button.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-padding: 7 18;" +
                        "-fx-cursor: hand;");
    }

    // =========================================================
    // NORMAL STYLE
    // =========================================================

    private void setNormalStyle(
            Button button) {

        button.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 20px;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-padding: 7 18;" +
                        "-fx-cursor: hand;");
    }

    // =========================================================
    // MODEL
    // =========================================================

    private static class AppNotification {

        String type;

        String message;

        String time;

        boolean read;

        AppNotification(
                String type,
                String message,
                String time,
                boolean read) {

            this.type = type;

            this.message = message;

            this.time = time;

            this.read = read;
        }
    }
}