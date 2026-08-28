package com.mainproject.view.admin;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * AgriLink Admin Dashboard - Analytics & Reports screen.
 * Java 17 / JavaFX 21.
 *
 * Matches the reference mockup: four metric cards with an area-chart
 * sparkline built into each one, a donut chart with a side legend for
 * category share, and ranked lists for top products and top farmers.
 *
 * Nothing here talks to a real backend - every action (range switch,
 * export, row click, nav click) updates the on-screen labels or pops an
 * alert so the screen stays fully clickable end to end.
 */
public class AnalyticsReports {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";
    private static final String CARD_STYLE = "-fx-background-color: white; -fx-background-radius: 12;"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);";

    private static final String[] NAV_ITEMS = {
            "Dashboard", "User Management", "Farmer Verification",
            "Product Management", "Order Management", "Live Marketplace",
            "Equipment Management", "Analytics & Reports", "Crop Price Management",
            "AI & Smart Tools", "Notifications", "Content Management",
            "Feedback & Reviews", "Reports & Complaints", "Payment Management"
    };

    private final Stage primaryStage;
    private final AdminDashboard dashboard;

    private BorderPane rootLayout;
    private Button activeNavButton;
    private ToggleGroup rangeGroup;

    // kept around so the range tabs can push fresh numbers into the cards
    private MetricCard userCard;
    private MetricCard farmerCard;
    private MetricCard ordersCard;
    private MetricCard revenueCard;

    public AnalyticsReports(Stage primaryStage, AdminDashboard dashboard) {
        this.primaryStage = primaryStage;
        this.dashboard = dashboard;
    }

    // builds the whole screen and swaps it onto the shared stage
    public void show() {
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + BG + ";");

        rootLayout.setLeft(buildSidebar());
        rootLayout.setTop(buildTopBar());

        ScrollPane scroller = new ScrollPane(buildContent());
        scroller.setFitToWidth(true);
        scroller.setStyle("-fx-background-color: transparent;");
        rootLayout.setCenter(scroller);

        primaryStage.getScene().setRoot(rootLayout);

        // "This Month" is selected by default, so load its numbers straight away
        applyRangeData(buildRangeData("This Month"));
    }

    // ------------------------------------------------------------------
    // Sidebar (same nav list as the dashboard, "Analytics & Reports" active)
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

        VBox navBox = new VBox(1);
        for (String item : NAV_ITEMS) {
            Button navButton = buildNavButton(item);
            navBox.getChildren().add(navButton);
            if (item.equals("Analytics & Reports")) {
                activeNavButton = navButton;
                styleActiveButton(navButton);
            }
        }

        ScrollPane navScroll = new ScrollPane(navBox);
        navScroll.setFitToWidth(true);
        navScroll.setStyle("-fx-background-color: transparent; -fx-background: " + GREEN_DARK + ";");
        VBox.setVgrow(navScroll, Priority.ALWAYS);

        sidebar.getChildren().addAll(logoBox, navScroll);
        return sidebar;
    }

    private Button buildNavButton(String label) {
        Button button = new Button(label);
        button.setPrefWidth(228);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(11, 20, 11, 20));
        button.setFont(Font.font("Segoe UI", 13));
        styleInactiveButton(button);
        button.setOnAction(e -> navigateTo(label, button));
        return button;
    }

    private void styleActiveButton(Button button) {
        button.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 0;");
    }

    private void styleInactiveButton(Button button) {
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #d7e4d9; -fx-background-radius: 0;");
    }

    // routes a sidebar click - full screens hand off to their own class,
    // everything else that isn't built yet just says so
    private void navigateTo(String pageName, Button clickedButton) {
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
            // already here, just make sure the highlight is on this button
            if (activeNavButton != null) {
                styleInactiveButton(activeNavButton);
            }
            styleActiveButton(clickedButton);
            activeNavButton = clickedButton;
            return;
        }

        showInfoAlert(pageName,
                "This section hasn't been built yet - only Dashboard and Analytics & Reports have full content so far.");
    }


    // Top bar (identical to AdminDashboard's, kept consistent across screens)


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

    private void handleSearch(String query) {
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
    // Page content
    // ------------------------------------------------------------------

    private VBox buildContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getChildren().addAll(buildPageHeader(), buildStatsRow(), buildBottomRow());
        return content;
    }

    // page title + Today/This Week/This Month/This Year tabs + Export Report
    private HBox buildPageHeader() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Analytics & Reports");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox rangeTabs = buildRangeTabs();

        Button exportButton = new Button("\u2913  Export Report");
        exportButton.setStyle("-fx-background-color: transparent; -fx-text-fill: " + GREEN
                + "; -fx-font-weight: bold; -fx-cursor: hand;");
        exportButton.setOnAction(e -> handleExportReport());

        header.getChildren().addAll(title, spacer, rangeTabs, exportButton);
        return header;
    }

    private HBox buildRangeTabs() {
        HBox tabs = new HBox(4);
        tabs.setAlignment(Pos.CENTER_LEFT);
        tabs.setPadding(new Insets(4));
        tabs.setStyle(
                "-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #e5e5e5; -fx-border-radius: 8;");

        rangeGroup = new ToggleGroup();
        String[] ranges = { "Today", "This Week", "This Month", "This Year" };
        for (String range : ranges) {
            ToggleButton tab = new ToggleButton(range);
            tab.setToggleGroup(rangeGroup);
            tab.setUserData(range);
            styleRangeTab(tab, false);
            tab.selectedProperty().addListener((obs, wasSelected, isSelected) -> styleRangeTab(tab, isSelected));
            if (range.equals("This Month")) {
                tab.setSelected(true);
            }
            tabs.getChildren().add(tab);
        }

        rangeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                // keep at least one tab selected instead of letting the group go empty
                oldToggle.setSelected(true);
                return;
            }
            applyRangeData(buildRangeData((String) newToggle.getUserData()));
        });

        return tabs;
    }

    private void styleRangeTab(ToggleButton tab, boolean selected) {
        tab.setFont(Font.font("Segoe UI", 12));
        if (selected) {
            tab.setStyle("-fx-background-color: " + GREEN
                    + "; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 6 14 6 14;");
        } else {
            tab.setStyle(
                    "-fx-background-color: transparent; -fx-text-fill: #666; -fx-background-radius: 6; -fx-padding: 6 14 6 14;");
        }
    }

    private void handleExportReport() {
        String range = rangeGroup.getSelectedToggle() != null
                ? (String) rangeGroup.getSelectedToggle().getUserData()
                : "This Month";
        showInfoAlert("Export Report",
                "Preparing a PDF report for \"" + range + "\". It will be saved to your downloads folder.");
    }

    // ------------------------------------------------------------------
    // Four metric cards, each with its own little area-chart sparkline
    // ------------------------------------------------------------------

    private HBox buildStatsRow() {
        HBox row = new HBox(20);

        userCard = createMetricCard("User Growth", GREEN);
        farmerCard = createMetricCard("Farmer Growth", "#1565c0");
        ordersCard = createMetricCard("Orders", "#ef6c00");
        revenueCard = createMetricCard("Revenue", "#6a1b9a");

        row.getChildren().addAll(userCard.view, farmerCard.view, ordersCard.view, revenueCard.view);
        for (Node card : row.getChildren()) {
            HBox.setHgrow(card, Priority.ALWAYS);
        }
        return row;
    }

    // holds the pieces of one metric card that need to change when the range
    // changes
    private static class MetricCard {
        VBox view;
        Label valueLabel;
        Label growthLabel;
        XYChart.Series<String, Number> sparkSeries;
    }

    private MetricCard createMetricCard(String title, String accentColor) {
        MetricCard card = new MetricCard();

        VBox box = new VBox(4);
        box.setPadding(new Insets(16));
        box.setPrefWidth(250);
        box.setStyle(CARD_STYLE);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", 12));
        titleLabel.setStyle("-fx-text-fill: #888;");

        card.valueLabel = new Label("--");
        card.valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        card.growthLabel = new Label();
        card.growthLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));

        card.sparkSeries = new XYChart.Series<>();
        AreaChart<String, Number> sparkline = buildSparkline(accentColor, card.sparkSeries);
        VBox.setVgrow(sparkline, Priority.ALWAYS);

        box.getChildren().addAll(titleLabel, card.valueLabel, card.growthLabel, sparkline);
        card.view = box;
        return card;
    }

    // a tiny axis-less area chart used as the sparkline inside a metric card
    private AreaChart<String, Number> buildSparkline(String color, XYChart.Series<String, Number> series) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setVisible(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setOpacity(0);
        yAxis.setVisible(false);
        yAxis.setTickLabelsVisible(false);
        yAxis.setOpacity(0);

        AreaChart<String, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setCreateSymbols(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.setHorizontalZeroLineVisible(false);
        chart.setVerticalZeroLineVisible(false);
        chart.setPrefHeight(70);
        chart.setStyle("-fx-background-color: transparent;");
        chart.getData().add(series);

        // the series Group already has its line/fill child nodes the moment it's
        // added, so this is safe to style right away instead of waiting to show
        Node seriesNode = series.getNode();
        if (seriesNode != null) {
            Node line = seriesNode.lookup(".chart-series-area-line");
            Node fill = seriesNode.lookup(".chart-series-area-fill");
            if (line != null) {
                line.setStyle("-fx-stroke: " + color + "; -fx-stroke-width: 2;");
            }
            if (fill != null) {
                fill.setStyle("-fx-fill: linear-gradient(to bottom, " + color + "66, " + color + "05);");
            }
        }
        return chart;
    }

    // ------------------------------------------------------------------
    // Bottom row: categories donut + top products + top farmers
    // ------------------------------------------------------------------

    private HBox buildBottomRow() {
        HBox row = new HBox(20);

        row.getChildren().addAll(buildCategoriesPanel(), buildProductsPanel(), buildFarmersPanel());
        for (Node panel : row.getChildren()) {
            HBox.setHgrow(panel, Priority.ALWAYS);
        }
        return row;
    }

    private VBox panelShell(String title) {
        VBox panel = new VBox(14);
        panel.setPadding(new Insets(18));
        panel.setPrefWidth(300);
        panel.setStyle(CARD_STYLE);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        panel.getChildren().add(titleLabel);
        return panel;
    }

    // ---- top categories: donut + a legend list built by hand so the % shows ----

    private record CategoryShare(String name, int percent, String color) {
    }

    private VBox buildCategoriesPanel() {
        VBox panel = panelShell("Top Categories");

        CategoryShare[] shares = {
                new CategoryShare("Vegetables", 45, "#3b82f6"),
                new CategoryShare("Fruits", 20, "#16a34a"),
                new CategoryShare("Grains", 15, "#22c55e"),
                new CategoryShare("Pulses", 10, "#f59e0b"),
                new CategoryShare("Others", 10, "#8b5cf6")
        };

        ObservableList<PieChart.Data> slices = FXCollections.observableArrayList();
        for (CategoryShare share : shares) {
            slices.add(new PieChart.Data(share.name(), share.percent()));
        }

        PieChart pieChart = new PieChart(slices);
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(false);
        pieChart.setPrefSize(150, 150);
        pieChart.setMaxSize(150, 150);
        pieChart.setStartAngle(90);

        for (int i = 0; i < shares.length; i++) {
            CategoryShare share = shares[i];
            Node sliceNode = slices.get(i).getNode();
            if (sliceNode != null) {
                sliceNode.setStyle("-fx-pie-color: " + share.color() + ";");
                sliceNode.setOnMouseClicked(
                        e -> showInfoAlert(share.name(),
                                share.name() + " make up " + share.percent() + "% of listed produce."));
            }
        }

        // a plain white circle over the middle turns the pie into a donut
        Circle hole = new Circle(32, Color.WHITE);
        StackPane donut = new StackPane(pieChart, hole);

        VBox legend = new VBox(10);
        legend.setAlignment(Pos.CENTER_LEFT);
        for (CategoryShare share : shares) {
            legend.getChildren().add(buildLegendRow(share));
        }

        HBox body = new HBox(20, donut, legend);
        body.setAlignment(Pos.CENTER_LEFT);
        panel.getChildren().add(body);
        return panel;
    }

    private HBox buildLegendRow(CategoryShare share) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        Region swatch = new Region();
        swatch.setMinSize(10, 10);
        swatch.setMaxSize(10, 10);
        swatch.setStyle("-fx-background-color: " + share.color() + "; -fx-background-radius: 2;");

        Label nameLabel = new Label(share.name());
        nameLabel.setFont(Font.font("Segoe UI", 12));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label percentLabel = new Label(share.percent() + "%");
        percentLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        row.getChildren().addAll(swatch, nameLabel, spacer, percentLabel);
        row.setPrefWidth(110);
        return row;
    }

    // ---- top performing products ----

    private record ProductStat(String name, int kgSold, int revenue, String emoji, String tint) {
    }

    private VBox buildProductsPanel() {
        VBox panel = panelShell("Top Performing Products");

        VBox list = new VBox(10);
        list.getChildren().addAll(
                buildProductRow(new ProductStat("Tomato", 2450, 73500, "\uD83C\uDF45", "#fee2e2")),
                buildProductRow(new ProductStat("Potato", 1800, 37800, "\uD83E\uDD54", "#fef3c7")),
                buildProductRow(new ProductStat("Onion", 1420, 28400, "\uD83E\uDDC5", "#ede9fe")),
                buildProductRow(new ProductStat("Cabbage", 980, 15600, "\uD83E\uDD6C", "#dcfce7")),
                buildProductRow(new ProductStat("Mango", 870, 34800, "\uD83E\uDD6D", "#ffedd5")));
        panel.getChildren().add(list);
        return panel;
    }

    private HBox buildProductRow(ProductStat stat) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-cursor: hand;");

        Label icon = new Label(stat.emoji());
        icon.setStyle("-fx-background-color: " + stat.tint() + "; -fx-background-radius: 50; -fx-padding: 6 10 6 10;");

        VBox textBox = new VBox(1);
        Label nameLabel = new Label(stat.name());
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        Label soldLabel = new Label(formatIndian(stat.kgSold()) + " kg sold");
        soldLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11;");
        textBox.getChildren().addAll(nameLabel, soldLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label amountLabel = new Label("\u20B9" + formatIndian(stat.revenue()));
        amountLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        amountLabel.setStyle("-fx-text-fill: " + GREEN + ";");

        row.getChildren().addAll(icon, textBox, spacer, amountLabel);
        row.setOnMouseClicked(e -> showInfoAlert(stat.name(),
                String.format("%s sold %s kg for \u20B9%s this period.", stat.name(),
                        formatIndian(stat.kgSold()), formatIndian(stat.revenue()))));
        return row;
    }

    // ---- top performing farmers ----

    private record FarmerStat(String name, int revenue) {
    }

    private VBox buildFarmersPanel() {
        VBox panel = panelShell("Top Performing Farmers");

        VBox list = new VBox(10);
        list.getChildren().addAll(
                buildFarmerRow(new FarmerStat("Ramesh Patil", 125000)),
                buildFarmerRow(new FarmerStat("Suresh Yadav", 98500)),
                buildFarmerRow(new FarmerStat("Mahesh Jadhav", 87200)),
                buildFarmerRow(new FarmerStat("Anita Deshmukh", 76800)),
                buildFarmerRow(new FarmerStat("Vikram Singh", 65400)));
        panel.getChildren().add(list);
        return panel;
    }

    private HBox buildFarmerRow(FarmerStat stat) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-cursor: hand;");

        Label avatar = new Label("\uD83D\uDC64");
        avatar.setMinSize(26, 26);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color: #eaf6ec; -fx-background-radius: 50;");

        Label nameLabel = new Label(stat.name());
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label amountLabel = new Label("\u20B9" + formatIndian(stat.revenue()));
        amountLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        amountLabel.setStyle("-fx-text-fill: " + GREEN + ";");

        row.getChildren().addAll(avatar, nameLabel, spacer, amountLabel);
        row.setOnMouseClicked(e -> showInfoAlert(stat.name(),
                String.format("%s generated \u20B9%s in revenue this period.", stat.name(),
                        formatIndian(stat.revenue()))));
        return row;
    }

    // Indian digit grouping (1,25,000 instead of 125,000) - built by hand since
    // String.format's "%,d" only knows the international 3-digit grouping
    private String formatIndian(int number) {
        String digits = Integer.toString(number);
        if (digits.length() <= 3) {
            return digits;
        }
        String lastThree = digits.substring(digits.length() - 3);
        String rest = digits.substring(0, digits.length() - 3);

        StringBuilder grouped = new StringBuilder();
        int count = 0;
        for (int i = rest.length() - 1; i >= 0; i--) {
            grouped.insert(0, rest.charAt(i));
            count++;
            if (count % 2 == 0 && i != 0) {
                grouped.insert(0, ',');
            }
        }
        return grouped + "," + lastThree;
    }

    // ------------------------------------------------------------------
    // Mock data per date range - swaps into the cards when a tab is clicked
    // ------------------------------------------------------------------

    private RangeData buildRangeData(String range) {
        switch (range) {
            case "Today":
                return new RangeData(
                        new String[] { "9am", "11am", "1pm", "3pm", "5pm", "7pm" },
                        "412", "+3.1%", true, new double[] { 5, 6, 6, 7, 8, 8 },
                        "180", "+1.8%", true, new double[] { 3, 3, 4, 4, 5, 5 },
                        "560", "+4.4%", true, new double[] { 6, 7, 7, 8, 9, 10 },
                        "\u20B91.6 Lakh", "+2.9%", true, new double[] { 1, 1.2, 1.3, 1.4, 1.5, 1.6 });
            case "This Week":
                return new RangeData(
                        new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" },
                        "2,180", "+6.4%", true, new double[] { 10, 12, 11, 14, 15, 16 },
                        "940", "+5.7%", true, new double[] { 8, 9, 9, 10, 11, 12 },
                        "3,120", "+8.2%", true, new double[] { 12, 13, 15, 16, 18, 19 },
                        "\u20B98.9 Lakh", "+7.1%", true, new double[] { 5, 6, 6.5, 7, 8, 8.9 });
            case "This Year":
                return new RangeData(
                        new String[] { "Q1", "Q2", "Q3", "Q4", "Q1'27", "Q2'27" },
                        "48,900", "+28.4%", true, new double[] { 20, 26, 33, 38, 44, 48.9 },
                        "19,600", "+24.1%", true, new double[] { 9, 12, 15, 17, 18, 19.6 },
                        "72,300", "+31.6%", true, new double[] { 30, 38, 48, 56, 65, 72.3 },
                        "\u20B9512 Lakh", "+33.8%", true, new double[] { 210, 260, 320, 380, 450, 512 });
            case "This Month":
            default:
                return new RangeData(
                        new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun" },
                        "12,450", "+13.9%", true, new double[] { 8, 9, 8.5, 10, 11, 12.45 },
                        "5,240", "+18.2%", true, new double[] { 3, 3.5, 4, 4.4, 4.9, 5.24 },
                        "18,420", "+20.3%", true, new double[] { 10, 12, 13, 15, 16.5, 18.42 },
                        "\u20B948.5 Lakh", "+22.1%", true, new double[] { 22, 28, 25, 34, 32, 48.5 });
        }
    }

    // pushes a RangeData set into the four cards
    private void applyRangeData(RangeData data) {
        fillCard(userCard, data.userValue, data.userGrowth, data.userUp, data.trendLabels, data.userSpark);
        fillCard(farmerCard, data.farmerValue, data.farmerGrowth, data.farmerUp, data.trendLabels, data.farmerSpark);
        fillCard(ordersCard, data.ordersValue, data.ordersGrowth, data.ordersUp, data.trendLabels, data.ordersSpark);
        fillCard(revenueCard, data.revenueValue, data.revenueGrowth, data.revenueUp, data.trendLabels,
                data.revenueSpark);
    }

    private void fillCard(MetricCard card, String value, String growthText, boolean up, String[] labels,
            double[] points) {
        card.valueLabel.setText(value);
        card.growthLabel.setText((up ? "\u25B2 " : "\u25BC ") + growthText);
        card.growthLabel.setStyle("-fx-text-fill: " + (up ? "#2e7d32" : "#c62828") + ";");

        card.sparkSeries.getData().clear();
        for (int i = 0; i < labels.length; i++) {
            card.sparkSeries.getData().add(new XYChart.Data<>(labels[i], points[i]));
        }
    }

    // one bundle of numbers for a given date-range tab
    private static class RangeData {
        String[] trendLabels;
        String userValue, userGrowth;
        boolean userUp;
        double[] userSpark;
        String farmerValue, farmerGrowth;
        boolean farmerUp;
        double[] farmerSpark;
        String ordersValue, ordersGrowth;
        boolean ordersUp;
        double[] ordersSpark;
        String revenueValue, revenueGrowth;
        boolean revenueUp;
        double[] revenueSpark;

        RangeData(String[] trendLabels,
                String userValue, String userGrowth, boolean userUp, double[] userSpark,
                String farmerValue, String farmerGrowth, boolean farmerUp, double[] farmerSpark,
                String ordersValue, String ordersGrowth, boolean ordersUp, double[] ordersSpark,
                String revenueValue, String revenueGrowth, boolean revenueUp, double[] revenueSpark) {
            this.trendLabels = trendLabels;
            this.userValue = userValue;
            this.userGrowth = userGrowth;
            this.userUp = userUp;
            this.userSpark = userSpark;
            this.farmerValue = farmerValue;
            this.farmerGrowth = farmerGrowth;
            this.farmerUp = farmerUp;
            this.farmerSpark = farmerSpark;
            this.ordersValue = ordersValue;
            this.ordersGrowth = ordersGrowth;
            this.ordersUp = ordersUp;
            this.ordersSpark = ordersSpark;
            this.revenueValue = revenueValue;
            this.revenueGrowth = revenueGrowth;
            this.revenueUp = revenueUp;
            this.revenueSpark = revenueSpark;
        }
    }
}