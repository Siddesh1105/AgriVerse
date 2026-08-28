package com.mainproject.view.buyer;

import com.mainproject.controller.AIChatController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AiRecommendations {

    private final BuyerDashboard mainController;
    private final AIChatController aiController;

    public AiRecommendations(BuyerDashboard controller) {
        this.mainController = controller;
        this.aiController = new AIChatController();
    }

    public Node getView() {

        BorderPane root = new BorderPane();

        root.setStyle(
            "-fx-background-color: #F8FAFC;"
        );

        // =========================
        // TOP HEADER
        // =========================

        HBox header = new HBox();

        header.setPadding(
            new Insets(20, 30, 15, 30)
        );

        header.setAlignment(
            Pos.CENTER_LEFT
        );

        Label title = new Label(
            "AgriLink AI Assistant 🤖"
        );

        title.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1E293B;"
        );

        header.getChildren().add(title);

        root.setTop(header);

        // =========================
        // CHAT AREA
        // =========================

        VBox chatArea = new VBox(12);

        chatArea.setPadding(
            new Insets(15, 30, 15, 30)
        );

        ScrollPane scrollPane =
            new ScrollPane(chatArea);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: #F8FAFC;" +
            "-fx-border-color: transparent;"
        );

        // Initial message

        addBotMessage(
            chatArea,
            "Hello! 👋 I am AgriLink AI Assistant.\n\n" +
            "I can help you with:\n" +
            "• Crop information\n" +
            "• Market prices\n" +
            "• Soil and fertilizers\n" +
            "• Irrigation\n" +
            "• Farming practices\n" +
            "• Weather-related questions\n\n" +
            "What would you like to know?"
        );

        root.setCenter(scrollPane);

        // =========================
        // INPUT AREA
        // =========================

        HBox inputBar = new HBox(10);

        inputBar.setPadding(
            new Insets(15, 30, 20, 30)
        );

        inputBar.setAlignment(
            Pos.CENTER
        );

        TextField inputField = new TextField();

        inputField.setPromptText(
            "Ask AgriAI anything about agriculture..."
        );

        inputField.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 12;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #CBD5E1;"
        );

        HBox.setHgrow(
            inputField,
            Priority.ALWAYS
        );

        Button askButton = new Button(
            "Ask AI 🤖"
        );

        askButton.setStyle(
            "-fx-background-color: #166534;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 20;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );

        // =========================
        // ASK AI ACTION
        // =========================

        Runnable askQuestion = () -> {

            String question =
                inputField.getText().trim();

            if (question.isEmpty()) {
                return;
            }

            // Show user's question
            addUserMessage(
                chatArea,
                question
            );

            inputField.clear();

            // Temporary message
            Label thinking = new Label(
                "AgriAI is thinking..."
            );

            thinking.setStyle(
                "-fx-text-fill: #64748B;" +
                "-fx-font-style: italic;"
            );

            chatArea.getChildren().add(
                thinking
            );

            // Run Gemini in background
            Thread aiThread = new Thread(() -> {

                String response =
                    aiController.getResponse(question);

                Platform.runLater(() -> {

                    chatArea.getChildren().remove(
                        thinking
                    );

                    addBotMessage(
                        chatArea,
                        response
                    );

                    scrollPane.setVvalue(1.0);
                });

            });

            aiThread.setDaemon(true);
            aiThread.start();

            scrollPane.setVvalue(1.0);
        };

        askButton.setOnAction(
            e -> askQuestion.run()
        );

        inputField.setOnAction(
            e -> askQuestion.run()
        );

        inputBar.getChildren().addAll(
            inputField,
            askButton
        );

        root.setBottom(inputBar);

        return root;
    }

    // =========================
    // USER MESSAGE
    // =========================

    private void addUserMessage(
        VBox chatArea,
        String message
    ) {

        HBox container =
            new HBox();

        container.setAlignment(
            Pos.CENTER_RIGHT
        );

        Label label =
            new Label(message);

        label.setWrapText(true);

        label.setMaxWidth(650);

        label.setStyle(
            "-fx-background-color: #DCFCE7;" +
            "-fx-text-fill: #14532D;" +
            "-fx-padding: 12;" +
            "-fx-background-radius: 12;"
        );

        container.getChildren().add(
            label
        );

        chatArea.getChildren().add(
            container
        );
    }

    // =========================
    // BOT MESSAGE
    // =========================

    private void addBotMessage(
        VBox chatArea,
        String message
    ) {

        HBox container =
            new HBox();

        container.setAlignment(
            Pos.CENTER_LEFT
        );

        Label label =
            new Label(message);

        label.setWrapText(true);

        label.setMaxWidth(700);

        label.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-text-fill: #1E293B;" +
            "-fx-padding: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #E2E8F0;" +
            "-fx-border-radius: 12;"
        );

        container.getChildren().add(
            label
        );

        chatArea.getChildren().add(
            container
        );
    }
}