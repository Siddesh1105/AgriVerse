package com.mainproject.view.buyer;

import com.mainproject.controller.NotificationController;
import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Notification {

    private final BuyerDashboard mainController;

    private final NotificationController notificationController;

    private VBox notificationList;

    private Label unreadLabel;

    public Notification(BuyerDashboard controller) {

        this.mainController = controller;

        this.notificationController =
                new NotificationController();
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root = new VBox(18);

        root.setPadding(
                new Insets(25, 30, 25, 30)
        );

        root.setStyle(
                "-fx-background-color:#F8FAFC;"
        );

        HBox header =
                createHeader();

        notificationList =
                new VBox(10);

        ScrollPane scrollPane =
                new ScrollPane(notificationList);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;" +
                "-fx-border-color:transparent;"
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        root.getChildren().addAll(
                header,
                scrollPane
        );

        loadNotifications();

        LanguageManager.apply(root);

        return root;
    }

    // =====================================================
    // HEADER
    // =====================================================

    private HBox createHeader() {

        HBox header =
                new HBox(12);

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox titleBox =
                new VBox(4);

        Label title =
                new Label("🔔 Notifications");

        title.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        unreadLabel =
                new Label();

        unreadLabel.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#64748B;"
        );

        titleBox.getChildren().addAll(
                title,
                unreadLabel
        );

        HBox.setHgrow(
                titleBox,
                Priority.ALWAYS
        );

        // =================================================
        // REFRESH
        // =================================================

        Button refreshButton =
                new Button("↻ Refresh");

        refreshButton.setStyle(
                "-fx-background-color:#E2E8F0;" +
                "-fx-text-fill:#334155;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-padding:8 14;" +
                "-fx-cursor:hand;"
        );

        refreshButton.setOnAction(e ->
                loadNotifications()
        );

        // =================================================
        // MARK ALL READ
        // =================================================

        Button markAllRead =
                new Button("✓ Mark All Read");

        markAllRead.setStyle(
                "-fx-background-color:#DBEAFE;" +
                "-fx-text-fill:#1D4ED8;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-padding:8 14;" +
                "-fx-cursor:hand;"
        );

        markAllRead.setOnAction(e -> {

            String buyerEmail =
                    mainController.getBuyerEmail();

            if (buyerEmail == null ||
                    buyerEmail.trim().isEmpty()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Buyer email not found."
                );

                return;
            }

            notificationController.markAllAsRead(
                    buyerEmail
            );

            loadNotifications();
        });

        // =================================================
        // CLEAR ALL
        // =================================================

        Button clearAll =
                new Button("🗑 Clear All");

        clearAll.setStyle(
                "-fx-background-color:#FEE2E2;" +
                "-fx-text-fill:#B91C1C;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-padding:8 14;" +
                "-fx-cursor:hand;"
        );

        clearAll.setOnAction(e ->
                clearAllNotifications()
        );

        header.getChildren().addAll(
                titleBox,
                refreshButton,
                markAllRead,
                clearAll
        );

        return header;
    }

    // =====================================================
    // LOAD NOTIFICATIONS
    // =====================================================

    private void loadNotifications() {

        if (notificationList == null) {
            return;
        }

        notificationList.getChildren().clear();

        String buyerEmail =
                mainController.getBuyerEmail();

        if (buyerEmail == null ||
                buyerEmail.trim().isEmpty()) {

            showEmptyMessage(
                    "Buyer email not found."
            );

            return;
        }

        List<com.mainproject.model.Notification>
                notifications =
                notificationController
                        .getNotificationsByUser(
                                buyerEmail
                        );

        int unreadCount =
                notificationController
                        .getUnreadCount(
                                buyerEmail
                        );

        updateUnreadLabel(
                unreadCount
        );

        if (notifications == null ||
                notifications.isEmpty()) {

            showEmptyMessage(
                    "🔔 No notifications yet."
            );

            return;
        }

        for (com.mainproject.model.Notification notification
                : notifications) {

            if (notification != null) {

                notificationList.getChildren().add(
                        createNotificationCard(
                                notification
                        )
                );
            }
        }
    }

    // =====================================================
    // UPDATE UNREAD COUNT
    // =====================================================

    private void updateUnreadLabel(
            int unreadCount) {

        if (unreadLabel == null) {
            return;
        }

        if (unreadCount <= 0) {

            unreadLabel.setText(
                    "All caught up! No unread notifications."
            );

        } else if (unreadCount == 1) {

            unreadLabel.setText(
                    "1 unread notification"
            );

        } else {

            unreadLabel.setText(
                    unreadCount
                            + " unread notifications"
            );
        }
    }

    // =====================================================
    // EMPTY MESSAGE
    // =====================================================

    private void showEmptyMessage(
            String message) {

        VBox emptyBox =
                new VBox(12);

        emptyBox.setAlignment(
                Pos.CENTER
        );

        emptyBox.setPadding(
                new Insets(60)
        );

        emptyBox.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:12;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:12;"
        );

        Label icon =
                new Label("🔔");

        icon.setStyle(
                "-fx-font-size:40px;"
        );

        Label text =
                new Label(message);

        text.setStyle(
                "-fx-font-size:16px;" +
                "-fx-text-fill:#64748B;"
        );

        emptyBox.getChildren().addAll(
                icon,
                text
        );

        notificationList.getChildren().add(
                emptyBox
        );
    }

    // =====================================================
    // NOTIFICATION CARD
    // =====================================================

    private HBox createNotificationCard(
            com.mainproject.model.Notification notification) {

        HBox card =
                new HBox(15);

        card.setAlignment(
                Pos.CENTER_LEFT
        );

        card.setPadding(
                new Insets(15)
        );

        boolean unread =
                !notification.isRead();

        if (unread) {

            card.setStyle(
                    "-fx-background-color:#F0FDF4;" +
                    "-fx-border-color:#86EFAC;" +
                    "-fx-border-radius:10;" +
                    "-fx-background-radius:10;"
            );

        } else {

            card.setStyle(
                    "-fx-background-color:white;" +
                    "-fx-border-color:#E2E8F0;" +
                    "-fx-border-radius:10;" +
                    "-fx-background-radius:10;"
            );
        }

        // ICON

        Label icon =
                new Label(
                        getNotificationIcon(
                                notification.getType()
                        )
                );

        icon.setStyle(
                "-fx-font-size:26px;"
        );

        // DETAILS

        VBox details =
                new VBox(5);

        Label title =
                new Label(
                        safe(
                                notification.getTitle()
                        )
                );

        title.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label message =
                new Label(
                        safe(
                                notification.getMessage()
                        )
                );

        message.setWrapText(true);

        message.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#475569;"
        );

        Label time =
                new Label(
                        getTimeAgo(
                                notification.getCreatedAt()
                        )
                );

        time.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#94A3B8;"
        );

        details.getChildren().addAll(
                title,
                message,
                time
        );

        HBox.setHgrow(
                details,
                Priority.ALWAYS
        );

        // UNREAD DOT

        Label unreadDot =
                new Label();

        if (unread) {

            unreadDot.setText("●");

            unreadDot.setStyle(
                    "-fx-text-fill:#16A34A;" +
                    "-fx-font-size:18px;"
            );
        }

        // MARK READ

        Button readButton =
                new Button();

        if (unread) {

            readButton.setText(
                    "Mark Read"
            );

            readButton.setStyle(
                    "-fx-background-color:#DBEAFE;" +
                    "-fx-text-fill:#1D4ED8;" +
                    "-fx-background-radius:7;" +
                    "-fx-cursor:hand;"
            );

            readButton.setOnAction(e -> {

                notificationController.markAsRead(
                        notification.getNotificationId()
                );

                loadNotifications();
            });

        } else {

            readButton.setText("Read");

            readButton.setDisable(true);
        }

        // DELETE

        Button deleteButton =
                new Button("✕");

        deleteButton.setStyle(
                "-fx-background-color:#FEE2E2;" +
                "-fx-text-fill:#B91C1C;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        deleteButton.setOnAction(e -> {

            notificationController.deleteNotification(
                    notification.getNotificationId()
            );

            loadNotifications();
        });

        card.getChildren().addAll(
                icon,
                details,
                unreadDot,
                readButton,
                deleteButton
        );

        return card;
    }

    // =====================================================
    // CLEAR ALL
    // =====================================================

    private void clearAllNotifications() {

        String buyerEmail =
                mainController.getBuyerEmail();

        if (buyerEmail == null ||
                buyerEmail.trim().isEmpty()) {

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Clear Notifications"
        );

        confirmation.setHeaderText(null);

        confirmation.setContentText(
                "Are you sure you want to delete all notifications?"
        );

        ButtonType result =
                confirmation.showAndWait()
                        .orElse(
                                ButtonType.CANCEL
                        );

        if (result == ButtonType.OK) {

            notificationController
                    .deleteAllNotifications(
                            buyerEmail
                    );

            loadNotifications();
        }
    }

    // =====================================================
    // ICON
    // =====================================================

    private String getNotificationIcon(
            String type) {

        if (type == null) {
            return "🔔";
        }

        switch (type.toUpperCase()) {

            case "ORDER":
                return "🛒";

            case "PAYMENT":
                return "💳";

            case "DELIVERY":
                return "📦";

            case "FARMER":
                return "🌾";

            case "PRICE":
                return "📈";

            case "SUCCESS":
                return "✅";

            case "WARNING":
                return "⚠️";

            case "ERROR":
                return "❌";

            default:
                return "🔔";
        }
    }

    // =====================================================
    // TIME AGO
    // =====================================================

    private String getTimeAgo(
            Date date) {

        if (date == null) {
            return "";
        }

        long difference =
                new Date().getTime()
                        - date.getTime();

        long seconds =
                difference / 1000;

        long minutes =
                seconds / 60;

        long hours =
                minutes / 60;

        long days =
                hours / 24;

        if (seconds < 60) {
            return "Just now";
        }

        if (minutes < 60) {

            return minutes
                    + (minutes == 1
                    ? " minute ago"
                    : " minutes ago");
        }

        if (hours < 24) {

            return hours
                    + (hours == 1
                    ? " hour ago"
                    : " hours ago");
        }

        if (days == 1) {
            return "Yesterday";
        }

        if (days < 7) {
            return days + " days ago";
        }

        return new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a"
        ).format(date);
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