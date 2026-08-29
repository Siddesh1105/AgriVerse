package com.mainproject.controller;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.mainproject.config.AIConfig;

public class AIChatController {

    private final Client client;

    public AIChatController() {

        client = Client.builder()
                .apiKey(AIConfig.API_KEY)
                .build();
    }

    public String getResponse(String question) {

        try {

            String prompt =
                    "You are AgriLink AI Assistant, "
                    + "an intelligent agriculture assistant for farmers. "
                    + "Give simple, practical and accurate answers. "
                    + "Help farmers with crop selection, irrigation, "
                    + "soil health, fertilizers, pesticides, farming "
                    + "practices, weather and market-related questions. "
                    + "Keep answers easy to understand. "
                    + "If current local information is required, "
                    + "tell the user to verify current local data.\n\n"
                    + "Farmer Question: "
                    + question;

            GenerateContentResponse response =
                    client.models.generateContent(
                            AIConfig.MODEL,
                            prompt,
                            null
                    );

            String answer = response.text();

            if (answer == null || answer.isBlank()) {

                return "Sorry, I could not generate a response.";
            }

            return answer;

        } catch (Exception e) {

            return "Gemini Error: "
                    + e.getMessage();
        }
    }
}