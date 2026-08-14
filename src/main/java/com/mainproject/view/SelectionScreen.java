package com.mainproject.view;

import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class SelectionScreen {

        private Scene SelectionScreenScene;

        Scene getSelectionScreenScene(Consumer<String> onRoleSelected) {

                Text title = new Text("Select Your Role");
                title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

                Text subtitle = new Text("Choose the role that best describes you");
                subtitle.setStyle("-fx-font-size: 15px; -fx-fill: #777777;");

                VBox headerBox = new VBox(8, title, subtitle);
                headerBox.setAlignment(Pos.CENTER);

                ToggleGroup roleGroup = new ToggleGroup();

                VBox farmerCard = createRoleCard(
                                "assets/icons/farmer.png",
                                "Farmer",
                                "Sell products, rent equipment and\nmanage your farm business",
                                roleGroup);

                VBox buyerCard = createRoleCard(
                                "assets/icons/buyer.png",
                                "Buyer",
                                "Buy products, connect with\nfarmers and get best deals",
                                roleGroup);

                VBox adminCard = createRoleCard(
                                "assets/icons/admin.png",
                                "Admin",
                                "Manage users, products\nand platform activities",
                                roleGroup);

                HBox cardRow = new HBox(20, farmerCard, buyerCard, adminCard);
                cardRow.setAlignment(Pos.CENTER);
                HBox.setHgrow(farmerCard, Priority.ALWAYS);
                HBox.setHgrow(buyerCard, Priority.ALWAYS);
                HBox.setHgrow(adminCard, Priority.ALWAYS);

                Button continueBtn = new Button("Continue");
                continueBtn.setPrefHeight(48);
                continueBtn.setPrefWidth(220);
                continueBtn.setStyle(
                                "-fx-background-color: #2E7D32; " +
                                                "-fx-text-fill: white; -fx-font-size: 16px; " +
                                                "-fx-font-weight: bold; -fx-background-radius: 8px;");

                continueBtn.setOnAction(event -> {

                        ToggleButton selectedButton = (ToggleButton) roleGroup.getSelectedToggle();

                        if (selectedButton == null) {
                                System.out.println("Please select a role");
                                return;
                        }

                        String role = (String) selectedButton.getUserData();

                        Stage stage = (Stage) SelectionScreenScene.getWindow();

                        switch (role) {

                                case "Farmer":

                                        FarmerDashboard farmerDashboard = new FarmerDashboard();
                                        Scene farmerScene = farmerDashboard.getFarmerDashboardScene();
                                        stage.setScene(farmerScene);

                                        break;

                                case "Buyer":

                                        BuyerDashboard buyerDashboard = new BuyerDashboard();
                                        Scene buyerScene = buyerDashboard.getBuyerDashboardScene();
                                        stage.setScene(buyerScene);

                                        break;

                                case "Admin":

                                        AdminDashboard adminDashboard = new AdminDashboard();
                                        Scene adminScene = adminDashboard.getAdminDashboardScene();
                                        stage.setScene(adminScene);
                                        
                                        break;
                        }
                });

                HBox continueBox = new HBox(continueBtn);
                continueBox.setAlignment(Pos.CENTER);

                VBox contentBox = new VBox(40, headerBox, cardRow, continueBox);
                contentBox.setAlignment(Pos.CENTER);
                contentBox.setPadding(new Insets(60));
                contentBox.setMaxWidth(1100);

                StackPane root = new StackPane(contentBox);
                root.setStyle(
                                "-fx-background-color: #ffffff; " +
                                                "-fx-background-radius: 20px; " +
                                                "-fx-border-color: #e5e5e5; " +
                                                "-fx-border-radius: 20px; " +
                                                "-fx-border-width: 1px;");
                root.setPadding(new Insets(20));

                StackPane outer = new StackPane(root);
                outer.setStyle("-fx-background-color: #f1efef;");
                outer.setPadding(new Insets(40));

                SelectionScreenScene = new Scene(outer, 1400, 1000);

                SelectionScreenScene.setFill(Color.WHITESMOKE);

                return SelectionScreenScene;
        }

        VBox createRoleCard(String iconPath, String roleName, String description, ToggleGroup group) {

                ImageView icon = new ImageView("file:src/main/resources/" + iconPath);
                icon.setFitWidth(90);
                icon.setFitHeight(90);
                icon.setPreserveRatio(true);

                StackPane iconHolder = new StackPane(icon);
                iconHolder.setPrefSize(100, 100);

                Text nameText = new Text(roleName);
                nameText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

                Text descText = new Text(description);
                descText.setStyle("-fx-font-size: 12px; -fx-fill: #888888;");
                descText.setTextAlignment(TextAlignment.CENTER);

                VBox textBox = new VBox(6, nameText, descText);
                textBox.setAlignment(Pos.CENTER);

                VBox cardContent = new VBox(15, iconHolder, textBox);
                cardContent.setAlignment(Pos.CENTER);
                cardContent.setPadding(new Insets(30, 20, 30, 20));

                ToggleButton card = new ToggleButton();
                card.setGraphic(cardContent);
                card.setToggleGroup(group);
                card.setUserData(roleName);
                card.setPrefSize(260, 260);
                card.setMaxWidth(Double.MAX_VALUE);

                card.setStyle(
                                "-fx-background-color: white; " +
                                                "-fx-border-color: #dcdcdc; " +
                                                "-fx-border-radius: 15px; " +
                                                "-fx-background-radius: 15px;");

                card.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                        if (isSelected) {
                                card.setStyle(
                                                "-fx-background-color: #eaf4ea; " +
                                                                "-fx-border-color: #2E7D32; " +
                                                                "-fx-border-width: 2px; " +
                                                                "-fx-border-radius: 15px; " +
                                                                "-fx-background-radius: 15px;");

                        } else {
                                card.setStyle(
                                                "-fx-background-color: white; " +
                                                                "-fx-border-color: #dcdcdc; " +
                                                                "-fx-border-radius: 15px; " +
                                                                "-fx-background-radius: 15px;");
                        }
                });

                VBox wrapper = new VBox(card);
                wrapper.setUserData(card);
                wrapper.setAlignment(Pos.CENTER);

                return wrapper;
        }
}
