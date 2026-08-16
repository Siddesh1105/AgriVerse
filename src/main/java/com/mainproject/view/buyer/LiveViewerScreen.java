package com.mainproject.view.buyer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LiveViewerScreen {

    private final BuyerDashboard mainController;

    public LiveViewerScreen(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Center: Video Stream Area
        VBox streamBox = new VBox(10);
        streamBox.setAlignment(Pos.CENTER);
        streamBox.setStyle("-fx-background-color: #0F172A; -fx-background-radius: 12;");
        VBox.setVgrow(streamBox, Priority.ALWAYS);

        Label lblStream = new Label("🎥 [Live Video Feed: Ramesh Patil Harvesting Fresh Tomatoes]\n324 Viewers Active");
        lblStream.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-text-alignment: center;");

        HBox quickBuyBar = new HBox(15);
        quickBuyBar.setAlignment(Pos.CENTER);
        quickBuyBar.setPadding(new Insets(12));
        quickBuyBar.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-background-radius: 8;");

        Label lblQuickProd = new Label("🍅 Fresh Tomato - ₹28/kg (50kg available)");
        lblQuickProd.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button btnBuyNow = new Button("Buy Now");
        btnBuyNow.setStyle("-fx-background-color: #22C55E; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnBuyNow.setOnAction(e -> mainController.setView(new Checkout(mainController).getView()));

        quickBuyBar.getChildren().addAll(lblQuickProd, btnBuyNow);
        streamBox.getChildren().addAll(lblStream, quickBuyBar);

        // Right: Live Chat Box
        VBox chatBox = new VBox(10);
        chatBox.setPrefWidth(320);
        chatBox.setPadding(new Insets(15));
        chatBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 12; -fx-background-radius: 12;");

        Label chatTitle = new Label("💬 Live Chat");
        chatTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        TextArea txtChat = new TextArea();
        txtChat.setEditable(false);
        txtChat.appendText("Ankit: Is this chemical-free?\nRamesh (Host): Yes, 100% organic.\nPooja: Ordered 10kg!\n");
        VBox.setVgrow(txtChat, Priority.ALWAYS);

        HBox inputBar = new HBox(8);
        TextField tfMessage = new TextField();
        HBox.setHgrow(tfMessage, Priority.ALWAYS);
        Button btnSend = new Button("Send");
        btnSend.setOnAction(e -> {
            if (!tfMessage.getText().trim().isEmpty()) {
                txtChat.appendText("You: " + tfMessage.getText() + "\n");
                tfMessage.clear();
            }
        });
        inputBar.getChildren().addAll(tfMessage, btnSend);

        chatBox.getChildren().addAll(chatTitle, txtChat, inputBar);

        root.setCenter(streamBox);
        root.setRight(chatBox);
        BorderPane.setMargin(chatBox, new Insets(0, 0, 0, 15));

        return root;
    }
}