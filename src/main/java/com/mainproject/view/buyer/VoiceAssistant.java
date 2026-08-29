package com.mainproject.view.buyer;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import com.mainproject.config.VoiceAssistantConfig;

public class VoiceAssistant {

        private final BuyerDashboard mainController;
        private VoiceAssistantConfig.Language currentLanguage = VoiceAssistantConfig.Language.ENGLISH;

        public VoiceAssistant(BuyerDashboard controller) {
                this.mainController = controller;
        }

        public Node getView() {

                VBox root = new VBox(20);
                root.setAlignment(Pos.TOP_CENTER);
                root.setPadding(new Insets(30, 40, 30, 40));
                root.setStyle(
                                "-fx-background-color: linear-gradient(" +
                                                "to bottom right, " +
                                                "#F0FDF4, " +
                                                "#FFFFFF" +
                                                ");");

                // =====================================================
                // HEADER
                // =====================================================

                Label title = new Label("Voice Assistant");
                title.setStyle(
                                "-fx-font-size: 30px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #14532D;");

                Label subtitle = new Label("Your smart farming assistant is ready to help");
                subtitle.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-text-fill: #64748B;");

                VBox header = new VBox(5);
                header.setAlignment(Pos.CENTER);
                header.getChildren().addAll(title, subtitle);

                // =====================================================
                // LANGUAGE SELECTOR
                // =====================================================

                Label langLabel = new Label("🌐 Language:");
                langLabel.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #166534;");

                HBox langBox = new HBox(10);
                langBox.setAlignment(Pos.CENTER);
                langBox.setPadding(new Insets(10, 0, 10, 0));

                Button englishBtn = createLanguageButton("🇬🇧 English");
                Button hindiBtn = createLanguageButton("🇮🇳 हिंदी");
                Button marathiBtn = createLanguageButton("🇮🇳 मराठी");

                englishBtn.setStyle(getActiveButtonStyle());
                hindiBtn.setStyle(getInactiveButtonStyle());
                marathiBtn.setStyle(getInactiveButtonStyle());

                englishBtn.setOnAction(e -> {
                        currentLanguage = VoiceAssistantConfig.Language.ENGLISH;
                        VoiceAssistantConfig.setLanguage(currentLanguage);
                        englishBtn.setStyle(getActiveButtonStyle());
                        hindiBtn.setStyle(getInactiveButtonStyle());
                        marathiBtn.setStyle(getInactiveButtonStyle());
                });

                hindiBtn.setOnAction(e -> {
                        currentLanguage = VoiceAssistantConfig.Language.HINDI;
                        VoiceAssistantConfig.setLanguage(currentLanguage);
                        englishBtn.setStyle(getInactiveButtonStyle());
                        hindiBtn.setStyle(getActiveButtonStyle());
                        marathiBtn.setStyle(getInactiveButtonStyle());
                });

                marathiBtn.setOnAction(e -> {
                        currentLanguage = VoiceAssistantConfig.Language.MARATHI;
                        VoiceAssistantConfig.setLanguage(currentLanguage);
                        englishBtn.setStyle(getInactiveButtonStyle());
                        hindiBtn.setStyle(getInactiveButtonStyle());
                        marathiBtn.setStyle(getActiveButtonStyle());
                });

                langBox.getChildren().addAll(langLabel, englishBtn, hindiBtn, marathiBtn);

                // =====================================================
                // GREETING
                // =====================================================

                Label greeting = new Label("Hello! 👋");
                greeting.setStyle(
                                "-fx-font-size: 21px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #166534;");

                Label question = new Label("How can I help you today?");
                question.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-text-fill: #475569;");

                VBox greetingBox = new VBox(5);
                greetingBox.setAlignment(Pos.CENTER);
                greetingBox.getChildren().addAll(greeting, question);

                // =====================================================
                // MICROPHONE CONTAINER
                // =====================================================

                StackPane micContainer = new StackPane();

                Circle outerCircle = new Circle(82);
                outerCircle.setFill(Color.web("#DCFCE7"));

                Circle middleCircle = new Circle(67);
                middleCircle.setFill(Color.web("#BBF7D0"));

                Button micButton = new Button("🎙");
                micButton.setStyle(
                                "-fx-font-size: 42px;" +
                                                "-fx-background-color: #166534;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-pref-width: 110px;" +
                                                "-fx-pref-height: 110px;" +
                                                "-fx-background-radius: 60px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(" +
                                                "gaussian," +
                                                "rgba(22,101,52,0.35)," +
                                                "15,0.3,0,5" +
                                                ");");

                micContainer.getChildren().addAll(outerCircle, middleCircle, micButton);

                // =====================================================
                // STATUS
                // =====================================================

                Label status = new Label("Tap the microphone to speak");
                status.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #475569;");
                status.setWrapText(true);
                status.setAlignment(Pos.CENTER);

                // =====================================================
                // RESULT BOX
                // =====================================================

                Label resultTitle = new Label("Your Voice");
                resultTitle.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #166534;");

                Label result = new Label("Your spoken text will appear here");
                result.setWrapText(true);
                result.setAlignment(Pos.CENTER);
                result.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #475569;");

                VBox resultBox = new VBox(7);
                resultBox.setAlignment(Pos.CENTER);
                resultBox.setPadding(new Insets(12, 25, 12, 25));
                resultBox.setMaxWidth(600);
                resultBox.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 15px;" +
                                                "-fx-border-color: #DCFCE7;" +
                                                "-fx-border-radius: 15px;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-effect: dropshadow(" +
                                                "gaussian," +
                                                "rgba(0,0,0,0.08)," +
                                                "10,0.2,0,3" +
                                                ");");
                resultBox.getChildren().addAll(resultTitle, result);

                // =====================================================
                // AI ANSWER BOX
                // =====================================================

                Label answerTitle = new Label("🤖 AgriVerse AI");
                answerTitle.setStyle(
                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #14532D;");

                Label answer = new Label("AI answer will appear here");
                answer.setWrapText(true);
                answer.setAlignment(Pos.CENTER);
                answer.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-text-fill: #1E293B;");

                VBox answerBox = new VBox(8);
                answerBox.setAlignment(Pos.CENTER);
                answerBox.setPadding(new Insets(15, 25, 15, 25));
                answerBox.setMaxWidth(600);
                answerBox.setStyle(
                                "-fx-background-color: #F0FDF4;" +
                                                "-fx-background-radius: 15px;" +
                                                "-fx-border-color: #86EFAC;" +
                                                "-fx-border-radius: 15px;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-effect: dropshadow(" +
                                                "gaussian," +
                                                "rgba(0,0,0,0.08)," +
                                                "10,0.2,0,3" +
                                                ");");
                answerBox.getChildren().addAll(answerTitle, answer);

                // =====================================================
                // ANIMATION
                // =====================================================

                ScaleTransition pulse = new ScaleTransition(Duration.millis(700), micButton);
                pulse.setFromX(1.0);
                pulse.setFromY(1.0);
                pulse.setToX(1.08);
                pulse.setToY(1.08);
                pulse.setCycleCount(ScaleTransition.INDEFINITE);
                pulse.setAutoReverse(true);

                // =====================================================
                // MICROPHONE BUTTON ACTION
                // =====================================================

                micButton.setOnAction(e -> {

                        if (micButton.isDisabled()) {
                                return;
                        }

                        micButton.setDisable(true);

                        answer.setText("Waiting for your question...");
                        result.setText("Listening to your voice...");
                        status.setText("🎙 Listening... Speak now!");
                        status.setStyle(
                                        "-fx-font-size: 14px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-text-fill: #DC2626;");

                        micButton.setStyle(
                                        "-fx-font-size: 42px;" +
                                                        "-fx-background-color: #DC2626;" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-pref-width: 110px;" +
                                                        "-fx-pref-height: 110px;" +
                                                        "-fx-background-radius: 60px;" +
                                                        "-fx-cursor: hand;" +
                                                        "-fx-effect: dropshadow(" +
                                                        "gaussian," +
                                                        "rgba(220,38,38,0.40)," +
                                                        "18,0.3,0,5" +
                                                        ");");

                        pulse.play();

                        Thread voiceThread = new Thread(() -> {

                                try {

                                        System.out.println();
                                        System.out.println("================================");
                                        System.out.println("🎙 VOICE ASSISTANT STARTED");
                                        System.out.println("Language: " + currentLanguage.name());
                                        System.out.println("================================");

                                        // STEP 1 - RECORD AUDIO
                                        Platform.runLater(() -> {
                                                status.setText("🎙 Recording... Speak now");
                                                result.setText("Listening...");
                                        });

                                        System.out.println("🎤 Starting microphone...");

                                        byte[] audio = VoiceAssistantConfig.recordAudio(5);

                                        if (audio == null || audio.length == 0) {
                                                throw new Exception(
                                                                "No audio was recorded. Please check your microphone.");
                                        }

                                        System.out.println("================================");
                                        System.out.println("✅ AUDIO RECORDED");
                                        System.out.println("Audio size: " + audio.length + " bytes");
                                        System.out.println("================================");

                                        // STEP 2 - GOOGLE SPEECH-TO-TEXT
                                        Platform.runLater(() -> {
                                                status.setText("☁ Sending to Speech-to-Text...");
                                                status.setStyle(
                                                                "-fx-font-size: 14px;" +
                                                                                "-fx-font-weight: bold;" +
                                                                                "-fx-text-fill: #2563EB;");
                                                result.setText("Converting speech to text...");
                                        });

                                        System.out.println();
                                        System.out.println("☁ Sending audio to Google Cloud...");

                                        String transcript = VoiceAssistantConfig.transcribe(audio);

                                        System.out.println();
                                        System.out.println("================================");
                                        System.out.println("📝 TRANSCRIPT:");
                                        System.out.println(transcript);
                                        System.out.println("================================");

                                        if (transcript == null || transcript.trim().isEmpty()) {
                                                throw new Exception("Could not understand your voice.");
                                        }

                                        // STEP 3 - SHOW TRANSCRIPT
                                        Platform.runLater(() -> {
                                                result.setText(transcript);
                                                status.setText("🤖 Asking AI...");
                                                status.setStyle(
                                                                "-fx-font-size: 14px;" +
                                                                                "-fx-font-weight: bold;" +
                                                                                "-fx-text-fill: #7C3AED;");
                                                answer.setText("AI is thinking...");
                                        });

                                        // STEP 4 - GROQ AI
                                        System.out.println();
                                        System.out.println("🤖 Sending to Groq AI...");

                                        String aiAnswer = VoiceAssistantConfig.askAssistant(transcript);

                                        System.out.println();
                                        System.out.println("================================");
                                        System.out.println("🤖 AI ANSWER:");
                                        System.out.println(aiAnswer);
                                        System.out.println("================================");

                                        if (aiAnswer == null || aiAnswer.trim().isEmpty()) {
                                                throw new Exception("AI returned an empty answer.");
                                        }

                                        // STEP 5 - UPDATE UI
                                        Platform.runLater(() -> {
                                                pulse.stop();
                                                micButton.setDisable(false);
                                                status.setText("✅ Answer received");
                                                status.setStyle(
                                                                "-fx-font-size: 14px;" +
                                                                                "-fx-font-weight: bold;" +
                                                                                "-fx-text-fill: #166534;");
                                                answer.setText(aiAnswer);
                                                resetMicButton(micButton);
                                        });

                                        // STEP 6 - TEXT TO SPEECH
                                        System.out.println();
                                        System.out.println("🔊 Converting answer to speech...");

                                        VoiceAssistantConfig.speak(aiAnswer);

                                        System.out.println();
                                        System.out.println("================================");
                                        System.out.println("✅ VOICE ASSISTANT COMPLETED");
                                        System.out.println("================================");

                                } catch (Exception ex) {

                                        System.err.println();
                                        System.err.println("================================");
                                        System.err.println("❌ VOICE ASSISTANT ERROR");
                                        System.err.println("================================");
                                        System.err.println("Error: " + ex.getMessage());

                                        ex.printStackTrace();

                                        Platform.runLater(() -> {
                                                pulse.stop();
                                                micButton.setDisable(false);

                                                status.setText("❌ Voice Assistant Error");
                                                status.setStyle(
                                                                "-fx-font-size: 14px;" +
                                                                                "-fx-font-weight: bold;" +
                                                                                "-fx-text-fill: #DC2626;");

                                                String errorMessage = ex.getMessage();
                                                if (errorMessage == null || errorMessage.isBlank()) {
                                                        errorMessage = "Unknown error occurred.";
                                                }

                                                answer.setText(errorMessage);
                                                resetMicButton(micButton);

                                                Alert error = new Alert(Alert.AlertType.ERROR);
                                                error.setTitle("Voice Assistant Error");
                                                error.setHeaderText("Something went wrong");
                                                error.setContentText(errorMessage);
                                                error.show();
                                        });
                                }

                        });

                        voiceThread.setName("AgriVerse-Voice-Assistant");
                        voiceThread.setDaemon(true);
                        voiceThread.start();
                });

                // =====================================================
                // SUGGESTIONS
                // =====================================================

                Label suggestionTitle = new Label("Try saying");
                suggestionTitle.setStyle(
                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1E293B;");

                HBox cardBox = new HBox(15);
                cardBox.setAlignment(Pos.CENTER);
                cardBox.getChildren().addAll(
                                createSuggestionCard("👨‍🌾", "Find Farmers", "Show me live farmers"),
                                createSuggestionCard("🍅", "Find Products", "Find tomato under ₹30"),
                                createSuggestionCard("📦", "Track Order", "Track my order"));

                // =====================================================
                // ADD ALL TO ROOT
                // =====================================================

                root.getChildren().addAll(
                                header,
                                langBox,
                                greetingBox,
                                micContainer,
                                status,
                                resultBox,
                                answerBox,
                                suggestionTitle,
                                cardBox);

                return root;
        }

        // =====================================================
        // HELPER METHODS
        // =====================================================

        private String getActiveButtonStyle() {
                return "-fx-padding: 10px 20px;" +
                                "-fx-font-size: 13px;" +
                                "-fx-background-color: #166534;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8px;" +
                                "-fx-cursor: hand;";
        }

        private String getInactiveButtonStyle() {
                return "-fx-padding: 10px 20px;" +
                                "-fx-font-size: 13px;" +
                                "-fx-background-color: #E8F5E9;" +
                                "-fx-text-fill: #166534;" +
                                "-fx-background-radius: 8px;" +
                                "-fx-cursor: hand;";
        }

        private Button createLanguageButton(String text) {
                Button btn = new Button(text);
                btn.setStyle(getInactiveButtonStyle());
                return btn;
        }

        private void resetMicButton(Button micButton) {
                micButton.setStyle(
                                "-fx-font-size: 42px;" +
                                                "-fx-background-color: #166534;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-pref-width: 110px;" +
                                                "-fx-pref-height: 110px;" +
                                                "-fx-background-radius: 60px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(" +
                                                "gaussian," +
                                                "rgba(22,101,52,0.35)," +
                                                "15,0.3,0,5" +
                                                ");");
        }

        private VBox createSuggestionCard(String icon, String title, String command) {

                VBox card = new VBox(8);
                card.setAlignment(Pos.CENTER);
                card.setPadding(new Insets(18));
                card.setPrefWidth(180);
                card.setPrefHeight(120);
                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 15px;" +
                                                "-fx-border-color: #DCFCE7;" +
                                                "-fx-border-radius: 15px;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-effect: dropshadow(" +
                                                "gaussian," +
                                                "rgba(0,0,0,0.08)," +
                                                "10,0.2,0,3" +
                                                ");");

                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 25px;");

                Label titleLabel = new Label(title);
                titleLabel.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #166534;");

                Label commandLabel = new Label(command);
                commandLabel.setStyle(
                                "-fx-font-size: 11px;" +
                                                "-fx-text-fill: #64748B;");
                commandLabel.setWrapText(true);
                commandLabel.setAlignment(Pos.CENTER);

                card.getChildren().addAll(iconLabel, titleLabel, commandLabel);
                return card;
        }
}
