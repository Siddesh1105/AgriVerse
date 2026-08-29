package com.mainproject.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Notifications screen for the AgriLink admin app.
 *
 * Same green sidebar / top bar shell as AdminDashboard so it feels like one
 * app, just with its own center content: a "Send Notification" form on the
 * left and a "Recent Notifications" table on the right, matching the
 * reference screenshot.
 *
 * Pattern follows the other screen classes (UserManagement, ProductManagement,
 * EquipmentManagement, AnalyticsReports): built with the shared Stage and a
 * reference back to AdminDashboard so the sidebar can jump between screens.
 */
public class NotificationManagement {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";

    private final Stage primaryStage;
    private final AdminDashboard dashboard;

    private BorderPane rootLayout;
    private Button activeNavButton;

    private ComboBox<String> audienceBox;
    private ComboBox<String> typeBox;
    private TextArea messageArea;
    private TableView<NotificationRow> table;
    private ObservableList<NotificationRow> notifications;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public NotificationManagement(Stage primaryStage, AdminDashboard dashboard) {
        this.primaryStage = primaryStage;
        this.dashboard = dashboard;
    }

    // entry point called from AdminDashboard's sidebar click
    public void show() {
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + BG + ";");

        rootLayout.setLeft(buildSidebar());
        rootLayout.setTop(buildTopBar());
        rootLayout.setCenter(buildContent());

        primaryStage.getScene().setRoot(rootLayout);
    }

    // ------------------------------------------------------------------
    // Sidebar (mirrors AdminDashboard so navigation feels seamless)
    // ------------------------------------------------------------------

    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color: " + GREEN_DARK + ";");

        VBox logoBox = new VBox(2);
        logoBox.setPadding(new Insets(22, 15, 22, 20));
        Label logo = new Label("\uD83C\uDF3F  AgriLink");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        Label subtitle = new Label("Admin Dashboard");
        subtitle.setTextFill(Color.web("#c8e6c9"));
        subtitle.setFont(Font.font("Segoe UI", 12));
        logoBox.getChildren().addAll(logo, subtitle);

        String[] navItems = {
                "Dashboard", "User Management", "Farmer Verification",
                "Product Management", "Order Management", "Live Marketplace",
                "Equipment Management", "Analytics & Reports", "Crop Price Management",
                "AI & Smart Tools", "Notifications", "Content Management",
                "Feedback & Reviews", "Reports & Complaints", "Payment Management",
                "Audit Logs", "System & Data Management"
        };

        VBox navBox = new VBox(1);
        for (String item : navItems) {
            Button navButton = buildNavButton(item);
            navBox.getChildren().add(navButton);
            if (item.equals("Notifications")) {
                activeNavButton = navButton;
                styleActiveButton(navButton);
            }
        }

        ScrollPane scrollPane = new ScrollPane(navBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: " + GREEN_DARK + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        sidebar.getChildren().addAll(logoBox, scrollPane);
        return sidebar;
    }

    private Button buildNavButton(String label) {
        Button button = new Button(label);
        button.setPrefWidth(228);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(11, 20, 11, 20));
        button.setFont(Font.font("Segoe UI", 13));
        styleInactiveButton(button);
        button.setOnAction(e -> handleNavClick(label));
        return button;
    }

    private void styleActiveButton(Button button) {
        button.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 0;");
    }

    private void styleInactiveButton(Button button) {
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #d7e4d9; -fx-background-radius: 0;");
    }

    // routes sidebar clicks - Dashboard and known screens go to their real
    // page, everything else just re-shows this screen with the click ignored
    private void handleNavClick(String pageName) {
        if (pageName.equals("Notifications")) {
            return; // already on this screen
        }
        if (pageName.equals("Dashboard")) {
            dashboard.showDashboard();
            return;
        }
        if (pageName.equals("User Management")) {
            new UserManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Farmer Verification")) {
            new FarmerVerification(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Product Management")) {
            new ProductManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Order Management")) {
            new OrderManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Live Marketplace")) {
            new LiveMarketplace(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Equipment Management")) {
            new EquipmentManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Analytics & Reports")) {
            new AnalyticsReports(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Content Management")) {
            new ContentManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Feedback & Reviews")) {
            new FeedbackReviews(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Reports & Complaints")) {
            new ReportsComplaints(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Payment Management")) {
            new PaymentManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Audit Logs")) {
            new AuditLogs(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("System & Data Management")) {
            new SystemDataManage(primaryStage, dashboard).show();
            return;
        }
        showInfoAlert(pageName, "This section hasn't been built yet in this demo.");
    }

    // ------------------------------------------------------------------
    // Top bar (same look/behaviour as AdminDashboard's)
    // ------------------------------------------------------------------

    private HBox buildTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(14, 25, 14, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #eaeaea; -fx-border-width: 0 0 1 0;");

        Button menuButton = new Button("\u2630");
        menuButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16;");
        menuButton.setOnAction(e -> toggleSidebar());

        TextField searchField = new TextField();
        searchField.setPromptText("Search by users, orders, products...");
        searchField.setPrefWidth(420);
        searchField.setStyle("-fx-background-color: transparent;");
        searchField.setOnAction(e -> handleSearch(searchField.getText()));

        HBox searchBox = new HBox(8, new Label("\uD83D\uDD0D"), searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(6, 15, 6, 15));
        searchBox.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 20;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button bellButton = new Button("\uD83D\uDD14");
        bellButton.setStyle("-fx-background-color: transparent; -fx-font-size: 15;");
        bellButton.setOnAction(
                e -> showInfoAlert("Notifications", "You have 42 pending approvals and 3 new orders today."));

        Button messageButton = new Button("\uD83D\uDCAC");
        messageButton.setStyle("-fx-background-color: transparent; -fx-font-size: 15;");
        messageButton.setOnAction(e -> showInfoAlert("Messages", "No new messages right now."));

        MenuButton profileMenu = new MenuButton("Super Admin");
        profileMenu.setStyle("-fx-background-color: transparent;");
        MenuItem profileItem = new MenuItem("My Profile");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem logoutItem = new MenuItem("Logout");
        profileItem.setOnAction(e -> showInfoAlert("Profile", "Opening admin profile page."));
        settingsItem.setOnAction(e -> showInfoAlert("Settings", "Opening settings page."));
        logoutItem.setOnAction(e -> handleLogout());
        profileMenu.getItems().addAll(profileItem, settingsItem, logoutItem);

        topBar.getChildren().addAll(menuButton, searchBox, spacer, bellButton, messageButton, profileMenu);
        return topBar;
    }

    private void toggleSidebar() {
        Node sidebar = rootLayout.getLeft();
        if (sidebar != null) {
            sidebar.setVisible(!sidebar.isVisible());
            sidebar.setManaged(sidebar.isVisible());
        }
    }

    private void handleSearch(String query) {
        if (query == null || query.isBlank()) {
            showInfoAlert("Search", "Type something first, then press Enter.");
        } else {
            showInfoAlert("Search Results", "Searching for: \"" + query + "\"");
        }
    }

    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Logout");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            showInfoAlert("Logged Out", "You have been logged out.");
        }
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ------------------------------------------------------------------
    // Center content: send form + recent notifications table
    // ------------------------------------------------------------------

    private ScrollPane buildContent() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(20));

        Label heading = new Label("Notification Management");
        heading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        HBox row = new HBox(20);
        VBox sendPanel = buildSendPanel();
        VBox listPanel = buildRecentNotificationsPanel();
        HBox.setHgrow(listPanel, Priority.ALWAYS);
        row.getChildren().addAll(sendPanel, listPanel);

        view.getChildren().addAll(heading, row);

        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + BG + "; -fx-background: " + BG + ";");
        return scrollPane;
    }

    private VBox buildSendPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(300);
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label sendTitle = new Label("Send Notification");
        sendTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        Label audienceLbl = new Label("Select Audience");
        audienceBox = new ComboBox<>(FXCollections.observableArrayList(
                "All Users", "Farmers", "Buyers", "Purchasers"));
        audienceBox.setValue("All Users");
        audienceBox.setMaxWidth(Double.MAX_VALUE);

        Label typeLbl = new Label("Notification Type");
        typeBox = new ComboBox<>(FXCollections.observableArrayList(
                "General Announcement", "Weather Alert", "Price Update", "Order Update"));
        typeBox.setValue("General Announcement");
        typeBox.setMaxWidth(Double.MAX_VALUE);

        Label msgLbl = new Label("Message");
        messageArea = new TextArea();
        messageArea.setPromptText("Type your message here...");
        messageArea.setPrefHeight(120);
        messageArea.setWrapText(true);

        Button sendBtn = new Button("Send Notification");
        sendBtn.setMaxWidth(Double.MAX_VALUE);
        sendBtn.setStyle("-fx-background-color:" + GREEN + "; -fx-text-fill:white; -fx-font-weight:bold;"
                + " -fx-padding:10; -fx-background-radius:6;");
        sendBtn.setOnAction(e -> handleSendNotification());

        Button clearBtn = new Button("Clear");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setStyle(
                "-fx-background-color: #f3f4f6; -fx-text-fill: #333; -fx-padding:8; -fx-background-radius:6;");
        clearBtn.setOnAction(e -> clearForm());

        panel.getChildren().addAll(sendTitle, audienceLbl, audienceBox, typeLbl, typeBox,
                msgLbl, messageArea, sendBtn, clearBtn);
        return panel;
    }

    // validates the message, inserts a new row at the top of the table and
    // resets the form - stands in for an actual send-to-server call
    private void handleSendNotification() {
        String message = messageArea.getText();
        if (message == null || message.isBlank()) {
            showInfoAlert("Message Required", "Please type a message before sending.");
            return;
        }

        String audience = audienceBox.getValue();
        String type = typeBox.getValue();
        String today = LocalDate.now().format(DATE_FMT);

        NotificationRow newRow = new NotificationRow(type, audience, today, "Sent");
        notifications.add(0, newRow);

        showInfoAlert("Notification Sent", "Your \"" + type + "\" notification was sent to " + audience + ".");
        clearForm();
    }

    private void clearForm() {
        audienceBox.setValue("All Users");
        typeBox.setValue("General Announcement");
        messageArea.clear();
    }

    private VBox buildRecentNotificationsPanel() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Recent Notifications");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Hyperlink viewAll = new Hyperlink("View All");
        viewAll.setOnAction(e -> showInfoAlert("All Notifications", "Opening the full notification history."));
        header.getChildren().addAll(title, spacer, viewAll);

        table = buildNotificationsTable();

        panel.getChildren().addAll(header, table);
        return panel;
    }

    private TableView<NotificationRow> buildNotificationsTable() {
        TableView<NotificationRow> tableView = new TableView<>();
        tableView.setPrefHeight(320);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<NotificationRow, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<NotificationRow, String> audienceCol = new TableColumn<>("Audience");
        audienceCol.setCellValueFactory(new PropertyValueFactory<>("audience"));

        TableColumn<NotificationRow, String> sentOnCol = new TableColumn<>("Sent On");
        sentOnCol.setCellValueFactory(new PropertyValueFactory<>("sentOn"));

        TableColumn<NotificationRow, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<NotificationRow, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label badge = new Label(status);
                badge.setPadding(new Insets(3, 10, 3, 10));
                badge.setStyle("-fx-background-color:#E8F5E9; -fx-text-fill:" + GREEN
                        + "; -fx-background-radius:10; -fx-font-size:11;");
                setGraphic(badge);
                setText(null);
            }
        });

        tableView.getColumns().addAll(titleCol, audienceCol, sentOnCol, statusCol);

        notifications = loadRecentNotifications();
        tableView.setItems(notifications);

        // clicking a row shows the full details, and offers a resend option
        tableView.setRowFactory(tv -> {
            TableRow<NotificationRow> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty()) {
                    showNotificationDetails(row.getItem());
                }
            });
            return row;
        });

        return tableView;
    }

    private ObservableList<NotificationRow> loadRecentNotifications() {
        ObservableList<NotificationRow> rows = FXCollections.observableArrayList();
        rows.add(new NotificationRow("New Crop Price Update", "Farmers", "29 May 2025", "Sent"));
        rows.add(new NotificationRow("Weather Alert - Heavy Rain", "All Users", "29 May 2025", "Sent"));
        rows.add(new NotificationRow("New Live Session Started", "Buyers", "28 May 2025", "Sent"));
        rows.add(new NotificationRow("Order Delivery Update", "Purchasers", "28 May 2025", "Sent"));
        rows.add(new NotificationRow("Maintenance Notification", "All Users", "27 May 2025", "Sent"));
        return rows;
    }

    private void showNotificationDetails(NotificationRow row) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notification Details");
        alert.setHeaderText(row.getTitle());
        alert.setContentText("Audience: " + row.getAudience()
                + "\nSent On: " + row.getSentOn()
                + "\nStatus: " + row.getStatus()
                + "\n\nResend this notification?");
        alert.getButtonTypes().setAll(new ButtonType("Resend"), ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().getText().equals("Resend")) {
            String today = LocalDate.now().format(DATE_FMT);
            notifications.add(0, new NotificationRow(row.getTitle(), row.getAudience(), today, "Sent"));
            showInfoAlert("Resent", "\"" + row.getTitle() + "\" was resent to " + row.getAudience() + ".");
        }
    }

    // ------------------------------------------------------------------
    // Simple data model for a table row
    // ------------------------------------------------------------------

    public static class NotificationRow {
        private final String title;
        private final String audience;
        private final String sentOn;
        private final String status;

        public NotificationRow(String title, String audience, String sentOn, String status) {
            this.title = title;
            this.audience = audience;
            this.sentOn = sentOn;
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public String getAudience() {
            return audience;
        }

        public String getSentOn() {
            return sentOn;
        }

        public String getStatus() {
            return status;
        }
    }
}