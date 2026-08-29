package com.mainproject.controller;

import java.util.List;

import com.google.cloud.firestore.ListenerRegistration;

import com.mainproject.dao.ChatDAO;
import com.mainproject.model.ChatMessage;

public class ChatController {

    private final ChatDAO chatDAO;

    public ChatController() {

        chatDAO = new ChatDAO();
    }

    // =====================================================
    // SEND MESSAGE
    // =====================================================

    public boolean sendMessage(
            String senderEmail,
            String receiverEmail,
            String message) {

        if (message == null ||
                message.trim().isEmpty()) {

            return false;
        }

        return chatDAO.sendMessage(
                senderEmail,
                receiverEmail,
                message.trim()
        );
    }

    // =====================================================
    // GET MESSAGES
    // =====================================================

    public List<ChatMessage> getMessages(
            String userEmail1,
            String userEmail2) {

        return chatDAO.getMessages(
                userEmail1,
                userEmail2
        );
    }

    // =====================================================
    // REAL-TIME CHAT
    // =====================================================

    public ListenerRegistration listenForMessages(
            String userEmail1,
            String userEmail2,
            ChatDAO.MessageListener listener) {

        return chatDAO.listenForMessages(
                userEmail1,
                userEmail2,
                listener
        );
    }
}