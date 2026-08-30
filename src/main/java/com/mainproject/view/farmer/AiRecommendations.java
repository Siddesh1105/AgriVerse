package com.mainproject.view.farmer;

import com.mainproject.config.AIConfig;
import com.mainproject.controller.AIChatController;
import com.mainproject.controller.WeatherRecommendationController;
import com.mainproject.model.AIChatMessage;
import com.mainproject.model.WeatherRecommendation;

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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class AiRecommendations {

    private VBox chatBox;
    private ScrollPane chatScroll;

    private TextField messageField;
    private Button sendButton;

    private final AIChatController aiController;
    private final String userEmail;

    private Node thinkingRow;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AiRecommendations(String userEmail) {

        this.userEmail = userEmail;
        this.aiController = new AIChatController();
    }

    // =====================================================
    // MAIN VIEW
    // =====================================================

    public Node getView() {

        BorderPane root = new BorderPane();

        root.setPadding(new Insets(20));

        root.setStyle(
                "-fx-background-color: #f1f8f5;"
        );

        // LEFT SIDE
        VBox weatherSection =
                createWeatherSection();

        // RIGHT SIDE
        VBox chatbot =
                createChatbot();

        HBox content = new HBox(20);

        content.setAlignment(Pos.TOP_LEFT);

        weatherSection.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(
                weatherSection,
                Priority.ALWAYS
        );

        content.getChildren().addAll(
                weatherSection,
                chatbot
        );

        root.setCenter(content);

        return root;
    }

    // =====================================================
    // WEATHER SECTION
    // =====================================================

    private VBox createWeatherSection() {

        VBox root = new VBox(18);

        root.setPadding(new Insets(10));

        root.setMaxWidth(Double.MAX_VALUE);

        // =================================================
        // HEADER
        // =================================================

        Label title = new Label(
                "Weather Recommendations"
        );

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1f2937;"
        );

        Label subtitle = new Label(
                "Smart farming suggestions based on the weather in your location."
        );

        subtitle.setWrapText(true);

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #64748b;"
        );

        VBox header = new VBox(6);

        header.getChildren().addAll(
                title,
                subtitle
        );

        // =================================================
        // WEATHER RECOMMENDATIONS CONTAINER
        // =================================================

        VBox weatherBox = new VBox(12);

        weatherBox.setPadding(
                new Insets(20)
        );

        weatherBox.setMaxWidth(Double.MAX_VALUE);

        weatherBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #b7ddd0;" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;"
        );

        Label loadingLabel = new Label(
                "Loading weather recommendations..."
        );

        loadingLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #64748b;"
        );

        weatherBox.getChildren().add(
                loadingLabel
        );

        // =================================================
        // LOAD WEATHER DATA
        // =================================================

        Thread thread = new Thread(() -> {

            try {

                WeatherRecommendationController
                        weatherController =
                        new WeatherRecommendationController(
                                userEmail
                        );

                List<WeatherRecommendation>
                        recommendations =
                        weatherController.getRecommendations();

                Platform.runLater(() -> {

                    weatherBox.getChildren().clear();

                    if (recommendations == null ||
                            recommendations.isEmpty()) {

                        Label noData = new Label(
                                "No weather recommendations are available right now."
                        );

                        noData.setStyle(
                                "-fx-font-size: 14px;" +
                                "-fx-text-fill: #64748b;"
                        );

                        weatherBox.getChildren().add(
                                noData
                        );

                        return;
                    }

                    // Show maximum 4 recommendations
                    int count = Math.min(
                            recommendations.size(),
                            4
                    );

                    for (int i = 0; i < count; i++) {

                        WeatherRecommendation recommendation =
                                recommendations.get(i);

                        VBox card =
                                createWeatherRecommendationCard(
                                        recommendation
                                );

                        weatherBox.getChildren().add(
                                card
                        );
                    }
                });

            } catch (Exception e) {

                e.printStackTrace();

                Platform.runLater(() -> {

                    weatherBox.getChildren().clear();

                    Label errorLabel = new Label(
                            "Unable to load weather recommendations."
                    );

                    errorLabel.setStyle(
                            "-fx-font-size: 14px;" +
                            "-fx-text-fill: #dc2626;"
                    );

                    weatherBox.getChildren().add(
                            errorLabel
                    );
                });
            }
        });

        thread.setDaemon(true);
        thread.start();

        root.getChildren().addAll(
                header,
                weatherBox
        );

        return root;
    }

    // =====================================================
    // WEATHER RECOMMENDATION CARD
    // =====================================================

    private VBox createWeatherRecommendationCard(
            WeatherRecommendation recommendation) {

        VBox card = new VBox(7);

        card.setPadding(
                new Insets(15)
        );

        card.setMaxWidth(Double.MAX_VALUE);

        card.setStyle(
                "-fx-background-color: #f6fbf8;" +
                "-fx-border-color: #d7e9e2;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );

        Label title = new Label(
                recommendation.getTitle()
        );

        title.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #147a65;"
        );

       

       

        Label description = new Label(
                recommendation.getDescription()
        );

        description.setWrapText(true);

        description.setMaxWidth(Double.MAX_VALUE);

        description.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #64748b;"
        );

        card.getChildren().addAll(
                title,
                description
        );

        return card;
    }

    // =====================================================
    // CHATBOT
    // =====================================================

    private VBox createChatbot() {

        VBox chatbot = new VBox();

        /*
         * RESPONSIVE CHATBOT SIZE
         */

        chatbot.setPrefWidth(420);
        chatbot.setMinWidth(360);
        chatbot.setMaxWidth(450);

        chatbot.setPrefHeight(620);

        chatbot.setMinHeight(500);

        chatbot.setMaxHeight(700);

        chatbot.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #cfe4dc;" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;"
        );

        // =================================================
        // CHAT HEADER
        // =================================================

        VBox header = new VBox(5);

        header.setPadding(
                new Insets(20, 20, 15, 20)
        );

        Label title = new Label(
                AIConfig.ASSISTANT_NAME
        );

        title.setStyle(
                "-fx-font-size: 21px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #147a65;"
        );

        Label subtitle = new Label(
                "Ask me anything about your farm."
        );

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #64748b;"
        );

        header.getChildren().addAll(
                title,
                subtitle
        );

        // =================================================
        // CHAT MESSAGES AREA
        // =================================================

        chatBox = new VBox(10);

        chatBox.setPadding(
                new Insets(12)
        );

        chatBox.setFillWidth(true);

        chatScroll = new ScrollPane();

        chatScroll.setContent(chatBox);

        chatScroll.setFitToWidth(true);

        chatScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        chatScroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        /*
         * IMPORTANT:
         * Only this area scrolls.
         */

        chatScroll.setPrefHeight(330);

        chatScroll.setMinHeight(250);

        chatScroll.setMaxHeight(380);

        chatScroll.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: white;" +
                "-fx-border-color: transparent;"
        );

        VBox.setVgrow(
                chatScroll,
                Priority.ALWAYS
        );

        // Initial AI message

        addBotMessage(
                "Hello! I am your AgriLink AI Assistant. "
                        + "How can I help you today?"
        );

        // =================================================
        // QUICK ACTIONS
        // =================================================

        HBox quickActions =
                createQuickActions();

        // =================================================
        // INPUT AREA
        // =================================================

        messageField = new TextField();

        messageField.setPromptText(
                "Ask about crops, soil or farming..."
        );

        messageField.setPrefHeight(44);

        messageField.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #cfe4dc;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10;" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #334155;"
        );

        sendButton = new Button("Send");

        sendButton.setPrefHeight(44);

        sendButton.setMinWidth(70);

        sendButton.setStyle(
                "-fx-background-color: #147a65;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        HBox inputArea = new HBox(8);

        inputArea.setPadding(
                new Insets(12, 15, 10, 15)
        );

        HBox.setHgrow(
                messageField,
                Priority.ALWAYS
        );

        inputArea.getChildren().addAll(
                messageField,
                sendButton
        );

        // =================================================
        // EVENTS
        // =================================================

        sendButton.setOnAction(
                e -> sendMessage()
        );

        messageField.setOnAction(
                e -> sendMessage()
        );

        // =================================================
        // DISCLAIMER
        // =================================================

        Label disclaimer = new Label(
                "AI responses may not always be accurate."
        );

        disclaimer.setPadding(
                new Insets(0, 15, 14, 15)
        );

        disclaimer.setStyle(
                "-fx-font-size: 10px;" +
                "-fx-text-fill: #94a3b8;"
        );

        chatbot.getChildren().addAll(
                header,
                chatScroll,
                quickActions,
                inputArea,
                disclaimer
        );

        return chatbot;
    }

    // =====================================================
    // QUICK ACTIONS
    // =====================================================

    private HBox createQuickActions() {

        HBox box = new HBox(8);

        box.setAlignment(Pos.CENTER_LEFT);

        box.setPadding(
                new Insets(5, 15, 5, 15)
        );

        Button crop = new Button("Best Crop");

        Button weather = new Button("Weather");

        Button soil = new Button("Soil Tips");

        styleQuickButton(crop);
        styleQuickButton(weather);
        styleQuickButton(soil);

        crop.setOnAction(
                e -> askQuestion(
                        "Which crop should I plant?"
                )
        );

        weather.setOnAction(
                e -> askQuestion(
                        "Give me useful weather advice for farming."
                )
        );

        soil.setOnAction(
                e -> askQuestion(
                        "How can I improve my soil health?"
                )
        );

        box.getChildren().addAll(
                crop,
                weather,
                soil
        );

        return box;
    }

    private void styleQuickButton(
            Button button) {

        button.setStyle(
                "-fx-background-color: #f3faf7;" +
                "-fx-text-fill: #147a65;" +
                "-fx-border-color: #b7ddd0;" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;" +
                "-fx-padding: 7 12;" +
                "-fx-font-size: 11px;" +
                "-fx-cursor: hand;"
        );
    }

    // =====================================================
    // ASK QUICK QUESTION
    // =====================================================

    private void askQuestion(
            String question) {

        if (messageField.isDisabled()) {
            return;
        }

        addUserMessage(question);

        sendToGemini(question);
    }

    // =====================================================
    // SEND MESSAGE
    // =====================================================

    private void sendMessage() {

        String userMessage =
                messageField.getText().trim();

        if (userMessage.isEmpty()) {
            return;
        }

        if (messageField.isDisabled()) {
            return;
        }

        addUserMessage(userMessage);

        messageField.clear();

        sendToGemini(userMessage);
    }

    // =====================================================
    // SEND TO AI
    // =====================================================

    private void sendToGemini(
            String userMessage) {

        messageField.setDisable(true);
        sendButton.setDisable(true);

        addThinkingMessage();

        Thread thread = new Thread(() -> {

            String answer;

            try {

                answer =
                        aiController.getResponse(
                                userMessage
                        );

                if (answer == null ||
                        answer.trim().isEmpty()) {

                    answer =
                            "Sorry, I could not generate a response. "
                                    + "Please try again.";
                }

            } catch (Exception e) {

                e.printStackTrace();

                answer =
                        "Sorry, something went wrong. "
                                + "Please try again.";
            }

            final String finalAnswer = answer;

            Platform.runLater(() -> {

                removeThinking();

                addBotMessage(
                        finalAnswer
                );

                messageField.setDisable(false);
                sendButton.setDisable(false);

                messageField.requestFocus();

                scrollToBottom();
            });
        });

        thread.setDaemon(true);

        thread.start();
    }

    // =====================================================
    // USER MESSAGE
    // =====================================================

    private void addUserMessage(
            String message) {

        addMessage(
                new AIChatMessage(
                        message,
                        true
                )
        );
    }

    // =====================================================
    // BOT MESSAGE
    // =====================================================

    private void addBotMessage(
            String message) {

        addMessage(
                new AIChatMessage(
                        message,
                        false
                )
        );
    }

    // =====================================================
    // DISPLAY MESSAGE
    // =====================================================

    private void addMessage(
            AIChatMessage message) {

        if (message.isUserMessage()) {

            addUserBubble(
                    message.getMessage()
            );

        } else {

            addBotBubble(
                    message.getMessage()
            );
        }

        scrollToBottom();
    }

    // =====================================================
    // USER CHAT BUBBLE
    // =====================================================

    private void addUserBubble(
            String message) {

        HBox row = new HBox();

        row.setAlignment(
                Pos.CENTER_RIGHT
        );

        Label label = new Label(message);

        label.setWrapText(true);

        /*
         * Prevent very wide messages
         */

        label.setMaxWidth(280);

        label.setPadding(
                new Insets(10, 14, 10, 14)
        );

        label.setStyle(
                "-fx-background-color: #dff3e9;" +
                "-fx-background-radius: 14;" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #1f2937;"
        );

        row.getChildren().add(label);

        chatBox.getChildren().add(row);
    }

    // =====================================================
    // BOT CHAT BUBBLE
    // =====================================================

    private void addBotBubble(
            String message) {

        HBox row = new HBox(8);

        row.setAlignment(
                Pos.TOP_LEFT
        );

        Label icon = new Label("AI");

        icon.setMinSize(38, 38);

        icon.setPrefSize(38, 38);

        icon.setAlignment(Pos.CENTER);

        icon.setStyle(
                "-fx-background-color: #dff3e9;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: #147a65;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;"
        );

        Label label = new Label(message);

        label.setWrapText(true);

        /*
         * IMPORTANT:
         * Limits message width so layout stays neat.
         */

        label.setMaxWidth(285);

        label.setPadding(
                new Insets(10, 14, 10, 14)
        );

        label.setStyle(
                "-fx-background-color: #ffffff;" +
                "-fx-border-color: #dce7e2;" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #334155;"
        );

        row.getChildren().addAll(
                icon,
                label
        );

        chatBox.getChildren().add(row);
    }

    // =====================================================
    // THINKING MESSAGE
    // =====================================================

    private void addThinkingMessage() {

        HBox row = new HBox(8);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        Label icon = new Label("AI");

        icon.setMinSize(38, 38);

        icon.setAlignment(Pos.CENTER);

        icon.setStyle(
                "-fx-background-color: #dff3e9;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: #147a65;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;"
        );

        Label thinking = new Label(
                "Thinking..."
        );

        thinking.setPadding(
                new Insets(10, 14, 10, 14)
        );

        thinking.setStyle(
                "-fx-background-color: #f8faf9;" +
                "-fx-border-color: #dce7e2;" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #64748b;"
        );

        row.getChildren().addAll(
                icon,
                thinking
        );

        thinkingRow = row;

        chatBox.getChildren().add(row);

        scrollToBottom();
    }

    // =====================================================
    // REMOVE THINKING
    // =====================================================

    private void removeThinking() {

        if (thinkingRow != null) {

            chatBox.getChildren().remove(
                    thinkingRow
            );

            thinkingRow = null;
        }
    }

    // =====================================================
    // SCROLL TO BOTTOM
    // =====================================================

    private void scrollToBottom() {

        Platform.runLater(() -> {

            chatScroll.applyCss();

            chatScroll.layout();

            chatScroll.setVvalue(1.0);
        });
    }
}