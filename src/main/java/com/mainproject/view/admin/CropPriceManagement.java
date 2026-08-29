package com.mainproject.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.time.LocalDate;
import java.util.Optional;

/**
 * AgriLink Admin - Crop Price Management screen (JavaFX, matches the
 * reference screenshot).
 *
 * Layout: same green sidebar + top bar shell as AdminDashboard, then a
 * filter bar, a "Crop Prices" table on the left, and a right-hand column
 * with a modal-price trend chart, a market-overview panel and a recent
 * alerts feed.
 *
 * Wire-up: call `new CropPriceManagement(primaryStage, this).show();` from
 * AdminDashboard.switchPage() for the "Crop Price Management" case, and
 * make AdminDashboard.switchPage(...) package/public-visible so this class
 * can hand off to the other nav items (see navigateTo() below).
 */
public class CropPriceManagement {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";

    private final Stage primaryStage;
    private final AdminDashboard dashboard;

    private final ObservableList<CropPrice> cropPriceData = loadCropPrices();
    private TableView<CropPrice> table;

    public CropPriceManagement(Stage primaryStage, AdminDashboard dashboard) {
        this.primaryStage = primaryStage;
        this.dashboard = dashboard;
    }

    public void show() {
        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + BG + ";");

        rootLayout.setLeft(buildSidebar());
        rootLayout.setTop(buildTopBar());

        ScrollPane scroll = new ScrollPane(buildContent());
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + BG + "; -fx-background-color: transparent;");
        rootLayout.setCenter(scroll);

        primaryStage.getScene().setRoot(rootLayout);
    }

    // ------------------------------------------------------------------
    // Sidebar - same nav list as AdminDashboard, this item highlighted
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
            navBox.getChildren().add(buildNavButton(item));
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
        if (label.equals("Crop Price Management")) {
            button.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 0;");
        } else {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: #d7e4d9; -fx-background-radius: 0;");
        }
        button.setOnAction(e -> navigateTo(label, button));
        return button;
    }

    // "Dashboard" and this page are handled locally; everything else is
    // routed back through AdminDashboard, which already owns that table
    private void navigateTo(String pageName, Button clickedButton) {
        if (pageName.equals("Crop Price Management")) {
            return;
        }
        if (pageName.equals("Dashboard")) {
            dashboard.showDashboard();
            return;
        }
        dashboard.switchPage(pageName, clickedButton);
    }

    // ------------------------------------------------------------------
    // Top bar
    // ------------------------------------------------------------------

    private HBox buildTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(14, 25, 14, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #eaeaea; -fx-border-width: 0 0 1 0;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search crops, markets...");
        searchField.setPrefWidth(360);
        searchField.setStyle("-fx-background-color: transparent;");
        searchField.setOnAction(e -> showInfoAlert("Search", "Searching for: \"" + searchField.getText() + "\""));

        HBox searchBox = new HBox(8, new Label("\uD83D\uDD0D"), searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(6, 15, 6, 15));
        searchBox.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 20;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        StackPane bellStack = buildBellWithBadge();

        MenuButton profileMenu = new MenuButton("Super Admin");
        profileMenu.setStyle("-fx-background-color: transparent;");
        MenuItem profileItem = new MenuItem("My Profile");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem logoutItem = new MenuItem("Logout");
        profileItem.setOnAction(e -> showInfoAlert("Profile", "Opening admin profile page."));
        settingsItem.setOnAction(e -> showInfoAlert("Settings", "Opening settings page."));
        logoutItem.setOnAction(e -> handleLogout());
        profileMenu.getItems().addAll(profileItem, settingsItem, logoutItem);

        VBox profileLabels = new VBox(0);
        Label nameLabel = new Label("Super Admin");
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        Label roleLabel = new Label("Administrator");
        roleLabel.setFont(Font.font("Segoe UI", 10));
        roleLabel.setStyle("-fx-text-fill: #888;");
        profileLabels.getChildren().addAll(nameLabel, roleLabel);

        topBar.getChildren().addAll(searchBox, spacer, bellStack, profileMenu);
        return topBar;
    }

    // small red "16" badge on the bell, matching the reference screenshot
    private StackPane buildBellWithBadge() {
        Button bellButton = new Button("\uD83D\uDD14");
        bellButton.setStyle("-fx-background-color: transparent; -fx-font-size: 15;");
        bellButton.setOnAction(e -> showInfoAlert("Notifications", "You have 16 price alerts to review."));

        Label badge = new Label("16");
        badge.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 9; "
                + "-fx-background-radius: 8; -fx-padding: 1 5 1 5;");
        StackPane.setAlignment(badge, Pos.TOP_RIGHT);

        return new StackPane(bellButton, badge);
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
    // Page content
    // ------------------------------------------------------------------

    private VBox buildContent() {
        VBox view = new VBox(18);
        view.setPadding(new Insets(20));

        view.getChildren().addAll(buildPageHeader(), buildFilterBar());

        HBox mainRow = new HBox(20);
        VBox tableSection = buildCropPriceTableSection();
        VBox sidePanel = buildSidePanel();
        sidePanel.setPrefWidth(320);
        HBox.setHgrow(tableSection, Priority.ALWAYS);
        mainRow.getChildren().addAll(tableSection, sidePanel);

        view.getChildren().add(mainRow);
        return view;
    }

    private VBox buildPageHeader() {
        VBox titleBox = new VBox(2);
        Label title = new Label("Crop Price Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        Label subtitle = new Label("Manage crop prices across different markets");
        subtitle.setStyle("-fx-text-fill: #888;");
        titleBox.getChildren().addAll(title, subtitle);
        return titleBox;
    }

    private HBox buildFilterBar() {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> cropFilter = new ComboBox<>(FXCollections.observableArrayList(
                "All Crops", "Wheat", "Rice", "Tomato", "Onion", "Potato", "Sugarcane", "Cotton", "Soybean"));
        cropFilter.setValue("All Crops");
        cropFilter.setOnAction(e -> applyFilters(cropFilter.getValue()));

        ComboBox<String> marketFilter = new ComboBox<>(FXCollections.observableArrayList(
                "All Markets", "Nashik Market", "Pune Market", "Solapur Market",
                "Kolhapur Market", "Amravati Market", "Akola Market"));
        marketFilter.setValue("All Markets");

        DatePicker datePicker = new DatePicker(LocalDate.of(2025, 5, 29));

        Button updateButton = new Button("Update Prices");
        updateButton.setStyle("-fx-background-color: white; -fx-border-color: #d0d5db; -fx-border-radius: 6; "
                + "-fx-background-radius: 6; -fx-padding: 8 16 8 16;");
        updateButton.setOnAction(e -> showInfoAlert("Update Prices",
                "Refreshing prices for " + cropFilter.getValue() + " in " + marketFilter.getValue()
                        + " as of " + datePicker.getValue() + "."));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addCropPriceButton = new Button("+  Add Crop Price");
        addCropPriceButton.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; "
                + "-fx-background-radius: 6; -fx-padding: 9 18 9 18; -fx-font-weight: bold;");
        addCropPriceButton.setOnAction(e -> handleAddCropPrice());

        bar.getChildren().addAll(cropFilter, marketFilter, datePicker, updateButton, spacer, addCropPriceButton);
        return bar;
    }

    private void applyFilters(String crop) {
        if (crop == null || crop.equals("All Crops")) {
            table.setItems(cropPriceData);
        } else {
            ObservableList<CropPrice> filtered = FXCollections.observableArrayList();
            for (CropPrice cp : cropPriceData) {
                if (cp.getCrop().equalsIgnoreCase(crop)) {
                    filtered.add(cp);
                }
            }
            table.setItems(filtered);
        }
    }

    private void handleAddCropPrice() {
        Dialog<CropPrice> dialog = new Dialog<>();
        dialog.setTitle("Add Crop Price");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField cropField = new TextField();
        cropField.setPromptText("Crop name");
        TextField marketField = new TextField();
        marketField.setPromptText("Market");
        TextField minField = new TextField();
        minField.setPromptText("Min price");
        TextField maxField = new TextField();
        maxField.setPromptText("Max price");
        TextField modalField = new TextField();
        modalField.setPromptText("Modal price");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.addRow(0, new Label("Crop:"), cropField);
        grid.addRow(1, new Label("Market:"), marketField);
        grid.addRow(2, new Label("Min Price:"), minField);
        grid.addRow(3, new Label("Max Price:"), maxField);
        grid.addRow(4, new Label("Modal Price:"), modalField);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK && !cropField.getText().isBlank()) {
                return new CropPrice(iconFor(cropField.getText()), cropField.getText(), marketField.getText(),
                        minField.getText(), maxField.getText(), modalField.getText(),
                        "0.0%", LocalDate.now().toString());
            }
            return null;
        });

        Optional<CropPrice> result = dialog.showAndWait();
        result.ifPresent(cropPrice -> {
            cropPriceData.add(0, cropPrice);
            showInfoAlert("Added", "New crop price entry added for " + cropPrice.getCrop() + ".");
        });
    }

    private String iconFor(String cropName) {
        return "\uD83C\uDF3E"; // generic sheaf-of-rice icon for anything user-entered
    }

    // ------------------------------------------------------------------
    // Crop price table
    // ------------------------------------------------------------------

    private VBox buildCropPriceTableSection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(18));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label title = new Label("Crop Prices");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        table = buildCropPriceTable();

        section.getChildren().addAll(title, table);
        return section;
    }

    private TableView<CropPrice> buildCropPriceTable() {
        TableView<CropPrice> tv = new TableView<>();
        tv.setPrefHeight(430);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<CropPrice, String> cropCol = new TableColumn<>("Crop");
        cropCol.setCellValueFactory(new PropertyValueFactory<>("crop"));
        cropCol.setCellFactory(col -> new TableCell<CropPrice, String>() {
            @Override
            protected void updateItem(String crop, boolean empty) {
                super.updateItem(crop, empty);
                if (empty || crop == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                CropPrice row = getTableView().getItems().get(getIndex());
                HBox box = new HBox(8, new Label(row.getIcon()), new Label(crop));
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
                setText(null);
            }
        });

        TableColumn<CropPrice, String> marketCol = new TableColumn<>("Market");
        marketCol.setCellValueFactory(new PropertyValueFactory<>("market"));

        TableColumn<CropPrice, String> minCol = new TableColumn<>("Min Price\n(\u20B9/Quintal)");
        minCol.setCellValueFactory(new PropertyValueFactory<>("minPrice"));

        TableColumn<CropPrice, String> maxCol = new TableColumn<>("Max Price\n(\u20B9/Quintal)");
        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxPrice"));

        TableColumn<CropPrice, String> modalCol = new TableColumn<>("Modal Price\n(\u20B9/Quintal)");
        modalCol.setCellValueFactory(new PropertyValueFactory<>("modalPrice"));

        TableColumn<CropPrice, String> changeCol = new TableColumn<>("Change");
        changeCol.setCellValueFactory(new PropertyValueFactory<>("change"));
        changeCol.setCellFactory(col -> new TableCell<CropPrice, String>() {
            @Override
            protected void updateItem(String change, boolean empty) {
                super.updateItem(change, empty);
                if (empty || change == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(change);
                setStyle(change.startsWith("-")
                        ? "-fx-text-fill: #d32f2f; -fx-font-weight: bold;"
                        : "-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            }
        });

        TableColumn<CropPrice, String> updatedCol = new TableColumn<>("Updated On");
        updatedCol.setCellValueFactory(new PropertyValueFactory<>("updatedOn"));

        TableColumn<CropPrice, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<CropPrice, Void>() {
            private final Button editButton = new Button("\u270E");
            {
                editButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                editButton.setOnAction(e -> handleEditCropPrice(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });

        tv.getColumns().addAll(cropCol, marketCol, minCol, maxCol, modalCol, changeCol, updatedCol, actionCol);
        tv.setItems(cropPriceData);
        return tv;
    }

    private void handleEditCropPrice(CropPrice row) {
        TextInputDialog dialog = new TextInputDialog(row.getModalPrice());
        dialog.setTitle("Edit Modal Price");
        dialog.setHeaderText(null);
        dialog.setContentText(row.getCrop() + " (" + row.getMarket() + ") \u2013 new modal price:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newPrice -> {
            row.setModalPrice(newPrice);
            row.setUpdatedOn(LocalDate.now().toString());
            table.refresh();
            showInfoAlert("Updated", "Modal price for " + row.getCrop() + " updated to \u20B9" + newPrice + ".");
        });
    }

    private static ObservableList<CropPrice> loadCropPrices() {
        ObservableList<CropPrice> list = FXCollections.observableArrayList();
        list.add(new CropPrice("\uD83C\uDF3E", "Wheat", "Nashik Market", "2,150", "2,350", "2,250", "+2.2%",
                "29 May 2025"));
        list.add(new CropPrice("\uD83C\uDF3E", "Rice", "Pune Market", "2,800", "3,200", "3,000", "+1.8%",
                "29 May 2025"));
        list.add(new CropPrice("\uD83C\uDF45", "Tomato", "Nashik Market", "1,200", "1,800", "1,500", "-3.2%",
                "29 May 2025"));
        list.add(new CropPrice("\uD83E\uDDC5", "Onion", "Pune Market", "1,000", "1,400", "1,200", "+4.5%",
                "29 May 2025"));
        list.add(new CropPrice("\uD83E\uDD54", "Potato", "Solapur Market", "1,000", "1,400", "1,200", "+2.6%",
                "29 May 2025"));
        list.add(new CropPrice("\uD83C\uDF3E", "Sugarcane", "Kolhapur Market", "280", "320", "300", "+0.8%",
                "29 May 2025"));
        list.add(new CropPrice("\u2601", "Cotton", "Amravati Market", "6,200", "6,900", "6,500", "+3.4%",
                "29 May 2025"));
        list.add(new CropPrice("\uD83C\uDF31", "Soybean", "Akola Market", "4,600", "5,000", "4,800", "-1.0%",
                "29 May 2025"));
        return list;
    }

    // ------------------------------------------------------------------
    // Right column: price trend chart + market overview + recent alerts
    // ------------------------------------------------------------------

    private VBox buildSidePanel() {
        VBox panel = new VBox(20);
        panel.getChildren().addAll(buildPriceTrendSection(), buildMarketOverviewSection(), buildRecentAlertsSection());
        return panel;
    }

    private VBox buildPriceTrendSection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(18));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Price Trend (Modal Price)");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        header.getChildren().add(title);

        HBox controls = new HBox(8);
        ComboBox<String> cropPicker = new ComboBox<>(FXCollections.observableArrayList(
                "Tomato", "Wheat", "Rice", "Onion", "Potato", "Sugarcane", "Cotton", "Soybean"));
        cropPicker.setValue("Tomato");
        ComboBox<String> rangePicker = new ComboBox<>(FXCollections.observableArrayList(
                "7 Days", "14 Days", "30 Days"));
        rangePicker.setValue("7 Days");
        controls.getChildren().addAll(cropPicker, rangePicker);

        LineChart<String, Number> chart = buildTrendChart();
        cropPicker.setOnAction(e -> updateTrendChart(chart, cropPicker.getValue()));

        section.getChildren().addAll(header, controls, chart);
        return section;
    }

    private LineChart<String, Number> buildTrendChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("\u20B9 / Quintal");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setPrefHeight(200);
        chart.setCreateSymbols(true);
        chart.setAnimated(false);
        chart.getData().add(trendSeriesFor("Tomato"));
        return chart;
    }

    private void updateTrendChart(LineChart<String, Number> chart, String crop) {
        chart.getData().clear();
        chart.getData().add(trendSeriesFor(crop));
    }

    // canned 7-day modal-price series per crop, close to the reference chart's
    // Tomato line (23-29 May); swap for a real query against price history later
    private XYChart.Series<String, Number> trendSeriesFor(String crop) {
        String[] days = { "23 May", "24 May", "25 May", "26 May", "27 May", "28 May", "29 May" };
        double[] values;
        switch (crop) {
            case "Wheat":
                values = new double[] { 2180, 2200, 2190, 2210, 2230, 2240, 2250 };
                break;
            case "Rice":
                values = new double[] { 2950, 2980, 2960, 2970, 2990, 3010, 3000 };
                break;
            case "Onion":
                values = new double[] { 1120, 1150, 1140, 1160, 1180, 1190, 1200 };
                break;
            default: // Tomato - matches the reference screenshot's dip-then-recover shape
                values = new double[] { 1750, 1700, 1650, 1580, 1550, 1520, 1500 };
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(crop);
        for (int i = 0; i < days.length; i++) {
            series.getData().add(new XYChart.Data<>(days[i], values[i]));
        }
        return series;
    }

    private VBox buildMarketOverviewSection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(18));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label title = new Label("Market Overview");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        VBox rows = new VBox(10);
        rows.getChildren().addAll(
                overviewRow("\uD83C\uDFEA", "Total Markets", "24"),
                overviewRow("\uD83C\uDF3E", "Crops Tracked", "36"),
                overviewRow("\u2705", "Price Updates Today", "156"),
                overviewRow("\u26A0", "Active Alerts", "8"));

        section.getChildren().addAll(title, rows);
        return section;
    }

    private HBox overviewRow(String icon, String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-background-color: #eaf6ec; -fx-background-radius: 50; -fx-padding: 5 8 5 8;");

        Label textLabel = new Label(label);
        textLabel.setStyle("-fx-text-fill: #555;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        row.getChildren().addAll(iconLabel, textLabel, spacer, valueLabel);
        return row;
    }

    private VBox buildRecentAlertsSection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(18));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label title = new Label("Recent Alerts");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        VBox alertsBox = new VBox(10);
        alertsBox.getChildren().addAll(
                alertRow("Tomato price dropped in Nashik Market", "29 May 2025, 10:30 AM"),
                alertRow("Onion price increased in Pune Market", "29 May 2025, 09:15 AM"));

        Hyperlink viewAll = new Hyperlink("View All Alerts");
        viewAll.setOnAction(e -> showInfoAlert("All Alerts", "Opening the full alerts list."));

        section.getChildren().addAll(title, alertsBox, viewAll);
        return section;
    }

    private HBox alertRow(String message, String timestamp) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.TOP_LEFT);

        Label icon = new Label("\u2713");
        icon.setStyle("-fx-text-fill: #2e7d32; -fx-background-color: #e6f4ea; -fx-background-radius: 50; "
                + "-fx-padding: 2 6 2 6; -fx-font-size: 10;");

        VBox textBox = new VBox(2);
        Label msgLabel = new Label(message);
        msgLabel.setWrapText(true);
        msgLabel.setFont(Font.font("Segoe UI", 12));
        Label timeLabel = new Label(timestamp);
        timeLabel.setFont(Font.font("Segoe UI", 10));
        timeLabel.setStyle("-fx-text-fill: #999;");
        textBox.getChildren().addAll(msgLabel, timeLabel);

        row.getChildren().addAll(icon, textBox);
        return row;
    }

    // ------------------------------------------------------------------
    // Row model for the crop price table
    // ------------------------------------------------------------------

    public static class CropPrice {
        private final String icon;
        private final String crop;
        private final String market;
        private final String minPrice;
        private final String maxPrice;
        private String modalPrice;
        private final String change;
        private String updatedOn;

        public CropPrice(String icon, String crop, String market, String minPrice, String maxPrice,
                String modalPrice, String change, String updatedOn) {
            this.icon = icon;
            this.crop = crop;
            this.market = market;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.modalPrice = modalPrice;
            this.change = change;
            this.updatedOn = updatedOn;
        }

        public String getIcon() {
            return icon;
        }

        public String getCrop() {
            return crop;
        }

        public String getMarket() {
            return market;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public String getModalPrice() {
            return modalPrice;
        }

        public void setModalPrice(String modalPrice) {
            this.modalPrice = modalPrice;
        }

        public String getChange() {
            return change;
        }

        public String getUpdatedOn() {
            return updatedOn;
        }

        public void setUpdatedOn(String updatedOn) {
            this.updatedOn = updatedOn;
        }
    }
}