package com.mainproject.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

import java.util.Optional;

/**
 * Equipment Management screen for the AgriLink admin panel.
 * Same pattern as the other screens - builds itself onto the shared Stage
 * and knows how to jump to every other screen that already exists.
 */
public class EquipmentManagement {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";

    private final Stage stage;
    private final AdminDashboard dashboard;

    private BorderPane rootLayout;
    private TableView<Equipment> table;
    private Label resultsLabel;
    private Button activeTabButton;

    private ObservableList<Equipment> allEquipment;
    private FilteredList<Equipment> filteredEquipment;

    // filter key of the active tab: "All Equipment", "Pending", "Approved" or
    // "Rejected"
    private String currentStatusFilter = "All Equipment";
    private int nextEquipmentNumber = 1006;

    public EquipmentManagement(Stage stage, AdminDashboard dashboard) {
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

            if (item.equals("Equipment Management")) {
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

    // grows by one branch every time a new screen gets built - same pattern as the
    // other screens
    private void handleNavClick(String pageName) {
        if (pageName.equals("Equipment Management")) {
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
        if (pageName.equals("Order Management")) {
            new OrderManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Live Marketplace")) {
            new LiveMarketplace(stage, dashboard).show();
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

        Label title = new Label("Equipment Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        topBar.getChildren().addAll(menuButton, title);
        return topBar;
    }

    // ------------------------------------------------------------------
    // Center content
    // ------------------------------------------------------------------

    private VBox buildContent() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(20));

        HBox tabsBar = buildTabsBar();

        VBox tableCard = new VBox(12);
        tableCard.setPadding(new Insets(18));
        tableCard.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        table = buildEquipmentTable();
        loadSampleEquipment();

        resultsLabel = new Label();
        updateResultsLabel();

        tableCard.getChildren().addAll(table, resultsLabel);
        content.getChildren().addAll(tabsBar, tableCard);
        return content;
    }

    // tabs on the left, "+ Add Equipment" button on the right - one row, like the
    // screenshot
    private HBox buildTabsBar() {
        HBox bar = new HBox(8);
        bar.setPadding(new Insets(10, 15, 10, 15));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: white; -fx-background-radius: 10;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 1);");

        // {filter key, label shown on the button}
        String[][] tabs = {
                { "All Equipment", "All Equipment" },
                { "Pending", "Pending Approval (6)" },
                { "Approved", "Approved (1,240)" },
                { "Rejected", "Rejected (18)" }
        };

        for (String[] tab : tabs) {
            String filterKey = tab[0];
            Button tabButton = new Button(tab[1]);
            tabButton.setPadding(new Insets(8, 18, 8, 18));
            tabButton.setFont(Font.font("Segoe UI", 13));

            if (filterKey.equals(currentStatusFilter)) {
                activeTabButton = tabButton;
                styleActiveTab(tabButton);
            } else {
                styleInactiveTab(tabButton);
            }

            tabButton.setOnAction(e -> handleTabClick(filterKey, tabButton));
            bar.getChildren().add(tabButton);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addEquipmentButton = new Button("+  Add Equipment");
        addEquipmentButton.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white;"
                + "-fx-background-radius: 8; -fx-font-weight: bold;");
        addEquipmentButton.setPadding(new Insets(10, 18, 10, 18));
        addEquipmentButton.setOnAction(e -> handleAddEquipment());

        bar.getChildren().addAll(spacer, addEquipmentButton);
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

    private TableView<Equipment> buildEquipmentTable() {
        TableView<Equipment> tv = new TableView<>();
        tv.setPrefHeight(340);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setPlaceholder(new Label("No equipment found for this filter."));

        TableColumn<Equipment, String> nameCol = new TableColumn<>("Equipment");
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
                Equipment equipment = (Equipment) getTableRow().getItem();
                Label icon = new Label(equipment.getIcon());
                icon.setMinSize(36, 36);
                icon.setMaxSize(36, 36);
                icon.setAlignment(Pos.CENTER);
                icon.setStyle("-fx-background-color: #eaf6ec; -fx-background-radius: 8; -fx-font-size: 18;");
                Label nameLabel = new Label(name);
                nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
                HBox box = new HBox(10, icon, nameLabel);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
                setText(null);
            }
        });

        TableColumn<Equipment, String> ownerCol = new TableColumn<>("Owner");
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("owner"));

        TableColumn<Equipment, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Equipment, String> priceCol = new TableColumn<>("Price/Day");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("priceDisplay"));

        TableColumn<Equipment, String> statusCol = new TableColumn<>("Status");
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

        TableColumn<Equipment, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Equipment, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> actionCell());

        tv.getColumns().addAll(nameCol, ownerCol, categoryCol, priceCol, statusCol, locationCol, actionCol);
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

    private TableCell<Equipment, Void> actionCell() {
        return new TableCell<>() {
            private final Button viewButton = new Button("\uD83D\uDC41");
            private final Button editButton = new Button("\u270F");
            private final Button statusButton = new Button("\uD83D\uDD52");
            private final HBox box = new HBox(8, viewButton, editButton, statusButton);

            {
                String plainStyle = "-fx-background-color: transparent; -fx-font-size: 13;";
                viewButton.setStyle(plainStyle);
                editButton.setStyle(plainStyle);
                statusButton.setStyle(plainStyle);
                viewButton.setOnAction(e -> handleViewEquipment(getTableRow().getItem()));
                editButton.setOnAction(e -> handleEditEquipment(getTableRow().getItem()));
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
        resultsLabel.setText("Showing " + filteredEquipment.size() + " equipment listings");
        resultsLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12;");
    }

    // ------------------------------------------------------------------
    // Data loading + filtering
    // ------------------------------------------------------------------

    private void loadSampleEquipment() {
        allEquipment = FXCollections.observableArrayList(
                new Equipment("EQP1001", "\uD83D\uDE9C", "John Deere Tractor", "Ramesh Patil", "Tractor", 2500,
                        "Approved", "Nashik, MH"),
                new Equipment("EQP1002", "\uD83D\uDE9C", "Rotavator", "Mahesh Jadhav", "Tillage", 800, "Pending",
                        "Pune, MH"),
                new Equipment("EQP1003", "\uD83D\uDE9C", "Seed Drill", "Suresh Yadav", "Sowing", 700, "Approved",
                        "Solapur, MH"),
                new Equipment("EQP1004", "\uD83D\uDE9C", "Sprayer Pump", "Anita Deshmukh", "Irrigation", 600,
                        "Approved", "Nagpur, MH"),
                new Equipment("EQP1005", "\uD83D\uDE9C", "Harvester", "Vikram Singh", "Harvesting", 3500, "Approved",
                        "Amravati, MH"));

        filteredEquipment = new FilteredList<>(allEquipment, equipment -> true);
        filteredEquipment.addListener((ListChangeListener<Equipment>) change -> {
            if (resultsLabel != null) {
                updateResultsLabel();
            }
        });

        table.setItems(filteredEquipment);
    }

    private void applyFilters() {
        filteredEquipment.setPredicate(equipment -> currentStatusFilter.equals("All Equipment")
                || equipment.getStatus().equals(currentStatusFilter));
    }

    private String iconForCategory(String category) {
        // every sample row in the screenshot uses a tractor-style thumbnail, so this
        // stays a single icon for now - easy to branch out per category later
        return "\uD83D\uDE9C";
    }

    // ------------------------------------------------------------------
    // Button actions: add / view / edit / change status
    // ------------------------------------------------------------------

    private void handleAddEquipment() {
        Dialog<Equipment> dialog = new Dialog<>();
        dialog.setTitle("Add New Equipment");
        dialog.setHeaderText(null);

        ButtonType addButtonType = new ButtonType("Add Equipment", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. John Deere Tractor");
        TextField ownerField = new TextField();
        ownerField.setPromptText("Owner name");
        ComboBox<String> categoryBox = new ComboBox<>(FXCollections.observableArrayList(
                "Tractor", "Tillage", "Sowing", "Irrigation", "Harvesting", "Other"));
        categoryBox.setValue("Tractor");
        TextField priceField = new TextField();
        priceField.setPromptText("Price per day, e.g. 2500");
        TextField locationField = new TextField();
        locationField.setPromptText("e.g. Nashik, MH");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.addRow(0, new Label("Equipment:"), nameField);
        grid.addRow(1, new Label("Owner:"), ownerField);
        grid.addRow(2, new Label("Category:"), categoryBox);
        grid.addRow(3, new Label("Price/Day:"), priceField);
        grid.addRow(4, new Label("Location:"), locationField);
        dialog.getDialogPane().setContent(grid);

        Button addButtonNode = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButtonNode.addEventFilter(ActionEvent.ACTION, event -> {
            if (nameField.getText().isBlank() || ownerField.getText().isBlank() || priceField.getText().isBlank()) {
                showInfoAlert("Missing Information", "Please fill in equipment name, owner and price before adding.");
                event.consume();
            }
        });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                String id = "EQP" + nextEquipmentNumber;
                nextEquipmentNumber++;
                double price = parseOrZero(priceField.getText());
                return new Equipment(id, iconForCategory(categoryBox.getValue()), nameField.getText().trim(),
                        ownerField.getText().trim(), categoryBox.getValue(), price, "Pending",
                        locationField.getText().trim());
            }
            return null;
        });

        Optional<Equipment> result = dialog.showAndWait();
        result.ifPresent(equipment -> {
            allEquipment.add(0, equipment);
            applyFilters();
            showInfoAlert("Equipment Added", equipment.getName() + " was added and is awaiting approval.");
        });
    }

    private double parseOrZero(String text) {
        try {
            return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void handleViewEquipment(Equipment equipment) {
        if (equipment == null) {
            return;
        }
        String details = "Equipment: " + equipment.getName()
                + "\nOwner: " + equipment.getOwner()
                + "\nCategory: " + equipment.getCategory()
                + "\nPrice/Day: " + equipment.getPriceDisplay()
                + "\nStatus: " + equipment.getStatus()
                + "\nLocation: " + equipment.getLocation();
        showInfoAlert("Equipment Details", details);
    }

    private void handleEditEquipment(Equipment equipment) {
        if (equipment == null) {
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Equipment");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nameField = new TextField(equipment.getName());
        TextField ownerField = new TextField(equipment.getOwner());
        ComboBox<String> categoryBox = new ComboBox<>(FXCollections.observableArrayList(
                "Tractor", "Tillage", "Sowing", "Irrigation", "Harvesting", "Other"));
        categoryBox.setValue(equipment.getCategory());
        TextField priceField = new TextField(String.valueOf((int) equipment.getPricePerDay()));
        TextField locationField = new TextField(equipment.getLocation());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.addRow(0, new Label("Equipment:"), nameField);
        grid.addRow(1, new Label("Owner:"), ownerField);
        grid.addRow(2, new Label("Category:"), categoryBox);
        grid.addRow(3, new Label("Price/Day:"), priceField);
        grid.addRow(4, new Label("Location:"), locationField);
        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            equipment.setName(nameField.getText().trim());
            equipment.setOwner(ownerField.getText().trim());
            equipment.setCategory(categoryBox.getValue());
            equipment.setPricePerDay(parseOrZero(priceField.getText()));
            equipment.setLocation(locationField.getText().trim());
            table.refresh();
        }
    }

    private void handleChangeStatus(Equipment equipment) {
        if (equipment == null) {
            return;
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(equipment.getStatus(), "Approved", "Pending", "Rejected");
        dialog.setTitle("Update Status");
        dialog.setHeaderText(null);
        dialog.setContentText("New status for " + equipment.getName() + ":");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newStatus -> {
            equipment.setStatus(newStatus);
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

    public static class Equipment {
        private final String id;
        private final String icon;
        private String name;
        private String owner;
        private String category;
        private double pricePerDay;
        private String status;
        private String location;

        public Equipment(String id, String icon, String name, String owner, String category,
                double pricePerDay, String status, String location) {
            this.id = id;
            this.icon = icon;
            this.name = name;
            this.owner = owner;
            this.category = category;
            this.pricePerDay = pricePerDay;
            this.status = status;
            this.location = location;
        }

        public String getId() {
            return id;
        }

        public String getIcon() {
            return icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public double getPricePerDay() {
            return pricePerDay;
        }

        public void setPricePerDay(double pricePerDay) {
            this.pricePerDay = pricePerDay;
        }

        public String getPriceDisplay() {
            return "\u20B9" + String.format("%,.0f", pricePerDay);
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}