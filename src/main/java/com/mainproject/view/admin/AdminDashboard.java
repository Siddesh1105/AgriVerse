package com.mainproject.view.admin;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * AgriLink Admin Dashboard - JavaFX version of the reference screenshot.
 * Built with Java 17 / JavaFX 21.
 *
 * Layout: green sidebar on the left, top search/notification bar,
 * stat cards, a recent orders table and a revenue line chart.
 *
 * Every button in the UI does something (alert, page switch, dialog, etc.)
 * so the app is clickable end to end even though it isn't hooked up to a
 * real backend.
 */
public class AdminDashboard extends Application {

        // colors used across the UI, kept in one place so the theme is easy to tweak
        private static final String GREEN = "#1f7a3d";
        private static final String GREEN_DARK = "#14532d";
        private static final String BG = "#f4f6f5";

        private BorderPane rootLayout;
        private StackPane contentArea;
        private Button activeNavButton;
        private Stage primaryStage;

        public static void main(String[] args) {
                launch(args);
        }

        @Override
        public void start(Stage stage) {
                primaryStage = stage;

                // the scene root gets replaced by showDashboard() straight away, this is just
                // a placeholder so the Scene has something to attach to
                Scene scene = new Scene(new StackPane(), 1320, 730);
                stage.setTitle("AgriLink Admin Dashboard");
                stage.setScene(scene);
                showDashboard();
                stage.show();
        }

        // rebuilds the whole dashboard screen and puts it back on the shared stage -
        // other screens (like UserManagement) call this to come back here
        public void showDashboard() {
                rootLayout = new BorderPane();
                rootLayout.setStyle("-fx-background-color: " + BG + ";");

                rootLayout.setLeft(buildSidebar());
                rootLayout.setTop(buildTopBar());

                contentArea = new StackPane();
                contentArea.setPadding(new Insets(20));
                contentArea.getChildren().add(buildDashboardView());
                rootLayout.setCenter(contentArea);

                primaryStage.getScene().setRoot(rootLayout);
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
                                "Notifications", "Content Management",
                                "Feedback & Reviews", "Reports & Complaints", "Payment Management",
                                "Audit Logs", "System & Data Management"
                };

                VBox navBox = new VBox(1);
                for (String item : navItems) {
                        Button navButton = buildNavButton(item);
                        navBox.getChildren().add(navButton);
                        if (item.equals("Dashboard")) {
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
                button.setOnAction(e -> switchPage(label, button));
                return button;
        }

        private void styleActiveButton(Button button) {
                button.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 0;");
        }

        private void styleInactiveButton(Button button) {
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: #d7e4d9; -fx-background-radius: 0;");
        }

        // swaps whatever is in the center content area and keeps the sidebar highlight
        // in sync. Public so sibling screens (e.g. CropPriceManagement) can route back
        // through here when their own sidebar nav items are clicked.
        public void switchPage(String pageName, Button clickedButton) {
                // every nav item that has a real screen hands off to its own class;
                // only "AI & Smart Tools" has no screen yet and falls through to the
                // placeholder view below.
                if (pageName.equals("User Management")) {
                        new UserManagement(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Product Management")) {
                        new ProductManagement(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Farmer Verification")) {
                        new FarmerVerification(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Order Management")) {
                        new OrderManagement(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Live Marketplace")) {
                        new LiveMarketplace(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Reports & Complaints")) {
                        new ReportsComplaints(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Equipment Management")) {
                        new EquipmentManagement(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Analytics & Reports")) {
                        new AnalyticsReports(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Crop Price Management")) {
                        new CropPriceManagement(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Audit Logs")) {
                        new AuditLogs(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("System & Data Management")) {
                        new SystemDataManage(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Content Management")) {
                        new ContentManagement(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Notifications")) {
                        new NotificationManagement(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Payment Management")) {
                        new PaymentManagement(primaryStage, this).show();
                        return;
                }
                if (pageName.equals("Feedback & Reviews")) {
                        new FeedbackReviews(primaryStage, this).show();
                        return;
                }

                if (activeNavButton != null) {
                        styleInactiveButton(activeNavButton);
                }
                styleActiveButton(clickedButton);
                activeNavButton = clickedButton;

                if (pageName.equals("Dashboard")) {
                        contentArea.getChildren().setAll(buildDashboardView());
                } else {
                        contentArea.getChildren().setAll(buildPlaceholderView(pageName));
                }
        }

        private VBox buildPlaceholderView(String pageName) {
                VBox box = new VBox(10);
                box.setAlignment(Pos.CENTER);
                Label title = new Label(pageName);
                title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
                Label info = new Label("This section hasn't been built yet - only Dashboard has full content.");
                info.setStyle("-fx-text-fill: #888;");
                box.getChildren().addAll(title, info);
                return box;
        }

        // ------------------------------------------------------------------
        // Top bar
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
                bellButton.setOnAction(e -> handleNotificationsClick());

                Button messageButton = new Button("\uD83D\uDCAC");
                messageButton.setStyle("-fx-background-color: transparent; -fx-font-size: 15;");
                messageButton.setOnAction(e -> handleMessagesClick());

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

        private void handleNotificationsClick() {
                showInfoAlert("Notifications", "You have 42 pending approvals and 3 new orders today.");
        }

        private void handleMessagesClick() {
                showInfoAlert("Messages", "No new messages right now.");
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
        // Dashboard content (stat cards + orders table + revenue chart)
        // ------------------------------------------------------------------

        private VBox buildDashboardView() {
                VBox view = new VBox(20);

                Label heading = new Label("Dashboard Overview");
                heading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

                GridPane statsGrid = buildStatsGrid();

                HBox bottomRow = new HBox(20);
                VBox ordersSection = buildRecentOrdersSection();
                VBox revenueSection = buildRevenueSection();
                HBox.setHgrow(ordersSection, Priority.ALWAYS);
                bottomRow.getChildren().addAll(ordersSection, revenueSection);

                view.getChildren().addAll(heading, statsGrid, bottomRow);
                return view;
        }

        private GridPane buildStatsGrid() {
                GridPane grid = new GridPane();
                grid.setHgap(20);
                grid.setVgap(20);

                grid.add(createStatCard("\uD83D\uDC65", "Total Users", "12,450", "+12.5% from last month", true), 0, 0);
                grid.add(createStatCard("\uD83C\uDF3E", "Total Farmers", "5,240", "+18.2% from last month", true), 1,
                                0);
                grid.add(createStatCard("\uD83D\uDED2", "Total Buyers", "7,210", "+10.3% from last month", true), 2, 0);
                grid.add(createStatCard("\uD83D\uDCE6", "Total Products", "25,840", "+15.1% from last month", true), 3,
                                0);

                grid.add(createStatCard("\uD83E\uDDFE", "Total Orders", "18,420", "+20.3% from last month", true), 0,
                                1);
                grid.add(createStatCard("\u20B9", "Total Revenue", "\u20B948.5 Lakh", "+22.1% from last month", true),
                                1, 1);
                grid.add(createStatCard("\uD83D\uDD34", "Live Sessions", "18", "+5.6% from last hour", true), 2, 1);
                grid.add(createStatCard("\u23F3", "Pending Approvals", "42", "View all pending", false), 3, 1);

                for (int i = 0; i < 4; i++) {
                        ColumnConstraints cc = new ColumnConstraints();
                        cc.setPercentWidth(25);
                        grid.getColumnConstraints().add(cc);
                }
                return grid;
        }

        private VBox createStatCard(String icon, String title, String value, String footerText,
                        boolean footerIsPlainGrowth) {
                VBox card = new VBox(8);
                card.setPadding(new Insets(18));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");
                card.setPrefWidth(260);

                Label iconLabel = new Label(icon);
                iconLabel.setFont(Font.font(18));
                iconLabel.setStyle("-fx-background-color: #eaf6ec; -fx-background-radius: 50; -fx-padding: 8 12 8 12;");

                Label titleLabel = new Label(title);
                titleLabel.setFont(Font.font("Segoe UI", 12));
                titleLabel.setStyle("-fx-text-fill: #777;");

                Label valueLabel = new Label(value);
                valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

                Label footerLabel = new Label(footerText);
                footerLabel.setFont(Font.font("Segoe UI", 11));
                if (footerIsPlainGrowth) {
                        footerLabel.setStyle("-fx-text-fill: #2e7d32;");
                } else {
                        // "Pending Approvals" footer acts like a link
                        footerLabel.setStyle("-fx-text-fill: #2e7d32; -fx-underline: true; -fx-cursor: hand;");
                        footerLabel.setOnMouseClicked(e -> handleViewPendingApprovals());
                }

                card.getChildren().addAll(iconLabel, titleLabel, valueLabel, footerLabel);
                return card;
        }

        private void handleViewPendingApprovals() {
                showInfoAlert("Pending Approvals", "There are 42 items waiting for admin approval.");
        }

        // ---- Recent orders table ----

        private VBox buildRecentOrdersSection() {
                VBox section = new VBox(12);
                section.setPadding(new Insets(18));
                section.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

                HBox header = new HBox();
                header.setAlignment(Pos.CENTER_LEFT);
                Label title = new Label("Recent Orders");
                title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Hyperlink viewAll = new Hyperlink("View All");
                viewAll.setOnAction(e -> handleViewAllOrders());
                header.getChildren().addAll(title, spacer, viewAll);

                TableView<Order> table = buildOrdersTable();

                section.getChildren().addAll(header, table);
                return section;
        }

        private TableView<Order> buildOrdersTable() {
                TableView<Order> table = new TableView<>();
                table.setPrefHeight(230);
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                TableColumn<Order, String> idCol = new TableColumn<>("Order ID");
                idCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));

                TableColumn<Order, String> customerCol = new TableColumn<>("Customer");
                customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));

                TableColumn<Order, String> productCol = new TableColumn<>("Product");
                productCol.setCellValueFactory(new PropertyValueFactory<>("product"));

                TableColumn<Order, String> amountCol = new TableColumn<>("Amount");
                amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

                TableColumn<Order, String> statusCol = new TableColumn<>("Status");
                statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
                statusCol.setCellFactory(col -> new TableCell<Order, String>() {
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
                                badge.setStyle("-fx-background-radius: 12; -fx-font-size: 11; "
                                                + statusColorStyle(status));
                                setGraphic(badge);
                                setText(null);
                        }
                });

                table.getColumns().addAll(idCol, customerCol, productCol, amountCol, statusCol);
                table.setItems(loadRecentOrders());

                // clicking a row pops up the order details - stands in for a real "view order"
                // screen
                table.setRowFactory(tv -> {
                        TableRow<Order> row = new TableRow<>();
                        row.setOnMouseClicked(e -> {
                                if (!row.isEmpty()) {
                                        showOrderDetails(row.getItem());
                                }
                        });
                        return row;
                });

                return table;
        }

        private String statusColorStyle(String status) {
                switch (status) {
                        case "Delivered":
                                return "-fx-background-color: #e6f4ea; -fx-text-fill: #2e7d32;";
                        case "Processing":
                                return "-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00;";
                        case "Shipped":
                                return "-fx-background-color: #e3f2fd; -fx-text-fill: #1565c0;";
                        case "Pending":
                                return "-fx-background-color: #f5f5f5; -fx-text-fill: #616161;";
                        default:
                                return "-fx-background-color: #eeeeee; -fx-text-fill: #333333;";
                }
        }

        private ObservableList<Order> loadRecentOrders() {
                ObservableList<Order> orders = FXCollections.observableArrayList();
                orders.add(new Order("ORD#13254", "Green Mart", "Tomato (100 kg)", "\u20B92,800", "Delivered"));
                orders.add(new Order("ORD#13253", "Fresh Store", "Potato (50 kg)", "\u20B91,250", "Processing"));
                orders.add(new Order("ORD#13252", "Daily Needs", "Onion (80 kg)", "\u20B91,600", "Shipped"));
                orders.add(new Order("ORD#13251", "Organic Basket", "Cabbage (60 kg)", "\u20B9900", "Pending"));
                return orders;
        }

        private void handleViewAllOrders() {
                showInfoAlert("All Orders", "Opening the full Order Management page.");
        }

        private void showOrderDetails(Order order) {
                String details = "Order ID: " + order.getOrderId()
                                + "\nCustomer: " + order.getCustomer()
                                + "\nProduct: " + order.getProduct()
                                + "\nAmount: " + order.getAmount()
                                + "\nStatus: " + order.getStatus();
                showInfoAlert("Order Details", details);
        }

        // ---- Revenue chart ----

        private VBox buildRevenueSection() {
                VBox section = new VBox(12);
                section.setPadding(new Insets(18));
                section.setPrefWidth(380);
                section.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

                HBox header = new HBox();
                header.setAlignment(Pos.CENTER_LEFT);
                Label title = new Label("Revenue Overview");
                title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                VBox amountBox = new VBox(2);
                amountBox.setAlignment(Pos.CENTER_RIGHT);
                Label monthLabel = new Label("This Month");
                monthLabel.setStyle("-fx-text-fill: #999; -fx-font-size: 11;");
                Label amountLabel = new Label("\u20B948.5 Lakh");
                amountLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
                amountBox.getChildren().addAll(monthLabel, amountLabel);

                header.getChildren().addAll(title, spacer, amountBox);

                LineChart<String, Number> chart = buildRevenueChart();

                section.getChildren().addAll(header, chart);
                return section;
        }

        private LineChart<String, Number> buildRevenueChart() {
                CategoryAxis xAxis = new CategoryAxis();
                NumberAxis yAxis = new NumberAxis();
                yAxis.setLabel("\u20B9 (Lakhs)");

                LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
                chart.setLegendVisible(false);
                chart.setPrefHeight(260);
                chart.setCreateSymbols(true);
                chart.setAnimated(false);

                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.getData().add(new XYChart.Data<>("Jan", 22));
                series.getData().add(new XYChart.Data<>("Feb", 28));
                series.getData().add(new XYChart.Data<>("Mar", 25));
                series.getData().add(new XYChart.Data<>("Apr", 34));
                series.getData().add(new XYChart.Data<>("May", 32));
                series.getData().add(new XYChart.Data<>("Jun", 48.5));

                chart.getData().add(series);
                return chart;
        }

        // ------------------------------------------------------------------
        // Simple data model for a table row
        // ------------------------------------------------------------------

        public static class Order {
                private final String orderId;
                private final String customer;
                private final String product;
                private final String amount;
                private final String status;

                public Order(String orderId, String customer, String product, String amount, String status) {
                        this.orderId = orderId;
                        this.customer = customer;
                        this.product = product;
                        this.amount = amount;
                        this.status = status;
                }

                public String getOrderId() {
                        return orderId;
                }

                public String getCustomer() {
                        return customer;
                }

                public String getProduct() {
                        return product;
                }

                public String getAmount() {
                        return amount;
                }

                public String getStatus() {
                        return status;
                }
        }
}