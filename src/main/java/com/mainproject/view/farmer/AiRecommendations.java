package com.mainproject.view.farmer;

import com.mainproject.config.AIConfig;
import com.mainproject.controller.AIChatController;
import com.mainproject.model.AIChatMessage;

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

public class AiRecommendations {

    private VBox chatBox;
    private ScrollPane chatScroll;

    private TextField messageField;
    private Button sendButton;

    private final AIChatController aiController;

    public AiRecommendations() {

        aiController = new AIChatController();
    }

    public Node getView() {

        BorderPane root =
                new BorderPane();

        root.setPadding(
                new Insets(18)
        );

        root.setStyle(
                "-fx-background-color: #f1f8f5;"
        );

        VBox recommendations =
                createRecommendations();

        VBox chatbot =
                createChatbot();

        HBox content =
                new HBox(20);

        content.getChildren().addAll(
                recommendations,
                chatbot
        );

        HBox.setHgrow(
                recommendations,
                Priority.ALWAYS
        );

        root.setCenter(content);

        return root;
    }

    private VBox createRecommendations() {

        VBox root =
                new VBox(18);

        root.setPadding(
                new Insets(10)
        );

        Label title =
                new Label(
                        "AI Recommendations"
                );

        title.setStyle(
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #17202a;"
        );

        Label subtitle =
                new Label(
                        "Smart insights and suggestions for your farm."
                );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #607080;"
        );

        VBox header =
                new VBox(4);

        header.getChildren().addAll(
                title,
                subtitle
        );

        VBox irrigationCard =
                createCard(
                        "Suggested Action",
                        "Increase irrigation",
                        "Soil moisture is low in your farm. "
                        + "Increase irrigation for better crop yield.",
                        "View Details"
                );

        VBox plantingCard =
                createCard(
                        "Best Time to Plant",
                        "Tomato",
                        "The next few days are suitable "
                        + "for planting based on current conditions.",
                        "View Calendar"
                );

        HBox cards =
                new HBox(16);

        cards.getChildren().addAll(
                irrigationCard,
                plantingCard
        );

        HBox.setHgrow(
                irrigationCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                plantingCard,
                Priority.ALWAYS
        );

        VBox other =
                new VBox(12);

        other.setPadding(
                new Insets(20)
        );

        other.setStyle(
                "-fx-background-color: #d8f1e5;" +
                "-fx-border-color: #b7e3d5;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );

        Label otherTitle =
                new Label(
                        "Other Recommendations"
                );

        otherTitle.setStyle(
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #087c69;"
        );

        Label recommendation1 =
                new Label(
                        "⚒  Use organic fertilizer for better soil health."
                );

        Label recommendation2 =
                new Label(
                        "☁  Check rainfall before spraying pesticides."
                );

        Label recommendation3 =
                new Label(
                        "▣  Check current market prices before selling."
                );

        recommendation1.setStyle(
                "-fx-font-size: 14px;"
        );

        recommendation2.setStyle(
                "-fx-font-size: 14px;"
        );

        recommendation3.setStyle(
                "-fx-font-size: 14px;"
        );

        other.getChildren().addAll(
                otherTitle,
                recommendation1,
                recommendation2,
                recommendation3
        );

        root.getChildren().addAll(
                header,
                cards,
                other
        );

        return root;
    }

    private VBox createCard(
            String smallTitle,
            String title,
            String description,
            String buttonText
    ) {

        VBox card =
                new VBox(12);

        card.setPadding(
                new Insets(20)
        );

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #b7e3d5;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );

        Label small =
                new Label(
                        smallTitle
                );

        small.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #607080;"
        );

        Label heading =
                new Label(
                        title
                );

        heading.setStyle(
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;"
        );

        Label text =
                new Label(
                        description
                );

        text.setWrapText(true);

        text.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #607080;"
        );

        Button button =
                new Button(
                        buttonText
                );

        button.setStyle(
                "-fx-background-color: #10866f;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 9 18;"
        );

        card.getChildren().addAll(
                small,
                heading,
                text,
                button
        );

        return card;
    }

    private VBox createChatbot() {

        VBox chatbot =
                new VBox();

        chatbot.setPrefWidth(
                390
        );

        chatbot.setMinWidth(
                350
        );

        chatbot.setMaxWidth(
                420
        );

        chatbot.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #cfe4dc;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );

        VBox header =
                new VBox(5);

        header.setPadding(
                new Insets(18)
        );

        Label title =
                new Label(
                        AIConfig.ASSISTANT_NAME
                );

        title.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #16866f;"
        );

        Label subtitle =
                new Label(
                        "Ask me anything about your farm."
                );

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #607080;"
        );

        header.getChildren().addAll(
                title,
                subtitle
        );

        chatBox =
                new VBox(12);

        chatBox.setPadding(
                new Insets(15)
        );

        addBotMessage(
                "Hello! I am your AgriLink AI Assistant. "
                + "How can I help you today?"
        );

        chatScroll =
                new ScrollPane(
                        chatBox
                );

        chatScroll.setFitToWidth(
                true
        );

        chatScroll.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: transparent;"
        );

        VBox.setVgrow(
                chatScroll,
                Priority.ALWAYS
        );

        HBox quickActions =
                createQuickActions();

        messageField =
                new TextField();

        messageField.setPromptText(
                "Ask about crops, soil, weather..."
        );

        messageField.setPrefHeight(
                42
        );

        messageField.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #cfe4dc;" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;" +
                "-fx-padding: 10;"
        );

        sendButton =
                new Button(
                        "➤"
                );

        sendButton.setPrefHeight(
                42
        );

        sendButton.setPrefWidth(
                50
        );

        sendButton.setStyle(
                "-fx-background-color: #10866f;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 17px;" +
                "-fx-background-radius: 7;"
        );

        HBox inputArea =
                new HBox(8);

        inputArea.setPadding(
                new Insets(12)
        );

        HBox.setHgrow(
                messageField,
                Priority.ALWAYS
        );

        inputArea.getChildren().addAll(
                messageField,
                sendButton
        );

        sendButton.setOnAction(
                e -> sendMessage()
        );

        messageField.setOnAction(
                e -> sendMessage()
        );

        Label disclaimer =
                new Label(
                        "AI responses may not always be accurate."
                );

        disclaimer.setPadding(
                new Insets(
                        0,
                        15,
                        12,
                        15
                )
        );

        disclaimer.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #71808a;"
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

    private HBox createQuickActions() {

        HBox box =
                new HBox(8);

        box.setPadding(
                new Insets(
                        5,
                        12,
                        5,
                        12
                )
        );

        Button crop =
                new Button(
                        "Best Crop"
                );

        Button weather =
                new Button(
                        "Weather"
                );

        Button soil =
                new Button(
                        "Soil Tips"
                );

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
            Button button
    ) {

        button.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #10866f;" +
                "-fx-border-color: #a8dcca;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 7 10;" +
                "-fx-font-size: 11px;"
        );
    }

    private void askQuestion(
            String question
    ) {

        addUserMessage(
                question
        );

        sendToGemini(
                question
        );
    }

    private void sendMessage() {

        String userMessage =
                messageField.getText().trim();

        if (userMessage.isEmpty()) {
            return;
        }

        addUserMessage(
                userMessage
        );

        messageField.clear();

        sendToGemini(
                userMessage
        );
    }

    private void sendToGemini(
            String userMessage
    ) {

        messageField.setDisable(
                true
        );

        sendButton.setDisable(
                true
        );

        addBotMessage(
                "Thinking..."
        );

        Thread thread =
                new Thread(() -> {

                    String answer =
                            aiController.getResponse(
                                    userMessage
                            );

                    Platform.runLater(() -> {

                        removeThinking();

                        addBotMessage(
                                answer
                        );

                        messageField.setDisable(
                                false
                        );

                        sendButton.setDisable(
                                false
                        );

                        messageField.requestFocus();

                        scrollToBottom();
                    });
                });

        thread.setDaemon(
                true
        );

        thread.start();
    }

    private void addUserMessage(
            String message
    ) {

        addMessage(
                new AIChatMessage(
                        message,
                        true
                )
        );
    }

    private void addBotMessage(
            String message
    ) {

        addMessage(
                new AIChatMessage(
                        message,
                        false
                )
        );
    }

    private void addMessage(
            AIChatMessage message
    ) {

        if (message.isUserMessage()) {

            HBox row =
                    new HBox();

            row.setAlignment(
                    Pos.CENTER_RIGHT
            );

            Label label =
                    new Label(
                            message.getMessage()
                    );

            label.setWrapText(
                    true
            );

            label.setMaxWidth(
                    280
            );

            label.setPadding(
                    new Insets(10)
            );

            label.setStyle(
                    "-fx-background-color: #dff3e9;" +
                    "-fx-background-radius: 10;" +
                    "-fx-font-size: 13px;"
            );

            row.getChildren().add(
                    label
            );

            chatBox.getChildren().add(
                    row
            );

        } else {

            HBox row =
                    new HBox(8);

            row.setAlignment(
                    Pos.CENTER_LEFT
            );

            Label icon =
                    new Label(
                            "AI"
                    );

            icon.setStyle(
                    "-fx-background-color: #dff3e9;" +
                    "-fx-background-radius: 20;" +
                    "-fx-padding: 7;" +
                    "-fx-text-fill: #10866f;" +
                    "-fx-font-weight: bold;"
            );

            Label label =
                    new Label(
                            message.getMessage()
                    );

            label.setWrapText(
                    true
            );

            label.setMaxWidth(
                    280
            );

            label.setPadding(
                    new Insets(10)
            );

            label.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #e1e8e5;" +
                    "-fx-border-radius: 10;" +
                    "-fx-background-radius: 10;" +
                    "-fx-font-size: 13px;"
            );

            row.getChildren().addAll(
                    icon,
                    label
            );

            chatBox.getChildren().add(
                    row
            );
        }

        scrollToBottom();
    }

    private void removeThinking() {

        if (chatBox.getChildren().isEmpty()) {
            return;
        }

        Node last =
                chatBox.getChildren().get(
                        chatBox.getChildren().size() - 1
                );

        if (last instanceof HBox) {

            HBox row =
                    (HBox) last;

            for (Node node :
                    row.getChildren()) {

                if (node instanceof Label) {

                    Label label =
                            (Label) node;

                    if ("Thinking..."
                            .equals(
                                    label.getText()
                            )) {

                        chatBox.getChildren()
                                .remove(last);

                        return;
                    }
                }
            }
        }
    }

    private void scrollToBottom() {

        Platform.runLater(
                () -> chatScroll.setVvalue(1.0)
        );
    }
}