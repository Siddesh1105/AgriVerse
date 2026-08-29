package com.mainproject.view.admin;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
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
import java.util.Optional;

/**
 * Payment Management screen for the AgriLink admin app.
 *
 * Same green sidebar / top bar shell as the other screens, with its own
 * center content: stat cards, status tabs, a search/filter bar, a paginated
 * transactions table with a per-row "View" action, and a bottom row of
 * charts (payment method split, transaction trend, top gateways) - matching
 * the reference screenshot.
 */
public class PaymentManagement {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";
    private static final int PAGE_SIZE = 7;

    private final Stage primaryStage;
    private final AdminDashboard dashboard;

    private BorderPane rootLayout;
    private Button activeNavButton;

    private String activeTab = "All Transactions";
    private final List<Button> tabButtons = new ArrayList<>();

    private TextField searchField;
    private ComboBox<String> methodFilter;
    private ComboBox<String> statusFilter;
    private DatePicker fromDate;
    private DatePicker toDate;

    private TableView<Transaction> table;
    private Label rangeLabel;
    private HBox pageButtonsBox;
    private Button prevPageBtn;
    private Button nextPageBtn;

    private final List<Transaction> allTransactions = new ArrayList<>();
    private List<Transaction> filteredTransactions = new ArrayList<>();
    private int currentPage = 1;

    private static final DateTimeFormatter ROW_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    public PaymentManagement(Stage primaryStage, AdminDashboard dashboard) {
        this.primaryStage = primaryStage;
        this.dashboard = dashboard;
        loadMockData();
        filteredTransactions = new ArrayList<>(allTransactions);
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
                "Audit Logs"
        };

        VBox navBox = new VBox(1);
        for (String item : navItems) {
            Button navButton = buildNavButton(item);
            navBox.getChildren().add(navButton);
            if (item.equals("Payment Management")) {
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
    // page, everything else falls back to the dashboard's placeholder view
    private void handleNavClick(String pageName) {
        if (pageName.equals("Payment Management")) {
            return; // already here
        }
        if (pageName.equals("Dashboard")) {
            dashboard.showDashboard();
            return;
        }
        if (pageName.equals("User Management")) {
            new UserManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Product Management")) {
            new ProductManagement(primaryStage, dashboard).show();
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
        if (pageName.equals("Notifications")) {
            new NotificationManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Audit Logs")) {
            new AuditLogs(primaryStage, dashboard).show();
            return;
        }
        dashboard.showDashboard();
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

        TextField topSearchField = new TextField();
        topSearchField.setPromptText("Search by users, orders, products...");
        topSearchField.setPrefWidth(420);
        topSearchField.setStyle("-fx-background-color: transparent;");
        topSearchField.setOnAction(e -> handleTopSearch(topSearchField.getText()));

        HBox searchBox = new HBox(8, new Label("\uD83D\uDD0D"), topSearchField);
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

    private void handleTopSearch(String query) {
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
    // Center content
    // ------------------------------------------------------------------

    private ScrollPane buildContent() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(20));

        VBox headingBox = new VBox(2);
        Label heading = new Label("Payment Management");
        heading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        Label subheading = new Label("Monitor and manage all payments and transactions");
        subheading.setStyle("-fx-text-fill: #888;");
        headingBox.getChildren().addAll(heading, subheading);

        GridPane statsGrid = buildStatsGrid();
        HBox tabsBar = buildTabsBar();
        HBox filterBar = buildFilterBar();
        VBox tableCard = buildTableCard();
        HBox chartsRow = buildChartsRow();

        view.getChildren().addAll(headingBox, statsGrid, tabsBar, filterBar, tableCard, chartsRow);

        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + BG + "; -fx-background: " + BG + ";");
        return scrollPane;
    }

    // ---- stat cards ----

    private GridPane buildStatsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);

        grid.add(createStatCard("\uD83D\uDCC4", "#EAF6EC", "Total Transactions", "18,920", "+6.6% this month",
                "#2e7d32"), 0, 0);
        grid.add(createStatCard("\uD83D\uDCB0", "#FFF3E0", "Total Amount", "\u20B948.5 Lakh", "+18.3% this month",
                "#2e7d32"), 1, 0);
        grid.add(createStatCard("\u2705", "#E8F5E9", "Successful Payments", "17,842", "94.3% success rate", "#2e7d32"),
                2, 0);
        grid.add(createStatCard("\u21A9", "#E3F2FD", "Refund Issued", "1,078", "\u20B92.45 Lakh", "#1565c0"), 3, 0);
        grid.add(createStatCard("\u274C", "#FDECEA", "Failed Payments", "156", "0.8% failure rate", "#c62828"), 4, 0);

        for (int i = 0; i < 5; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(20);
            grid.getColumnConstraints().add(cc);
        }
        return grid;
    }

    private VBox createStatCard(String icon, String iconBg, String title, String value, String footerText,
            String footerColor) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));
        iconLabel.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 50; -fx-padding: 6 10 6 10;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", 12));
        titleLabel.setStyle("-fx-text-fill: #777;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));

        Label footerLabel = new Label(footerText);
        footerLabel.setFont(Font.font("Segoe UI", 11));
        footerLabel.setStyle("-fx-text-fill: " + footerColor + ";");

        card.getChildren().addAll(iconLabel, titleLabel, valueLabel, footerLabel);
        return card;
    }

    // ---- status tabs ----

    private HBox buildTabsBar() {
        HBox bar = new HBox(6);
        bar.setPadding(new Insets(6));
        bar.setStyle("-fx-background-color: white; -fx-background-radius: 8;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 1);");

        String[] tabs = { "All Transactions", "Successful", "Pending", "Failed", "Refunded" };
        tabButtons.clear();
        for (String tabName : tabs) {
            Button tabBtn = new Button(tabName);
            tabBtn.setPadding(new Insets(8, 16, 8, 16));
            tabBtn.setOnAction(e -> selectTab(tabName));
            tabButtons.add(tabBtn);
            bar.getChildren().add(tabBtn);
        }
        styleTabs();
        return bar;
    }

    private void styleTabs() {
        for (Button b : tabButtons) {
            if (b.getText().equals(activeTab)) {
                b.setStyle("-fx-background-color: " + GREEN
                        + "; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-weight: bold;");
            } else {
                b.setStyle("-fx-background-color: transparent; -fx-text-fill: #555; -fx-background-radius: 6;");
            }
        }
    }

    private void selectTab(String tabName) {
        activeTab = tabName;
        styleTabs();
        applyFilters();
    }

    // ---- search + filter bar ----

    private HBox buildFilterBar() {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14));
        bar.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        searchField = new TextField();
        searchField.setPromptText("Search by Transaction ID, Order ID...");
        searchField.setPrefWidth(240);

        methodFilter = new ComboBox<>(FXCollections.observableArrayList(
                "All Payment Methods", "UPI", "Card", "Net Banking", "Wallet"));
        methodFilter.setValue("All Payment Methods");

        statusFilter = new ComboBox<>(FXCollections.observableArrayList(
                "All Status", "Success", "Pending", "Failed", "Refunded"));
        statusFilter.setValue("All Status");

        fromDate = new DatePicker(LocalDate.of(2025, 5, 1));
        toDate = new DatePicker(LocalDate.of(2025, 5, 29));

        Button filterBtn = new Button("\u2261 Filter");
        filterBtn.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-font-weight: bold;"
                + " -fx-padding: 8 16; -fx-background-radius: 6;");
        filterBtn.setOnAction(e -> applyFilters());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        bar.getChildren().addAll(searchField, methodFilter, statusFilter, fromDate, toDate, spacer, filterBtn);
        return bar;
    }

    // filters the in-memory dataset by tab, search text, method, status and
    // date range, then rebuilds the table starting back at page 1
    private void applyFilters() {
        String search = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String method = methodFilter.getValue();
        String status = statusFilter.getValue();
        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();

        filteredTransactions = new ArrayList<>();
        for (Transaction t : allTransactions) {
            boolean matchesTab = activeTab.equals("All Transactions") || t.getStatus().equals(tabToStatus(activeTab));
            boolean matchesSearch = search.isEmpty()
                    || t.getTransactionId().toLowerCase().contains(search)
                    || t.getOrderId().toLowerCase().contains(search);
            boolean matchesMethod = method.equals("All Payment Methods") || t.getMethod().equals(method);
            boolean matchesStatus = status.equals("All Status") || t.getStatus().equals(status);
            LocalDate txDate = t.getDateTime().toLocalDate();
            boolean matchesFrom = from == null || !txDate.isBefore(from);
            boolean matchesTo = to == null || !txDate.isAfter(to);

            if (matchesTab && matchesSearch && matchesMethod && matchesStatus && matchesFrom && matchesTo) {
                filteredTransactions.add(t);
            }
        }

        currentPage = 1;
        refreshTable();

        if (filteredTransactions.isEmpty()) {
            showInfoAlert("No Results", "No transactions match the selected filters.");
        }
    }

    private String tabToStatus(String tab) {
        if (tab.equals("Successful")) {
            return "Success";
        }
        return tab; // Pending, Failed, Refunded map 1:1
    }

    // ---- transactions table ----

    private VBox buildTableCard() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        table = buildTransactionsTable();
        HBox pagination = buildPaginationBar();

        card.getChildren().addAll(table, pagination);
        refreshTable();
        return card;
    }

    private TableView<Transaction> buildTransactionsTable() {
        TableView<Transaction> tableView = new TableView<>();
        tableView.setPrefHeight(320);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Transaction, String> idCol = new TableColumn<>("Transaction ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));

        TableColumn<Transaction, String> orderCol = new TableColumn<>("Order ID");
        orderCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        TableColumn<Transaction, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));

        TableColumn<Transaction, String> farmerCol = new TableColumn<>("Farmer");
        farmerCol.setCellValueFactory(new PropertyValueFactory<>("farmer"));

        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amountDisplay"));

        TableColumn<Transaction, String> methodCol = new TableColumn<>("Method");
        methodCol.setCellValueFactory(new PropertyValueFactory<>("method"));

        TableColumn<Transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<Transaction, String>() {
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

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date & Time");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateTimeDisplay"));

        TableColumn<Transaction, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<Transaction, Void>() {
            private final Button viewBtn = new Button("\uD83D\uDC41 View");
            {
                viewBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + GREEN + "; -fx-font-size: 11;");
                viewBtn.setOnAction(e -> {
                    Transaction t = getTableView().getItems().get(getIndex());
                    showTransactionDetails(t);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewBtn);
            }
        });

        tableView.getColumns().addAll(idCol, orderCol, customerCol, farmerCol, amountCol, methodCol, statusCol, dateCol,
                actionCol);
        return tableView;
    }

    private String statusColorStyle(String status) {
        switch (status) {
            case "Success":
                return "-fx-background-color: #e6f4ea; -fx-text-fill: #2e7d32;";
            case "Pending":
                return "-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00;";
            case "Failed":
                return "-fx-background-color: #fdecea; -fx-text-fill: #c62828;";
            case "Refunded":
                return "-fx-background-color: #e3f2fd; -fx-text-fill: #1565c0;";
            default:
                return "-fx-background-color: #eeeeee; -fx-text-fill: #333333;";
        }
    }

    private void showTransactionDetails(Transaction t) {
        String details = "Transaction ID: " + t.getTransactionId()
                + "\nOrder ID: " + t.getOrderId()
                + "\nCustomer: " + t.getCustomer()
                + "\nFarmer: " + t.getFarmer()
                + "\nAmount: " + t.getAmountDisplay()
                + "\nMethod: " + t.getMethod()
                + "\nStatus: " + t.getStatus()
                + "\nDate & Time: " + t.getDateTimeDisplay();
        showInfoAlert("Transaction Details", details);
    }

    // ---- pagination ----

    private HBox buildPaginationBar() {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(10, 0, 0, 0));

        rangeLabel = new Label();
        rangeLabel.setStyle("-fx-text-fill: #777; -fx-font-size: 12;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        prevPageBtn = new Button("\u2039");
        prevPageBtn.setOnAction(e -> goToPage(currentPage - 1));

        pageButtonsBox = new HBox(4);
        pageButtonsBox.setAlignment(Pos.CENTER);

        nextPageBtn = new Button("\u203A");
        nextPageBtn.setOnAction(e -> goToPage(currentPage + 1));

        HBox pager = new HBox(4, prevPageBtn, pageButtonsBox, nextPageBtn);
        pager.setAlignment(Pos.CENTER);

        bar.getChildren().addAll(rangeLabel, spacer, pager);
        return bar;
    }

    private int totalPages() {
        return Math.max(1, (int) Math.ceil(filteredTransactions.size() / (double) PAGE_SIZE));
    }

    private void goToPage(int page) {
        int total = totalPages();
        if (page < 1 || page > total) {
            return;
        }
        currentPage = page;
        refreshTable();
    }

    // repaints the table rows, the "showing X to Y of Z" label and the page
    // number buttons for the current filtered dataset / page
    private void refreshTable() {
        int total = filteredTransactions.size();
        int totalPages = totalPages();
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int fromIndex = Math.min((currentPage - 1) * PAGE_SIZE, total);
        int toIndex = Math.min(fromIndex + PAGE_SIZE, total);

        ObservableList<Transaction> pageItems = FXCollections.observableArrayList(
                filteredTransactions.subList(fromIndex, toIndex));
        table.setItems(pageItems);

        if (total == 0) {
            rangeLabel.setText("Showing 0 of 0 transactions");
        } else {
            rangeLabel.setText("Showing " + (fromIndex + 1) + " to " + toIndex + " of " + total + " transactions");
        }

        prevPageBtn.setDisable(currentPage <= 1);
        nextPageBtn.setDisable(currentPage >= totalPages);
        rebuildPageButtons(totalPages);
    }

    private void rebuildPageButtons(int totalPages) {
        pageButtonsBox.getChildren().clear();

        List<Integer> pagesToShow = new ArrayList<>();
        if (totalPages <= 7) {
            for (int i = 1; i <= totalPages; i++) {
                pagesToShow.add(i);
            }
        } else {
            pagesToShow.add(1);
            pagesToShow.add(2);
            pagesToShow.add(3);
            pagesToShow.add(-1); // ellipsis marker
            pagesToShow.add(totalPages);
        }

        for (int pageNum : pagesToShow) {
            if (pageNum == -1) {
                Label ellipsis = new Label("...");
                ellipsis.setPadding(new Insets(0, 6, 0, 6));
                pageButtonsBox.getChildren().add(ellipsis);
                continue;
            }
            Button pageBtn = new Button(String.valueOf(pageNum));
            pageBtn.setPrefWidth(30);
            if (pageNum == currentPage) {
                pageBtn.setStyle(
                        "-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 4;");
            } else {
                pageBtn.setStyle("-fx-background-color: #f3f4f6; -fx-text-fill: #333; -fx-background-radius: 4;");
            }
            final int target = pageNum;
            pageBtn.setOnAction(e -> goToPage(target));
            pageButtonsBox.getChildren().add(pageBtn);
        }
    }

    // ------------------------------------------------------------------
    // Bottom row: payment method split, trend chart, top gateways
    // ------------------------------------------------------------------

    private HBox buildChartsRow() {
        HBox row = new HBox(20);

        VBox methodsCard = buildPaymentMethodsCard();
        VBox trendCard = buildTransactionTrendCard();
        VBox gatewaysCard = buildTopGatewaysCard();

        HBox.setHgrow(trendCard, Priority.ALWAYS);
        row.getChildren().addAll(methodsCard, trendCard, gatewaysCard);
        return row;
    }

    private VBox buildPaymentMethodsCard() {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setPrefWidth(260);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label title = new Label("Payment Methods");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("UPI (45.2%)", 45.2),
                new PieChart.Data("Card (28.7%)", 28.7),
                new PieChart.Data("Net Banking (15.3%)", 15.3),
                new PieChart.Data("Wallet (10.8%)", 10.8));
        PieChart pieChart = new PieChart(pieData);
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(220);
        pieChart.setOnMouseClicked(
                e -> showInfoAlert("Payment Methods", "Breakdown of payment methods used across all transactions."));

        card.getChildren().addAll(title, pieChart);
        return card;
    }

    private VBox buildTransactionTrendCard() {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label title = new Label("Transaction Trend");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setPrefHeight(220);
        chart.setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("23 May", 28000));
        series.getData().add(new XYChart.Data<>("24 May", 24000));
        series.getData().add(new XYChart.Data<>("25 May", 30000));
        series.getData().add(new XYChart.Data<>("26 May", 26000));
        series.getData().add(new XYChart.Data<>("27 May", 32000));
        series.getData().add(new XYChart.Data<>("28 May", 34000));
        series.getData().add(new XYChart.Data<>("29 May", 40000));
        chart.getData().add(series);

        card.getChildren().addAll(title, chart);
        return card;
    }

    private VBox buildTopGatewaysCard() {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setPrefWidth(260);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label title = new Label("Top Payment Gateways");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        VBox list = new VBox(10);
        list.getChildren().addAll(
                buildGatewayRow("\uD83D\uDCB3", "Razorpay", "\u20B932.4 Lakh", "66.6%"),
                buildGatewayRow("\uD83D\uDCF1", "PhonePe", "\u20B910.8 Lakh", "22.2%"),
                buildGatewayRow("\uD83C\uDFE6", "Paytm", "\u20B94.1 Lakh", "8.5%"),
                buildGatewayRow("\u2022\u2022\u2022", "Others", "\u20B91.2 Lakh", "2.4%"));

        card.getChildren().addAll(title, list);
        return card;
    }

    private HBox buildGatewayRow(String icon, String name, String amount, String percent) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setCursor(javafx.scene.Cursor.HAND);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 50; -fx-padding: 6 10 6 10;");

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Segoe UI", 12));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox amountBox = new VBox(1);
        amountBox.setAlignment(Pos.CENTER_RIGHT);
        Label amountLabel = new Label(amount);
        amountLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        Label percentLabel = new Label(percent);
        percentLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 10;");
        amountBox.getChildren().addAll(amountLabel, percentLabel);

        row.getChildren().addAll(iconLabel, nameLabel, spacer, amountBox);
        row.setOnMouseClicked(
                e -> showInfoAlert(name, name + " processed " + amount + " (" + percent + " of total volume)."));
        return row;
    }

    // ------------------------------------------------------------------
    // Mock data
    // ------------------------------------------------------------------

    private void loadMockData() {
        allTransactions.add(new Transaction("TXN125487", "ORD#10254", "Green Mart", "Ramesh Patil", 2800, "UPI",
                "Success", LocalDateTime.of(2025, 5, 29, 10, 30)));
        allTransactions.add(new Transaction("TXN125486", "ORD#10253", "Fresh Store", "Mahesh Jadhav", 1250, "Card",
                "Success", LocalDateTime.of(2025, 5, 29, 10, 15)));
        allTransactions.add(new Transaction("TXN125485", "ORD#10252", "Daily Needs", "Suresh Yadav", 1600, "UPI",
                "Pending", LocalDateTime.of(2025, 5, 29, 9, 58)));
        allTransactions.add(new Transaction("TXN125484", "ORD#10251", "Organic Basket", "Vikram Singh", 900,
                "Net Banking", "Failed", LocalDateTime.of(2025, 5, 29, 9, 45)));
        allTransactions.add(new Transaction("TXN125483", "ORD#10250", "Green Mart", "Vikram Singh", 2200, "Wallet",
                "Success", LocalDateTime.of(2025, 5, 29, 9, 30)));
        allTransactions.add(new Transaction("TXN125482", "ORD#10249", "Fresh Store", "Anita Deshmukh", 1750, "Card",
                "Refunded", LocalDateTime.of(2025, 5, 29, 9, 10)));
        allTransactions.add(new Transaction("TXN125481", "ORD#10248", "Daily Needs", "Mahesh Jadhav", 1050, "UPI",
                "Success", LocalDateTime.of(2025, 5, 29, 8, 55)));
        allTransactions.add(new Transaction("TXN125480", "ORD#10247", "Organic Basket", "Ramesh Patil", 3100, "Card",
                "Success", LocalDateTime.of(2025, 5, 28, 19, 20)));
        allTransactions.add(new Transaction("TXN125479", "ORD#10246", "Green Mart", "Suresh Yadav", 1400, "UPI",
                "Failed", LocalDateTime.of(2025, 5, 28, 17, 45)));
        allTransactions.add(new Transaction("TXN125478", "ORD#10245", "Fresh Store", "Vikram Singh", 2650,
                "Net Banking", "Success", LocalDateTime.of(2025, 5, 28, 16, 10)));
        allTransactions.add(new Transaction("TXN125477", "ORD#10244", "Daily Needs", "Anita Deshmukh", 950, "Wallet",
                "Pending", LocalDateTime.of(2025, 5, 28, 14, 5)));
        allTransactions.add(new Transaction("TXN125476", "ORD#10243", "Organic Basket", "Mahesh Jadhav", 1820, "UPI",
                "Success", LocalDateTime.of(2025, 5, 28, 12, 30)));
        allTransactions.add(new Transaction("TXN125475", "ORD#10242", "Green Mart", "Ramesh Patil", 2450, "Card",
                "Refunded", LocalDateTime.of(2025, 5, 28, 11, 15)));
        allTransactions.add(new Transaction("TXN125474", "ORD#10241", "Fresh Store", "Suresh Yadav", 1300, "UPI",
                "Success", LocalDateTime.of(2025, 5, 27, 20, 5)));
        allTransactions.add(new Transaction("TXN125473", "ORD#10240", "Daily Needs", "Vikram Singh", 1980,
                "Net Banking", "Success", LocalDateTime.of(2025, 5, 27, 18, 40)));
        allTransactions.add(new Transaction("TXN125472", "ORD#10239", "Organic Basket", "Anita Deshmukh", 870, "Wallet",
                "Failed", LocalDateTime.of(2025, 5, 27, 15, 25)));
        allTransactions.add(new Transaction("TXN125471", "ORD#10238", "Green Mart", "Mahesh Jadhav", 2100, "Card",
                "Success", LocalDateTime.of(2025, 5, 27, 13, 10)));
        allTransactions.add(new Transaction("TXN125470", "ORD#10237", "Fresh Store", "Ramesh Patil", 1550, "UPI",
                "Success", LocalDateTime.of(2025, 5, 26, 21, 0)));
        allTransactions.add(new Transaction("TXN125469", "ORD#10236", "Daily Needs", "Suresh Yadav", 2900, "Card",
                "Success", LocalDateTime.of(2025, 5, 26, 17, 30)));
        allTransactions.add(new Transaction("TXN125468", "ORD#10235", "Organic Basket", "Vikram Singh", 1150, "UPI",
                "Pending", LocalDateTime.of(2025, 5, 26, 10, 50)));
        allTransactions.add(new Transaction("TXN125467", "ORD#10234", "Green Mart", "Anita Deshmukh", 2350,
                "Net Banking", "Success", LocalDateTime.of(2025, 5, 25, 16, 20)));
    }

    // ------------------------------------------------------------------
    // Simple data model for a table row
    // ------------------------------------------------------------------

    public static class Transaction {
        private final String transactionId;
        private final String orderId;
        private final String customer;
        private final String farmer;
        private final int amount;
        private final String method;
        private final String status;
        private final LocalDateTime dateTime;

        public Transaction(String transactionId, String orderId, String customer, String farmer,
                int amount, String method, String status, LocalDateTime dateTime) {
            this.transactionId = transactionId;
            this.orderId = orderId;
            this.customer = customer;
            this.farmer = farmer;
            this.amount = amount;
            this.method = method;
            this.status = status;
            this.dateTime = dateTime;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getCustomer() {
            return customer;
        }

        public String getFarmer() {
            return farmer;
        }

        public String getAmountDisplay() {
            return "\u20B9" + amount;
        }

        public String getMethod() {
            return method;
        }

        public String getStatus() {
            return status;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public String getDateTimeDisplay() {
            return dateTime.format(ROW_FMT);
        }
    }
}
