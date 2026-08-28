package com.mainproject.view.admin;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Reports & Complaints screen - JavaFX version of the reference screenshot.
 * Java 17 / JavaFX 21, styled to match AdminDashboard.
 *
 * Shows a tab strip (All / Pending / Under Review / Resolved) with live counts,
 * a searchable table of reports, and a "View" action per row that opens a
 * details dialog where the admin can change the report's status.
 *
 * Every button on this screen is wired to a method - nothing is decorative.
 */
public class ReportsComplaints {

    // same palette as AdminDashboard so the two screens look like one app
    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";

    private final Stage primaryStage;
    private final AdminDashboard adminDashboard;

    private BorderPane rootLayout;
    private TableView<Complaint> table;
    private ObservableList<Complaint> allComplaints;

    private String activeTab = "All";
    private String searchText = "";

    private Button allTabButton;
    private Button pendingTabButton;
    private Button underReviewTabButton;
    private Button resolvedTabButton;

    public ReportsComplaints(Stage primaryStage, AdminDashboard adminDashboard) {
        this.primaryStage = primaryStage;
        this.adminDashboard = adminDashboard;
    }

    // builds the screen and swaps it onto the shared stage
    public void show() {
        allComplaints = loadComplaints();

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
            if (item.equals("Reports & Complaints")) {
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
        if (pageName.equals("Reports & Complaints")) {
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
    // Main content: heading, toolbar, tabs, table
    // ------------------------------------------------------------------

    private VBox buildContentView() {
        VBox view = new VBox(18);
        view.setPadding(new Insets(20));

        Label heading = new Label("Reports & Complaints");
        heading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        HBox toolbar = buildToolbar();
        HBox tabsBar = buildTabsBar();
        table = buildComplaintsTable();

        VBox card = new VBox(14);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");
        card.getChildren().addAll(tabsBar, table);

        view.getChildren().addAll(heading, toolbar, card);
        refreshTable();
        return view;
    }

    private HBox buildToolbar() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search by report ID, reported by, or against...");
        searchField.setPrefWidth(360);
        searchField.setOnAction(e -> handleSearch(searchField.getText()));

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 6;");
        searchButton.setOnAction(e -> handleSearch(searchField.getText()));

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> handleRefresh());

        Button exportButton = new Button("Export");
        exportButton.setOnAction(e -> handleExport());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(10, searchField, searchButton, spacer, refreshButton, exportButton);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        return toolbar;
    }

    private HBox buildTabsBar() {
        allTabButton = buildTabButton("All", allComplaints.size());
        pendingTabButton = buildTabButton("Pending", countByStatus("Pending"));
        underReviewTabButton = buildTabButton("Under Review", countByStatus("Under Review"));
        resolvedTabButton = buildTabButton("Resolved", countByStatus("Resolved"));

        styleActiveTab(allTabButton);
        styleInactiveTab(pendingTabButton);
        styleInactiveTab(underReviewTabButton);
        styleInactiveTab(resolvedTabButton);

        HBox tabsBar = new HBox(8, allTabButton, pendingTabButton, underReviewTabButton, resolvedTabButton);
        tabsBar.setAlignment(Pos.CENTER_LEFT);
        return tabsBar;
    }

    private Button buildTabButton(String status, int count) {
        Button button = new Button(status + " (" + count + ")");
        button.setPadding(new Insets(8, 16, 8, 16));
        button.setOnAction(e -> handleTabClick(status, button));
        return button;
    }

    private void styleActiveTab(Button button) {
        button.setStyle("-fx-background-color: #eaf6ec; -fx-text-fill: " + GREEN + "; "
                + "-fx-background-radius: 20; -fx-border-color: " + GREEN + "; -fx-border-radius: 20; -fx-font-weight: bold;");
    }

    private void styleInactiveTab(Button button) {
        button.setStyle("-fx-background-color: white; -fx-text-fill: #666; "
                + "-fx-background-radius: 20; -fx-border-color: #e0e0e0; -fx-border-radius: 20;");
    }

    private void handleTabClick(String status, Button clicked) {
        activeTab = status;
        styleInactiveTab(allTabButton);
        styleInactiveTab(pendingTabButton);
        styleInactiveTab(underReviewTabButton);
        styleInactiveTab(resolvedTabButton);
        styleActiveTab(clicked);
        refreshTable();
    }

    // ------------------------------------------------------------------
    // Table
    // ------------------------------------------------------------------

    private TableView<Complaint> buildComplaintsTable() {
        TableView<Complaint> tableView = new TableView<>();
        tableView.setPrefHeight(320);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No reports found."));

        TableColumn<Complaint, String> idCol = new TableColumn<>("Report ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("reportId"));

        TableColumn<Complaint, String> reportedByCol = new TableColumn<>("Reported By");
        reportedByCol.setCellValueFactory(new PropertyValueFactory<>("reportedBy"));

        TableColumn<Complaint, String> againstCol = new TableColumn<>("Against");
        againstCol.setCellValueFactory(new PropertyValueFactory<>("against"));

        TableColumn<Complaint, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));

        TableColumn<Complaint, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Complaint, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<Complaint, String>() {
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
                badge.setStyle("-fx-background-radius: 12; -fx-font-size: 11; " + statusColorStyle(status));
                setGraphic(badge);
                setText(null);
            }
        });

        TableColumn<Complaint, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<Complaint, Void>() {
            private final Button viewButton = new Button("View");
            {
                viewButton.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; "
                        + "-fx-border-radius: 4; -fx-background-radius: 4;");
                viewButton.setOnAction(e -> {
                    Complaint complaint = getTableView().getItems().get(getIndex());
                    handleViewComplaint(complaint);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewButton);
            }
        });

        tableView.getColumns().addAll(idCol, reportedByCol, againstCol, reasonCol, dateCol, statusCol, actionCol);
        return tableView;
    }

    private String statusColorStyle(String status) {
        switch (status) {
            case "Resolved":
                return "-fx-background-color: #e6f4ea; -fx-text-fill: #2e7d32;";
            case "Under Review":
                return "-fx-background-color: #e3f2fd; -fx-text-fill: #1565c0;";
            case "Pending":
                return "-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00;";
            case "Dismissed":
                return "-fx-background-color: #fdecea; -fx-text-fill: #c62828;";
            default:
                return "-fx-background-color: #eeeeee; -fx-text-fill: #333333;";
        }
    }

    // rebuilds the visible rows from allComplaints based on the active tab + search text,
    // then refreshes the tab counts so they always reflect reality
    private void refreshTable() {
        ObservableList<Complaint> filtered = FXCollections.observableArrayList();
        for (Complaint complaint : allComplaints) {
            boolean matchesTab = activeTab.equals("All") || complaint.getStatus().equals(activeTab);
            boolean matchesSearch = searchText.isBlank()
                    || complaint.getReportId().toLowerCase().contains(searchText.toLowerCase())
                    || complaint.getReportedBy().toLowerCase().contains(searchText.toLowerCase())
                    || complaint.getAgainst().toLowerCase().contains(searchText.toLowerCase());
            if (matchesTab && matchesSearch) {
                filtered.add(complaint);
            }
        }
        table.setItems(filtered);
        updateTabCounts();
    }

    private void updateTabCounts() {
        allTabButton.setText("All (" + allComplaints.size() + ")");
        pendingTabButton.setText("Pending (" + countByStatus("Pending") + ")");
        underReviewTabButton.setText("Under Review (" + countByStatus("Under Review") + ")");
        resolvedTabButton.setText("Resolved (" + countByStatus("Resolved") + ")");
    }

    private int countByStatus(String status) {
        int count = 0;
        for (Complaint complaint : allComplaints) {
            if (complaint.getStatus().equals(status)) {
                count++;
            }
        }
        return count;
    }

    // ------------------------------------------------------------------
    // Button actions
    // ------------------------------------------------------------------

    private void handleSearch(String query) {
        searchText = query == null ? "" : query.trim();
        refreshTable();
    }

    private void handleRefresh() {
        searchText = "";
        activeTab = "All";
        allComplaints = loadComplaints();
        styleInactiveTab(pendingTabButton);
        styleInactiveTab(underReviewTabButton);
        styleInactiveTab(resolvedTabButton);
        styleActiveTab(allTabButton);
        refreshTable();
        showInfoAlert("Refreshed", "The reports list has been refreshed.");
    }

    private void handleExport() {
        showInfoAlert("Export", "Exporting " + table.getItems().size() + " report(s) to CSV.");
    }

    // opens a details dialog for the clicked row and lets the admin update its status
    private void handleViewComplaint(Complaint complaint) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Report Details - " + complaint.getReportId());
        dialog.initOwner(primaryStage);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 0, 15, 0));

        addDetailRow(grid, 0, "Report ID:", complaint.getReportId());
        addDetailRow(grid, 1, "Reported By:", complaint.getReportedBy());
        addDetailRow(grid, 2, "Against:", complaint.getAgainst());
        addDetailRow(grid, 3, "Reason:", complaint.getReason());
        addDetailRow(grid, 4, "Date:", complaint.getDate());
        addDetailRow(grid, 5, "Status:", complaint.getStatus());

        Label actionLabel = new Label("Update Status:");
        actionLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        Button markUnderReviewBtn = new Button("Mark Under Review");
        Button markResolvedBtn = new Button("Mark Resolved");
        Button dismissBtn = new Button("Dismiss");

        markUnderReviewBtn.setOnAction(e -> {
            handleStatusChange(complaint, "Under Review");
            dialog.close();
        });
        markResolvedBtn.setOnAction(e -> {
            handleStatusChange(complaint, "Resolved");
            dialog.close();
        });
        dismissBtn.setOnAction(e -> {
            handleStatusChange(complaint, "Dismissed");
            dialog.close();
        });

        HBox actionButtons = new HBox(8, markUnderReviewBtn, markResolvedBtn, dismissBtn);

        VBox content = new VBox(14, grid, actionLabel, actionButtons);
        content.setPadding(new Insets(10));
        content.setPrefWidth(420);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void addDetailRow(GridPane grid, int row, String labelText, String valueText) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        Label value = new Label(valueText);
        value.setWrapText(true);
        value.setMaxWidth(320);
        grid.add(label, 0, row);
        grid.add(value, 1, row);
    }

    // updates the complaint's status in-place and re-filters the table so tabs/counts stay correct
    private void handleStatusChange(Complaint complaint, String newStatus) {
        complaint.setStatus(newStatus);
        refreshTable();
        showInfoAlert("Status Updated", complaint.getReportId() + " is now marked as \"" + newStatus + "\".");
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ------------------------------------------------------------------
    // Sample data - matches the counts shown in the reference screenshot
    // (All 7, Pending 4, Under Review 2, Resolved 1)
    // ------------------------------------------------------------------

    private ObservableList<Complaint> loadComplaints() {
        ObservableList<Complaint> complaints = FXCollections.observableArrayList();
        complaints.add(new Complaint("REP#1007", "Green Mart", "Ramesh Patil", "Fake Product Information", "29 May 2025", "Pending"));
        complaints.add(new Complaint("REP#1006", "Fresh Store", "Mahesh Jadhav", "Late Delivery", "28 May 2025", "Under Review"));
        complaints.add(new Complaint("REP#1005", "Daily Needs", "Suresh Yadav", "Inappropriate Live", "28 May 2025", "Pending"));
        complaints.add(new Complaint("REP#1004", "Organic Basket", "Anita Deshmukh", "Product Quality Issue", "27 May 2025", "Under Review"));
        complaints.add(new Complaint("REP#1003", "Green Mart", "Vikram Singh", "Wrong Pricing", "27 May 2025", "Resolved"));
        complaints.add(new Complaint("REP#1002", "Fresh Store", "Sunita Kale", "Rude Behaviour", "26 May 2025", "Pending"));
        complaints.add(new Complaint("REP#1001", "Daily Needs", "Ramesh Patil", "Damaged Product", "25 May 2025", "Pending"));
        return complaints;
    }

    // ------------------------------------------------------------------
    // Simple data model for a table row
    // ------------------------------------------------------------------

    public static class Complaint {
        private final String reportId;
        private final String reportedBy;
        private final String against;
        private final String reason;
        private final String date;
        private String status;

        public Complaint(String reportId, String reportedBy, String against, String reason, String date, String status) {
            this.reportId = reportId;
            this.reportedBy = reportedBy;
            this.against = against;
            this.reason = reason;
            this.date = date;
            this.status = status;
        }

        public String getReportId() {
            return reportId;
        }

        public String getReportedBy() {
            return reportedBy;
        }

        public String getAgainst() {
            return against;
        }

        public String getReason() {
            return reason;
        }

        public String getDate() {
            return date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}