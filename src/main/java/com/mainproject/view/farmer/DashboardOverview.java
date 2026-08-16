package com.mainproject.view.farmer;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class DashboardOverview {

    private final FarmerDashboard navigator;

    public DashboardOverview(FarmerDashboard navigator) {
        this.navigator = navigator;
    }

    public Node getView() {
        VBox root = new VBox(18);
        root.setPadding(new Insets(5));

        // Header: Greeting + "+ Add Product" Button
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);
        Label title = new Label("Good Morning, Rajesh 👋");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label subtitle = new Label("Here's what's happening on your farm today.");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addProductBtn = new Button("+ Add Product");
        addProductBtn.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 8 16; -fx-cursor: hand;");
        addProductBtn.setOnAction(e -> navigator.navigateTo("AddProduct"));

        header.getChildren().addAll(titleBox, spacer, addProductBtn);

        // 4 Key Stats Cards
        HBox statsRow = new HBox(14);
        statsRow.getChildren().addAll(
            createStatCard("Total Products", "156"),
            createStatCard("Active Products", "98"),
            createStatCard("Pending Orders", "24"),
            createStatCard("Completed Orders", "128")
        );

        // Earnings Cards Row
        HBox earningsRow = new HBox(14);
        VBox earnMonth = createEarningCard("Earnings (This Month)", "₹45,680");
        VBox earnTotal = createEarningCard("Total Earnings", "₹2,45,680");
        HBox.setHgrow(earnMonth, Priority.ALWAYS);
        HBox.setHgrow(earnTotal, Priority.ALWAYS);
        earningsRow.getChildren().addAll(earnMonth, earnTotal);

        // 6 Quick Access Grid Cards
        HBox gridRow1 = new HBox(14);
        gridRow1.getChildren().addAll(
            createActionCard("Marketplace", "Sell your products", "🏪", () -> navigator.navigateTo("Marketplace")),
            createActionCard("Equipment Rental", "Rent or list equipment", "🚜", () -> navigator.navigateTo("Equipment Rental")),
            createActionCard("Crop Prices", "Check latest prices", "📈", () -> navigator.navigateTo("Crop Prices"))
        );

        HBox gridRow2 = new HBox(14);
        gridRow2.getChildren().addAll(
            createActionCard("Weather", "Current weather info", "⛅", () -> navigator.navigateTo("Weather")),
            createActionCard("AI Recommendations", "Smart farm insights", "✨", () -> navigator.navigateTo("AI Recommendations")),
            createActionCard("Notifications", "View all alerts", "🔔", () -> navigator.navigateTo("Notifications"))
        );

        root.getChildren().addAll(header, statsRow, earningsRow, gridRow1, gridRow2);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scroll;
    }

    private VBox createStatCard(String title, String count) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(14, 16, 14, 16));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #A2D9CE; -fx-border-radius: 12px; -fx-border-width: 1px;");
        HBox.setHgrow(card, Priority.ALWAYS);

        Label tLbl = new Label(title);
        tLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #566573;");
        Label cLbl = new Label(count);
        cLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");

        card.getChildren().addAll(tLbl, cLbl);
        return card;
    }

    private VBox createEarningCard(String title, String amount) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setStyle("-fx-background-color: #D4EFDF; -fx-background-radius: 12px; -fx-border-color: #A2D9CE; -fx-border-radius: 12px;");

        Label t = new Label(title);
        t.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #117864;");
        Label a = new Label(amount);
        a.setStyle("-fx-font-size: 28px; -fx-font-weight: 900; -fx-text-fill: #117864;");

        card.getChildren().addAll(t, a);
        return card;
    }

    private VBox createActionCard(String title, String subtitle, String icon, Runnable action) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #A2D9CE; -fx-border-radius: 12px; -fx-cursor: hand;");
        HBox.setHgrow(card, Priority.ALWAYS);

        Label iLbl = new Label(icon);
        iLbl.setStyle("-fx-font-size: 24px;");
        Label tLbl = new Label(title);
        tLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1B2631;");
        Label sLbl = new Label(subtitle);
        sLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #566573;");

        card.getChildren().addAll(iLbl, tLbl, sLbl);
        card.setOnMouseClicked(e -> action.run());
        return card;
    }
}