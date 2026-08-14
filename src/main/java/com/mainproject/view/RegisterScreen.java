package com.mainproject.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class RegisterScreen {

    private Scene RegisterScreenScene;

    Scene getRegisterScreenScene(Runnable callbackaction) {

        ImageView farmerImage = new ImageView("file:src/main/resources/assets/icons/registerscreen.png");
        farmerImage.setFitWidth(800);
        farmerImage.setFitHeight(1000);
        // farmerImage.setPreserveRatio(true);

        VBox leftPanel = new VBox(farmerImage);
        leftPanel.setStyle("-fx-background-color: white;  ");
        leftPanel.setAlignment(Pos.CENTER);

        Text title = new Text("Create Your Account");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Text subtitle = new Text("Join AgriLink by AgriVerse");
        subtitle.setStyle("-fx-font-size: 15px; -fx-fill: #777777;");

        VBox headerBox = new VBox(6, title, subtitle);

        Label fullNameLabel = new Label("Full Name");
        fullNameLabel.setStyle("-fx-font-weight: bold;");
        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Enter your full name");
        fullNameField.setPrefHeight(42);

        Label emailLabel = new Label("Email / Phone");
        emailLabel.setStyle("-fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email or phone");
        emailField.setPrefHeight(42);

        Label passLabel = new Label("Password");
        passLabel.setStyle("-fx-font-weight: bold;");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Create a password");
        passField.setPrefHeight(42);

        Label confirmPassLabel = new Label("Confirm Password");
        confirmPassLabel.setStyle("-fx-font-weight: bold;");
        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Confirm your password");
        confirmPassField.setPrefHeight(42);

        Label roleLabel = new Label("Select Role");
        roleLabel.setStyle("-fx-font-weight: bold;");

        ToggleGroup roleGroup = new ToggleGroup();

        ToggleButton farmerBtn = createRoleButton("Farmer", roleGroup);
        ToggleButton buyerBtn = createRoleButton("Buyer", roleGroup);
        ToggleButton adminBtn = createRoleButton("Admin", roleGroup);
        farmerBtn.setSelected(true);

        HBox roleBox = new HBox(15, farmerBtn, buyerBtn, adminBtn);
        HBox.setHgrow(farmerBtn, Priority.ALWAYS);
        HBox.setHgrow(buyerBtn, Priority.ALWAYS);
        HBox.setHgrow(adminBtn, Priority.ALWAYS);
        farmerBtn.setMaxWidth(Double.MAX_VALUE);
        buyerBtn.setMaxWidth(Double.MAX_VALUE);
        adminBtn.setMaxWidth(Double.MAX_VALUE);

        Button registerBtn = new Button("Register");
        registerBtn.setPrefHeight(48);
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setStyle(
                "-fx-background-color: #2E7D32; " +
                        "-fx-text-fill: white; -fx-font-size: 16px; " +
                        "-fx-font-weight: bold; -fx-background-radius: 8px;");
        registerBtn.setOnAction(event -> {
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passField.getText();
            String confirmPassword = confirmPassField.getText();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                LoginScreen.showAlert(AlertType.WARNING, "Missing Information",
                        "Please fill in all fields before registering.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                LoginScreen.showAlert(AlertType.WARNING, "Password Mismatch",
                        "Password and Confirm Password do not match.");
                return;
            }

            Toggle selected = roleGroup.getSelectedToggle();
            String role = (selected != null) ? ((ToggleButton) selected).getText() : "Farmer";

            System.out.println("Registering " + fullName + " (" + email + ") as " + role);

            LoginScreen.showAlert(AlertType.INFORMATION, "Registration Successful",
                    "Your account has been created. Please log in.");

            callbackaction.run();
        });

        Text alreadytext = new Text("Already have an account?");
        alreadytext.setStyle("-fx-fill: #555555;");

        
        Hyperlink backtoLoginScreen = new Hyperlink("Login here");
        backtoLoginScreen.setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;");
        backtoLoginScreen.setOnAction(event -> {
            System.out.println("back to the Homepage");
            callbackaction.run();
        });

        HBox loginBox = new HBox(5, alreadytext, backtoLoginScreen);
        loginBox.setAlignment(Pos.CENTER);

        VBox formCard = new VBox(18,
                headerBox,
                fullNameLabel, fullNameField,
                emailLabel, emailField,
                passLabel, passField,
                confirmPassLabel, confirmPassField,
                roleLabel, roleBox,
                registerBtn,
                loginBox);
        formCard.setPadding(new Insets(40));
        formCard.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 15px; " +
                        "-fx-border-color: #dcdcdc; " +
                        "-fx-border-radius: 15px; " +
                        "-fx-border-width: 1px;");
        formCard.setMaxWidth(560);

        StackPane rightPanel = new StackPane(formCard);
        rightPanel.setStyle("-fx-background-color: #f1efef;");
        rightPanel.setPadding(new Insets(40));

        formCard.prefWidthProperty().bind(rightPanel.widthProperty().multiply(0.75));

        HBox hbmain = new HBox(leftPanel, rightPanel);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        rightPanel.prefWidthProperty().bind(hbmain.widthProperty().multiply(0.55));
        leftPanel.prefWidthProperty().bind(hbmain.widthProperty().multiply(0.45));
        hbmain.setStyle("-fx-background-color: #9e9d98;");

        RegisterScreenScene = new Scene(hbmain,1400,1000);
        RegisterScreenScene.setFill(Color.WHITESMOKE);

        return RegisterScreenScene;
    }

    private ToggleButton createRoleButton(String text, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setPrefHeight(50);
        btn.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: #dcdcdc; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-font-weight: bold;");
        btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {

            if (isSelected) {
                btn.setStyle(
                        "-fx-background-color: #eaf4ea; " +
                                "-fx-border-color: #2E7D32; " +
                                "-fx-border-radius: 10px; " +
                                "-fx-background-radius: 10px; " +
                                "-fx-text-fill: #2E7D32; " +
                                "-fx-font-weight: bold;");
            } else {
                btn.setStyle(
                        "-fx-background-color: white; " +
                                "-fx-border-color: #dcdcdc; " +
                                "-fx-border-radius: 10px; " +
                                "-fx-background-radius: 10px; " +
                                "-fx-font-weight: bold;");
            }
        });

        return btn;
    }
}
