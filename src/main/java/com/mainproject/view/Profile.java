package com.mainproject.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Profile {

    public Node getView() {
        VBox root = new VBox(18);
        root.setPadding(new Insets(10));

        VBox titles = new VBox(2);
        Label title = new Label("Profile");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label sub = new Label("Manage your personal and farm details.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        titles.getChildren().addAll(title, sub);

        // Main Profile Card
        HBox mainCard = new HBox(30);
        mainCard.setPadding(new Insets(25));
        mainCard.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14px; -fx-border-color: #A2D9CE;");

        // Left Avatar Column
        VBox avatarCol = new VBox(12);
        avatarCol.setAlignment(Pos.CENTER);
        Circle pic = new Circle(45, Color.web("#D4EFDF"));
        Label picIcon = new Label("👨‍🌾");
        picIcon.setStyle("-fx-font-size: 40px;");
        StackPane picStack = new StackPane(pic, picIcon);

        Button changePhotoBtn = new Button("Change Photo");
        changePhotoBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #A2D9CE; -fx-border-radius: 6px; -fx-cursor: hand;");
        avatarCol.getChildren().addAll(picStack, changePhotoBtn);

        // Right Info Grid
        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(14);
        HBox.setHgrow(grid, Priority.ALWAYS);

        grid.add(createField("Full Name", "Rajesh Patil"), 0, 0);
        grid.add(createField("Phone Number", "+91 98765 43210"), 1, 0);
        grid.add(createField("Email", "rajeshpatil@email.com"), 0, 1);
        grid.add(createField("Farm Name", "Patil Farms"), 1, 1);
        grid.add(createField("Location", "Nashik, Maharashtra"), 0, 2);
        grid.add(createField("Member Since", "Jan 10, 2024"), 1, 2);

        Button updateBtn = new Button("Update Profile");
        updateBtn.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 8 20; -fx-cursor: hand;");
        grid.add(updateBtn, 0, 3);

        mainCard.getChildren().addAll(avatarCol, grid);
        root.getChildren().addAll(titles, mainCard);
        return new ScrollPane(root);
    }

    private VBox createField(String label, String value) {
        VBox box = new VBox(4);
        Label l = new Label(label);
        l.setStyle("-fx-font-size: 12px; -fx-text-fill: #566573;");
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1B2631; -fx-background-color: #F4FAF7; -fx-padding: 8 12; -fx-background-radius: 8px; -fx-border-color: #A2D9CE; -fx-border-radius: 8px;");
        box.getChildren().addAll(l, v);
        return box;
    }
}