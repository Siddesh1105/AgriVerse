package com.mainproject.view.buyer;

import com.mainproject.controller.FarmerController;
import com.mainproject.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

/** Shows all registered farmers so a buyer can start a direct chat. */
public class FarmerChatList {
    private final BuyerDashboard dashboard;
    private final FarmerController farmerController = new FarmerController();

    public FarmerChatList(BuyerDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Node getView() {
        VBox root = new VBox(18);
        root.setPadding(new Insets(25, 30, 25, 30));
        root.setStyle("-fx-background-color:#F8FAFC;");

        Label title = new Label("💬 Messages");
        title.setStyle("-fx-font-size:25px;-fx-font-weight:bold;-fx-text-fill:#1E293B;");
        Label subtitle = new Label("Choose a farmer to start or continue a conversation.");
        subtitle.setStyle("-fx-font-size:14px;-fx-text-fill:#64748B;");

        TextField search = new TextField();
        search.setPromptText("Search farmers by name or email...");
        search.setPrefHeight(42);

        GridPane grid = new GridPane();
        grid.setHgap(18); grid.setVgap(18);
        List<User> farmers = farmerController.getAllFarmers();
        populate(grid, farmers, "");
        search.textProperty().addListener((o, oldV, newV) -> populate(grid, farmers, newV == null ? "" : newV.trim().toLowerCase()));

        root.getChildren().addAll(new VBox(5, title, subtitle), search, grid);
        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        return scroll;
    }

    private void populate(GridPane grid, List<User> farmers, String query) {
        grid.getChildren().clear();
        int row = 0, col = 0, count = 0;
        if (farmers != null) for (User farmer : farmers) {
            if (farmer == null || farmer.getEmail() == null || farmer.getEmail().trim().isEmpty()) continue;
            String name = safe(farmer.getFullName(), "Farmer");
            String email = farmer.getEmail().trim();
            if (!query.isEmpty() && !(name + " " + email).toLowerCase().contains(query)) continue;

            VBox card = new VBox(9);
            card.setPadding(new Insets(18)); card.setPrefWidth(300);
            card.setStyle("-fx-background-color:white;-fx-border-color:#E2E8F0;-fx-border-radius:12;-fx-background-radius:12;");
            Label icon = new Label("👨‍🌾"); icon.setStyle("-fx-font-size:32px;");
            Label n = new Label(name); n.setStyle("-fx-font-size:17px;-fx-font-weight:bold;-fx-text-fill:#166534;");
            Label e = new Label(email); e.setStyle("-fx-text-fill:#64748B;"); e.setWrapText(true);
            Button chat = new Button("💬 Message Farmer");
            chat.setMaxWidth(Double.MAX_VALUE);
            chat.setStyle("-fx-background-color:#117864;-fx-text-fill:white;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;");
            chat.setOnAction(ev -> dashboard.setView(new ChatWithFarmer(dashboard, name, email).getView()));
            card.getChildren().addAll(icon, n, e, chat);
            grid.add(card, col, row); count++; col++; if (col == 3) { col = 0; row++; }
        }
        if (count == 0) {
            Label empty = new Label("👨‍🌾 No farmers found."); empty.setStyle("-fx-font-size:16px;-fx-text-fill:#64748B;");
            grid.add(empty, 0, 0);
        }
    }

    private String safe(String value, String fallback) { return value == null || value.trim().isEmpty() ? fallback : value.trim(); }
}
