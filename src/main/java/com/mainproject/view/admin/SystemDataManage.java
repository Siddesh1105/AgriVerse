package com.mainproject.view.admin;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


public class SystemDataManage {

    // same palette as AdminDashboard so every screen looks like one app
    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("d MMM yyyy, hh:mm a", Locale.ENGLISH);

    private final Stage primaryStage;
    private final AdminDashboard adminDashboard;

    private BorderPane rootLayout;

    private List<ServiceStatus> services;
    private List<ActivityEntry> activityLog;
    private List<BackupRecord> backupHistory;

    private LocalDateTime lastBackupTime;
    private LocalDateTime nextBackupTime;
    private String backupStatus;

    private Label lastBackupValueLabel;
    private Label nextBackupValueLabel;
    private Label backupStatusBadge;
    private VBox activityListBox;

    public SystemDataManage(Stage primaryStage, AdminDashboard adminDashboard) {
        this.primaryStage = primaryStage;
        this.adminDashboard = adminDashboard;
    }

    // builds the screen and swaps it onto the shared stage
    public void show() {
        services = loadServices();
        activityLog = loadActivityLog();
        backupHistory = loadBackupHistory();

        lastBackupTime = LocalDateTime.of(2025, 5, 29, 2, 0);
        nextBackupTime = LocalDateTime.of(2025, 5, 30, 2, 0);
        backupStatus = "Completed";

        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + BG + ";");
        rootLayout.setLeft(buildSidebar());
        rootLayout.setTop(buildTopBar());
        rootLayout.setCenter(buildContentView());

        primaryStage.getScene().setRoot(rootLayout);
    }

    // ------------------------------------------------------------------
    // Sidebar (kept identical to AdminDashboard so navigation is seamless)
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
                "System & Data manage", "Notifications", "Content Management",
                "Feedback & Reviews", "Reports & Complaints", "Payment Management", "Audit Logs"
        };

        VBox navBox = new VBox(1);
        for (String item : navItems) {
            Button navButton = buildNavButton(item);
            if (item.equals("System & Data manage")) {
                styleActiveButton(navButton);
            } else {
                styleInactiveButton(navButton);
            }
            navBox.getChildren().add(navButton);
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
        button.setOnAction(e -> handleNavClick(label));
        return button;
    }

    private void styleActiveButton(Button button) {
        button.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 0;");
    }

    private void styleInactiveButton(Button button) {
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #d7e4d9; -fx-background-radius: 0;");
    }

    // sends the admin to whichever screen they clicked in the sidebar
    private void handleNavClick(String pageName) {
        if (pageName.equals("System & Data manage")) {
            return; // already on this screen
        }
        if (pageName.equals("Dashboard")) {
            adminDashboard.showDashboard();
            return;
        }
        if (pageName.equals("User Management")) {
            new UserManagement(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Product Management")) {
            new ProductManagement(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Farmer Verification")) {
            new FarmerVerification(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Order Management")) {
            new OrderManagement(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Live Marketplace")) {
            new LiveMarketplace(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Analytics & Reports")) {
            new AnalyticsReports(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Reports & Complaints")) {
            new ReportsComplaints(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Audit Logs")) {
            new AuditLogs(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Feedback & Reviews")) {
            new FeedbackReviews(primaryStage, adminDashboard).show();
            return;
        }
        // everything else falls back to Equipment Management for now,
        // the same placeholder pattern AdminDashboard itself uses
        new EquipmentManagement(primaryStage, adminDashboard).show();
    }

    // ------------------------------------------------------------------
    // Top bar
    // ------------------------------------------------------------------

    private HBox buildTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(14, 25, 14, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #eaeaea; -fx-border-width: 0 0 1 0;");

        Label backArrow = new Label("\u2190 Back to Dashboard");
        backArrow.setStyle("-fx-font-size: 13; -fx-text-fill: #555; -fx-cursor: hand;");
        backArrow.setOnMouseClicked(e -> adminDashboard.showDashboard());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button bellButton = new Button("\uD83D\uDD14");
        bellButton.setStyle("-fx-background-color: transparent; -fx-font-size: 15;");
        bellButton.setOnAction(e -> showInfoAlert("Notifications", "You have 42 pending approvals and 3 new orders today."));

        MenuButton profileMenu = new MenuButton("Super Admin");
        profileMenu.setStyle("-fx-background-color: transparent;");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> handleLogout());
        profileMenu.getItems().add(logoutItem);

        topBar.getChildren().addAll(backArrow, spacer, bellButton, profileMenu);
        return topBar;
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

    // ------------------------------------------------------------------
    // Main content: left column (status/overview/activity) + right column (backup)
    // ------------------------------------------------------------------

    private ScrollPane buildContentView() {
        HBox columns = new HBox(20);
        columns.setPadding(new Insets(20));

        VBox leftColumn = new VBox(20);
        leftColumn.getChildren().addAll(buildSystemStatusSection(), buildDataOverviewSection(), buildSystemActivitySection());
        HBox.setHgrow(leftColumn, Priority.ALWAYS);

        VBox rightColumn = buildBackupStatusCard();
        rightColumn.setPrefWidth(300);
        rightColumn.setMinWidth(280);

        columns.getChildren().addAll(leftColumn, rightColumn);

        ScrollPane scrollPane = new ScrollPane(columns);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + BG + "; -fx-background: " + BG + ";");
        return scrollPane;
    }

    // ---- System Status ----

    private VBox buildSystemStatusSection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(18));
        section.setStyle(cardStyle());

        Label title = new Label("System Status");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        HBox statusRow = new HBox(12);
        for (ServiceStatus service : services) {
            statusRow.getChildren().add(buildServiceStatusCard(service));
        }

        section.getChildren().addAll(title, statusRow);
        return section;
    }

    private HBox buildServiceStatusCard(ServiceStatus service) {
        Label icon = new Label(service.isOperational() ? "\u2705" : "\u26A0");
        icon.setFont(Font.font(15));

        Label nameLabel = new Label(service.getName());
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        Label statusLabel = new Label(service.getStatusText());
        statusLabel.setFont(Font.font("Segoe UI", 11));
        statusLabel.setStyle(service.isOperational() ? "-fx-text-fill: #2e7d32;" : "-fx-text-fill: #c62828;");

        VBox textBox = new VBox(1, nameLabel, statusLabel);

        MenuButton menuButton = new MenuButton("\u25BE");
        menuButton.setStyle("-fx-background-color: transparent;");
        MenuItem viewDetails = new MenuItem("View Details");
        MenuItem viewLogs = new MenuItem("View Logs");
        MenuItem restartService = new MenuItem("Restart Service");
        viewDetails.setOnAction(e -> handleViewServiceDetails(service));
        viewLogs.setOnAction(e -> showInfoAlert(service.getName() + " Logs", "Showing the latest logs for " + service.getName() + "."));
        restartService.setOnAction(e -> handleRestartService(service));
        menuButton.getItems().addAll(viewDetails, viewLogs, restartService);

        HBox card = new HBox(10, icon, textBox, menuButton);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(10, 14, 10, 14));
        card.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8; -fx-border-color: #ececec; -fx-border-radius: 8;");
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private void handleViewServiceDetails(ServiceStatus service) {
        String details = "Service: " + service.getName()
                + "\nStatus: " + service.getStatusText()
                + "\nUptime: 99.98% (last 30 days)";
        showInfoAlert(service.getName() + " Details", details);
    }

    private void handleRestartService(ServiceStatus service) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Restart Service");
        confirm.setHeaderText(null);
        confirm.setContentText("Restart " + service.getName() + " now? This may briefly interrupt connected users.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            showInfoAlert("Service Restarted", service.getName() + " has been restarted successfully.");
        }
    }

    // ---- Data Overview ----

    private VBox buildDataOverviewSection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(18));
        section.setStyle(cardStyle());

        Label title = new Label("Data Overview");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        HBox statsRow = new HBox(12);
        statsRow.getChildren().add(buildOverviewCard("\uD83D\uDC65", "Users", "12,450"));
        statsRow.getChildren().add(buildOverviewCard("\uD83D\uDCE6", "Products", "25,840"));
        statsRow.getChildren().add(buildOverviewCard("\uD83E\uDDFE", "Orders", "18,420"));
        statsRow.getChildren().add(buildOverviewCard("\uD83D\uDD34", "Live Sessions", "620"));
        statsRow.getChildren().add(buildOverviewCard("\u267E", "Live Equipment", "1,240"));
        statsRow.getChildren().add(buildOverviewCard("\uD83D\uDEDC", "Equipment", "1,240"));
        statsRow.getChildren().add(buildOverviewCard("\u2B50", "Reviews", "8,450"));

        section.getChildren().addAll(title, statsRow);
        return section;
    }

    private VBox buildOverviewCard(String icon, String label, String value) {
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        Label nameLabel = new Label(label);
        nameLabel.setFont(Font.font("Segoe UI", 11));
        nameLabel.setStyle("-fx-text-fill: #888;");

        VBox card = new VBox(4, iconLabel, valueLabel, nameLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12, 10, 12, 10));
        card.setPrefWidth(105);
        card.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8; -fx-border-color: #ececec; -fx-border-radius: 8;");
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    // ---- System Activity ----

    private VBox buildSystemActivitySection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(18));
        section.setStyle(cardStyle());

        Label title = new Label("System Activity");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        activityListBox = new VBox(10);
        renderActivityRows(4);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button viewAllButton = new Button("View All Activity \u25BE");
        viewAllButton.setStyle("-fx-background-color: white; -fx-text-fill: " + GREEN + "; "
                + "-fx-border-color: " + GREEN + "; -fx-border-radius: 6; -fx-background-radius: 6;");
        viewAllButton.setOnAction(e -> handleViewAllActivity());

        HBox footerRow = new HBox(spacer, viewAllButton);
        footerRow.setAlignment(Pos.CENTER_RIGHT);

        section.getChildren().addAll(title, activityListBox, footerRow);
        return section;
    }

    // draws only the first `count` activity entries, matching the compact list in the screenshot
    private void renderActivityRows(int count) {
        activityListBox.getChildren().clear();
        int shown = 0;
        for (ActivityEntry entry : activityLog) {
            if (shown >= count) {
                break;
            }
            activityListBox.getChildren().add(buildActivityRow(entry));
            shown++;
        }
    }

    private HBox buildActivityRow(ActivityEntry entry) {
        Label icon = new Label(entry.getIcon());
        icon.setFont(Font.font(13));

        Label description = new Label(entry.getDescription());
        description.setFont(Font.font("Segoe UI", 12));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label timestamp = new Label(entry.getTimestamp());
        timestamp.setFont(Font.font("Segoe UI", 11));
        timestamp.setStyle("-fx-text-fill: #888;");

        HBox row = new HBox(10, icon, description, spacer, timestamp);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void handleViewAllActivity() {
        VBox fullList = new VBox(10);
        fullList.setPadding(new Insets(10));
        for (ActivityEntry entry : activityLog) {
            fullList.getChildren().add(buildActivityRow(entry));
        }

        ScrollPane scrollPane = new ScrollPane(fullList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(420, 320);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("All System Activity");
        dialog.initOwner(primaryStage);
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    // ---- Backup Status (right column) ----

    private VBox buildBackupStatusCard() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setStyle(cardStyle());

        Label title = new Label("Backup Status");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        Label lastBackupLabel = new Label("Last Backup");
        lastBackupLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11;");
        lastBackupValueLabel = new Label(lastBackupTime.format(DISPLAY_FORMAT));
        lastBackupValueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        Label statusLabel = new Label("Status");
        statusLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11;");
        backupStatusBadge = new Label(backupStatus);
        backupStatusBadge.setPadding(new Insets(3, 10, 3, 10));
        backupStatusBadge.setStyle(backupBadgeStyle(backupStatus));

        Label nextBackupLabel = new Label("Next Backup");
        nextBackupLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11;");
        nextBackupValueLabel = new Label(nextBackupTime.format(DISPLAY_FORMAT));
        nextBackupValueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        Button backupNowButton = new Button("Backup Now");
        backupNowButton.setMaxWidth(Double.MAX_VALUE);
        backupNowButton.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10;");
        backupNowButton.setOnAction(e -> handleBackupNow());

        Button viewHistoryButton = new Button("View Backup History \u25BE");
        viewHistoryButton.setMaxWidth(Double.MAX_VALUE);
        viewHistoryButton.setStyle("-fx-background-color: white; -fx-text-fill: #333; "
                + "-fx-border-color: #cccccc; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10;");
        viewHistoryButton.setOnAction(e -> handleViewBackupHistory());

        card.getChildren().addAll(title, lastBackupLabel, lastBackupValueLabel,
                statusLabel, backupStatusBadge, nextBackupLabel, nextBackupValueLabel,
                backupNowButton, viewHistoryButton);
        return card;
    }

    private String backupBadgeStyle(String status) {
        if (status.equals("Completed")) {
            return "-fx-background-color: #e6f4ea; -fx-text-fill: #2e7d32; -fx-background-radius: 12; -fx-font-size: 11;";
        }
        if (status.equals("Running")) {
            return "-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00; -fx-background-radius: 12; -fx-font-size: 11;";
        }
        return "-fx-background-color: #fdecea; -fx-text-fill: #c62828; -fx-background-radius: 12; -fx-font-size: 11;";
    }

    // runs a backup right now: updates the status badge, records history and
    // pushes the last/next backup timestamps forward, same as a real backup job would
    private void handleBackupNow() {
        backupStatus = "Running";
        backupStatusBadge.setText(backupStatus);
        backupStatusBadge.setStyle(backupBadgeStyle(backupStatus));

        showInfoAlert("Backup Started", "A manual backup has started. You'll be notified once it completes.");

        LocalDateTime completedAt = LocalDateTime.now();
        lastBackupTime = completedAt;
        nextBackupTime = completedAt.plusDays(1);
        backupStatus = "Completed";

        lastBackupValueLabel.setText(lastBackupTime.format(DISPLAY_FORMAT));
        nextBackupValueLabel.setText(nextBackupTime.format(DISPLAY_FORMAT));
        backupStatusBadge.setText(backupStatus);
        backupStatusBadge.setStyle(backupBadgeStyle(backupStatus));

        backupHistory.add(0, new BackupRecord(lastBackupTime.format(DISPLAY_FORMAT), "Completed", "1.2 GB"));

        activityLog.add(0, new ActivityEntry("\uD83D\uDEE1", "Backup completed successfully", lastBackupTime.format(DISPLAY_FORMAT)));
        renderActivityRows(4);

        showInfoAlert("Backup Complete", "The manual backup finished successfully.");
    }

    private void handleViewBackupHistory() {
        VBox historyBox = new VBox(10);
        historyBox.setPadding(new Insets(10));
        for (BackupRecord record : backupHistory) {
            Label entry = new Label(record.getDate() + "  \u2013  " + record.getStatus() + "  \u2013  " + record.getSize());
            entry.setFont(Font.font("Segoe UI", 12));
            historyBox.getChildren().add(entry);
        }

        ScrollPane scrollPane = new ScrollPane(historyBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(360, 280);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Backup History");
        dialog.initOwner(primaryStage);
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    // ---- shared helpers ----

    private String cardStyle() {
        return "-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);";
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ------------------------------------------------------------------
    // Sample data
    // ------------------------------------------------------------------

    private List<ServiceStatus> loadServices() {
        List<ServiceStatus> list = new ArrayList<>();
        list.add(new ServiceStatus("Firebase", "Operational", true));
        list.add(new ServiceStatus("Firestore", "Operational", true));
        list.add(new ServiceStatus("Authentication", "Operational", true));
        list.add(new ServiceStatus("Storage", "Operational", true));
        return list;
    }

    private List<ActivityEntry> loadActivityLog() {
        List<ActivityEntry> list = new ArrayList<>();
        list.add(new ActivityEntry("\uD83D\uDEE1", "Backup completed successfully", "29 May 2025, 02:00 AM"));
        list.add(new ActivityEntry("\u2699", "Firestore rules updated", "28 May 2025, 11:30 PM"));
        list.add(new ActivityEntry("\uD83D\uDC64", "New admin logged in", "28 May 2025, 10:15 PM"));
        list.add(new ActivityEntry("\uD83D\uDD0D", "Security scan completed", "28 May 2025, 09:00 PM"));
        list.add(new ActivityEntry("\uD83D\uDCE6", "Bulk product import finished", "27 May 2025, 06:40 PM"));
        list.add(new ActivityEntry("\uD83D\uDD10", "Admin password changed", "27 May 2025, 03:10 PM"));
        list.add(new ActivityEntry("\uD83D\uDDC4", "Old order records archived", "26 May 2025, 11:00 AM"));
        list.add(new ActivityEntry("\uD83D\uDEE1", "Scheduled backup completed", "26 May 2025, 02:00 AM"));
        return list;
    }

    private List<BackupRecord> loadBackupHistory() {
        List<BackupRecord> list = new ArrayList<>();
        list.add(new BackupRecord("29 May 2025, 02:00 AM", "Completed", "1.2 GB"));
        list.add(new BackupRecord("28 May 2025, 02:00 AM", "Completed", "1.2 GB"));
        list.add(new BackupRecord("27 May 2025, 02:00 AM", "Completed", "1.1 GB"));
        list.add(new BackupRecord("26 May 2025, 02:00 AM", "Completed", "1.1 GB"));
        list.add(new BackupRecord("25 May 2025, 02:00 AM", "Failed", "0 GB"));
        return list;
    }

    // ------------------------------------------------------------------
    // Simple data models
    // ------------------------------------------------------------------

    public static class ServiceStatus {
        private final String name;
        private final String statusText;
        private final boolean operational;

        public ServiceStatus(String name, String statusText, boolean operational) {
            this.name = name;
            this.statusText = statusText;
            this.operational = operational;
        }

        public String getName() {
            return name;
        }

        public String getStatusText() {
            return statusText;
        }

        public boolean isOperational() {
            return operational;
        }
    }

    public static class ActivityEntry {
        private final String icon;
        private final String description;
        private final String timestamp;

        public ActivityEntry(String icon, String description, String timestamp) {
            this.icon = icon;
            this.description = description;
            this.timestamp = timestamp;
        }

        public String getIcon() {
            return icon;
        }

        public String getDescription() {
            return description;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }

    public static class BackupRecord {
        private final String date;
        private final String status;
        private final String size;

        public BackupRecord(String date, String status, String size) {
            this.date = date;
            this.status = status;
            this.size = size;
        }

        public String getDate() {
            return date;
        }

        public String getStatus() {
            return status;
        }

        public String getSize() {
            return size;
        }
    }
}