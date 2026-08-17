package com.mainproject.model;

public class ChatMessage {

    private final String message;
    private final boolean user;

    public ChatMessage(String message, boolean user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return user;
    }
}