package com.mainproject.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import java.util.Comparator;
import java.util.Optional;

/**
 * Product Management screen for the AgriLink admin panel.
 * Same idea as UserManagement: this class doesn't extend Application, it
 * just builds a screen and swaps it onto the Stage that AdminDashboard owns.
 * It can jump straight to Dashboard or User Management from its own sidebar,
 * and those screens can jump back here the same way.
 */
public class ProductManagement {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";

    private final Stage stage;
    private final AdminDashboard dashboard;

    private BorderPane rootLayout;
    private TableView<Product> table;
    private TextField searchField;
    private Label resultsLabel;
    private Button activeTabButton;

    private ObservableList<Product> allProducts;
    private FilteredList<Product> filteredProducts;
    private SortedList<Product> sortedProducts;

    // what the currently selected tab filters on: "All Products", "Approved",
    // "Pending" or "Rejected"
    private String currentStatusFilter = "All Products";
    private int nextProductNumber = 1008;

    public ProductManagement(Stage stage, AdminDashboard dashboard) {
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

            if (item.equals("Product Management")) {
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

    // this if-chain is what actually connects the screens together - as more
    // screens get built, they just get added here the same way
    private void handleNavClick(String pageName) {
        if (pageName.equals("Product Management")) {
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
        if (pageName.equals("Order Management")) {
            new OrderManagement(stage, dashboard).show();
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
    // Top bar: search, sort dropdown, Add Product button
    // ------------------------------------------------------------------

    private HBox buildTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(16, 25, 16, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #eaeaea; -fx-border-width: 0 0 1 0;");

        Button menuButton = new Button("\u2630");
        menuButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16;");
        menuButton.setOnAction(e -> toggleSidebar());

        searchField = new TextField();
        searchField.setPromptText("Search products, farmers...");
        searchField.setPrefWidth(320);
        searchField.setStyle("-fx-background-color: transparent;");
        searchField.textProperty().addListener((obs, oldText, newText) -> applyFilters());

        ComboBox<String> sortBox = new ComboBox<>(FXCollections.observableArrayList(
                "Newest First", "Oldest First", "Price (Low-High)", "Price (High-Low)", "Name (A-Z)"));
        sortBox.setValue("Newest First");
        sortBox.setStyle("-fx-background-color: transparent;");
        sortBox.setOnAction(e -> handleSortChange(sortBox.getValue()));

        HBox searchBox = new HBox(8, new Label("\uD83D\uDD0D"), searchField, sortBox);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(6, 15, 6, 15));
        searchBox.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 20;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addProductButton = new Button("+  Add Product");
        addProductButton.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white;"
                + "-fx-background-radius: 8; -fx-font-weight: bold;");
        addProductButton.setPadding(new Insets(10, 18, 10, 18));
        addProductButton.setOnAction(e -> handleAddProduct());

        topBar.getChildren().addAll(menuButton, searchBox, spacer, addProductButton);
        return topBar;
    }

    // ------------------------------------------------------------------
    // Center content: status tabs + table card
    // ------------------------------------------------------------------

    private VBox buildContent() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(20));

        HBox tabsBar = buildTabsBar();

        VBox tableCard = new VBox(12);
        tableCard.setPadding(new Insets(18));
        tableCard.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        table = buildProductTable();
        loadSampleProducts();

        resultsLabel = new Label();
        updateResultsLabel();

        tableCard.getChildren().addAll(table, resultsLabel);
        content.getChildren().addAll(tabsBar, tableCard);
        return content;
    }

    private HBox buildTabsBar() {
        HBox bar = new HBox(8);
        bar.setPadding(new Insets(10, 15, 10, 15));
        bar.setStyle("-fx-background-color: white; -fx-background-radius: 10;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 1);");

        // {filter key, label shown on the button}
        String[][] tabs = {
                { "All Products", "All Products" },
                { "Pending", "Pending Approval (21)" },
                { "Approved", "Approved (24,650)" },
                { "Rejected", "Rejected (120)" }
        };

        for (String[] tab : tabs) {
            String filterKey = tab[0];
            Button tabButton = new Button(tab[1]);
            tabButton.setPadding(new Insets(8, 18, 8, 18));
            tabButton.setFont(Font.font("Segoe UI", 13));

            if (filterKey.equals("All Products")) {
                activeTabButton = tabButton;
                styleActiveTab(tabButton);
            } else {
                styleInactiveTab(tabButton);
            }

            tabButton.setOnAction(e -> handleTabClick(filterKey, tabButton));
            bar.getChildren().add(tabButton);
        }
        return bar;
    }

    private void handleTabClick(String filterKey, Button clickedTab) {
        currentStatusFilter = filterKey;
        styleInactiveTab(activeTabButton);
        styleActiveTab(clickedTab);
        activeTabButton = clickedTab;
        applyFilters();
    }

    private void styleActiveTab(Button b) {
        b.setStyle("-fx-background-color: #eaf6ec; -fx-text-fill: " + GREEN + ";"
                + "-fx-background-radius: 8; -fx-font-weight: bold;");
    }

    private void styleInactiveTab(Button b) {
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: #666666; -fx-background-radius: 8;");
    }

    // ---- table ----

    private TableView<Product> buildProductTable() {
        TableView<Product> tv = new TableView<>();
        tv.setPrefHeight(320);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setPlaceholder(new Label("No products found for this filter."));

        TableColumn<Product, String> nameCol = new TableColumn<>("Product");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty || name == null || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Product product = (Product) getTableRow().getItem();
                Label iconLabel = new Label(product.getIcon());
                Label nameLabel = new Label(name);
                HBox box = new HBox(8, iconLabel, nameLabel);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
                setText(null);
            }
        });

        TableColumn<Product, String> farmerCol = new TableColumn<>("Farmer");
        farmerCol.setCellValueFactory(new PropertyValueFactory<>("farmer"));

        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, String> statusCol = new TableColumn<>("Status");
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

        TableColumn<Product, String> listedCol = new TableColumn<>("Listed On");
        listedCol.setCellValueFactory(new PropertyValueFactory<>("listedOn"));

        TableColumn<Product, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> actionCell());

        tv.getColumns().addAll(nameCol, farmerCol, categoryCol, priceCol, statusCol, listedCol, actionCol);
        return tv;
    }

    private String statusBadgeStyle(String status) {
        switch (status) {
            case "Approved":
                return "-fx-background-color: #e6f4ea; -fx-text-fill: #2e7d32;";
            case "Pending":
                return "-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00;";
            case "Rejected":
                return "-fx-background-color: #fdecea; -fx-text-fill: #c62828;";
            default:
                return "-fx-background-color: #eeeeee; -fx-text-fill: #333333;";
        }
    }

    private TableCell<Product, Void> actionCell() {
        return new TableCell<>() {
            private final Button viewButton = new Button("\uD83D\uDC41");
            private final Button editButton = new Button("\u270F");
            private final Button statusButton = new Button("\uD83D\uDD04");
            private final HBox box = new HBox(8, viewButton, editButton, statusButton);

            {
                String plainStyle = "-fx-background-color: transparent; -fx-font-size: 13;";
                viewButton.setStyle(plainStyle);
                editButton.setStyle(plainStyle);
                statusButton.setStyle(plainStyle);
                viewButton.setOnAction(e -> handleViewProduct(getTableRow().getItem()));
                editButton.setOnAction(e -> handleEditProduct(getTableRow().getItem()));
                statusButton.setOnAction(e -> handleChangeStatus(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        };
    }

    private void updateResultsLabel() {
        resultsLabel.setText("Showing " + filteredProducts.size() + " products");
        resultsLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12;");
    }

    // ------------------------------------------------------------------
    // Data loading + filtering + sorting
    // ------------------------------------------------------------------

    private void loadSampleProducts() {
        allProducts = FXCollections.observableArrayList(
                new Product("PRD1001", "\uD83C\uDF45", "Tomato (100 kg)", "Ramesh Patil", "Vegetables", "\u20B928 / kg",
                        "Approved", LocalDate.of(2025, 5, 20)),
                new Product("PRD1002", "\uD83E\uDD54", "Potato (50 kg)", "Mahesh Jadhav", "Vegetables", "\u20B925 / kg",
                        "Pending", LocalDate.of(2025, 5, 19)),
                new Product("PRD1003", "\uD83E\uDDC5", "Onion (80 kg)", "Suresh Yadav", "Vegetables", "\u20B920 / kg",
                        "Approved", LocalDate.of(2025, 5, 19)),
                new Product("PRD1004", "\uD83C\uDF3E", "Wheat (100 kg)", "Vikram Singh", "Grains", "\u20B922 / kg",
                        "Approved", LocalDate.of(2025, 5, 18)),
                new Product("PRD1005", "\uD83E\uDD6D", "Mango (50 kg)", "Anita Deshmukh", "Fruits", "\u20B940 / kg",
                        "Rejected", LocalDate.of(2025, 5, 17)),
                new Product("PRD1006", "\uD83E\uDD6C", "Cabbage (60 kg)", "Ramesh Patil", "Vegetables", "\u20B915 / kg",
                        "Approved", LocalDate.of(2025, 5, 16)));

        filteredProducts = new FilteredList<>(allProducts, product -> true);
        filteredProducts.addListener((javafx.collections.ListChangeListener<Product>) change -> {
            if (resultsLabel != null) {
                updateResultsLabel();
            }
        });

        sortedProducts = new SortedList<>(filteredProducts, Comparator.comparing(Product::getListedDate).reversed());
        table.setItems(sortedProducts);
    }

    private void applyFilters() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        filteredProducts.setPredicate(product -> {
            boolean matchesStatus = currentStatusFilter.equals("All Products")
                    || product.getStatus().equalsIgnoreCase(currentStatusFilter);
            boolean matchesSearch = query.isEmpty()
                    || product.getName().toLowerCase().contains(query)
                    || product.getFarmer().toLowerCase().contains(query);
            return matchesStatus && matchesSearch;
        });
    }

    private void handleSortChange(String option) {
        switch (option) {
            case "Newest First":
                sortedProducts.setComparator(Comparator.comparing(Product::getListedDate).reversed());
                break;
            case "Oldest First":
                sortedProducts.setComparator(Comparator.comparing(Product::getListedDate));
                break;
            case "Price (Low-High)":
                sortedProducts.setComparator(Comparator.comparingDouble(this::parsePrice));
                break;
            case "Price (High-Low)":
                sortedProducts.setComparator(Comparator.comparingDouble(this::parsePrice).reversed());
                break;
            case "Name (A-Z)":
                sortedProducts.setComparator(Comparator.comparing(Product::getName));
                break;
            default:
                break;
        }
    }

    private double parsePrice(Product product) {
        String digitsOnly = product.getPrice().replaceAll("[^0-9.]", "");
        try {
            return Double.parseDouble(digitsOnly);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String iconForCategory(String category) {
        switch (category) {
            case "Vegetables":
                return "\uD83E\uDD66";
            case "Fruits":
                return "\uD83C\uDF4E";
            case "Grains":
                return "\uD83C\uDF3E";
            default:
                return "\uD83D\uDCE6";
        }
    }

    // ------------------------------------------------------------------
    // Button actions: add / view / edit / change status
    // ------------------------------------------------------------------

    private void handleAddProduct() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");
        dialog.setHeaderText(null);

        ButtonType addButtonType = new ButtonType("Add Product", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Tomato (100 kg)");
        TextField farmerField = new TextField();
        farmerField.setPromptText("Farmer name");
        ComboBox<String> categoryBox = new ComboBox<>(
                FXCollections.observableArrayList("Vegetables", "Fruits", "Grains", "Other"));
        categoryBox.setValue("Vegetables");
        TextField priceField = new TextField();
        priceField.setPromptText("e.g. \u20B920 / kg");
        ComboBox<String> statusBox = new ComboBox<>(
                FXCollections.observableArrayList("Approved", "Pending", "Rejected"));
        statusBox.setValue("Pending");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.addRow(0, new Label("Product:"), nameField);
        grid.addRow(1, new Label("Farmer:"), farmerField);
        grid.addRow(2, new Label("Category:"), categoryBox);
        grid.addRow(3, new Label("Price:"), priceField);
        grid.addRow(4, new Label("Status:"), statusBox);
        dialog.getDialogPane().setContent(grid);

        Button addButtonNode = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButtonNode.addEventFilter(ActionEvent.ACTION, event -> {
            if (nameField.getText().isBlank() || farmerField.getText().isBlank() || priceField.getText().isBlank()) {
                showInfoAlert("Missing Information", "Please fill in product, farmer and price before adding.");
                event.consume();
            }
        });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                String id = "PRD" + nextProductNumber;
                nextProductNumber++;
                String category = categoryBox.getValue();
                return new Product(id, iconForCategory(category), nameField.getText().trim(),
                        farmerField.getText().trim(), category, priceField.getText().trim(),
                        statusBox.getValue(), LocalDate.now());
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> {
            allProducts.add(0, product);
            applyFilters();
            showInfoAlert("Product Added", product.getName() + " was added successfully.");
        });
    }

    private void handleViewProduct(Product product) {
        if (product == null) {
            return;
        }
        String details = "Product: " + product.getName()
                + "\nFarmer: " + product.getFarmer()
                + "\nCategory: " + product.getCategory()
                + "\nPrice: " + product.getPrice()
                + "\nStatus: " + product.getStatus()
                + "\nListed On: " + product.getListedOn();
        showInfoAlert("Product Details", details);
    }

    private void handleEditProduct(Product product) {
        if (product == null) {
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nameField = new TextField(product.getName());
        TextField farmerField = new TextField(product.getFarmer());
        ComboBox<String> categoryBox = new ComboBox<>(
                FXCollections.observableArrayList("Vegetables", "Fruits", "Grains", "Other"));
        categoryBox.setValue(product.getCategory());
        TextField priceField = new TextField(product.getPrice());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.addRow(0, new Label("Product:"), nameField);
        grid.addRow(1, new Label("Farmer:"), farmerField);
        grid.addRow(2, new Label("Category:"), categoryBox);
        grid.addRow(3, new Label("Price:"), priceField);
        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            product.setName(nameField.getText().trim());
            product.setFarmer(farmerField.getText().trim());
            product.setCategory(categoryBox.getValue());
            product.setIcon(iconForCategory(categoryBox.getValue()));
            product.setPrice(priceField.getText().trim());
            table.refresh();
        }
    }

    private void handleChangeStatus(Product product) {
        if (product == null) {
            return;
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(product.getStatus(), "Approved", "Pending", "Rejected");
        dialog.setTitle("Update Status");
        dialog.setHeaderText(null);
        dialog.setContentText("New status for " + product.getName() + ":");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newStatus -> {
            product.setStatus(newStatus);
            table.refresh();
            applyFilters();
        });
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

    public static class Product {
        private final String id;
        private String icon;
        private String name;
        private String farmer;
        private String category;
        private String price;
        private String status;
        private final LocalDate listedDate;

        public Product(String id, String icon, String name, String farmer, String category,
                String price, String status, LocalDate listedDate) {
            this.id = id;
            this.icon = icon;
            this.name = name;
            this.farmer = farmer;
            this.category = category;
            this.price = price;
            this.status = status;
            this.listedDate = listedDate;
        }

        public String getId() {
            return id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFarmer() {
            return farmer;
        }

        public void setFarmer(String farmer) {
            this.farmer = farmer;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDate getListedDate() {
            return listedDate;
        }

        public String getListedOn() {
            return listedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        }
    }
}