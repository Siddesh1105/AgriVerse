package com.mainproject.controller;

import com.mainproject.dao.ReviewDAO;
import com.mainproject.model.Review;
import java.util.List;

public class ReviewController {
    private final ReviewDAO reviewDAO = new ReviewDAO();
    public boolean submitReview(Review review){return reviewDAO.submitReview(review);}
    public boolean hasReviewed(String reviewerEmail,String transactionId){return reviewDAO.hasReviewed(reviewerEmail,transactionId);}
    public List<Review> getAllReviews(){return reviewDAO.getAllReviews();}
    public List<Review> getReviewsFor(String email){return reviewDAO.getReviewsFor(email);}
    public double getAverageRating(String email){return reviewDAO.getAverageRating(email);}
    public boolean updateStatus(String reviewId,String status){return reviewDAO.updateStatus(reviewId,status);}
}
