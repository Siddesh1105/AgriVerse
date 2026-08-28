package com.mainproject.view.farmer;


import com.mainproject.controller.NotificationController;
import com.mainproject.model.Notification;

import javafx.application.Platform;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class Notifications {

    private final String farmerEmail;
    private final NotificationController notificationController;

    private final VBox notificationContainer = new VBox(10);

    private final Label unreadLabel = new Label("0 unread");

    private List<Notification> notifications = new ArrayList<>();

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Notifications(FarmerDashboard farmerDashboard, String farmerEmail) {

        this.farmerEmail = farmerEmail;
        this.notificationController = new NotificationController();

        System.out.println(
                "Notifications opened for: "
                        + farmerEmail);
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root = new VBox(16);

        root.setPadding(
                new Insets(10));

        // =================================================
        // HEADER
        // =================================================

        Label title = new Label(
                "Notifications");

        title.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: 800;" +
                        "-fx-text-fill: #1B2631;");

        Label subtitle = new Label(
                "Stay updated with your farm activities.");

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #566573;");

        VBox header = new VBox(
                3,
                title,
                subtitle);

        // =================================================
        // ACTION BUTTONS
        // =================================================

        Button markAllButton = new Button(
                "✓ Mark all as read");

        markAllButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #117864;" +
                        "-fx-border-color: #A2D9CE;" +
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

        unreadLabel.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #117864;");

        HBox actionBar = new HBox(
                10,
                markAllButton,
                clearButton,
                spacer,
                unreadLabel);

        actionBar.setAlignment(
                Pos.CENTER_LEFT);

        // =================================================
        // MARK ALL
        // =================================================

        markAllButton.setOnAction(
                event -> {

                    new Thread(() -> {

                        boolean success = notificationController
                                .markAllAsRead(
                                        farmerEmail);

                        if (success) {
                            loadNotifications();
                        }

                    }).start();
                });

        // =================================================
        // CLEAR ALL
        // =================================================

        clearButton.setOnAction(
                event -> {

                    new Thread(() -> {

                        boolean success = notificationController
                                .deleteAll(
                                        farmerEmail);

                        if (success) {
                            loadNotifications();
                        }

                    }).start();
                });

        // =================================================
        // SCROLL
        // =================================================

        ScrollPane scroll = new ScrollPane(
                notificationContainer);

        scroll.setFitToWidth(
                true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;");

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        root.getChildren().addAll(
                header,
                actionBar,
                scroll);

        // =================================================
        // LOAD FIRESTORE
        // =================================================

        loadNotifications();
        return root;
    }

    // =====================================================
    // LOAD NOTIFICATIONS
    // =====================================================

    private void loadNotifications() {

        Platform.runLater(() -> {

            notificationContainer
                    .getChildren()
                    .clear();

            Label loading = new Label(
                    "Loading notifications...");

            loading.setStyle(
                    "-fx-text-fill: #566573;" +
                            "-fx-padding: 20px;");

            notificationContainer
                    .getChildren()
                    .add(
                            loading);
        });

        Thread thread = new Thread(() -> {

            List<Notification> result = notificationController
                    .getFarmerNotifications(
                            farmerEmail);

            Platform.runLater(() -> {

                notifications = result;

                refreshNotifications();
            });
        });

        thread.setDaemon(
                true);

        thread.start();
    }

    // =====================================================
    // REFRESH UI
    // =====================================================

    private void refreshNotifications() {

        notificationContainer
                .getChildren()
                .clear();

        int unread = 0;

        int displayed = 0;

        for (Notification notification : notifications) {

            if (!notification.isRead()) {
                unread++;
            }

            notificationContainer
                    .getChildren()
                    .add(
                            createNotificationCard(
                                    notification));

            displayed++;
        }

        unreadLabel.setText(
                unread + " unread");

        if (displayed == 0) {

            Label empty = new Label(
                    "No notifications found.");

            empty.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: #566573;" +
                            "-fx-padding: 30px;");

            notificationContainer
                    .getChildren()
                    .add(
                            empty);
        }
    }

    // =====================================================
    // CREATE CARD
    // =====================================================

    private HBox createNotificationCard(
            Notification notification) {

        HBox card = new HBox(15);

        card.setPadding(
                new Insets(15));

        card.setAlignment(
                Pos.CENTER_LEFT);

        String background = notification.isRead()
                ? "#FFFFFF"
                : "#E8F8F5";

        card.setStyle(
                "-fx-background-color: "
                        + background + ";" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;");

        // =================================================
        // ICON
        // =================================================

        Label icon = new Label(
                getIcon(
                        notification.getType()));

        icon.setStyle(
                "-fx-font-size: 22px;");

        // =================================================
        // CONTENT
        // =================================================

        VBox content = new VBox(4);

        HBox.setHgrow(
                content,
                Priority.ALWAYS);

        Label title = new Label(
                notification.getTitle());

        title.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1B2631;");

        Label message = new Label(
                notification.getMessage());

        message.setWrapText(
                true);

        message.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #566573;");

        Label type = new Label(
                notification.getType());

        type.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: #117864;");

        content.getChildren().addAll(
                title,
                message,
                type);

        // =================================================
        // TIME
        // =================================================

        Label time = new Label(
                notification.getTimestamp());

        time.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: #566573;");

        // =================================================
        // UNREAD DOT
        // =================================================

        Label dot = new Label(
                notification.isRead()
                        ? ""
                        : "●");

        dot.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #117864;");

        // =================================================
        // DELETE BUTTON
        // =================================================

        Button deleteButton = new Button(
                "Delete");

        deleteButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #C0392B;" +
                        "-fx-border-color: #E6B0AA;" +
                        "-fx-border-radius: 7px;" +
                        "-fx-background-radius: 7px;" +
                        "-fx-padding: 6 12;" +
                        "-fx-cursor: hand;");

        deleteButton.setOnAction(
                event -> {

                    event.consume();

                    Alert alert =
                            new Alert(
                                    Alert.AlertType.CONFIRMATION);

                    alert.setTitle(
                            "Delete Notification");

                    alert.setHeaderText(
                            "Delete this notification?");

                    alert.setContentText(
                            "This notification will be permanently deleted.");

                    alert.showAndWait()
                            .ifPresent(
                                    response -> {

                                        if (response
                                                == ButtonType.OK) {

                                            new Thread(
                                                    () -> {

                                                        boolean success =
                                                                notificationController
                                                                        .deleteNotification(
                                                                                notification
                                                                                        .getNotificationId());

                                                        if (success) {

                                                            notifications
                                                                    .remove(
                                                                            notification);

                                                            Platform.runLater(
                                                                    this::refreshNotifications);
                                                        }

                                                    }).start();
                                        }

                                    });
                });

        // =================================================
        // ADD EVERYTHING
        // =================================================

        card.getChildren().addAll(
                icon,
                content,
                time,
                dot,
                deleteButton);

        // =================================================
        // CLICK → MARK AS READ
        // =================================================

        card.setOnMouseClicked(
                event -> {

                    if (!notification.isRead()) {

                        new Thread(
                                () -> {

                                    boolean success =
                                            notificationController
                                                    .markAsRead(
                                                            notification
                                                                    .getNotificationId());

                                    if (success) {

                                        notification.setRead(
                                                true);

                                        Platform.runLater(
                                                this::refreshNotifications);
                                    }

                                }).start();
                    }
                });

        return card;
    }

    // =====================================================
    // ICON
    // =====================================================

    private String getIcon(
            String type) {

        switch (type) {

            case "Orders":
                return "📦";

            case "Weather":
                return "🌧";

            case "Market":
                return "📈";

            case "System":
                return "🔔";

            default:
                return "🔔";
        }
    }
}