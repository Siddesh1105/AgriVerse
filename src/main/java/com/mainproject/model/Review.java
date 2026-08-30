package com.mainproject.model;

import java.util.Date;

/** Feedback exchanged only after a completed product order or equipment rental. */
public class Review {
    private String reviewId;
    private String reviewerEmail;
    private String reviewerName;
    private String reviewerRole;
    private String revieweeEmail;
    private String revieweeName;
    private String revieweeRole;
    private String transactionId;
    private String reviewType; // PRODUCT_ORDER / EQUIPMENT_RENTAL
    private int rating;
    private String comment;
    private String status; // ACTIVE / HIDDEN
    private Date createdAt;

    public Review() {}

    public String getReviewId(){return reviewId;} public void setReviewId(String v){reviewId=v;}
    public String getReviewerEmail(){return reviewerEmail;} public void setReviewerEmail(String v){reviewerEmail=v;}
    public String getReviewerName(){return reviewerName;} public void setReviewerName(String v){reviewerName=v;}
    public String getReviewerRole(){return reviewerRole;} public void setReviewerRole(String v){reviewerRole=v;}
    public String getRevieweeEmail(){return revieweeEmail;} public void setRevieweeEmail(String v){revieweeEmail=v;}
    public String getRevieweeName(){return revieweeName;} public void setRevieweeName(String v){revieweeName=v;}
    public String getRevieweeRole(){return revieweeRole;} public void setRevieweeRole(String v){revieweeRole=v;}
    public String getTransactionId(){return transactionId;} public void setTransactionId(String v){transactionId=v;}
    public String getReviewType(){return reviewType;} public void setReviewType(String v){reviewType=v;}
    public int getRating(){return rating;} public void setRating(int v){rating=v;}
    public String getComment(){return comment;} public void setComment(String v){comment=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public Date getCreatedAt(){return createdAt;} public void setCreatedAt(Date v){createdAt=v;}
}
