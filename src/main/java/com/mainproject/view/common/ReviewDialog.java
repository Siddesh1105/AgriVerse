package com.mainproject.view.common;

import com.mainproject.controller.ReviewController;
import com.mainproject.model.Review;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public final class ReviewDialog {
    private ReviewDialog() {}
    public static void show(String reviewerEmail, String reviewerName, String reviewerRole,
                            String revieweeEmail, String revieweeName, String revieweeRole,
                            String transactionId, String reviewType) {
        ReviewController controller = new ReviewController();
        if (controller.hasReviewed(reviewerEmail, transactionId)) {
            alert(Alert.AlertType.INFORMATION, "Review already submitted", "You have already submitted feedback for this completed transaction."); return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Leave a Review");
        dialog.setHeaderText("Rate your experience with " + (blank(revieweeName) ? revieweeEmail : revieweeName));
        ComboBox<Integer> rating = new ComboBox<>();
        rating.getItems().addAll(5,4,3,2,1); rating.setValue(5);
        TextArea comment = new TextArea(); comment.setPromptText("Write your feedback (optional)..."); comment.setWrapText(true); comment.setPrefRowCount(5);
        VBox box = new VBox(10, new Label("Rating (1 to 5 stars)"), rating, new Label("Review"), comment);
        box.setPadding(new Insets(15)); dialog.getDialogPane().setContent(box);
        ButtonType submit = new ButtonType("Submit Review", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, submit);
        dialog.showAndWait().ifPresent(result -> {
            if (result != submit) return;
            Review r = new Review();
            r.setReviewerEmail(reviewerEmail); r.setReviewerName(reviewerName); r.setReviewerRole(reviewerRole);
            r.setRevieweeEmail(revieweeEmail); r.setRevieweeName(revieweeName); r.setRevieweeRole(revieweeRole);
            r.setTransactionId(transactionId); r.setReviewType(reviewType); r.setRating(rating.getValue()); r.setComment(comment.getText().trim()); r.setStatus("ACTIVE");
            boolean ok = controller.submitReview(r);
            alert(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR, ok ? "Thank you!" : "Unable to submit", ok ? "Your feedback has been submitted successfully." : "This review could not be saved. It may already exist.");
        });
    }
    private static boolean blank(String s){return s==null||s.trim().isEmpty();}
    private static void alert(Alert.AlertType t,String title,String text){Alert a=new Alert(t,text,ButtonType.OK);a.setTitle(title);a.setHeaderText(null);a.showAndWait();}
}
