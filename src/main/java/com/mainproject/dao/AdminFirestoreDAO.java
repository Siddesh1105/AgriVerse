package com.mainproject.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.mainproject.config.FirebaseConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Small generic Firestore DAO used only by admin modules that did not have
 * a domain DAO in the original application. Existing domain DAOs are left
 * untouched and continue to be used by farmer/buyer flows.
 */
public class AdminFirestoreDAO {

    private final Firestore db;

    public AdminFirestoreDAO() {
        db = FirebaseConfig.getFirestore();
    }

    public List<Map<String, Object>> getAll(String collection) {
        List<Map<String, Object>> rows = new ArrayList<>();
        try {
            QuerySnapshot snapshot = db.collection(collection).get().get();
            for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {
                Map<String, Object> row = new HashMap<>();
                if (doc.getData() != null) row.putAll(doc.getData());
                row.put("_id", doc.getId());
                rows.add(row);
            }
        } catch (Exception e) {
            System.out.println("Admin data load error [" + collection + "]: " + e.getMessage());
        }
        return rows;
    }

    public Map<String, Object> get(String collection, String id) {
        try {
            DocumentSnapshot doc = db.collection(collection).document(id).get().get();
            if (!doc.exists()) return null;
            Map<String, Object> row = new HashMap<>();
            if (doc.getData() != null) row.putAll(doc.getData());
            row.put("_id", doc.getId());
            return row;
        } catch (Exception e) {
            System.out.println("Admin data read error [" + collection + "]: " + e.getMessage());
            return null;
        }
    }

    public String create(String collection, Map<String, Object> data) {
        try {
            Map<String, Object> copy = new HashMap<>(data);
            copy.putIfAbsent("createdAt", new Date());
            return db.collection(collection).add(copy).get().getId();
        } catch (Exception e) {
            System.out.println("Admin data create error [" + collection + "]: " + e.getMessage());
            return null;
        }
    }

    public boolean set(String collection, String id, Map<String, Object> data) {
        try {
            db.collection(collection).document(id).set(data).get();
            return true;
        } catch (Exception e) {
            System.out.println("Admin data set error [" + collection + "]: " + e.getMessage());
            return false;
        }
    }

    public boolean update(String collection, String id, Map<String, Object> data) {
        try {
            db.collection(collection).document(id).update(data).get();
            return true;
        } catch (Exception e) {
            System.out.println("Admin data update error [" + collection + "]: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String collection, String id) {
        try {
            db.collection(collection).document(id).delete().get();
            return true;
        } catch (Exception e) {
            System.out.println("Admin data delete error [" + collection + "]: " + e.getMessage());
            return false;
        }
    }

    public void audit(String admin, String action, String module, String recordId) {
        Map<String, Object> row = new HashMap<>();
        row.put("admin", admin == null || admin.isBlank() ? "Super Admin" : admin);
        row.put("action", action == null ? "" : action);
        row.put("module", module == null ? "" : module);
        row.put("recordId", recordId == null ? "" : recordId);
        row.put("ipAddress", "Admin Console");
        row.put("createdAt", new Date());
        create("adminAuditLogs", row);
    }
}
