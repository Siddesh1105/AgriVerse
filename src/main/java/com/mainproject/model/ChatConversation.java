package com.mainproject.model;

import com.google.cloud.Timestamp;
import java.util.List;

public class ChatConversation {

    private String chatId;

    private List<String> participants;

    private String lastMessage;

    private String lastSenderEmail;

    private Timestamp updatedAt;

    // Required by Firestore
    public ChatConversation() {
    }

    public ChatConversation(
            String chatId,
            List<String> participants,
            String lastMessage,
            String lastSenderEmail,
            Timestamp updatedAt) {

        this.chatId = chatId;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.lastSenderEmail = lastSenderEmail;
        this.updatedAt = updatedAt;
    }

    // =====================================================
    // CHAT ID
    // =====================================================

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    // =====================================================
    // PARTICIPANTS
    // =====================================================

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    // =====================================================
    // LAST MESSAGE
    // =====================================================

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    // =====================================================
    // LAST SENDER
    // =====================================================

    public String getLastSenderEmail() {
        return lastSenderEmail;
    }

    public void setLastSenderEmail(String lastSenderEmail) {
        this.lastSenderEmail = lastSenderEmail;
    }

    // =====================================================
    // UPDATED AT
    // =====================================================

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}