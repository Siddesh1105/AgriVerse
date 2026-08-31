package com.mainproject.view.admin;

import com.mainproject.dao.UserDAO;
import com.mainproject.dao.ProductDAO;
import com.mainproject.dao.OrderDAO;
import com.mainproject.controller.ReviewController;
import com.mainproject.model.User;
import com.mainproject.model.Product;
import com.mainproject.model.Order;
import com.mainproject.model.Review;

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

import java.util.List;
import java.util.Optional;

/**
 * AgriLink Admin Dashboard
 *
 * Central navigation controller for all Admin screens.
 *
 * Java 17 + JavaFX 21
 */
public class AdminDashboard extends Application {

        // ============================================================
        // COLORS
        // ============================================================

        private static final String GREEN = "#1f7a3d";
        private static final String GREEN_DARK = "#14532d";
        private static final String BG = "#f4f6f5";

        // ============================================================
        // VARIABLES
        // ============================================================

        private BorderPane rootLayout;
        private StackPane contentArea;

        private Button activeNavButton;

        private Stage primaryStage;

        private final UserDAO userDAO = new UserDAO();
        private final ProductDAO productDAO = new ProductDAO();
        private final OrderDAO orderDAO = new OrderDAO();

        // ============================================================
        // MAIN
        // ============================================================

        public static void main(String[] args) {
                launch(args);
        }

        // ============================================================
        // START
        // ============================================================
        //
        // Only used when AdminDashboard is launched directly (e.g. for
        // standalone testing). The normal app flow now starts at
        // AdminLogin, which calls showDashboard(Stage) below instead.
        // ============================================================

        @Override
        public void start(Stage stage) {

                primaryStage = stage;

                Scene scene = new Scene(
                                new StackPane(),
                                1320,
                                730);

                stage.setTitle("AgriLink Admin Dashboard");
                stage.setScene(scene);

                showDashboard();

                stage.show();
        }

        // ============================================================
        // SHOW DASHBOARD - called with a Stage from outside
        // ============================================================
        //
        // AdminLogin (and any other screen that doesn't already hold a
        // reference to this AdminDashboard instance) uses this overload to
        // hand its Stage over: showDashboard(Stage) stores the Stage, then
        // delegates to the existing showDashboard() below.
        // ============================================================

        public void showDashboard(Stage stage) {
                this.primaryStage = stage;
                showDashboard();
        }

        // ============================================================
        // SHOW DASHBOARD
        // ============================================================

        public void showDashboard() {

                rootLayout = new BorderPane();

                rootLayout.setStyle(
                                "-fx-background-color: " + BG + ";");

                // LEFT SIDEBAR
                rootLayout.setLeft(
                                buildSidebar());

                // TOP BAR
                rootLayout.setTop(
                                buildTopBar());

                // CENTER CONTENT
                contentArea = new StackPane();

                contentArea.setPadding(
                                new Insets(20));

                // Make the complete Dashboard Overview scrollable. This keeps the
                // existing dashboard content unchanged while allowing the lower
                // sections to remain accessible on smaller screens.
                ScrollPane dashboardScrollPane = new ScrollPane(buildDashboardView());
                dashboardScrollPane.setFitToWidth(true);
                dashboardScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                dashboardScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                dashboardScrollPane.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background: transparent;");

                contentArea.getChildren().add(
                                dashboardScrollPane);

                rootLayout.setCenter(
                                contentArea);

                primaryStage.getScene().setRoot(
                                rootLayout);
        }

        // ============================================================
        // SIDEBAR
        // ============================================================

        private VBox buildSidebar() {

                VBox sidebar = new VBox();

                sidebar.setPrefWidth(230);

                sidebar.setStyle(
                                "-fx-background-color: " + GREEN_DARK + ";");

                // --------------------------------------------------------
                // LOGO
                // --------------------------------------------------------

                VBox logoBox = new VBox(2);

                logoBox.setPadding(
                                new Insets(
                                                22,
                                                15,
                                                22,
                                                20));

                Label logo = new Label(
                                "\uD83C\uDF3F  AgriLink");

                logo.setTextFill(Color.WHITE);

                logo.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                20));

                Label subtitle = new Label(
                                "Admin Dashboard");

                subtitle.setTextFill(
                                Color.web("#c8e6c9"));

                subtitle.setFont(
                                Font.font(
                                                "Segoe UI",
                                                12));

                logoBox.getChildren().addAll(
                                logo,
                                subtitle);

                // --------------------------------------------------------
                // NAVIGATION ITEMS
                // --------------------------------------------------------

                String[] navItems = {

                                "Dashboard",

                                "User Management",

                                "Farmer Verification",

                                "Product Management",

                                "Order Management",

                                "Equipment Management",

                                "Analytics & Reports",

                                "Crop Price Management",

                                "Notifications",

                                "Feedback & Reviews",

                                "Payment Management"
                };

                VBox navBox = new VBox(1);

                for (String item : navItems) {

                        Button navButton = buildNavButton(item);

                        navBox.getChildren().add(
                                        navButton);

                        if (item.equals("Dashboard")) {

                                activeNavButton = navButton;

                                styleActiveButton(
                                                navButton);
                        }
                }

                // --------------------------------------------------------
                // SCROLL PANE
                // --------------------------------------------------------

                ScrollPane scrollPane = new ScrollPane(navBox);

                scrollPane.setFitToWidth(true);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background: " + GREEN_DARK + ";");

                VBox.setVgrow(
                                scrollPane,
                                Priority.ALWAYS);

                sidebar.getChildren().addAll(
                                logoBox,
                                scrollPane);

                return sidebar;
        }

        // ============================================================
        // CREATE NAVIGATION BUTTON
        // ============================================================

        private Button buildNavButton(
                        String label) {

                Button button = new Button(label);

                button.setPrefWidth(228);

                button.setAlignment(
                                Pos.CENTER_LEFT);

                button.setPadding(
                                new Insets(
                                                11,
                                                20,
                                                11,
                                                20));

                button.setFont(
                                Font.font(
                                                "Segoe UI",
                                                13));

                styleInactiveButton(button);

                button.setOnAction(
                                event -> navigateToPage(
                                                label,
                                                button));

                return button;
        }

        // ============================================================
        // ACTIVE BUTTON STYLE
        // ============================================================

        private void styleActiveButton(
                        Button button) {

                button.setStyle(
                                "-fx-background-color: " + GREEN + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-background-radius: 0;");
        }

        // ============================================================
        // INACTIVE BUTTON STYLE
        // ============================================================

        private void styleInactiveButton(
                        Button button) {

                button.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #d7e4d9;" +
                                                "-fx-background-radius: 0;");
        }

        // ============================================================
        // CENTRAL NAVIGATION METHOD
        // ============================================================
        //
        // IMPORTANT:
        // This method is PUBLIC because UserManagement and other
        // admin screens need to use the same navigation controller.
        //
        // ============================================================

        public void navigateToPage(
                        String pageName) {

                navigateToPage(
                                pageName,
                                null);
        }

        // ============================================================
        // CENTRAL NAVIGATION METHOD WITH BUTTON
        // ============================================================

        private void navigateToPage(
                        String pageName,
                        Button clickedButton) {
                // Keep one central navigation controller for every admin screen.
                // All admin pages call this method so navigation works directly
                // from any page to any other page.

                if (pageName == null) {
                        return;
                }

                String page = pageName.trim();

                if (page.equals("Dashboard")) {
                        showDashboard();
                        return;
                }

                if (page.equals("User Management")) {
                        new UserManagement(primaryStage, this).show();
                        return;
                }

                if (page.equals("Farmer Verification")) {
                        new FarmerVerification(primaryStage, this).show();
                        return;
                }

                if (page.equals("Product Management")) {
                        new ProductManagement(primaryStage, this).show();
                        return;
                }

                if (page.equals("Order Management")) {
                        new OrderManagement(primaryStage, this).show();
                        return;
                }

                if (page.equals("Equipment Management")) {
                        new EquipmentManagement(primaryStage, this).show();
                        return;
                }

                if (page.equals("Analytics & Reports")) {
                        new AnalyticsReports(primaryStage, this).show();
                        return;
                }

                if (page.equals("Notifications")) {
                        new NotificationManagement(primaryStage, this).show();
                        return;
                }

                if (page.equals("Crop Price Management")) {
                        new CropPriceManagement(primaryStage, this).show();
                        return;
                }

                if (page.equals("Feedback & Reviews")) {
                        new FeedbackReviews(primaryStage, this).show();
                        return;
                }

                if (page.equals("Payment Management")) {
                        new PaymentManagement(primaryStage, this).show();
                        return;
                }

                // These modules do not have an active screen class in the
                // supplied admin ZIP, so retain the existing placeholder behavior.
                showPlaceholderPage(page);
        }

        // ============================================================
        // PLACEHOLDER PAGE
        // ============================================================

        private void showPlaceholderPage(
                        String pageName) {

                if (rootLayout == null) {
                        return;
                }

                VBox box = new VBox(15);

                box.setAlignment(
                                Pos.CENTER);

                Label icon = new Label("\uD83D\uDEE0");

                icon.setFont(
                                Font.font(40));

                Label title = new Label(pageName);

                title.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                26));

                Label info = new Label(
                                "This admin module is ready to be connected.");

                info.setStyle(
                                "-fx-text-fill: #777;");

                Button backButton = new Button(
                                "Back to Dashboard");

                backButton.setStyle(
                                "-fx-background-color: " + GREEN + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-padding: 10 20 10 20;");

                backButton.setOnAction(
                                event -> showDashboard());

                box.getChildren().addAll(
                                icon,
                                title,
                                info,
                                backButton);

                contentArea
                                .getChildren()
                                .setAll(box);
        }

        // ============================================================
        // TOP BAR
        // ============================================================

        private HBox buildTopBar() {

                HBox topBar = new HBox(15);

                topBar.setPadding(
                                new Insets(
                                                14,
                                                25,
                                                14,
                                                20));

                topBar.setAlignment(
                                Pos.CENTER_LEFT);

                topBar.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #eaeaea;" +
                                                "-fx-border-width: 0 0 1 0;");

                // --------------------------------------------------------
                // MENU
                // --------------------------------------------------------

                Button menuButton = new Button("\u2630");

                menuButton.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-font-size: 16;");

                menuButton.setOnAction(
                                event -> toggleSidebar());

                // --------------------------------------------------------
                // SEARCH
                // --------------------------------------------------------

                TextField searchField = new TextField();

                searchField.setPromptText(
                                "Search by users, orders, products...");

                searchField.setPrefWidth(
                                420);

                searchField.setStyle(
                                "-fx-background-color: transparent;");

                searchField.setOnAction(
                                event -> handleSearch(
                                                searchField.getText()));

                HBox searchBox = new HBox(
                                8,
                                new Label("\uD83D\uDD0D"),
                                searchField);

                searchBox.setAlignment(
                                Pos.CENTER_LEFT);

                searchBox.setPadding(
                                new Insets(
                                                6,
                                                15,
                                                6,
                                                15));

                searchBox.setStyle(
                                "-fx-background-color: #f3f4f6;" +
                                                "-fx-background-radius: 20;");

                // --------------------------------------------------------
                // SPACER
                // --------------------------------------------------------

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                // --------------------------------------------------------
                // NOTIFICATION
                // --------------------------------------------------------

                Button bellButton = new Button(
                                "\uD83D\uDD14");

                bellButton.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-font-size: 15;");

                bellButton.setOnAction(
                                event -> handleNotificationsClick());

                // --------------------------------------------------------
                // MESSAGE
                // --------------------------------------------------------

                Button messageButton = new Button(
                                "\uD83D\uDCAC");

                messageButton.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-font-size: 15;");

                messageButton.setOnAction(
                                event -> handleMessagesClick());

                // --------------------------------------------------------
                // PROFILE
                // --------------------------------------------------------

                MenuButton profileMenu = new MenuButton(
                                "Super Admin");

                profileMenu.setStyle(
                                "-fx-background-color: transparent;");

                MenuItem profileItem = new MenuItem(
                                "My Profile");

                MenuItem settingsItem = new MenuItem(
                                "Settings");

                MenuItem logoutItem = new MenuItem(
                                "Logout");

                profileItem.setOnAction(
                                event -> showInfoAlert(
                                                "Profile",
                                                "Opening admin profile page."));

                settingsItem.setOnAction(
                                event -> showInfoAlert(
                                                "Settings",
                                                "Opening settings page."));

                logoutItem.setOnAction(
                                event -> handleLogout());

                profileMenu.getItems().addAll(
                                profileItem,
                                settingsItem,
                                logoutItem);

                topBar.getChildren().addAll(
                                menuButton,
                                searchBox,
                                spacer,
                                bellButton,
                                messageButton,
                                profileMenu);

                return topBar;
        }

        // ============================================================
        // TOGGLE SIDEBAR
        // ============================================================

        private void toggleSidebar() {

                if (rootLayout == null) {
                        return;
                }

                Node sidebar = rootLayout.getLeft();

                if (sidebar != null) {

                        boolean visible = sidebar.isVisible();

                        sidebar.setVisible(
                                        !visible);

                        sidebar.setManaged(
                                        !visible);
                }
        }

        // ============================================================
        // SEARCH
        // ============================================================

        private void handleSearch(
                        String query) {

                if (query == null ||
                                query.isBlank()) {

                        showInfoAlert(
                                        "Search",
                                        "Type something first, then press Enter.");

                } else {

                        showInfoAlert(
                                        "Search Results",
                                        "Searching for: \"" +
                                                        query +
                                                        "\"");
                }
        }

        // ============================================================
        // NOTIFICATIONS
        // ============================================================

        private void handleNotificationsClick() {
                navigateToPage("Notifications");
        }

        // ============================================================
        // MESSAGES
        // ============================================================

        private void handleMessagesClick() {

                showInfoAlert(
                                "Messages",
                                "No new messages right now.");
        }

        // ============================================================
        // LOGOUT
        // ============================================================

        private void handleLogout() {

                Alert confirm = new Alert(
                                Alert.AlertType.CONFIRMATION);

                confirm.setTitle(
                                "Logout");

                confirm.setHeaderText(
                                null);

                confirm.setContentText(
                                "Are you sure you want to logout?");

                Optional<ButtonType> result = confirm.showAndWait();

                if (result.isPresent() &&
                                result.get() == ButtonType.OK) {

                        // send the admin back to the login screen instead of just
                        // showing an alert and leaving the dashboard on screen
                        new AdminLogin().start(primaryStage);
                }
        }

        // ============================================================
        // INFORMATION ALERT
        // ============================================================

        private void showInfoAlert(
                        String title,
                        String message) {

                Alert alert = new Alert(
                                Alert.AlertType.INFORMATION);

                alert.setTitle(
                                title);

                alert.setHeaderText(
                                null);

                alert.setContentText(
                                message);

                alert.showAndWait();
        }

        // ============================================================
        // DASHBOARD VIEW
        // ============================================================

        private VBox buildDashboardView() {

                VBox view = new VBox(20);

                Label heading = new Label(
                                "Dashboard Overview");

                heading.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                24));

                GridPane statsGrid = buildStatsGrid();

                HBox bottomRow = new HBox(20);

                VBox ordersSection = buildRecentOrdersSection();

                VBox revenueSection = buildRevenueSection();

                HBox.setHgrow(
                                ordersSection,
                                Priority.ALWAYS);

                bottomRow.getChildren().addAll(
                                ordersSection,
                                revenueSection);

                view.getChildren().addAll(
                                heading,
                                statsGrid,
                                bottomRow);

                return view;
        }

        // ============================================================
        // STATISTICS
        // ============================================================

        private GridPane buildStatsGrid() {

                GridPane grid = new GridPane();
                grid.setHgap(20);
                grid.setVgap(20);

                List<User> users = userDAO.getAllUsers();
                List<User> farmers = userDAO.getAllFarmers();
                long buyers = users.stream().filter(u -> u != null && "Buyer".equalsIgnoreCase(u.getRole())).count();
                List<Product> products = productDAO.getAllProducts();
                List<Order> orders = orderDAO.getAllOrders();

                long pendingFarmers = farmers.stream().filter(u -> {
                        String s = u == null ? null : u.getVerificationStatus();
                        return s == null || s.isBlank() || "Pending".equalsIgnoreCase(s);
                }).count();

                long pendingProducts = products.stream().filter(p -> "Pending".equalsIgnoreCase(p.getStatus())).count();

                double revenue = orders.stream().mapToDouble(Order::getTotalAmount).sum();

                // Review analytics are loaded through the Review MVC flow and do not affect
                // existing stats.
                List<Review> reviews = new ReviewController().getAllReviews();
                long activeReviews = reviews.stream().filter(r -> r != null && "ACTIVE".equalsIgnoreCase(r.getStatus()))
                                .count();
                double averageRating = reviews.stream()
                                .filter(r -> r != null && "ACTIVE".equalsIgnoreCase(r.getStatus()))
                                .mapToInt(Review::getRating).average().orElse(0.0);

                grid.add(createStatCard("👥", "Total Users", String.valueOf(users.size()), "Live Firestore data", true),
                                0, 0);
                grid.add(createStatCard("🌾", "Total Farmers", String.valueOf(farmers.size()), "Live Firestore data",
                                true), 1, 0);
                grid.add(createStatCard("🛒", "Total Buyers", String.valueOf(buyers), "Live Firestore data", true), 2,
                                0);
                grid.add(createStatCard("📦", "Total Products", String.valueOf(products.size()), "Live Firestore data",
                                true), 3, 0);
                grid.add(createStatCard("🧾", "Total Orders", String.valueOf(orders.size()), "Live Firestore data",
                                true), 0, 1);
                grid.add(createStatCard("₹", "Total Revenue", "₹" + String.format("%.2f", revenue),
                                "Calculated from orders", true), 1, 1);
                grid.add(createStatCard("🔴", "Pending Farmers", String.valueOf(pendingFarmers), "Needs verification",
                                false), 2, 1);
                grid.add(createStatCard("⏳", "Pending Products", String.valueOf(pendingProducts), "Needs approval",
                                false), 3, 1);
                grid.add(createStatCard("⭐", "Active Reviews", String.valueOf(activeReviews), "Buyer & farmer feedback",
                                true), 0, 2);
                grid.add(createStatCard("🌟", "Average Rating", String.format("%.1f / 5", averageRating),
                                "From active reviews", true), 1, 2);

                for (int i = 0; i < 4; i++) {
                        ColumnConstraints cc = new ColumnConstraints();
                        cc.setPercentWidth(25);
                        grid.getColumnConstraints().add(cc);
                }
                return grid;
        }

        // ============================================================
        // STAT CARD
        // ============================================================

        private VBox createStatCard(
                        String icon,
                        String title,
                        String value,
                        String footerText,
                        boolean footerIsPlainGrowth) {

                VBox card = new VBox(8);

                card.setPadding(
                                new Insets(18));

                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

                card.setPrefWidth(
                                260);

                Label iconLabel = new Label(icon);

                iconLabel.setFont(
                                Font.font(18));

                iconLabel.setStyle(
                                "-fx-background-color: #eaf6ec;" +
                                                "-fx-background-radius: 50;" +
                                                "-fx-padding: 8 12 8 12;");

                Label titleLabel = new Label(title);

                titleLabel.setFont(
                                Font.font(
                                                "Segoe UI",
                                                12));

                titleLabel.setStyle(
                                "-fx-text-fill: #777;");

                Label valueLabel = new Label(value);

                valueLabel.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                22));

                Label footerLabel = new Label(
                                footerText);

                footerLabel.setFont(
                                Font.font(
                                                "Segoe UI",
                                                11));

                if (footerIsPlainGrowth) {

                        footerLabel.setStyle(
                                        "-fx-text-fill: #2e7d32;");

                } else {

                        footerLabel.setStyle(
                                        "-fx-text-fill: #2e7d32;" +
                                                        "-fx-underline: true;" +
                                                        "-fx-cursor: hand;");

                        footerLabel.setOnMouseClicked(
                                        event -> handleViewPendingApprovals());
                }

                card.getChildren().addAll(
                                iconLabel,
                                titleLabel,
                                valueLabel,
                                footerLabel);

                return card;
        }

        // ============================================================
        // PENDING APPROVALS
        // ============================================================

        private void handleViewPendingApprovals() {
                navigateToPage("Farmer Verification");
        }

        // ============================================================
        // RECENT ORDERS
        // ============================================================

        private VBox buildRecentOrdersSection() {

                VBox section = new VBox(12);

                section.setPadding(
                                new Insets(18));

                section.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

                HBox header = new HBox();

                header.setAlignment(
                                Pos.CENTER_LEFT);

                Label title = new Label(
                                "Recent Orders");

                title.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                16));

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                Hyperlink viewAll = new Hyperlink(
                                "View All");

                viewAll.setOnAction(
                                event -> navigateToPage(
                                                "Order Management"));

                header.getChildren().addAll(
                                title,
                                spacer,
                                viewAll);

                TableView<Order> table = buildOrdersTable();

                section.getChildren().addAll(
                                header,
                                table);

                return section;
        }

        // ============================================================
        // ORDERS TABLE
        // ============================================================

        private TableView<Order> buildOrdersTable() {
                TableView<Order> table = new TableView<>();
                table.setPrefHeight(230);
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                TableColumn<Order, String> id = new TableColumn<>("Order ID");
                id.setCellValueFactory(new PropertyValueFactory<>("orderId"));
                TableColumn<Order, String> buyer = new TableColumn<>("Buyer");
                buyer.setCellValueFactory(new PropertyValueFactory<>("buyerName"));
                TableColumn<Order, String> payment = new TableColumn<>("Payment");
                payment.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
                TableColumn<Order, String> amount = new TableColumn<>("Amount");
                amount.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                                "₹" + String.format("%.2f", c.getValue().getTotalAmount())));
                TableColumn<Order, String> status = new TableColumn<>("Status");
                status.setCellValueFactory(new PropertyValueFactory<>("status"));

                table.getColumns().addAll(id, buyer, payment, amount, status);
                table.setItems(loadRecentOrders());
                table.setRowFactory(tv -> {
                        TableRow<Order> row = new TableRow<>();
                        row.setOnMouseClicked(e -> {
                                if (!row.isEmpty())
                                        showOrderDetails(row.getItem());
                        });
                        return row;
                });
                return table;
        }

        // ============================================================
        // STATUS STYLE
        // ============================================================

        private String statusColorStyle(
                        String status) {

                switch (status) {

                        case "Delivered":
                                return "-fx-background-color: #e6f4ea;" +
                                                "-fx-text-fill: #2e7d32;";

                        case "Processing":
                                return "-fx-background-color: #fff3e0;" +
                                                "-fx-text-fill: #ef6c00;";

                        case "Shipped":
                                return "-fx-background-color: #e3f2fd;" +
                                                "-fx-text-fill: #1565c0;";

                        case "Pending":
                                return "-fx-background-color: #f5f5f5;" +
                                                "-fx-text-fill: #616161;";

                        default:
                                return "-fx-background-color: #eeeeee;" +
                                                "-fx-text-fill: #333333;";
                }
        }

        // ============================================================
        // SAMPLE ORDERS
        // ============================================================

        private ObservableList<Order> loadRecentOrders() {

                ObservableList<Order> orders = FXCollections.observableArrayList();
                try {
                        List<Order> all = orderDAO.getAllOrders();
                        int limit = Math.min(10, all.size());
                        for (int i = 0; i < limit; i++) {
                                orders.add(all.get(i));
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return orders;
        }

        // ============================================================
        // ORDER DETAILS
        // ============================================================

        private void showOrderDetails(Order order) {
                if (order == null)
                        return;
                showInfoAlert("Order Details",
                                "Order ID: " + order.getOrderId() +
                                                "\nBuyer: " + order.getBuyerName() +
                                                "\nPayment: " + order.getPaymentMethod() +
                                                "\nTotal: ₹" + String.format("%.2f", order.getTotalAmount()) +
                                                "\nStatus: " + order.getStatus());
        }

        // ============================================================
        // REVENUE SECTION
        // ============================================================

        private VBox buildRevenueSection() {

                VBox section = new VBox(12);

                section.setPadding(
                                new Insets(18));

                section.setPrefWidth(
                                380);

                section.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

                HBox header = new HBox();

                header.setAlignment(
                                Pos.CENTER_LEFT);

                Label title = new Label(
                                "Revenue Overview");

                title.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                16));

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                VBox amountBox = new VBox(2);

                amountBox.setAlignment(
                                Pos.CENTER_RIGHT);

                Label monthLabel = new Label(
                                "This Month");

                monthLabel.setStyle(
                                "-fx-text-fill: #999;" +
                                                "-fx-font-size: 11;");

                Label amountLabel = new Label(
                                "\u20B948.5 Lakh");

                amountLabel.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                16));

                amountBox.getChildren().addAll(
                                monthLabel,
                                amountLabel);

                header.getChildren().addAll(
                                title,
                                spacer,
                                amountBox);

                LineChart<String, Number> chart = buildRevenueChart();

                section.getChildren().addAll(
                                header,
                                chart);

                return section;
        }

        // ============================================================
        // REVENUE CHART
        // ============================================================

        private LineChart<String, Number> buildRevenueChart() {
                CategoryAxis xAxis = new CategoryAxis();
                NumberAxis yAxis = new NumberAxis();
                yAxis.setLabel("₹");
                LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
                chart.setLegendVisible(false);
                chart.setPrefHeight(260);
                chart.setAnimated(false);
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                List<Order> orders = orderDAO.getAllOrders();
                int shown = Math.min(7, orders.size());
                for (int i = shown - 1; i >= 0; i--) {
                        Order o = orders.get(i);
                        String label = o.getOrderDate() == null ? "Order " + (shown - i)
                                        : o.getOrderDate().toString().substring(0,
                                                        Math.min(10, o.getOrderDate().toString().length()));
                        series.getData().add(new XYChart.Data<>(label, o.getTotalAmount()));
                }
                chart.getData().add(series);
                return chart;
        }

}