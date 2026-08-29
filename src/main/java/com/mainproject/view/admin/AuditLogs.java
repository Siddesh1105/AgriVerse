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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Audit Logs screen - JavaFX version of the reference screenshot.
 * Java 17 / JavaFX 21, styled to match AdminDashboard.
 *
 * Shows a filter bar (Admin / Action / Date range / Filter / Export Logs),
 * a table of log entries, and a working page-number pagination bar under it.
 *
 * Every button on this screen is wired to a method - nothing is decorative.
 */
public class AuditLogs {

    // same palette as AdminDashboard so every screen looks like one app
    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";

    private static final int PAGE_SIZE = 5;

    private final Stage primaryStage;
    private final AdminDashboard adminDashboard;

    private BorderPane rootLayout;
    private TableView<AuditLog> table;
    private Label rangeLabel;
    private HBox pageNumberBox;
    private Button prevPageButton;
    private Button nextPageButton;

    private List<AuditLog> allLogs;
    private List<AuditLog> filteredLogs;
    private int currentPage = 1;

    private ComboBox<String> adminFilter;
    private ComboBox<String> actionFilter;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    public AuditLogs(Stage primaryStage, AdminDashboard adminDashboard) {
        this.primaryStage = primaryStage;
        this.adminDashboard = adminDashboard;
    }

    // builds the screen and swaps it onto the shared stage
    public void show() {
        allLogs = loadAllLogs();
        filteredLogs = allLogs;
        currentPage = 1;

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
                "AI & Smart Tools", "Notifications", "Content Management",
                "Feedback & Reviews", "Reports & Complaints", "Payment Management",
                "Audit Logs", "System & Data Management"
        };

        VBox navBox = new VBox(1);
        for (String item : navItems) {
            Button navButton = buildNavButton(item);
            if (item.equals("Audit Logs")) {
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
        if (pageName.equals("Audit Logs")) {
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
        if (pageName.equals("Farmer Verification")) {
            new FarmerVerification(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Product Management")) {
            new ProductManagement(primaryStage, adminDashboard).show();
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
        if (pageName.equals("Equipment Management")) {
            new EquipmentManagement(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Analytics & Reports")) {
            new AnalyticsReports(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Notifications")) {
            new NotificationManagement(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Content Management")) {
            new ContentManagement(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Feedback & Reviews")) {
            new FeedbackReviews(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Reports & Complaints")) {
            new ReportsComplaints(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Payment Management")) {
            new PaymentManagement(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("System & Data Management")) {
            new SystemDataManage(primaryStage, adminDashboard).show();
            return;
        }
        showInfoAlert(pageName, "This section hasn't been built yet in this demo.");
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
        bellButton.setOnAction(
                e -> showInfoAlert("Notifications", "You have 42 pending approvals and 3 new orders today."));

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
    // Main content: heading, filter bar, table, pagination
    // ------------------------------------------------------------------

    private VBox buildContentView() {
        VBox view = new VBox(18);
        view.setPadding(new Insets(20));

        Label heading = new Label("Audit Logs");
        heading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        HBox filterBar = buildFilterBar();
        table = buildLogsTable();
        HBox paginationBar = buildPaginationBar();

        VBox card = new VBox(14);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");
        card.getChildren().addAll(filterBar, table, paginationBar);

        view.getChildren().addAll(heading, card);
        refreshTable();
        return view;
    }

    // ---- Filter bar: Select Admin / Select Action / date range / Filter / Export
    // Logs ----

    private HBox buildFilterBar() {
        adminFilter = new ComboBox<>();
        adminFilter.setPromptText("Select Admin");
        adminFilter.setPrefWidth(160);
        adminFilter.getItems().addAll("Super Admin", "Admin Priya", "Admin Rahul", "Admin Neha");

        actionFilter = new ComboBox<>();
        actionFilter.setPromptText("Select Action");
        actionFilter.setPrefWidth(180);
        actionFilter.getItems().addAll("Approved Farmer", "Rejected Farmer", "Approved Product", "Removed Product",
                "Updated Order Status", "Cancelled Order", "Started Live Session", "Ended Live Session",
                "Suspended User", "Reactivated User");

        startDatePicker = new DatePicker(LocalDate.of(2025, 6, 1));
        startDatePicker.setPrefWidth(140);

        endDatePicker = new DatePicker(LocalDate.of(2025, 6, 29));
        endDatePicker.setPrefWidth(140);

        HBox dateRangeBox = new HBox(6, startDatePicker, new Label("-"), endDatePicker);
        dateRangeBox.setAlignment(Pos.CENTER_LEFT);

        Button filterButton = new Button("\u25BC Filter");
        filterButton.setStyle("-fx-background-color: " + GREEN
                + "; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 18 8 18;");
        filterButton.setOnAction(e -> handleFilterClick());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button exportButton = new Button("\u2B07 Export Logs");
        exportButton.setStyle("-fx-background-color: white; -fx-text-fill: " + GREEN + "; "
                + "-fx-border-color: " + GREEN
                + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8 18 8 18;");
        exportButton.setOnAction(e -> handleExportLogs());

        HBox filterBar = new HBox(10, adminFilter, actionFilter, dateRangeBox, filterButton, spacer, exportButton);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        return filterBar;
    }

    private void handleFilterClick() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        if (start != null && end != null && start.isAfter(end)) {
            showInfoAlert("Invalid Date Range", "The start date must be before the end date.");
            return;
        }

        String selectedAdmin = adminFilter.getValue();
        String selectedAction = actionFilter.getValue();

        List<AuditLog> results = new ArrayList<>();
        for (AuditLog log : allLogs) {
            boolean matchesAdmin = selectedAdmin == null || log.getAdmin().equals(selectedAdmin);
            boolean matchesAction = selectedAction == null || log.getAction().equals(selectedAction);
            boolean matchesDate = true;
            LocalDate logDate = log.getDateTime().toLocalDate();
            if (start != null && logDate.isBefore(start)) {
                matchesDate = false;
            }
            if (end != null && logDate.isAfter(end)) {
                matchesDate = false;
            }
            if (matchesAdmin && matchesAction && matchesDate) {
                results.add(log);
            }
        }

        filteredLogs = results;
        currentPage = 1;
        refreshTable();
    }

    private void handleExportLogs() {
        showInfoAlert("Export Logs", "Exporting " + filteredLogs.size() + " log entr"
                + (filteredLogs.size() == 1 ? "y" : "ies") + " to CSV.");
    }

    // ---- Table ----

    private TableView<AuditLog> buildLogsTable() {
        TableView<AuditLog> tableView = new TableView<>();
        tableView.setPrefHeight(280);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No logs match the selected filters."));

        TableColumn<AuditLog, String> dateCol = new TableColumn<>("Date & Time");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("formattedDateTime"));

        TableColumn<AuditLog, String> adminCol = new TableColumn<>("Admin");
        adminCol.setCellValueFactory(new PropertyValueFactory<>("admin"));

        TableColumn<AuditLog, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

        TableColumn<AuditLog, String> moduleCol = new TableColumn<>("Module");
        moduleCol.setCellValueFactory(new PropertyValueFactory<>("module"));

        TableColumn<AuditLog, String> recordIdCol = new TableColumn<>("Record ID");
        recordIdCol.setCellValueFactory(new PropertyValueFactory<>("recordId"));

        TableColumn<AuditLog, String> ipCol = new TableColumn<>("IP Address");
        ipCol.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));

        tableView.getColumns().addAll(dateCol, adminCol, actionCol, moduleCol, recordIdCol, ipCol);

        // clicking a row shows the full entry - stands in for a real "view log" screen
        tableView.setRowFactory(tv -> {
            TableRow<AuditLog> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty()) {
                    showLogDetails(row.getItem());
                }
            });
            return row;
        });

        return tableView;
    }

    private void showLogDetails(AuditLog log) {
        String details = "Date & Time: " + log.getFormattedDateTime()
                + "\nAdmin: " + log.getAdmin()
                + "\nAction: " + log.getAction()
                + "\nModule: " + log.getModule()
                + "\nRecord ID: " + log.getRecordId()
                + "\nIP Address: " + log.getIpAddress();
        showInfoAlert("Log Details", details);
    }

    // ---- Pagination ----

    private HBox buildPaginationBar() {
        rangeLabel = new Label();
        rangeLabel.setStyle("-fx-text-fill: #777; -fx-font-size: 12;");

        prevPageButton = new Button("<");
        prevPageButton.setOnAction(e -> handlePrevPage());

        nextPageButton = new Button(">");
        nextPageButton.setOnAction(e -> handleNextPage());

        pageNumberBox = new HBox(6);
        pageNumberBox.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bar = new HBox(6, rangeLabel, spacer, prevPageButton, pageNumberBox, nextPageButton);
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            refreshTable();
        }
    }

    private void handleNextPage() {
        int totalPages = totalPages();
        if (currentPage < totalPages) {
            currentPage++;
            refreshTable();
        }
    }

    private void handlePageClick(int page) {
        currentPage = page;
        refreshTable();
    }

    private int totalPages() {
        return Math.max(1, (int) Math.ceil(filteredLogs.size() / (double) PAGE_SIZE));
    }

    // repaints the table rows, the "Showing X to Y of Z" label and the page-number
    // buttons
    // for whatever filteredLogs + currentPage currently are
    private void refreshTable() {
        int total = filteredLogs.size();
        int totalPages = totalPages();
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int fromIndex = (currentPage - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, total);

        ObservableList<AuditLog> pageItems = FXCollections.observableArrayList();
        for (int i = fromIndex; i < toIndex; i++) {
            pageItems.add(filteredLogs.get(i));
        }
        table.setItems(pageItems);

        if (total == 0) {
            rangeLabel.setText("Showing 0 of 0 logs");
        } else {
            rangeLabel.setText("Showing " + (fromIndex + 1) + " to " + toIndex + " of " + total + " logs");
        }

        prevPageButton.setDisable(currentPage <= 1);
        nextPageButton.setDisable(currentPage >= totalPages);

        rebuildPageNumbers(totalPages);
    }

    // builds the "1 2 3 4 5 ... 309" style page-number row, always keeping the
    // current page visible and trimming the middle with an ellipsis when needed
    private void rebuildPageNumbers(int totalPages) {
        pageNumberBox.getChildren().clear();
        List<Integer> pageTokens = buildPageTokenList(currentPage, totalPages);

        for (Integer page : pageTokens) {
            if (page == -1) {
                Label ellipsis = new Label("...");
                ellipsis.setPadding(new Insets(6, 6, 6, 6));
                pageNumberBox.getChildren().add(ellipsis);
            } else {
                Button pageButton = new Button(String.valueOf(page));
                pageButton.setMinWidth(32);
                if (page == currentPage) {
                    pageButton.setStyle(
                            "-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 4;");
                } else {
                    pageButton.setStyle(
                            "-fx-background-color: white; -fx-text-fill: #333; -fx-border-color: #e0e0e0; -fx-background-radius: 4;");
                }
                int pageNumber = page;
                pageButton.setOnAction(e -> handlePageClick(pageNumber));
                pageNumberBox.getChildren().add(pageButton);
            }
        }
    }

    // -1 in the returned list means "show an ellipsis here"
    private List<Integer> buildPageTokenList(int current, int total) {
        List<Integer> tokens = new ArrayList<>();
        if (total <= 7) {
            for (int p = 1; p <= total; p++) {
                tokens.add(p);
            }
            return tokens;
        }

        tokens.add(1);
        if (current <= 4) {
            for (int p = 2; p <= 5; p++) {
                tokens.add(p);
            }
            tokens.add(-1);
        } else if (current >= total - 3) {
            tokens.add(-1);
            for (int p = total - 4; p <= total - 1; p++) {
                tokens.add(p);
            }
        } else {
            tokens.add(-1);
            tokens.add(current - 1);
            tokens.add(current);
            tokens.add(current + 1);
            tokens.add(-1);
        }
        tokens.add(total);
        return tokens;
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ------------------------------------------------------------------
    // Sample data - generates 1,542 log entries so pagination (309 pages
    // at 5 rows each) behaves exactly like the reference screenshot
    // ------------------------------------------------------------------

    private List<AuditLog> loadAllLogs() {
        String[] admins = { "Super Admin", "Admin Priya", "Admin Rahul", "Admin Neha" };
        String[][] actionModuleRecord = {
                { "Approved Farmer", "Farmer Verification", "FMR" },
                { "Approved Product", "Product Management", "PRD" },
                { "Updated Order Status", "Order Management", "ORD" },
                { "Ended Live Session", "Live Marketplace", "LIVE" },
                { "Suspended User", "User Management", "USR" },
                { "Rejected Farmer", "Farmer Verification", "FMR" },
                { "Removed Product", "Product Management", "PRD" },
                { "Cancelled Order", "Order Management", "ORD" },
                { "Started Live Session", "Live Marketplace", "LIVE" },
                { "Reactivated User", "User Management", "USR" }
        };
        String[] ipPool = { "192.168.1.10", "192.168.1.11", "192.168.1.12" };

        LocalDateTime cursor = LocalDateTime.of(2025, 5, 29, 10, 32);
        int totalLogs = 1542;

        List<AuditLog> logs = new ArrayList<>(totalLogs);
        for (int i = 0; i < totalLogs; i++) {
            String admin = admins[i % admins.length];
            String[] entry = actionModuleRecord[i % actionModuleRecord.length];
            String recordId = entry[2] + "#" + (1000 + i);
            String ip = ipPool[i % ipPool.length];
            LocalDateTime dateTime = cursor.minusMinutes(i * 7L);

            logs.add(new AuditLog(dateTime, admin, entry[0], entry[1], recordId, ip));
        }
        return logs;
    }

    // ------------------------------------------------------------------
    // Simple data model for a table row
    // ------------------------------------------------------------------

    public static class AuditLog {
        private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy, hh:mm a",
                Locale.ENGLISH);

        private final LocalDateTime dateTime;
        private final String admin;
        private final String action;
        private final String module;
        private final String recordId;
        private final String ipAddress;

        public AuditLog(LocalDateTime dateTime, String admin, String action, String module, String recordId,
                String ipAddress) {
            this.dateTime = dateTime;
            this.admin = admin;
            this.action = action;
            this.module = module;
            this.recordId = recordId;
            this.ipAddress = ipAddress;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public String getFormattedDateTime() {
            return dateTime.format(DISPLAY_FORMAT);
        }

        public String getAdmin() {
            return admin;
        }

        public String getAction() {
            return action;
        }

        public String getModule() {
            return module;
        }

        public String getRecordId() {
            return recordId;
        }

        public String getIpAddress() {
            return ipAddress;
        }
    }
}