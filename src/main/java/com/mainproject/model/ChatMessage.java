package com.mainproject.model;

import com.google.cloud.Timestamp;

public class ChatMessage {

    private String messageId;

    private String senderEmail;
    private String receiverEmail;

    private String message;

    private Timestamp timestamp;

    // Required by Firestore
    public ChatMessage() {
    }

    public ChatMessage(
            String senderEmail,
            String receiverEmail,
            String message) {

        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.message = message;

        this.timestamp = Timestamp.now();
    }

    // =====================================================
    // MESSAGE ID
    // =====================================================

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    // =====================================================
    // SENDER
    // =====================================================

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    // =====================================================
    // RECEIVER
    // =====================================================

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    // =====================================================
    // MESSAGE
    // =====================================================

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // =====================================================
    // TIMESTAMP
    // =====================================================

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}