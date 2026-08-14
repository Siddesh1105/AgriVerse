package com.mainproject.view;

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

    @Override
    public void start(Stage myStage) {
        Homestage = myStage;

        ImageView loginImage = new ImageView("file:src/main/resources/assets/icons/loginimage.png");
        loginImage.setFitWidth(800);
        loginImage.setFitHeight(1100);
        loginImage.setPreserveRatio(true);

        VBox vb1 = new VBox(40);
        vb1.setAlignment(Pos.CENTER);
        vb1.setPrefSize(700, 1000);
        vb1.getChildren().addAll(loginImage);

        Text text1 = new Text("Welcome Back!");
        text1.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        HBox hb1 = new HBox(text1);

        Text text2 = new Text("Login to continue");
        text2.setStyle("-fx-fill: #777777; -fx-font-size: 24px; -fx-font-weight: bold;");
        HBox hb2 = new HBox(text2);

        Label emailLabel = new Label("Email / Phone");
        emailLabel.setStyle("-fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email or phone");
        emailField.setPrefHeight(40);

        Label passLabel = new Label("Password");
        passLabel.setStyle("-fx-font-weight: bold;");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        passField.setPrefHeight(40);

        Hyperlink forgotPassLink = new Hyperlink("Forgot Password?");
        forgotPassLink.setStyle("-fx-font-size: 13px; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");
        HBox hbForgot = new HBox(forgotPassLink);
        hbForgot.setAlignment(Pos.CENTER_RIGHT);

        forgotPassLink.setOnAction(event -> {
            System.out.println("Forgot password clicked");
        });

        Button loginBtn = new Button("Login");
        loginBtn.setPrefHeight(45);
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(
                "-fx-background-color: #2E7D32;" +
                        " -fx-text-fill: white; -fx-font-size: 16px; " +
                        "-fx-font-weight: bold; -fx-background-radius: 8px;");

        loginBtn.setOnAction(event -> {
            String email = emailField.getText().trim();
            String password = passField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert(AlertType.WARNING, "Missing Information",
                        "Please enter both your email/phone and password.");
                return;
            }

            System.out.println("Login attempt for: " + email);

            SelectionScreen selectionScreen = new SelectionScreen();
            switchScene(selectionScreen.getSelectionScreenScene(selectedRole -> {
                System.out.println("Role selected after login: " + selectedRole);
            }));

        });

        Text noAccountText = new Text("Don't have an account? ");
        noAccountText.setStyle("-fx-fill: #777777; -fx-font-size: 13px;");

        Hyperlink registerLink = new Hyperlink("Register here");
        registerLink.setStyle("-fx-font-size: 13px; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");

        registerLink.setOnAction(event -> {
            RegisterScreen registerScreen = new RegisterScreen();
            Runnable callBackAction = new Runnable() {
                public void run() {
                    Homestage.setScene(HomePageScene);
                    System.out.println("clicked");
                }
            };
            Homestage.setScene(registerScreen.getRegisterScreenScene(callBackAction));
        });

        HBox hbRegister = new HBox(noAccountText, registerLink);
        hbRegister.setAlignment(Pos.CENTER);

        VBox vb2 = new VBox(20);
        vb2.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #ffffff; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; " +
                "-fx-padding: 30px; ");
        vb2.setPrefSize(300, 500);
        vb2.setMaxSize(500, 700);
        vb2.getChildren().addAll(hb1, hb2, emailLabel, emailField, passLabel, passField, hbForgot, loginBtn,
                hbRegister);

        StackPane rightPanel = new StackPane(vb2);
        rightPanel.setStyle("-fx-background-color: #f1efef;");

        HBox HBMain = new HBox(vb1, rightPanel);

        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        rightPanel.prefWidthProperty().bind(HBMain.widthProperty().multiply(0.8));

        HomePageScene = new Scene(HBMain,1400,1000);
        HomePageScene.setFill(Color.WHITE);
        Homestage.setScene(HomePageScene);
        switchScene(HomePageScene);
        Homestage.show();

    }

    static void switchScene(Scene scene) {
        if (Homestage != null) {
            Homestage.setScene(scene);
        }
    }

    static void logoutToLogin() {
        if (Homestage != null) {
            new LoginScreen().start(Homestage);
        }
    }

    public void backtoLoginScreen() {
        Homestage.setScene(HomePageScene);
    }

    static void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getHomePageScene() {
        return HomePageScene;

    }
}