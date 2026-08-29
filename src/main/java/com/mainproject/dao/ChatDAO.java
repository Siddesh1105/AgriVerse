package com.mainproject.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.SetOptions;

import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.ChatConversation;
import com.mainproject.model.ChatMessage;

public class ChatDAO {

    private final Firestore db;

    public ChatDAO() {
        db = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // CREATE UNIQUE CHAT ID
    // SAME FOR BOTH USERS
    // =====================================================

    public String getChatId(
            String email1,
            String email2) {

        String[] emails = {
                email1.toLowerCase().trim(),
                email2.toLowerCase().trim()
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

            senderEmail = senderEmail.trim().toLowerCase();
            receiverEmail = receiverEmail.trim().toLowerCase();

            String chatId = getChatId(
                    senderEmail,
                    receiverEmail
            );

            DocumentReference chatRef =
                    db.collection("chats")
                            .document(chatId);

            DocumentReference messageRef =
                    chatRef.collection("messages")
                            .document();

            Timestamp now = Timestamp.now();

            // =================================================
            // CREATE MESSAGE
            // =================================================

            ChatMessage chatMessage =
                    new ChatMessage(
                            senderEmail,
                            receiverEmail,
                            message
                    );

            chatMessage.setMessageId(
                    messageRef.getId()
            );

            chatMessage.setTimestamp(now);

            // =================================================
            // CREATE / UPDATE CHAT
            // =================================================

            ChatConversation conversation =
                    new ChatConversation(
                            chatId,
                            Arrays.asList(
                                    senderEmail,
                                    receiverEmail
                            ),
                            message,
                            senderEmail,
                            now
                    );

            chatRef.set(
                    conversation,
                    SetOptions.merge()
            ).get();

            // =================================================
            // SAVE MESSAGE
            // =================================================

            messageRef.set(chatMessage).get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET MESSAGES
    // =====================================================

    public List<ChatMessage> getMessages(
            String email1,
            String email2) {

        List<ChatMessage> messages =
                new ArrayList<>();

        try {

            String chatId =
                    getChatId(email1, email2);

            List<QueryDocumentSnapshot> documents =
                    db.collection("chats")
                            .document(chatId)
                            .collection("messages")
                            .orderBy(
                                    "timestamp",
                                    Query.Direction.ASCENDING
                            )
                            .get()
                            .get()
                            .getDocuments();

            for (QueryDocumentSnapshot document
                    : documents) {

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
    // REAL TIME MESSAGE LISTENER
    // =====================================================

    public ListenerRegistration listenForMessages(
            String email1,
            String email2,
            MessageListener listener) {

        String chatId =
                getChatId(email1, email2);

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
    // GET ALL CHATS OF USER
    // =====================================================

    public List<ChatConversation> getUserChats(
            String userEmail) {

        List<ChatConversation> chats =
                new ArrayList<>();

        try {

            List<QueryDocumentSnapshot> documents =
                    db.collection("chats")
                            .whereArrayContains(
                                    "participants",
                                    userEmail.toLowerCase().trim()
                            )
                            .orderBy(
                                    "updatedAt",
                                    Query.Direction.DESCENDING
                            )
                            .get()
                            .get()
                            .getDocuments();

            for (QueryDocumentSnapshot document
                    : documents) {

                ChatConversation conversation =
                        document.toObject(
                                ChatConversation.class
                        );

                conversation.setChatId(
                        document.getId()
                );

                chats.add(conversation);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return chats;
    }

    // =====================================================
    // REAL TIME USER CHAT LIST
    // =====================================================

    public ListenerRegistration listenForUserChats(
            String userEmail,
            ConversationListener listener) {

        return db.collection("chats")
                .whereArrayContains(
                        "participants",
                        userEmail.toLowerCase().trim()
                )
                .orderBy(
                        "updatedAt",
                        Query.Direction.DESCENDING
                )
                .addSnapshotListener(
                        (snapshots, error) -> {

                            if (error != null) {

                                error.printStackTrace();
                                return;
                            }

                            List<ChatConversation> chats =
                                    new ArrayList<>();

                            if (snapshots != null) {

                                for (DocumentSnapshot document
                                        : snapshots.getDocuments()) {

                                    ChatConversation conversation =
                                            document.toObject(
                                                    ChatConversation.class
                                            );

                                    if (conversation != null) {

                                        conversation.setChatId(
                                                document.getId()
                                        );

                                        chats.add(conversation);
                                    }
                                }
                            }

                            listener.onChatsUpdated(chats);
                        }
                );
    }

    // =====================================================
    // MESSAGE LISTENER
    // =====================================================

    public interface MessageListener {

        void onMessagesUpdated(
                List<ChatMessage> messages
        );
    }

    // =====================================================
    // CONVERSATION LISTENER
    // =====================================================

    public interface ConversationListener {

        void onChatsUpdated(
                List<ChatConversation> chats
        );
    }
}