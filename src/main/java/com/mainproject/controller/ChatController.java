package com.mainproject.controller;

import java.util.List;

import com.google.cloud.firestore.ListenerRegistration;

import com.mainproject.dao.ChatDAO;
import com.mainproject.model.ChatConversation;
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

        if (senderEmail == null ||
                receiverEmail == null ||
                message == null ||
                senderEmail.trim().isEmpty() ||
                receiverEmail.trim().isEmpty() ||
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
            String email1,
            String email2) {

        return chatDAO.getMessages(
                email1,
                email2
        );
    }

    // =====================================================
    // REAL TIME MESSAGES
    // =====================================================

    public ListenerRegistration listenForMessages(
            String email1,
            String email2,
            ChatDAO.MessageListener listener) {

        return chatDAO.listenForMessages(
                email1,
                email2,
                listener
        );
    }

    // =====================================================
    // GET USER CHATS
    // =====================================================

    public List<ChatConversation> getUserChats(
            String userEmail) {

        return chatDAO.getUserChats(userEmail);
    }

    // =====================================================
    // REAL TIME CHAT LIST
    // =====================================================

    public ListenerRegistration listenForUserChats(
            String userEmail,
            ChatDAO.ConversationListener listener) {

        return chatDAO.listenForUserChats(
                userEmail,
                listener
        );
    }
}