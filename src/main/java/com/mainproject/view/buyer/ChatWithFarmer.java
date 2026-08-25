package com.mainproject.view.buyer;

import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ChatWithFarmer {

    private final BuyerDashboard mainController;

    public ChatWithFarmer (BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("💬 Chat with Ramesh Patil (Online)");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        root.setTop(title);

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.appendText("Ramesh: Hello! Let me know if you need fresh harvest.\nYou: Is tomato harvest ready for delivery?\nRamesh: Yes, harvested today morning!\n");

        HBox inputBar = new HBox(10);
        inputBar.setPadding(new Insets(10, 0, 0, 0));
        TextField tfInput = new TextField();
        tfInput.setPromptText("Type a message...");
        HBox.setHgrow(tfInput, Priority.ALWAYS);

        Button btnSend = new Button("Send");
        btnSend.setStyle("-fx-background-color: #166534; -fx-text-fill: white;");
        btnSend.setOnAction(e -> {
            if (!tfInput.getText().trim().isEmpty()) {
                chatArea.appendText("You: " + tfInput.getText() + "\n");
                tfInput.clear();
            }
        });
        inputBar.getChildren().addAll(tfInput, btnSend);

        root.setCenter(chatArea);
        root.setBottom(inputBar);
        LanguageManager.apply(root);
        return root;
    }
}