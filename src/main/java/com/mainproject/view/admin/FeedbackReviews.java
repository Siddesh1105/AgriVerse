package com.mainproject.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Feedback & Reviews screen - JavaFX version of the reference screenshot.
 * Java 17 / JavaFX 21, styled to match AdminDashboard.
 *
 * Shows a tab strip (All Feedback / Product Reviews / Farmer Reviews), a
 * filter bar, a paginated feedback table, and three summary cards
 * (category split, average ratings, status breakdown) at the bottom.
 *
 * Every button on this screen is wired to a method - nothing is decorative.
 */
public class FeedbackReviews {

    // same palette as AdminDashboard so every screen looks like one app
    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";

    private static final int PAGE_SIZE = 8;
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    private final Stage primaryStage;
    private final AdminDashboard adminDashboard;

    private BorderPane rootLayout;

    private List<FeedbackEntry> allFeedback;
    private List<FeedbackEntry> filteredFeedback;
    private int currentPage = 1;
    private String activeTab = "All";

    private TableView<FeedbackEntry> table;
    private Label rangeLabel;
    private HBox pageNumberBox;
    private Button prevPageButton;
    private Button nextPageButton;

    private Button allTabButton;
    private Button productTabButton;
    private Button farmerTabButton;

    private TextField searchField;
    private ComboBox<String> typeFilter;
    private ComboBox<String> statusFilter;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    private ProgressBar approvedBar;
    private ProgressBar pendingBar;
    private ProgressBar underReviewBar;
    private Label approvedSummaryLabel;
    private Label pendingSummaryLabel;
    private Label underReviewSummaryLabel;

    public FeedbackReviews(Stage primaryStage, AdminDashboard adminDashboard) {
        this.primaryStage = primaryStage;
        this.adminDashboard = adminDashboard;
    }

    // builds the screen and swaps it onto the shared stage
    public void show() {
        allFeedback = loadAllFeedback();
        filteredFeedback = allFeedback;
        currentPage = 1;
        activeTab = "All";

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
            if (item.equals("Feedback & Reviews")) {
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
        if (pageName.equals("Feedback & Reviews")) {
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
        if (pageName.equals("Reports & Complaints")) {
            new ReportsComplaints(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Payment Management")) {
            new PaymentManagement(primaryStage, adminDashboard).show();
            return;
        }
        if (pageName.equals("Audit Logs")) {
            new AuditLogs(primaryStage, adminDashboard).show();
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
    // Main content
    // ------------------------------------------------------------------

    private ScrollPane buildContentView() {
        VBox view = new VBox(18);
        view.setPadding(new Insets(20));

        Label heading = new Label("Feedback & Reviews");
        heading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        Label subheading = new Label("Manage user feedback and product reviews");
        subheading.setStyle("-fx-text-fill: #888; -fx-font-size: 12;");
        VBox headingBox = new VBox(2, heading, subheading);

        HBox tabsBar = buildTabsBar();
        HBox filterBar = buildFilterBar();
        table = buildFeedbackTable();
        HBox paginationBar = buildPaginationBar();

        VBox tableCard = new VBox(14);
        tableCard.setPadding(new Insets(18));
        tableCard.setStyle(cardStyle());
        tableCard.getChildren().addAll(tabsBar, filterBar, table, paginationBar);

        HBox summaryRow = buildSummaryRow();

        view.getChildren().addAll(headingBox, tableCard, summaryRow);
        refreshFilteredList();

        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + BG + "; -fx-background: " + BG + ";");
        return scrollPane;
    }

    // ---- Tabs ----

    private HBox buildTabsBar() {
        allTabButton = buildTabButton("All Feedback", "All");
        productTabButton = buildTabButton("Product Reviews", "Product");
        farmerTabButton = buildTabButton("Farmer Reviews", "Farmer");

        styleActiveTab(allTabButton);
        styleInactiveTab(productTabButton);
        styleInactiveTab(farmerTabButton);

        HBox tabsBar = new HBox(8, allTabButton, productTabButton, farmerTabButton);
        tabsBar.setAlignment(Pos.CENTER_LEFT);
        return tabsBar;
    }

    private Button buildTabButton(String label, String tabKey) {
        Button button = new Button(label);
        button.setPadding(new Insets(8, 16, 8, 16));
        button.setOnAction(e -> handleTabClick(tabKey, button));
        return button;
    }

    private void styleActiveTab(Button button) {
        button.setStyle("-fx-background-color: #eaf6ec; -fx-text-fill: " + GREEN + "; "
                + "-fx-background-radius: 20; -fx-border-color: " + GREEN
                + "; -fx-border-radius: 20; -fx-font-weight: bold;");
    }

    private void styleInactiveTab(Button button) {
        button.setStyle("-fx-background-color: white; -fx-text-fill: #666; "
                + "-fx-background-radius: 20; -fx-border-color: #e0e0e0; -fx-border-radius: 20;");
    }

    private void handleTabClick(String tabKey, Button clicked) {
        activeTab = tabKey;
        styleInactiveTab(allTabButton);
        styleInactiveTab(productTabButton);
        styleInactiveTab(farmerTabButton);
        styleActiveTab(clicked);
        currentPage = 1;
        refreshFilteredList();
    }

    // ---- Filter bar ----

    private HBox buildFilterBar() {
        searchField = new TextField();
        searchField.setPromptText("Search feedback...");
        searchField.setPrefWidth(220);
        searchField.setOnAction(e -> {
            currentPage = 1;
            refreshFilteredList();
        });

        typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All Types", "Product Review", "Farmer Review", "Feedback");
        typeFilter.setValue("All Types");
        typeFilter.setPrefWidth(150);

        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "Approved", "Pending", "Under Review");
        statusFilter.setValue("All Status");
        statusFilter.setPrefWidth(150);

        startDatePicker = new DatePicker(LocalDate.of(2025, 5, 1));
        startDatePicker.setPrefWidth(135);
        endDatePicker = new DatePicker(LocalDate.of(2025, 5, 29));
        endDatePicker.setPrefWidth(135);
        HBox dateRangeBox = new HBox(6, startDatePicker, new Label("-"), endDatePicker);
        dateRangeBox.setAlignment(Pos.CENTER_LEFT);

        Button filterButton = new Button("\u25BC Filter");
        filterButton.setStyle("-fx-background-color: " + GREEN
                + "; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 18 8 18;");
        filterButton.setOnAction(e -> {
            currentPage = 1;
            refreshFilteredList();
        });

        HBox filterBar = new HBox(10, searchField, typeFilter, statusFilter, dateRangeBox, filterButton);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        return filterBar;
    }

    // ---- Table ----

    private TableView<FeedbackEntry> buildFeedbackTable() {
        TableView<FeedbackEntry> tableView = new TableView<>();
        tableView.setPrefHeight(360);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No feedback matches the selected filters."));

        TableColumn<FeedbackEntry, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<FeedbackEntry, FeedbackEntry> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(col -> new javafx.beans.property.SimpleObjectProperty<>(col.getValue()));
        userCol.setCellFactory(col -> new TableCell<FeedbackEntry, FeedbackEntry>() {
            @Override
            protected void updateItem(FeedbackEntry entry, boolean empty) {
                super.updateItem(entry, empty);
                if (empty || entry == null) {
                    setGraphic(null);
                    return;
                }
                Label name = new Label(entry.getUserName());
                name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
                Label role = new Label(entry.getUserRole());
                role.setStyle("-fx-text-fill: #999; -fx-font-size: 10;");
                setGraphic(new VBox(1, name, role));
            }
        });

        TableColumn<FeedbackEntry, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<FeedbackEntry, String> subjectCol = new TableColumn<>("Subject / Product");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));

        TableColumn<FeedbackEntry, String> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("starsDisplay"));
        ratingCol.setCellFactory(col -> new TableCell<FeedbackEntry, String>() {
            @Override
            protected void updateItem(String stars, boolean empty) {
                super.updateItem(stars, empty);
                if (empty || stars == null) {
                    setText(null);
                } else {
                    setText(stars);
                    setStyle("-fx-text-fill: #f5a623;");
                }
            }
        });

        TableColumn<FeedbackEntry, String> messageCol = new TableColumn<>("Message");
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

        TableColumn<FeedbackEntry, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));

        TableColumn<FeedbackEntry, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<FeedbackEntry, String>() {
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

        TableColumn<FeedbackEntry, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<FeedbackEntry, Void>() {
            private final Button viewButton = new Button("\uD83D\uDC41");
            {
                viewButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                viewButton.setOnAction(e -> {
                    FeedbackEntry entry = getTableView().getItems().get(getIndex());
                    handleViewFeedback(entry);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewButton);
            }
        });

        tableView.getColumns().addAll(idCol, userCol, typeCol, subjectCol, ratingCol, messageCol, dateCol, statusCol,
                actionCol);
        return tableView;
    }

    private String statusColorStyle(String status) {
        switch (status) {
            case "Approved":
                return "-fx-background-color: #e6f4ea; -fx-text-fill: #2e7d32;";
            case "Under Review":
                return "-fx-background-color: #e3f2fd; -fx-text-fill: #1565c0;";
            case "Pending":
                return "-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00;";
            default:
                return "-fx-background-color: #eeeeee; -fx-text-fill: #333333;";
        }
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
        if (currentPage < totalPages()) {
            currentPage++;
            refreshTable();
        }
    }

    private void handlePageClick(int page) {
        currentPage = page;
        refreshTable();
    }

    private int totalPages() {
        return Math.max(1, (int) Math.ceil(filteredFeedback.size() / (double) PAGE_SIZE));
    }

    // re-applies the tab + search + type/status/date filters, resets the visible
    // page,
    // then repaints the table, pagination and tab counts
    private void refreshFilteredList() {
        String search = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String type = typeFilter.getValue();
        String status = statusFilter.getValue();
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        List<FeedbackEntry> results = new ArrayList<>();
        for (FeedbackEntry entry : allFeedback) {
            boolean matchesTab = activeTab.equals("All")
                    || (activeTab.equals("Farmer") && entry.getType().equals("Farmer Review"))
                    || (activeTab.equals("Product") && !entry.getType().equals("Farmer Review"));
            boolean matchesSearch = search.isEmpty()
                    || entry.getId().toLowerCase().contains(search)
                    || entry.getUserName().toLowerCase().contains(search)
                    || entry.getSubject().toLowerCase().contains(search)
                    || entry.getMessage().toLowerCase().contains(search);
            boolean matchesType = type == null || type.equals("All Types") || entry.getType().equals(type);
            boolean matchesStatus = status == null || status.equals("All Status") || entry.getStatus().equals(status);
            boolean matchesDate = (start == null || !entry.getDate().isBefore(start))
                    && (end == null || !entry.getDate().isAfter(end));

            if (matchesTab && matchesSearch && matchesType && matchesStatus && matchesDate) {
                results.add(entry);
            }
        }

        filteredFeedback = results;
        updateTabCounts();
        refreshTable();
    }

    private void updateTabCounts() {
        allTabButton.setText("All Feedback (" + allFeedback.size() + ")");
        productTabButton.setText("Product Reviews (" + countCategory("Product") + ")");
        farmerTabButton.setText("Farmer Reviews (" + countCategory("Farmer") + ")");
    }

    private int countCategory(String category) {
        int count = 0;
        for (FeedbackEntry entry : allFeedback) {
            boolean isFarmer = entry.getType().equals("Farmer Review");
            if (category.equals("Farmer") && isFarmer) {
                count++;
            } else if (category.equals("Product") && !isFarmer) {
                count++;
            }
        }
        return count;
    }

    private void refreshTable() {
        int total = filteredFeedback.size();
        int totalPages = totalPages();
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int fromIndex = (currentPage - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, total);

        ObservableList<FeedbackEntry> pageItems = FXCollections.observableArrayList();
        for (int i = fromIndex; i < toIndex; i++) {
            pageItems.add(filteredFeedback.get(i));
        }
        table.setItems(pageItems);

        if (total == 0) {
            rangeLabel.setText("Showing 0 of 0 feedback");
        } else {
            rangeLabel.setText("Showing " + (fromIndex + 1) + " to " + toIndex + " of " + total + " feedback");
        }

        prevPageButton.setDisable(currentPage <= 1);
        nextPageButton.setDisable(currentPage >= totalPages);

        rebuildPageNumbers(totalPages);
    }

    // builds the "1 2 3 4 5 ... 31" style page-number row, always keeping the
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

    // ---- View / status-change dialog ----

    private void handleViewFeedback(FeedbackEntry entry) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Feedback Details - " + entry.getId());
        dialog.initOwner(primaryStage);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 0, 15, 0));

        addDetailRow(grid, 0, "User:", entry.getUserName() + " (" + entry.getUserRole() + ")");
        addDetailRow(grid, 1, "Type:", entry.getType());
        addDetailRow(grid, 2, "Subject / Product:", entry.getSubject());
        addDetailRow(grid, 3, "Rating:", entry.getStarsDisplay());
        addDetailRow(grid, 4, "Message:", entry.getMessage());
        addDetailRow(grid, 5, "Date:", entry.getFormattedDate());
        addDetailRow(grid, 6, "Status:", entry.getStatus());

        Label actionLabel = new Label("Update Status:");
        actionLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        Button approveBtn = new Button("Approve");
        Button pendingBtn = new Button("Mark Pending");
        Button underReviewBtn = new Button("Mark Under Review");

        approveBtn.setOnAction(e -> {
            handleStatusChange(entry, "Approved");
            dialog.close();
        });
        pendingBtn.setOnAction(e -> {
            handleStatusChange(entry, "Pending");
            dialog.close();
        });
        underReviewBtn.setOnAction(e -> {
            handleStatusChange(entry, "Under Review");
            dialog.close();
        });

        HBox actionButtons = new HBox(8, approveBtn, pendingBtn, underReviewBtn);

        VBox content = new VBox(14, grid, actionLabel, actionButtons);
        content.setPadding(new Insets(10));
        content.setPrefWidth(440);

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

    private void handleStatusChange(FeedbackEntry entry, String newStatus) {
        entry.setStatus(newStatus);
        table.refresh();
        updateFeedbackStatusSummary();
        showInfoAlert("Status Updated", entry.getId() + " is now marked as \"" + newStatus + "\".");
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ------------------------------------------------------------------
    // Summary row: Feedback Summary (donut) / Average Ratings / Feedback Status
    // ------------------------------------------------------------------

    private HBox buildSummaryRow() {
        HBox row = new HBox(20, buildFeedbackSummaryCard(), buildAverageRatingsCard(), buildFeedbackStatusCard());
        HBox.setHgrow(row, Priority.ALWAYS);
        return row;
    }

    private VBox buildFeedbackSummaryCard() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setStyle(cardStyle());
        HBox.setHgrow(card, Priority.ALWAYS);

        Label title = new Label("Feedback Summary");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        int productCount = countCategory("Product");
        int farmerCount = countCategory("Farmer");
        int total = productCount + farmerCount;

        PieChart pieChart = new PieChart();
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(false);
        pieChart.setPrefSize(140, 140);
        pieChart.setStartAngle(90);

        PieChart.Data productSlice = new PieChart.Data("Product Reviews", productCount);
        PieChart.Data farmerSlice = new PieChart.Data("Farmer Reviews", farmerCount);
        pieChart.getData().addAll(productSlice, farmerSlice);
        productSlice.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-pie-color: #3b82f6;");
            }
        });
        farmerSlice.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-pie-color: #ef4444;");
            }
        });

        // white circle over the middle turns the pie into a donut
        Circle hole = new Circle(32);
        hole.setFill(Color.WHITE);
        StackPane donutStack = new StackPane(pieChart, hole);

        double productPct = total == 0 ? 0 : (productCount * 100.0 / total);
        double farmerPct = total == 0 ? 0 : (farmerCount * 100.0 / total);

        Label productLegend = buildLegendRow("#3b82f6", "Product Reviews",
                productCount + " (" + String.format("%.1f", productPct) + "%)");
        Label farmerLegend = buildLegendRow("#ef4444", "Farmer Reviews",
                farmerCount + " (" + String.format("%.1f", farmerPct) + "%)");
        VBox legendBox = new VBox(8, productLegend, farmerLegend);

        HBox chartRow = new HBox(20, donutStack, legendBox);
        chartRow.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(title, chartRow);
        return card;
    }

    private Label buildLegendRow(String colorHex, String name, String countText) {
        Label swatch = new Label("\u25A0");
        swatch.setStyle("-fx-text-fill: " + colorHex + ";");
        Label combined = new Label(name + "   " + countText);
        combined.setGraphic(swatch);
        combined.setFont(Font.font("Segoe UI", 12));
        return combined;
    }

    private VBox buildAverageRatingsCard() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setStyle(cardStyle());
        HBox.setHgrow(card, Priority.ALWAYS);

        Label title = new Label("Average Ratings");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        double productAvg = averageRating("Product");
        double farmerAvg = averageRating("Farmer");

        VBox productBox = buildRatingColumn("Product Reviews", productAvg);
        VBox farmerBox = buildRatingColumn("Farmer Reviews", farmerAvg);

        HBox ratingsRow = new HBox(30, productBox, farmerBox);
        card.getChildren().addAll(title, ratingsRow);
        return card;
    }

    private VBox buildRatingColumn(String label, double average) {
        Label nameLabel = new Label(label);
        nameLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11;");

        Label scoreLabel = new Label(String.format("%.1f / 5", average));
        scoreLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        int rounded = (int) Math.round(average);
        Label starsLabel = new Label(starsText(rounded));
        starsLabel.setStyle("-fx-text-fill: #f5a623; -fx-font-size: 14;");

        return new VBox(4, nameLabel, scoreLabel, starsLabel);
    }

    private double averageRating(String category) {
        int sum = 0;
        int count = 0;
        for (FeedbackEntry entry : allFeedback) {
            boolean isFarmer = entry.getType().equals("Farmer Review");
            if (category.equals("Farmer") && !isFarmer) {
                continue;
            }
            if (category.equals("Product") && isFarmer) {
                continue;
            }
            sum += entry.getRating();
            count++;
        }
        return count == 0 ? 0 : (double) sum / count;
    }

    private String starsText(int rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i < rating ? "\u2605" : "\u2606");
        }
        return sb.toString();
    }

    private VBox buildFeedbackStatusCard() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setStyle(cardStyle());
        card.setPrefWidth(260);

        Label title = new Label("Feedback Status");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        approvedBar = new ProgressBar();
        approvedBar.setPrefWidth(160);
        approvedBar.setStyle("-fx-accent: #2e7d32;");
        approvedSummaryLabel = new Label();
        HBox approvedRow = buildStatusRow("Approved", approvedBar, approvedSummaryLabel);

        pendingBar = new ProgressBar();
        pendingBar.setPrefWidth(160);
        pendingBar.setStyle("-fx-accent: #ef6c00;");
        pendingSummaryLabel = new Label();
        HBox pendingRow = buildStatusRow("Pending", pendingBar, pendingSummaryLabel);

        underReviewBar = new ProgressBar();
        underReviewBar.setPrefWidth(160);
        underReviewBar.setStyle("-fx-accent: #1565c0;");
        underReviewSummaryLabel = new Label();
        HBox underReviewRow = buildStatusRow("Under Review", underReviewBar, underReviewSummaryLabel);

        card.getChildren().addAll(title, approvedRow, pendingRow, underReviewRow);
        updateFeedbackStatusSummary();
        return card;
    }

    private HBox buildStatusRow(String label, ProgressBar bar, Label summaryLabel) {
        Label nameLabel = new Label(label);
        nameLabel.setPrefWidth(90);
        nameLabel.setFont(Font.font("Segoe UI", 12));

        summaryLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11;");

        VBox barAndSummary = new VBox(3, bar, summaryLabel);
        HBox row = new HBox(10, nameLabel, barAndSummary);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    // recomputes the Approved/Pending/Under Review counts across ALL feedback
    // (not just the filtered page) and repaints the three progress bars
    private void updateFeedbackStatusSummary() {
        int approved = countByStatus("Approved");
        int pending = countByStatus("Pending");
        int underReview = countByStatus("Under Review");
        int total = allFeedback.size();

        approvedBar.setProgress(total == 0 ? 0 : approved / (double) total);
        pendingBar.setProgress(total == 0 ? 0 : pending / (double) total);
        underReviewBar.setProgress(total == 0 ? 0 : underReview / (double) total);

        approvedSummaryLabel.setText(approved + " (" + formatPct(approved, total) + "%)");
        pendingSummaryLabel.setText(pending + " (" + formatPct(pending, total) + "%)");
        underReviewSummaryLabel.setText(underReview + " (" + formatPct(underReview, total) + "%)");
    }

    private int countByStatus(String status) {
        int count = 0;
        for (FeedbackEntry entry : allFeedback) {
            if (entry.getStatus().equals(status)) {
                count++;
            }
        }
        return count;
    }

    private String formatPct(int part, int total) {
        double pct = total == 0 ? 0 : (part * 100.0 / total);
        return String.format("%.1f", pct);
    }

    private String cardStyle() {
        return "-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);";
    }

    // ------------------------------------------------------------------
    // Sample data - the first 8 entries match the reference screenshot
    // exactly; the rest are generated so the tab counts (245 / 168 / 77)
    // and pagination (31 pages at 8 rows each) line up the same way.
    // ------------------------------------------------------------------

    private List<FeedbackEntry> loadAllFeedback() {
        List<FeedbackEntry> list = new ArrayList<>();
        list.add(new FeedbackEntry("FBK1254", "Green Mart", "Buyer", "Product Review", "Tomato (Ramesh Patil)",
                5, "Excellent quality and fresh!", LocalDate.of(2025, 5, 29), "Approved"));
        list.add(new FeedbackEntry("FBK1253", "Fresh Store", "Buyer", "Feedback", "App Experience",
                4, "Good platform, easy to use.", LocalDate.of(2025, 5, 29), "Pending"));
        list.add(new FeedbackEntry("FBK1252", "Mahesh Jadhav", "Farmer", "Farmer Review", "Ramesh Patil",
                5, "Great buyer, smooth deal.", LocalDate.of(2025, 5, 28), "Approved"));
        list.add(new FeedbackEntry("FBK1251", "Daily Needs", "Buyer", "Product Review", "Potato (Suresh Yadav)",
                4, "Good potatoes, fast delivery.", LocalDate.of(2025, 5, 28), "Approved"));
        list.add(new FeedbackEntry("FBK1250", "Suresh Yadav", "Farmer", "Feedback", "Payment Process",
                2, "Payment failed twice.", LocalDate.of(2025, 5, 28), "Under Review"));
        list.add(new FeedbackEntry("FBK1249", "Organic Basket", "Buyer", "Product Review", "Onion (Anita Deshmukh)",
                5, "Very fresh and good quality.", LocalDate.of(2025, 5, 27), "Approved"));
        list.add(new FeedbackEntry("FBK1248", "Anita Deshmukh", "Farmer", "Farmer Review", "Green Mart",
                4, "Good communication.", LocalDate.of(2025, 5, 27), "Approved"));
        list.add(new FeedbackEntry("FBK1247", "Vikram Singh", "Farm Equipment Owner", "Feedback", "Feature Request",
                3, "Add more filters in search.", LocalDate.of(2025, 5, 27), "Pending"));

        // 237 more entries: 75 Farmer Review + 162 Product Review/Feedback,
        // which combined with the 8 above gives 77 / 168 / 245 - same as the screenshot
        list.addAll(generateBulkFeedback(1246, 237, 75, 162));
        return list;
    }

    private List<FeedbackEntry> generateBulkFeedback(int startId, int totalCount, int farmerTarget, int productTarget) {
        String[] buyerNames = { "City Grocers", "Metro Mart", "Sunrise Foods", "Value Bazaar", "Prime Basket",
                "Local Fresh", "QuickBuy", "Family Store" };
        String[] farmerNames = { "Ramesh Patil", "Suresh Yadav", "Mahesh Jadhav", "Anita Deshmukh", "Vikram Singh",
                "Sunita Kale", "Prakash More", "Geeta Pawar" };
        String[] products = { "Tomato", "Potato", "Onion", "Cabbage", "Carrot", "Spinach", "Brinjal", "Wheat" };
        String[] appTopics = { "App Experience", "Payment Process", "Feature Request", "Delivery Tracking",
                "Customer Support", "Search Filters" };
        String[] productMessages = { "Fresh and good quality.", "Delivered on time.", "Packaging could be better.",
                "Great value for money.", "Exactly as described." };
        String[] farmerMessages = { "Smooth deal, would work again.", "Good communication throughout.",
                "Payment was on time.", "Very cooperative buyer.", "Professional and reliable." };
        String[] feedbackMessages = { "Works well overall.", "Could use more filters.",
                "Had a minor issue, resolved quickly.", "Really like the new update.", "Support was helpful." };
        String[] statuses = { "Approved", "Pending", "Under Review" };
        int[] ratingPool = { 5, 4, 5, 5, 4, 3, 5, 4 };

        List<FeedbackEntry> list = new ArrayList<>(totalCount);
        LocalDate cursorDate = LocalDate.of(2025, 5, 26);
        int farmerAssigned = 0;
        int productAssigned = 0;

        for (int i = 0; i < totalCount; i++) {
            boolean makeFarmer;
            if (farmerAssigned >= farmerTarget) {
                makeFarmer = false;
            } else if (productAssigned >= productTarget) {
                makeFarmer = true;
            } else {
                // keeps the two categories interleaved roughly in proportion to their targets
                makeFarmer = (farmerAssigned * productTarget) <= (productAssigned * farmerTarget);
            }

            String id = "FBK" + (startId - i);
            String status = statuses[i % statuses.length];
            LocalDate date = cursorDate.minusDays(i / 6);
            int rating = ratingPool[i % ratingPool.length];

            if (makeFarmer) {
                String farmer = farmerNames[i % farmerNames.length];
                String buyer = buyerNames[i % buyerNames.length];
                list.add(new FeedbackEntry(id, farmer, "Farmer", "Farmer Review", buyer,
                        rating, farmerMessages[i % farmerMessages.length], date, status));
                farmerAssigned++;
            } else {
                boolean isAppFeedback = (i % 4 == 0);
                String buyer = buyerNames[i % buyerNames.length];
                if (isAppFeedback) {
                    list.add(new FeedbackEntry(id, buyer, "Buyer", "Feedback", appTopics[i % appTopics.length],
                            rating, feedbackMessages[i % feedbackMessages.length], date, status));
                } else {
                    String farmer = farmerNames[i % farmerNames.length];
                    String product = products[i % products.length];
                    list.add(new FeedbackEntry(id, buyer, "Buyer", "Product Review", product + " (" + farmer + ")",
                            rating, productMessages[i % productMessages.length], date, status));
                }
                productAssigned++;
            }
        }
        return list;
    }

    // ------------------------------------------------------------------
    // Simple data model for a table row
    // ------------------------------------------------------------------

    public static class FeedbackEntry {
        private final String id;
        private final String userName;
        private final String userRole;
        private final String type;
        private final String subject;
        private final int rating;
        private final String message;
        private final LocalDate date;
        private String status;

        public FeedbackEntry(String id, String userName, String userRole, String type, String subject,
                int rating, String message, LocalDate date, String status) {
            this.id = id;
            this.userName = userName;
            this.userRole = userRole;
            this.type = type;
            this.subject = subject;
            this.rating = rating;
            this.message = message;
            this.date = date;
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserRole() {
            return userRole;
        }

        public String getType() {
            return type;
        }

        public String getSubject() {
            return subject;
        }

        public int getRating() {
            return rating;
        }

        public String getStarsDisplay() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                sb.append(i < rating ? "\u2605" : "\u2606");
            }
            return sb.toString();
        }

        public String getMessage() {
            return message;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getFormattedDate() {
            return date.format(DISPLAY_DATE);
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}