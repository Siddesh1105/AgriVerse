package com.mainproject.View;

import com.mainproject.controller.AuthController;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginScreen extends Application {

        public static Stage Homestage;
        private Scene HomePageScene;

        // Firebase Authentication Controller
        private AuthController authController = new AuthController();

        @Override
        public void start(Stage myStage) {

                Homestage = myStage;

                // =====================================================
                // LEFT SIDE IMAGE
                // =====================================================

                ImageView loginImage = new ImageView(
                                "file:src/main/resources/assets/icons/loginimage.png");

                // OLD RESOLUTION
                loginImage.setFitWidth(800);
                loginImage.setFitHeight(1100);
                loginImage.setPreserveRatio(true);

                VBox vb1 = new VBox(40);

                vb1.setAlignment(Pos.CENTER);

                // OLD SIZE
                vb1.setPrefSize(700, 1000);

                vb1.getChildren().addAll(loginImage);

                // =====================================================
                // WELCOME TEXT
                // =====================================================

                Text text1 = new Text("Welcome Back!");

                text1.setStyle(
                                "-fx-font-size: 40px;" +
                                                "-fx-font-weight: bold;");

                HBox hb1 = new HBox(text1);

                // =====================================================
                // SUBTITLE
                // =====================================================

                Text text2 = new Text("Login to continue");

                text2.setStyle(
                                "-fx-fill: #777777;" +
                                                "-fx-font-size: 24px;" +
                                                "-fx-font-weight: bold;");

                HBox hb2 = new HBox(text2);

                // =====================================================
                // EMAIL
                // =====================================================

                Label emailLabel = new Label("Email");

                emailLabel.setStyle(
                                "-fx-font-weight: bold;");

                TextField emailField = new TextField();

                emailField.setPromptText(
                                "Enter your email");

                emailField.setPrefHeight(40);

                // =====================================================
                // PASSWORD
                // =====================================================

                Label passLabel = new Label("Password");

                passLabel.setStyle(
                                "-fx-font-weight: bold;");

                PasswordField passField = new PasswordField();

                passField.setPromptText(
                                "Enter your password");

                passField.setPrefHeight(40);

                // =====================================================
                // FORGOT PASSWORD
                // =====================================================

                Hyperlink forgotPassLink = new Hyperlink("Forgot Password?");

                forgotPassLink.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #2E7D32;" +
                                                "-fx-font-weight: bold;");

                HBox hbForgot = new HBox(forgotPassLink);

                hbForgot.setAlignment(
                                Pos.CENTER_RIGHT);

                // =====================================================
                // FORGOT PASSWORD ACTION
                // =====================================================

                forgotPassLink.setOnAction(event -> {

                        String email = emailField.getText().trim();

                        // Check email
                        if (email.isEmpty()) {

                                showAlert(
                                                AlertType.WARNING,
                                                "Email Required",
                                                "Please enter your email address first.");

                                emailField.requestFocus();

                                return;
                        }

                        System.out.println(
                                        "Sending password reset email to: " + email);

                        // Firebase password reset
                        boolean resetSuccess = authController.resetPassword(email);

                        // Password reset successful
                        if (resetSuccess) {

                                showAlert(
                                                AlertType.INFORMATION,
                                                "Password Reset",
                                                "A password reset link has been sent to "
                                                                + email
                                                                + ". Please check your email.");

                        }

                        // Password reset failed
                        else {

                                showAlert(
                                                AlertType.ERROR,
                                                "Password Reset Failed",
                                                "Unable to send the password reset email.");
                        }
                });

                // =====================================================
                // LOGIN BUTTON
                // =====================================================

                Button loginBtn = new Button("Login");

                loginBtn.setPrefHeight(45);

                loginBtn.setMaxWidth(
                                Double.MAX_VALUE);

                loginBtn.setStyle(
                                "-fx-background-color: #2E7D32;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8px;");

                // =====================================================
                // LOGIN BUTTON ACTION
                // =====================================================

                loginBtn.setOnAction(event -> {

                        String email = emailField.getText().trim();

                        String password = passField.getText();

                        // -------------------------------------------------
                        // CHECK EMPTY FIELDS
                        // -------------------------------------------------

                        if (email.isEmpty() || password.isEmpty()) {

                                showAlert(
                                                AlertType.WARNING,
                                                "Missing Information",
                                                "Please enter both your email and password.");

                                return;
                        }

                        System.out.println(
                                        "Login attempt for: " + email);

                        // -------------------------------------------------
                        // FIREBASE LOGIN
                        // -------------------------------------------------

                        boolean loginSuccess = authController.signIn(
                                        email,
                                        password);

                        // -------------------------------------------------
                        // LOGIN SUCCESS
                        // -------------------------------------------------

                        if (loginSuccess) {

                                System.out.println(
                                                "Firebase Login Successful!");

                                // Create SelectionScreen
                                SelectionScreen selectionScreen = new SelectionScreen();

                                // Runnable callback
                                Runnable callBackAction = new Runnable() {

                                        @Override
                                        public void run() {

                                                System.out.println(
                                                                "Opening Selection Screen...");

                                                Homestage.setScene(
                                                                selectionScreen
                                                                                .getSelectionScreenScene(
                                                                                                selectedRole -> {

                                                                                                        System.out.println(
                                                                                                                        "Role selected: "
                                                                                                                                        + selectedRole);

                                                                                                }));
                                        }
                                };

                                // Execute callback
                                callBackAction.run();

                        }

                        // -------------------------------------------------
                        // LOGIN FAILED
                        // -------------------------------------------------

                        else {

                                System.out.println(
                                                "Firebase Login Failed!");

                                showAlert(
                                                AlertType.ERROR,
                                                "Login Failed",
                                                "Invalid email or password.");
                        }

                });

                // =====================================================
                // REGISTER TEXT
                // =====================================================

                Text noAccountText = new Text("Don't have an account? ");

                noAccountText.setStyle(
                                "-fx-fill: #777777;" +
                                                "-fx-font-size: 13px;");

                Hyperlink registerLink = new Hyperlink("Register here");

                registerLink.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #2E7D32;" +
                                                "-fx-font-weight: bold;");

                // =====================================================
                // REGISTER BUTTON ACTION
                // =====================================================

                registerLink.setOnAction(event -> {

                        RegisterScreen registerScreen = new RegisterScreen();

                        Runnable callBackAction = new Runnable() {

                                @Override
                                public void run() {

                                        System.out.println(
                                                        "Returning to Login Screen...");

                                        Homestage.setScene(
                                                        HomePageScene);
                                }
                        };

                        // Open Register Screen
                        Homestage.setScene(
                                        registerScreen
                                                        .getRegisterScreenScene(
                                                                        callBackAction));

                });

                // =====================================================
                // REGISTER HBOX
                // =====================================================

                HBox hbRegister = new HBox(
                                noAccountText,
                                registerLink);

                hbRegister.setAlignment(
                                Pos.CENTER);

                // =====================================================
                // RIGHT LOGIN PANEL
                // =====================================================

                VBox vb2 = new VBox(20);

                vb2.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #ffffff;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 15px;" +
                                                "-fx-background-radius: 15px;" +
                                                "-fx-padding: 30px;");

                // OLD SIZE
                vb2.setPrefSize(
                                300,
                                500);

                // OLD MAX SIZE
                vb2.setMaxSize(
                                500,
                                700);

                vb2.getChildren().addAll(
                                hb1,
                                hb2,
                                emailLabel,
                                emailField,
                                passLabel,
                                passField,
                                hbForgot,
                                loginBtn,
                                hbRegister);

                // =====================================================
                // RIGHT PANEL
                // =====================================================

                StackPane rightPanel = new StackPane(vb2);

                rightPanel.setStyle(
                                "-fx-background-color: #f1efef;");

                // =====================================================
                // MAIN HBOX
                // =====================================================

                HBox HBMain = new HBox(
                                vb1,
                                rightPanel);

                HBox.setHgrow(
                                rightPanel,
                                Priority.ALWAYS);

                rightPanel.prefWidthProperty().bind(
                                HBMain.widthProperty()
                                                .multiply(0.8));

                // =====================================================
                // SCENE
                // =====================================================

                HomePageScene = new Scene(HBMain, 1400, 1000);

                HomePageScene.setFill(
                                Color.WHITE);

                Homestage.setScene(
                                HomePageScene);

                switchScene(
                                HomePageScene);

                Homestage.show();
        }

        // =========================================================
        // SWITCH SCENE
        // =========================================================

        static void switchScene(Scene scene) {

                if (Homestage != null) {

                        Homestage.setScene(scene);
                }
        }

        // =========================================================
        // LOGOUT
        // =========================================================

        static void logoutToLogin() {

                if (Homestage != null) {

                        new LoginScreen()
                                        .start(Homestage);
                }
        }

        // =========================================================
        // ALERT
        // =========================================================

        static void showAlert(
                        AlertType type,
                        String title,
                        String message) {

                Alert alert = new Alert(type);

                alert.setTitle(title);

                alert.setHeaderText(null);

                alert.setContentText(message);

                alert.showAndWait();
        }

        // =========================================================
        // GET LOGIN SCENE
        // =========================================================

        public Scene getHomePageScene() {

                return HomePageScene;
        }
}