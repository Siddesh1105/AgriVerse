package com.mainproject.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.ChatMessage;

public class ChatDAO {

    private final Firestore db;

    public ChatDAO() {
        db = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // CREATE UNIQUE CHAT ID
    // SAME ID FOR BUYER AND FARMER
    // =====================================================

    public String getChatId(
            String userEmail1,
            String userEmail2) {

        String[] emails = {
                userEmail1.toLowerCase().trim(),
                userEmail2.toLowerCase().trim()
        };

        Arrays.sort(emails);

        return emails[0] + "_" + emails[1];
    }

    // =====================================================
    // SEND MESSAGE
    // =====================================================

    public boolean sendMessage(
            String senderEmail,
            String receiverEmail,
            String message) {

        try {

            String chatId = getChatId(
                    senderEmail,
                    receiverEmail
            );

            DocumentReference chatRef = db
                    .collection("chats")
                    .document(chatId);

            ChatMessage chatMessage =
                    new ChatMessage(
                            senderEmail,
                            receiverEmail,
                            message
                    );

            DocumentReference messageRef = chatRef
                    .collection("messages")
                    .document();

            chatMessage.setMessageId(
                    messageRef.getId()
            );

            // Save chat information
            chatRef.set(
                    new java.util.HashMap<String, Object>() {{
                        put("chatId", chatId);
                        put("user1", senderEmail);
                        put("user2", receiverEmail);
                    }},
                    com.google.cloud.firestore.SetOptions.merge()
            );

            // Save message
            messageRef.set(chatMessage).get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    // =====================================================
    // GET ALL MESSAGES
    // =====================================================

    public List<ChatMessage> getMessages(
            String userEmail1,
            String userEmail2) {

        List<ChatMessage> messages =
                new ArrayList<>();

        try {

            String chatId = getChatId(
                    userEmail1,
                    userEmail2
            );

            CollectionReference messagesRef =
                    db.collection("chats")
                            .document(chatId)
                            .collection("messages");

            List<QueryDocumentSnapshot> documents =
                    messagesRef
                            .orderBy(
                                    "timestamp",
                                    Query.Direction.ASCENDING
                            )
                            .get()
                            .get()
                            .getDocuments();

            for (QueryDocumentSnapshot document : documents) {

                ChatMessage message =
                        document.toObject(
                                ChatMessage.class
                        );

                message.setMessageId(
                        document.getId()
                );

                messages.add(message);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return messages;
    }

    // =====================================================
    // REAL-TIME MESSAGE LISTENER
    // =====================================================

    public ListenerRegistration listenForMessages(
            String userEmail1,
            String userEmail2,
            MessageListener listener) {

        String chatId = getChatId(
                userEmail1,
                userEmail2
        );

        return db.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy(
                        "timestamp",
                        Query.Direction.ASCENDING
                )
                .addSnapshotListener(
                        (snapshots, error) -> {

                            if (error != null) {

                                error.printStackTrace();
                                return;
                            }

                            List<ChatMessage> messages =
                                    new ArrayList<>();

                            if (snapshots != null) {

                                for (DocumentSnapshot document
                                        : snapshots.getDocuments()) {

                                    ChatMessage message =
                                            document.toObject(
                                                    ChatMessage.class
                                            );

                                    if (message != null) {

                                        message.setMessageId(
                                                document.getId()
                                        );

                                        messages.add(message);
                                    }
                                }
                            }

                            listener.onMessagesUpdated(
                                    messages
                            );
                        }
                );
    }

    // =====================================================
    // LISTENER INTERFACE
    // =====================================================

    public interface MessageListener {

        void onMessagesUpdated(
                List<ChatMessage> messages
        );
    }
}