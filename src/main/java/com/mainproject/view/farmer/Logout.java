package com.mainproject.view.farmer;



import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Logout {

    public static void show(StackPane rootPane, Runnable onCancel) {
        // Semi-transparent backdrop overlay
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.35);");

        // Modal Box
        VBox modal = new VBox(15);
        modal.setMaxSize(340, 220);
        modal.setAlignment(Pos.CENTER);
        modal.setPadding(new Insets(25));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16px; -fx-border-color: #A2D9CE; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 12, 0, 0, 4);");

        Label icon = new Label("⏻");
        icon.setStyle("-fx-font-size: 38px; -fx-text-fill: #117864;");

        Label title = new Label("Logout?");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");

        Label sub = new Label("Are you sure you want to logout from AgriLink?");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");

        HBox btnBox = new HBox(12);
        btnBox.setAlignment(Pos.CENTER);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #A2D9CE; -fx-border-radius: 8px; -fx-text-fill: #1B2631; -fx-padding: 7 18; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> {
            rootPane.getChildren().remove(overlay);
            onCancel.run();
        });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 7 18; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> {
            System.out.println("User logged out.");
            rootPane.getChildren().remove(overlay);
        });

        btnBox.getChildren().addAll(cancelBtn, logoutBtn);
        modal.getChildren().addAll(icon, title, sub, btnBox);

        overlay.getChildren().add(modal);
        rootPane.getChildren().add(overlay);
    }
}