package com.mainproject.dao;

import com.google.cloud.firestore.*;
import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.Review;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ReviewDAO {
    private static final String COLLECTION = "reviews";
    private final Firestore db = FirebaseConfig.getFirestore();

    public boolean submitReview(Review review) {
        try {
            if (review == null || blank(review.getReviewerEmail()) || blank(review.getRevieweeEmail())
                    || blank(review.getTransactionId()) || review.getRating() < 1 || review.getRating() > 5) return false;
            if (hasReviewed(review.getReviewerEmail(), review.getTransactionId())) return false;
            DocumentReference ref = db.collection(COLLECTION).document();
            review.setReviewId(ref.getId());
            if (blank(review.getStatus())) review.setStatus("ACTIVE");
            if (review.getCreatedAt() == null) review.setCreatedAt(new Date());
            ref.set(review).get(10, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean hasReviewed(String reviewerEmail, String transactionId) {
        try {
            QuerySnapshot snap = db.collection(COLLECTION).whereEqualTo("reviewerEmail", reviewerEmail)
                    .whereEqualTo("transactionId", transactionId).get().get(10, TimeUnit.SECONDS);
            return !snap.isEmpty();
        } catch (Exception e) { return false; }
    }

    public List<Review> getAllReviews() { return read(db.collection(COLLECTION)); }
    public List<Review> getReviewsFor(String revieweeEmail) {
        return read(db.collection(COLLECTION).whereEqualTo("revieweeEmail", revieweeEmail));
    }

    public boolean updateStatus(String reviewId, String status) {
        try { db.collection(COLLECTION).document(reviewId).update("status", status).get(10, TimeUnit.SECONDS); return true; }
        catch (Exception e) { e.printStackTrace(); return false; }
    }

    public double getAverageRating(String revieweeEmail) {
        List<Review> list = getReviewsFor(revieweeEmail); double sum=0; int count=0;
        for (Review r:list) if (r != null && "ACTIVE".equalsIgnoreCase(r.getStatus())) { sum += r.getRating(); count++; }
        return count == 0 ? 0 : sum / count;
    }

    private List<Review> read(Query query) {
        List<Review> list = new ArrayList<>();
        try {
            QuerySnapshot snap = query.get().get(10, TimeUnit.SECONDS);
            for (QueryDocumentSnapshot d : snap.getDocuments()) { Review r=d.toObject(Review.class); r.setReviewId(d.getId()); list.add(r); }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    private boolean blank(String s){return s==null||s.trim().isEmpty();}
}
