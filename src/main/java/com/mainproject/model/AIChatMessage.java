package com.mainproject.model;

public class AIChatMessage {

    private final String message;
    private final boolean userMessage;

    public AIChatMessage(String message, boolean userMessage) {
        this.message = message;
        this.userMessage = userMessage;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUserMessage() {
        return userMessage;
    }
}