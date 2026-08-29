package com.mainproject.view.buyer;

import java.util.List;

import com.google.cloud.firestore.ListenerRegistration;
import com.mainproject.controller.ChatController;
import com.mainproject.model.ChatMessage;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ChatWithFarmer {

    private final BuyerDashboard mainController;

    private final ChatController chatController;

    private final String farmerName;
    private final String farmerEmail;

    private final String buyerEmail;

    private ListenerRegistration listenerRegistration;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ChatWithFarmer(
            BuyerDashboard controller,
            String farmerName,
            String farmerEmail) {

        this.mainController = controller;

        this.farmerName = farmerName;
        this.farmerEmail = farmerEmail;

        this.buyerEmail = controller.getUserEmail();

        this.chatController = new ChatController();
    }

    // =====================================================
    // MAIN VIEW
    // =====================================================

    public Node getView() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        root.setStyle(
                "-fx-background-color: #F8FAFC;"
        );

        // =================================================
        // HEADER
        // =================================================

        VBox header = new VBox(5);

        Label title = new Label(
                "💬 Chat with " + farmerName
        );

        title.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1E293B;"
        );

        Label subtitle = new Label(
                "Send messages directly to this farmer."
        );

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #64748B;"
        );

        header.getChildren().addAll(title, subtitle);

        header.setPadding(
                new Insets(0, 0, 15, 0)
        );

        root.setTop(header);

        // =================================================
        // MESSAGES
        // =================================================

        VBox messagesBox = new VBox(12);

        messagesBox.setPadding(new Insets(15));

        ScrollPane scrollPane = new ScrollPane(messagesBox);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #CBD5E1;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );

        root.setCenter(scrollPane);

        // =================================================
        // INPUT BAR
        // =================================================

        HBox inputBar = new HBox(12);

        inputBar.setAlignment(Pos.CENTER);

        inputBar.setPadding(
                new Insets(15, 0, 0, 0)
        );

        TextField messageField = new TextField();

        messageField.setPromptText(
                "Type a message..."
        );

        messageField.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-padding: 12;"
        );

        HBox.setHgrow(
                messageField,
                Priority.ALWAYS
        );

        Button sendButton = new Button("Send ➤");

        sendButton.setStyle(
                "-fx-background-color: #166534;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12 25;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        inputBar.getChildren().addAll(
                messageField,
                sendButton
        );

        root.setBottom(inputBar);

        // =================================================
        // SEND MESSAGE
        // =================================================

        Runnable sendMessage = () -> {

            String text = messageField.getText().trim();

            if (text.isEmpty()) {
                return;
            }

            boolean success = chatController.sendMessage(
                    buyerEmail,
                    farmerEmail,
                    text
            );

            if (success) {
                messageField.clear();
            }
        };

        sendButton.setOnAction(
                e -> sendMessage.run()
        );

        messageField.setOnAction(
                e -> sendMessage.run()
        );

        // =================================================
        // REAL-TIME LISTENER
        // =================================================

        startMessageListener(
                messagesBox,
                scrollPane
        );

        

        return root;
    }

    // =====================================================
    // FIREBASE REAL-TIME LISTENER
    // =====================================================

    private void startMessageListener(
            VBox messagesBox,
            ScrollPane scrollPane) {

        listenerRegistration =
                chatController.listenForMessages(

                        buyerEmail,
                        farmerEmail,

                        messages -> {

                            Platform.runLater(() -> {

                                displayMessages(
                                        messages,
                                        messagesBox
                                );

                                scrollPane.setVvalue(1.0);
                            });
                        }
                );
    }

    // =====================================================
    // DISPLAY MESSAGES
    // =====================================================

    private void displayMessages(
            List<ChatMessage> messages,
            VBox messagesBox) {

        messagesBox.getChildren().clear();

        for (ChatMessage message : messages) {

            boolean isBuyer =
                    message.getSenderEmail()
                            .equalsIgnoreCase(
                                    buyerEmail
                            );

            HBox row = new HBox();

            row.setPadding(
                    new Insets(3)
            );

            row.setAlignment(
                    isBuyer
                            ? Pos.CENTER_RIGHT
                            : Pos.CENTER_LEFT
            );

            VBox bubble = new VBox(5);

            bubble.setMaxWidth(450);

            Label sender = new Label(
                    isBuyer
                            ? "You"
                            : farmerName
            );

            sender.setStyle(
                    "-fx-font-size: 11px;" +
                    "-fx-font-weight: bold;"
            );

            Label text = new Label(
                    message.getMessage()
            );

            text.setWrapText(true);

            text.setMaxWidth(400);

            text.setStyle(
                    "-fx-font-size: 14px;"
            );

            bubble.getChildren().addAll(
                    sender,
                    text
            );

            if (isBuyer) {

                bubble.setStyle(
                        "-fx-background-color: #DCFCE7;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10 15;"
                );

            } else {

                bubble.setStyle(
                        "-fx-background-color: #E2E8F0;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10 15;"
                );
            }

            row.getChildren().add(bubble);

            messagesBox.getChildren().add(row);
        }
    }

    // =====================================================
    // STOP LISTENER
    // =====================================================

    public void stopListening() {

        if (listenerRegistration != null) {

            listenerRegistration.remove();

            listenerRegistration = null;
        }
    }
}