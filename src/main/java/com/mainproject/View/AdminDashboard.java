package com.mainproject.View;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class AdminDashboard {

    private static final String GREEN = "#2E7D32";
    private static final String BG = "#f1efef";
    private static final String BORDER = "#e5e5e5";
    private static final String MUTED = "#888888";

    private Scene adminDashboardScene;

    Scene getAdminDashboardScene() {

        VBox sidebar = buildSidebar();
        VBox content = buildContent();

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + BG + "; -fx-background-color: " + BG + "; -fx-border-width: 0;");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(scroll);
        root.setStyle("-fx-background-color: " + BG + ";");

        adminDashboardScene = new Scene(root, 1600, 1000);
        adminDashboardScene.setFill(Color.WHITE);
        return adminDashboardScene;
    }

    private VBox buildSidebar() {

        Text logoIcon = new Text("🌿");
        logoIcon.setStyle("-fx-font-size: 22px;");
        Text logoText = new Text("AgriLink");
        logoText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: " + GREEN + ";");
        HBox logoBox = new HBox(8, logoIcon, logoText);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new Insets(20, 20, 25, 20));

        String[][] items = {
                { "🏠", "Dashboard" },
                { "👥", "User Management" },
                { "✅", "Farmer Verification" },
                { "📦", "Products" },
                { "🧾", "Orders" },
                { "🗂️", "Content Management" },
                { "⚙️", "System Management" },
                { "📊", "Analytics" },
                { "🔔", "Notifications" }
        };

        ToggleGroup group = new ToggleGroup();
        VBox navBox = new VBox(4);
        navBox.setPadding(new Insets(0, 10, 0, 10));

        for (int i = 0; i < items.length; i++) {
            ToggleButton navItem = createNavItem(items[i][0], items[i][1], group);
            if (i == 0) {
                navItem.setSelected(true);
            }
            navBox.getChildren().add(navItem);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        ToggleButton logoutItem = createNavItem("🚪", "Logout", null);
        logoutItem.setOnAction(e -> LoginScreen.logoutToLogin());
        VBox logoutBox = new VBox(logoutItem);
        logoutBox.setPadding(new Insets(0, 10, 20, 10));

        VBox sidebar = new VBox(logoBox, navBox, spacer, logoutBox);
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: " + BORDER + "; -fx-border-width: 0 1 0 0;");
        return sidebar;
    }

    private ToggleButton createNavItem(String icon, String label, ToggleGroup group) {
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 15px;");
        Text labelText = new Text(label);
        labelText.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        HBox inner = new HBox(10, iconText, labelText);
        inner.setAlignment(Pos.CENTER_LEFT);

        ToggleButton item = new ToggleButton();
        item.setGraphic(inner);
        item.setMaxWidth(Double.MAX_VALUE);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPrefHeight(40);
        if (group != null) {
            item.setToggleGroup(group);
        }

        item.setStyle(navStyle(false));
        item.selectedProperty().addListener((obs, was, isSel) -> item.setStyle(navStyle(isSel)));

        return item;
    }

    private String navStyle(boolean active) {
        if (active) {
            return "-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 8px; "
                    + "-fx-border-width: 0;";
        }
        return "-fx-background-color: transparent; -fx-text-fill: #444444; -fx-background-radius: 8px; "
                + "-fx-border-width: 0;";
    }

    private VBox buildContent() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(30, 40, 40, 40));

        content.getChildren().addAll(
                buildHeader(),
                buildStatCards(),
                buildOverviewAndActivityRow());

        return content;
    }

    private VBox buildHeader() {
        Text title = new Text("Admin Dashboard");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Circle avatar = new Circle(16, Color.web(GREEN));
        Text initials = new Text("AD");
        initials.setStyle("-fx-fill: white; -fx-font-size: 11px; -fx-font-weight: bold;");
        StackPane avatarPane = new StackPane(avatar, initials);

        Text userName = new Text("Admin");
        userName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        HBox userBox = new HBox(8, avatarPane, userName);
        userBox.setAlignment(Pos.CENTER);

        HBox headerRow = new HBox(20, title, spacer, userBox);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Text welcome = new Text("Welcome back, Admin");
        welcome.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        Text subtitle = new Text("Manage and monitor the platform.");
        subtitle.setStyle("-fx-font-size: 12px; -fx-fill: " + MUTED + ";");

        return new VBox(4, headerRow, welcome, subtitle);
    }

    private HBox buildStatCards() {
        HBox row = new HBox(20,
                createStatCard("👥", "Total Users", "2568", "+12% this month"),
                createStatCard("🧑‍🌾", "Total Farmers", "1245", "+10% this month"),
                createStatCard("🛍️", "Total Buyers", "1323", "+13% this month"),
                createStatCard("🧾", "Total Orders", "3541", "+10% this month"));
        for (int i = 0; i < row.getChildren().size(); i++) {
            HBox.setHgrow(row.getChildren().get(i), Priority.ALWAYS);
        }
        return row;
    }

    private VBox createStatCard(String icon, String label, String value, String sub) {
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 20px;");

        Text labelText = new Text(label);
        labelText.setStyle("-fx-font-size: 12px; -fx-fill: " + MUTED + "; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(labelText, spacer, iconText);
        top.setAlignment(Pos.CENTER_LEFT);

        Text valueText = new Text(value);
        valueText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Text subText = new Text(sub);
        subText.setStyle("-fx-font-size: 11px; -fx-fill: " + GREEN + "; -fx-font-weight: bold;");

        VBox card = new VBox(10, top, valueText, subText);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12px; "
                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12px;");
        card.setPrefWidth(250);
        return card;
    }

    private HBox buildOverviewAndActivityRow() {
        VBox overviewPanel = buildPlatformOverviewChart();
        HBox.setHgrow(overviewPanel, Priority.ALWAYS);

        VBox activityPanel = buildRecentActivityPanel();
        activityPanel.setPrefWidth(320);

        return new HBox(20, overviewPanel, activityPanel);
    }

    private VBox buildPlatformOverviewChart() {
        Text heading = new Text("Platform Overview");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setCreateSymbols(true);
        chart.setPrefHeight(280);
        chart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Users");
        series.setData(FXCollections.observableArrayList(
                new XYChart.Data<>("Jan", 900),
                new XYChart.Data<>("Feb", 1900),
                new XYChart.Data<>("Mar", 3050),
                new XYChart.Data<>("Apr", 1950),
                new XYChart.Data<>("May", 2100),
                new XYChart.Data<>("Jun", 1900),
                new XYChart.Data<>("Jul", 3300),
                new XYChart.Data<>("Aug", 4000)));

        chart.getData().add(series);

        VBox panel = new VBox(14, heading, chart);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 12px; "
                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12px;");
        return panel;
    }

    private VBox buildRecentActivityPanel() {
        Text heading = new Text("Recent Activity");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox list = new VBox(14,
                createActivityRow("🧑‍🌾", "New Farmer Registered", "Ramesh Patil", "2 min ago"),
                createActivityRow("🧾", "New Order Placed", "Order #ORD1217", "10 min ago"),
                createActivityRow("✅", "Product Approved", "Tomatoes by Ramesh Patil", "25 min ago"),
                createActivityRow("💬", "New Feedback Received", "From Mahesh Kumar", "1 hour ago"));

        VBox panel = new VBox(14, heading, list);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 12px; "
                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12px;");
        return panel;
    }

    private HBox createActivityRow(String icon, String title, String detail, String time) {
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 16px;");

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        Text detailText = new Text(detail);
        detailText.setStyle("-fx-font-size: 11px; -fx-fill: " + MUTED + ";");

        VBox textBox = new VBox(2, titleText, detailText);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text timeText = new Text(time);
        timeText.setStyle("-fx-font-size: 10px; -fx-fill: " + MUTED + ";");

        HBox row = new HBox(10, iconText, textBox, spacer, timeText);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
}