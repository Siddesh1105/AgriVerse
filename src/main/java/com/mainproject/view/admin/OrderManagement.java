package com.mainproject.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Order Management screen for the AgriLink admin panel.
 * Same pattern as the other screens - builds itself onto the shared Stage
 * and knows how to jump to every other screen that already exists.
 */
public class OrderManagement {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";
    private static final int PAGE_SIZE = 5;
    private static final int TOTAL_PAGES = 3684;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter SLASH_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Stage stage;
    private final AdminDashboard dashboard;

    private BorderPane rootLayout;
    private TableView<Order> table;
    private TextField searchField;
    private ComboBox<String> statusComboBox;
    private Button dateRangeButton;
    private Label resultsLabel;
    private Button activeTabButton;
    private final Map<String, Button> tabButtonsByKey = new HashMap<>();
    private List<Button> pageButtons;

    private ObservableList<Order> allOrders;
    private FilteredList<Order> filteredOrders;

    private String currentStatusFilter = "All Orders";
    private boolean suppressComboSync = false;
    private int currentPage = 1;
    private int totalOrdersCount = 18420;

    // advanced filter state, set through the "Filter" button dialog
    private String advancedCustomer = "";
    private String advancedFarmer = "";
    private Double minAmount = null;
    private Double maxAmount = null;

    // date range filter state, set through the date range button dialog
    private boolean dateFilterActive = false;
    private LocalDate rangeFrom = LocalDate.of(2025, 6, 1);
    private LocalDate rangeTo = LocalDate.of(2025, 6, 29);

    public OrderManagement(Stage stage, AdminDashboard dashboard) {
        this.stage = stage;
        this.dashboard = dashboard;
    }

    public void show() {
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + BG + ";");
        rootLayout.setLeft(buildSidebar());
        rootLayout.setTop(buildTopBar());
        rootLayout.setCenter(buildContent());
        stage.getScene().setRoot(rootLayout);
    }

    // ------------------------------------------------------------------
    // Sidebar
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
            Button navButton = new Button(item);
            navButton.setPrefWidth(228);
            navButton.setAlignment(Pos.CENTER_LEFT);
            navButton.setPadding(new Insets(11, 20, 11, 20));
            navButton.setFont(Font.font("Segoe UI", 13));

            if (item.equals("Order Management")) {
                styleActiveNav(navButton);
            } else {
                styleInactiveNav(navButton);
            }

            navButton.setOnAction(e -> handleNavClick(item));
            navBox.getChildren().add(navButton);
        }

        ScrollPane scrollPane = new ScrollPane(navBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: " + GREEN_DARK + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        sidebar.getChildren().addAll(logoBox, scrollPane);
        return sidebar;
    }

    private void handleNavClick(String pageName) {
        if (pageName.equals("Order Management")) {
            return; // already on this screen
        }
        if (pageName.equals("Dashboard")) {
            dashboard.showDashboard();
            return;
        }
        if (pageName.equals("User Management")) {
            new UserManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Farmer Verification")) {
            new FarmerVerification(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Product Management")) {
            new ProductManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Live Marketplace")) {
            new LiveMarketplace(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Equipment Management")) {
            new EquipmentManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Analytics & Reports")) {
            new AnalyticsReports(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Notifications")) {
            new NotificationManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Content Management")) {
            new ContentManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Feedback & Reviews")) {
            new FeedbackReviews(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Reports & Complaints")) {
            new ReportsComplaints(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Payment Management")) {
            new PaymentManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Audit Logs")) {
            new AuditLogs(stage, dashboard).show();
            return;
        }
        if (pageName.equals("System & Data Management")) {
            new SystemDataManage(stage, dashboard).show();
            return;
        }
        showInfoAlert(pageName, "This section hasn't been built yet in this demo.");
    }

    private void styleActiveNav(Button b) {
        b.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 0;");
    }

    private void styleInactiveNav(Button b) {
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: #d7e4d9; -fx-background-radius: 0;");
    }

    private void toggleSidebar() {
        Node sidebar = rootLayout.getLeft();
        if (sidebar != null) {
            sidebar.setVisible(!sidebar.isVisible());
            sidebar.setManaged(sidebar.isVisible());
        }
    }

    // ------------------------------------------------------------------
    // Top bar
    // ------------------------------------------------------------------

    private HBox buildTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(16, 25, 16, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #eaeaea; -fx-border-width: 0 0 1 0;");

        Button menuButton = new Button("\u2630");
        menuButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16;");
        menuButton.setOnAction(e -> toggleSidebar());

        Label title = new Label("Order Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        topBar.getChildren().addAll(menuButton, title);
        return topBar;
    }

    // ------------------------------------------------------------------
    // Center content
    // ------------------------------------------------------------------

    private VBox buildContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        HBox tabsBar = buildTabsBar();
        HBox filterBar = buildFilterBar();

        VBox tableCard = new VBox(12);
        tableCard.setPadding(new Insets(18));
        tableCard.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        table = buildOrdersTable();
        loadSampleOrders();
        HBox paginationBar = buildPaginationBar();

        tableCard.getChildren().addAll(table, paginationBar);
        content.getChildren().addAll(tabsBar, filterBar, tableCard);
        return content;
    }

    private HBox buildTabsBar() {
        HBox bar = new HBox(8);
        bar.setPadding(new Insets(10, 15, 10, 15));
        bar.setStyle("-fx-background-color: white; -fx-background-radius: 10;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 1);");

        String[] tabs = { "All Orders", "Pending", "Processing", "Shipped", "Delivered", "Cancelled" };
        for (String tabName : tabs) {
            Button tabButton = new Button(tabName);
            tabButton.setPadding(new Insets(8, 16, 8, 16));
            tabButton.setFont(Font.font("Segoe UI", 13));

            if (tabName.equals(currentStatusFilter)) {
                activeTabButton = tabButton;
                styleActiveTab(tabButton);
            } else {
                styleInactiveTab(tabButton);
            }

            tabButton.setOnAction(e -> setActiveFilter(tabName));
            tabButtonsByKey.put(tabName, tabButton);
            bar.getChildren().add(tabButton);
        }
        return bar;
    }

    // shared by tab clicks and the "Select Status" dropdown so both stay in sync
    private void setActiveFilter(String filterKey) {
        currentStatusFilter = filterKey;

        Button matchingTab = tabButtonsByKey.get(filterKey);
        if (matchingTab != null) {
            if (activeTabButton != null) {
                styleInactiveTab(activeTabButton);
            }
            styleActiveTab(matchingTab);
            activeTabButton = matchingTab;
        }

        suppressComboSync = true;
        statusComboBox.setValue(filterKey);
        suppressComboSync = false;

        currentPage = 1;
        applyFilters();
    }

    private void styleActiveTab(Button b) {
        b.setStyle("-fx-background-color: #eaf6ec; -fx-text-fill: " + GREEN + ";"
                + "-fx-background-radius: 8; -fx-font-weight: bold;");
    }

    private void styleInactiveTab(Button b) {
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: #666666; -fx-background-radius: 8;");
    }

    // ---- filter row: search box, status dropdown, date range, advanced filter
    // ----

    private HBox buildFilterBar() {
        HBox bar = new HBox(12);
        bar.setPadding(new Insets(12, 15, 12, 15));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: white; -fx-background-radius: 10;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 1);");

        searchField = new TextField();
        searchField.setPromptText("Search Order ID, customer, farmer...");
        searchField.setPrefWidth(220);
        searchField.textProperty().addListener((obs, oldText, newText) -> applyFilters());
        HBox searchBox = new HBox(6, new Label("\uD83D\uDD0D"), searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(6, 12, 6, 12));
        searchBox.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 8;");

        statusComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "All Orders", "Pending", "Processing", "Shipped", "Delivered", "Cancelled"));
        statusComboBox.setValue("All Orders");
        statusComboBox.setPromptText("Select Status");
        statusComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!suppressComboSync && newVal != null) {
                setActiveFilter(newVal);
            }
        });

        dateRangeButton = new Button(formatRangeLabel());
        dateRangeButton.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 8;");
        dateRangeButton.setPadding(new Insets(8, 14, 8, 14));
        dateRangeButton.setOnAction(e -> handleDateRangeClick());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button filterButton = new Button("\u25BD  Filter");
        filterButton.setStyle(
                "-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 8; -fx-background-radius: 8;");
        filterButton.setPadding(new Insets(8, 14, 8, 14));
        filterButton.setOnAction(e -> handleAdvancedFilter());

        bar.getChildren().addAll(searchBox, statusComboBox, dateRangeButton, spacer, filterButton);
        return bar;
    }

    private String formatRangeLabel() {
        return rangeFrom.format(SLASH_FORMAT) + "  -  " + rangeTo.format(SLASH_FORMAT);
    }

    private void handleDateRangeClick() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Select Date Range");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        DatePicker fromPicker = new DatePicker(rangeFrom);
        DatePicker toPicker = new DatePicker(rangeTo);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.addRow(0, new Label("From:"), fromPicker);
        grid.addRow(1, new Label("To:"), toPicker);
        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (fromPicker.getValue() == null || toPicker.getValue() == null
                    || fromPicker.getValue().isAfter(toPicker.getValue())) {
                showInfoAlert("Invalid Range", "Please choose a valid from/to date.");
                return;
            }
            rangeFrom = fromPicker.getValue();
            rangeTo = toPicker.getValue();
            dateFilterActive = true;
            dateRangeButton.setText(formatRangeLabel());
            applyFilters();
        }
    }

    private void handleAdvancedFilter() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Advanced Filters");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField customerField = new TextField(advancedCustomer);
        customerField.setPromptText("Customer name");
        TextField farmerField = new TextField(advancedFarmer);
        farmerField.setPromptText("Farmer name");
        TextField minField = new TextField(minAmount == null ? "" : String.valueOf(minAmount.intValue()));
        minField.setPromptText("Min amount");
        TextField maxField = new TextField(maxAmount == null ? "" : String.valueOf(maxAmount.intValue()));
        maxField.setPromptText("Max amount");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.addRow(0, new Label("Customer:"), customerField);
        grid.addRow(1, new Label("Farmer:"), farmerField);
        grid.addRow(2, new Label("Min Amount:"), minField);
        grid.addRow(3, new Label("Max Amount:"), maxField);
        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            advancedCustomer = customerField.getText().trim();
            advancedFarmer = farmerField.getText().trim();
            minAmount = parseOrNull(minField.getText());
            maxAmount = parseOrNull(maxField.getText());
            applyFilters();
        }
    }

    private Double parseOrNull(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ---- table ----

    private TableView<Order> buildOrdersTable() {
        TableView<Order> tv = new TableView<>();
        tv.setPrefHeight(320);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setPlaceholder(new Label("No orders found for this filter."));

        TableColumn<Order, String> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        idCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String orderId, boolean empty) {
                super.updateItem(orderId, empty);
                if (empty || orderId == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label avatar = new Label("\uD83D\uDC64");
                avatar.setMinSize(28, 28);
                avatar.setMaxSize(28, 28);
                avatar.setAlignment(Pos.CENTER);
                avatar.setStyle("-fx-background-color: #eaf6ec; -fx-background-radius: 14;");
                Label idLabel = new Label(orderId);
                HBox box = new HBox(8, avatar, idLabel);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
                setText(null);
            }
        });

        TableColumn<Order, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));

        TableColumn<Order, String> farmerCol = new TableColumn<>("Farmer");
        farmerCol.setCellValueFactory(new PropertyValueFactory<>("farmer"));

        TableColumn<Order, String> productCol = new TableColumn<>("Product");
        productCol.setCellValueFactory(new PropertyValueFactory<>("product"));

        TableColumn<Order, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amountDisplay"));

        TableColumn<Order, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<>() {
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
                badge.setStyle("-fx-background-radius: 12; -fx-font-size: 11; " + statusBadgeStyle(status));
                setGraphic(badge);
                setText(null);
            }
        });

        TableColumn<Order, String> dateCol = new TableColumn<>("Order Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDateDisplay"));

        TableColumn<Order, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> actionCell());

        tv.getColumns().addAll(idCol, customerCol, farmerCol, productCol, amountCol, statusCol, dateCol, actionCol);
        return tv;
    }

    private String statusBadgeStyle(String status) {
        switch (status) {
            case "Delivered":
                return "-fx-background-color: #e6f4ea; -fx-text-fill: #2e7d32;";
            case "Processing":
                return "-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00;";
            case "Shipped":
                return "-fx-background-color: #e3f2fd; -fx-text-fill: #1565c0;";
            case "Pending":
                return "-fx-background-color: #f5f5f5; -fx-text-fill: #616161;";
            case "Cancelled":
                return "-fx-background-color: #fdecea; -fx-text-fill: #c62828;";
            default:
                return "-fx-background-color: #eeeeee; -fx-text-fill: #333333;";
        }
    }

    private TableCell<Order, Void> actionCell() {
        return new TableCell<>() {
            private final Button viewButton = new Button("\uD83D\uDC41");
            private final Button editButton = new Button("\u270F");
            private final Button deleteButton = new Button("\uD83D\uDDD1");
            private final HBox box = new HBox(8, viewButton, editButton, deleteButton);

            {
                String plainStyle = "-fx-background-color: transparent; -fx-font-size: 13;";
                viewButton.setStyle(plainStyle);
                editButton.setStyle(plainStyle);
                deleteButton.setStyle(plainStyle + " -fx-text-fill: #c62828;");
                viewButton.setOnAction(e -> handleViewOrder(getTableRow().getItem()));
                editButton.setOnAction(e -> handleEditOrder(getTableRow().getItem()));
                deleteButton.setOnAction(e -> handleDeleteOrder(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        };
    }

    // ---- pagination footer ----

    private HBox buildPaginationBar() {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(15, 0, 0, 0));

        resultsLabel = new Label();
        updateResultsLabel();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button prevButton = new Button("<");
        prevButton.setStyle("-fx-background-color: transparent;");
        prevButton.setOnAction(e -> handlePrevPage());

        pageButtons = new ArrayList<>();
        HBox pageNumberBox = new HBox(6);
        for (int i = 1; i <= 5; i++) {
            final int pageNumber = i;
            Button pageButton = new Button(String.valueOf(pageNumber));
            pageButton.setOnAction(e -> handlePageClick(pageNumber));
            pageButtons.add(pageButton);
            pageNumberBox.getChildren().add(pageButton);
        }

        Label ellipsis = new Label("...");
        ellipsis.setPadding(new Insets(0, 4, 0, 4));

        Button lastPageButton = new Button(String.valueOf(TOTAL_PAGES));
        lastPageButton.setOnAction(e -> handlePageClick(TOTAL_PAGES));
        pageButtons.add(lastPageButton);

        Button nextButton = new Button(">");
        nextButton.setStyle("-fx-background-color: transparent;");
        nextButton.setOnAction(e -> handleNextPage());

        highlightPageButton(currentPage);

        HBox rightSide = new HBox(6, prevButton, pageNumberBox, ellipsis, lastPageButton, nextButton);
        rightSide.setAlignment(Pos.CENTER_LEFT);

        bar.getChildren().addAll(resultsLabel, spacer, rightSide);
        return bar;
    }

    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            highlightPageButton(currentPage);
            updateResultsLabel();
        }
    }

    private void handleNextPage() {
        if (currentPage < TOTAL_PAGES) {
            currentPage++;
            highlightPageButton(currentPage);
            updateResultsLabel();
        }
    }

    private void handlePageClick(int page) {
        currentPage = page;
        highlightPageButton(page);
        updateResultsLabel();
    }

    private void highlightPageButton(int page) {
        for (Button b : pageButtons) {
            if (b.getText().equals(String.valueOf(page))) {
                b.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 6;");
            } else {
                b.setStyle("-fx-background-color: transparent; -fx-text-fill: #555555; -fx-background-radius: 6;");
            }
        }
    }

    // for "All Orders" this uses the mock 18,420 total against the current page;
    // any other filter just reflects how many sample rows actually matched
    private void updateResultsLabel() {
        int shown = filteredOrders.size();
        if (currentStatusFilter.equals("All Orders")) {
            int start = shown == 0 ? 0 : (currentPage - 1) * PAGE_SIZE + 1;
            int end = Math.min(currentPage * PAGE_SIZE, totalOrdersCount);
            resultsLabel.setText("Showing " + start + " to " + end + " of "
                    + String.format("%,d", totalOrdersCount) + " orders");
        } else {
            int start = shown == 0 ? 0 : 1;
            resultsLabel.setText("Showing " + start + " to " + shown + " of " + shown + " orders");
        }
    }

    // ------------------------------------------------------------------
    // Data loading + filtering
    // ------------------------------------------------------------------

    private void loadSampleOrders() {
        allOrders = FXCollections.observableArrayList(
                new Order("ORD#10254", "Green Mart", "Ramesh Patil", "Tomato (100 kg)", 2800, "Delivered",
                        LocalDate.of(2025, 5, 20)),
                new Order("ORD#10253", "Fresh Store", "Mahesh Jadhav", "Potato (50 kg)", 1250, "Processing",
                        LocalDate.of(2025, 5, 19)),
                new Order("ORD#10252", "Daily Needs", "Suresh Yadav", "Onion (80 kg)", 1600, "Shipped",
                        LocalDate.of(2025, 5, 19)),
                new Order("ORD#10251", "Organic Basket", "Ramesh Patil", "Cabbage (60 kg)", 900, "Pending",
                        LocalDate.of(2025, 5, 18)),
                new Order("ORD#10250", "Green Mart", "Vikram Singh", "Wheat (100 kg)", 2200, "Delivered",
                        LocalDate.of(2025, 5, 18)));

        filteredOrders = new FilteredList<>(allOrders, order -> true);
        filteredOrders.addListener((ListChangeListener<Order>) change -> updateResultsLabel());

        table.setItems(filteredOrders);
    }

    private void applyFilters() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();

        filteredOrders.setPredicate(order -> {
            boolean matchesStatus = currentStatusFilter.equals("All Orders")
                    || order.getStatus().equals(currentStatusFilter);

            boolean matchesSearch = query.isEmpty()
                    || order.getOrderId().toLowerCase().contains(query)
                    || order.getCustomer().toLowerCase().contains(query)
                    || order.getFarmer().toLowerCase().contains(query)
                    || order.getProduct().toLowerCase().contains(query);

            boolean matchesDate = !dateFilterActive
                    || (!order.getOrderDate().isBefore(rangeFrom) && !order.getOrderDate().isAfter(rangeTo));

            boolean matchesCustomer = advancedCustomer.isEmpty()
                    || order.getCustomer().toLowerCase().contains(advancedCustomer.toLowerCase());

            boolean matchesFarmer = advancedFarmer.isEmpty()
                    || order.getFarmer().toLowerCase().contains(advancedFarmer.toLowerCase());

            boolean matchesMin = minAmount == null || order.getAmount() >= minAmount;
            boolean matchesMax = maxAmount == null || order.getAmount() <= maxAmount;

            return matchesStatus && matchesSearch && matchesDate && matchesCustomer && matchesFarmer && matchesMin
                    && matchesMax;
        });
    }

    // ------------------------------------------------------------------
    // Button actions: view / edit / delete
    // ------------------------------------------------------------------

    private void handleViewOrder(Order order) {
        if (order == null) {
            return;
        }
        String details = "Order ID: " + order.getOrderId()
                + "\nCustomer: " + order.getCustomer()
                + "\nFarmer: " + order.getFarmer()
                + "\nProduct: " + order.getProduct()
                + "\nAmount: " + order.getAmountDisplay()
                + "\nStatus: " + order.getStatus()
                + "\nOrder Date: " + order.getOrderDateDisplay();
        showInfoAlert("Order Details", details);
    }

    private void handleEditOrder(Order order) {
        if (order == null) {
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Order");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField productField = new TextField(order.getProduct());
        TextField amountField = new TextField(String.valueOf((int) order.getAmount()));
        ComboBox<String> statusBox = new ComboBox<>(FXCollections.observableArrayList(
                "Pending", "Processing", "Shipped", "Delivered", "Cancelled"));
        statusBox.setValue(order.getStatus());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.addRow(0, new Label("Product:"), productField);
        grid.addRow(1, new Label("Amount:"), amountField);
        grid.addRow(2, new Label("Status:"), statusBox);
        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            order.setProduct(productField.getText().trim());
            Double newAmount = parseOrNull(amountField.getText());
            if (newAmount != null) {
                order.setAmount(newAmount);
            }
            order.setStatus(statusBox.getValue());
            table.refresh();
            applyFilters();
        }
    }

    private void handleDeleteOrder(Order order) {
        if (order == null) {
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Order");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete " + order.getOrderId() + "? This cannot be undone.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            allOrders.remove(order);
            totalOrdersCount--;
            updateResultsLabel();
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
    // Simple data model for a table row
    // ------------------------------------------------------------------

    public static class Order {
        private final String orderId;
        private String customer;
        private String farmer;
        private String product;
        private double amount;
        private String status;
        private final LocalDate orderDate;

        public Order(String orderId, String customer, String farmer, String product,
                double amount, String status, LocalDate orderDate) {
            this.orderId = orderId;
            this.customer = customer;
            this.farmer = farmer;
            this.product = product;
            this.amount = amount;
            this.status = status;
            this.orderDate = orderDate;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getFarmer() {
            return farmer;
        }

        public void setFarmer(String farmer) {
            this.farmer = farmer;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getAmountDisplay() {
            return "\u20B9" + String.format("%,.0f", amount);
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDate getOrderDate() {
            return orderDate;
        }

        public String getOrderDateDisplay() {
            return orderDate.format(DATE_FORMAT);
        }
    }
}