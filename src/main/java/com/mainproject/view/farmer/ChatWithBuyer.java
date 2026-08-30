package com.mainproject.view.farmer;

import java.util.List;
import com.google.cloud.firestore.ListenerRegistration;
import com.mainproject.controller.ChatController;
import com.mainproject.model.ChatMessage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/** Farmer-side real-time conversation with a buyer. */
public class ChatWithBuyer {
    private final FarmerDashboard dashboard;
    private final ChatController chatController = new ChatController();
    private final String buyerName;
    private final String buyerEmail;
    private final String farmerEmail;
    private ListenerRegistration listenerRegistration;

    public ChatWithBuyer(FarmerDashboard dashboard, String buyerName, String buyerEmail) {
        this.dashboard = dashboard;
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.farmerEmail = dashboard.getFarmerEmail();
    }

    public Node getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color:#F8FAFC;");

        Label title = new Label("💬 Chat with " + buyerName);
        title.setStyle("-fx-font-size:22px;-fx-font-weight:bold;-fx-text-fill:#1E293B;");
        Label subtitle = new Label("Send messages directly to this buyer.");
        subtitle.setStyle("-fx-font-size:13px;-fx-text-fill:#64748B;");
        root.setTop(new VBox(5, title, subtitle));
        BorderPane.setMargin(root.getTop(), new Insets(0,0,15,0));

        VBox messagesBox = new VBox(12); messagesBox.setPadding(new Insets(15));
        ScrollPane scroll = new ScrollPane(messagesBox); scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:white;-fx-border-color:#CBD5E1;-fx-border-radius:10;");
        root.setCenter(scroll);

        TextField input = new TextField(); input.setPromptText("Type a message..."); input.setPrefHeight(48); HBox.setHgrow(input, Priority.ALWAYS);
        Button send = new Button("Send"); send.setPrefHeight(48);
        send.setStyle("-fx-background-color:#117864;-fx-text-fill:white;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;");
        Runnable sendAction = () -> {
            String text = input.getText();
            if (text == null || text.trim().isEmpty()) return;
            if (chatController.sendMessage(farmerEmail, buyerEmail, text)) input.clear();
            else new Alert(Alert.AlertType.ERROR, "Unable to send message. Please try again.").showAndWait();
        };
        send.setOnAction(e -> sendAction.run()); input.setOnAction(e -> sendAction.run());
        HBox bar = new HBox(10, input, send); bar.setPadding(new Insets(15,0,0,0)); root.setBottom(bar);

        listenerRegistration = chatController.listenForMessages(farmerEmail, buyerEmail, messages -> Platform.runLater(() -> render(messagesBox, messages, scroll)));
        root.sceneProperty().addListener((obs, oldScene, newScene) -> { if (newScene == null && listenerRegistration != null) { listenerRegistration.remove(); listenerRegistration = null; } });
        return root;
    }

    private void render(VBox box, List<ChatMessage> messages, ScrollPane scroll) {
        box.getChildren().clear();
        for (ChatMessage message : messages) {
            boolean isFarmer = farmerEmail.equalsIgnoreCase(message.getSenderEmail());
            HBox row = new HBox(); row.setAlignment(isFarmer ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT); row.setPadding(new Insets(3));
            VBox bubble = new VBox(5); bubble.setMaxWidth(450);
            Label sender = new Label(isFarmer ? "You" : buyerName); sender.setStyle("-fx-font-size:11px;-fx-font-weight:bold;");
            Label text = new Label(message.getMessage()); text.setWrapText(true); text.setMaxWidth(400); text.setStyle("-fx-font-size:14px;");
            bubble.getChildren().addAll(sender, text);
            bubble.setStyle((isFarmer ? "-fx-background-color:#DCFCE7;" : "-fx-background-color:#E2E8F0;") + "-fx-background-radius:15;-fx-padding:10 15;");
            row.getChildren().add(bubble); box.getChildren().add(row);
        }
        scroll.setVvalue(1.0);
    }
}
