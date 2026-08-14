package com.mainproject.view;

import com.mainproject.controller.AuthController;
import com.mainproject.dao.UserDAO;
import com.mainproject.model.User;

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

    private AuthController authController;
    private UserDAO userDAO;

    // =====================================================
    // START
    // =====================================================

    @Override
    public void start(Stage myStage) {

        Homestage =
                myStage;

        authController =
                new AuthController();

        userDAO =
                new UserDAO();

        // =================================================
        // LEFT IMAGE
        // =================================================

        ImageView loginImage =
                new ImageView(
                        "file:src/main/resources/assets/icons/loginimage.png"
                );

        loginImage.setFitWidth(
                800
        );

        loginImage.setFitHeight(
                1100
        );

        loginImage.setPreserveRatio(
                true
        );

        VBox leftPanel =
                new VBox(
                        40
                );

        leftPanel.setAlignment(
                Pos.CENTER
        );

        leftPanel.setPrefSize(
                700,
                1000
        );

        leftPanel.getChildren()
                .add(
                        loginImage
                );

        // =================================================
        // TITLE
        // =================================================

        Text text1 =
                new Text(
                        "Welcome Back!"
                );

        text1.setStyle(
                "-fx-font-size: 40px;" +
                "-fx-font-weight: bold;"
        );

        HBox hb1 =
                new HBox(
                        text1
                );

        // =================================================
        // SUBTITLE
        // =================================================

        Text text2 =
                new Text(
                        "Login to continue"
                );

        text2.setStyle(
                "-fx-fill: #777777;" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;"
        );

        HBox hb2 =
                new HBox(
                        text2
                );

        // =================================================
        // EMAIL
        // =================================================

        Label emailLabel =
                new Label(
                        "Email"
                );

        emailLabel.setStyle(
                "-fx-font-weight: bold;"
        );

        TextField emailField =
                new TextField();

        emailField.setPromptText(
                "Enter your email"
        );

        emailField.setPrefHeight(
                40
        );

        // =================================================
        // PASSWORD
        // =================================================

        Label passLabel =
                new Label(
                        "Password"
                );

        passLabel.setStyle(
                "-fx-font-weight: bold;"
        );

        PasswordField passField =
                new PasswordField();

        passField.setPromptText(
                "Enter your password"
        );

        passField.setPrefHeight(
                40
        );

        // =================================================
        // FORGOT PASSWORD
        // =================================================

        Hyperlink forgotPassLink =
                new Hyperlink(
                        "Forgot Password?"
                );

        forgotPassLink.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #2E7D32;" +
                "-fx-font-weight: bold;"
        );

        HBox hbForgot =
                new HBox(
                        forgotPassLink
                );

        hbForgot.setAlignment(
                Pos.CENTER_RIGHT
        );

        forgotPassLink.setOnAction(
                event -> {

                    String email =
                            emailField
                                    .getText()
                                    .trim();

                    if (email.isEmpty()) {

                        showAlert(
                                AlertType.WARNING,
                                "Email Required",
                                "Please enter your email first."
                        );

                        return;
                    }

                    boolean sent =
                            authController
                                    .resetPassword(
                                            email
                                    );

                    if (sent) {

                        showAlert(
                                AlertType.INFORMATION,
                                "Password Reset",
                                "Password reset email has been sent."
                        );

                    } else {

                        showAlert(
                                AlertType.ERROR,
                                "Reset Failed",
                                "Unable to send password reset email."
                        );
                    }
                }
        );

        // =================================================
        // LOGIN BUTTON
        // =================================================

        Button loginBtn =
                new Button(
                        "Login"
                );

        loginBtn.setPrefHeight(
                45
        );

        loginBtn.setMaxWidth(
                Double.MAX_VALUE
        );

        loginBtn.setStyle(
                "-fx-background-color: #2E7D32;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;"
        );

        // =================================================
        // LOGIN ACTION
        // =================================================

        loginBtn.setOnAction(
                event -> {

                    String email =
                            emailField
                                    .getText()
                                    .trim();

                    String password =
                            passField.getText();

                    // =====================================
                    // VALIDATION
                    // =====================================

                    if (
                            email.isEmpty()
                            ||
                            password.isEmpty()
                    ) {

                        showAlert(
                                AlertType.WARNING,
                                "Missing Information",
                                "Please enter both email and password."
                        );

                        return;
                    }

                    System.out.println(
                            "Login attempt for: "
                                    + email
                    );

                    // =====================================
                    // FIREBASE LOGIN
                    // =====================================

                    boolean loginSuccess =
                            authController
                                    .signIn(
                                            email,
                                            password
                                    );

                    if (!loginSuccess) {

                        System.out.println(
                                "Firebase Login Failed!"
                        );

                        showAlert(
                                AlertType.ERROR,
                                "Login Failed",
                                "Invalid email or password."
                        );

                        return;
                    }

                    System.out.println(
                            "Firebase Login Successful!"
                    );

                    // =====================================
                    // GET USER FROM FIRESTORE
                    // =====================================

                    User user =
                            userDAO.getUserByEmail(
                                    email
                            );

                    if (user == null) {

                        showAlert(
                                AlertType.ERROR,
                                "User Error",
                                "User information was not found in Firestore."
                        );

                        return;
                    }

                    // =====================================
                    // GET ROLE
                    // =====================================

                    String role =
                            user.getRole();

                    System.out.println(
                            "User Name: "
                                    + user.getFullName()
                    );

                    System.out.println(
                            "User Email: "
                                    + user.getEmail()
                    );

                    System.out.println(
                            "User Role: "
                                    + role
                    );

                    // =====================================
                    // ROLE CHECK
                    // =====================================

                    if (role == null
                            || role.trim().isEmpty()) {

                        showAlert(
                                AlertType.ERROR,
                                "Role Error",
                                "User role was not found in Firestore."
                        );

                        return;
                    }

                    // =====================================
                    // FARMER
                    // =====================================

                    if (
                            role.equalsIgnoreCase(
                                    "Farmer"
                            )
                    ) {

                        System.out.println(
                                "Opening Farmer Dashboard..."
                        );

                        FarmerDashboard dashboard =
                                new FarmerDashboard(
                                        user
                                );

                        switchScene(
                                dashboard.getScene()
                        );

                    }

                    // =====================================
                    // BUYER
                    // =====================================

                    else if (
                            role.equalsIgnoreCase(
                                    "Buyer"
                            )
                    ) {

                        System.out.println(
                                "Opening Buyer Dashboard..."
                        );

                        BuyerDashboard dashboard =
                                new BuyerDashboard(
                                        user
                                );

                        switchScene(
                                dashboard.getScene()
                        );
                    }

                    // =====================================
                    // UNKNOWN ROLE
                    // =====================================

                    else {

                        showAlert(
                                AlertType.ERROR,
                                "Invalid Role",
                                "Unknown user role: "
                                        + role
                        );
                    }
                }
        );

        // =================================================
        // REGISTER LINK
        // =================================================

        Text noAccountText =
                new Text(
                        "Don't have an account? "
                );

        noAccountText.setStyle(
                "-fx-fill: #777777;" +
                "-fx-font-size: 13px;"
        );

        Hyperlink registerLink =
                new Hyperlink(
                        "Register here"
                );

        registerLink.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #2E7D32;" +
                "-fx-font-weight: bold;"
        );

        registerLink.setOnAction(
                event -> {

                    RegisterScreen registerScreen =
                            new RegisterScreen();

                    Runnable callbackAction =
                            new Runnable() {

                                @Override
                                public void run() {

                                    Homestage.setScene(
                                            HomePageScene
                                    );

                                    System.out.println(
                                            "Returning to Login Screen..."
                                    );
                                }
                            };

                    Homestage.setScene(
                            registerScreen
                                    .getRegisterScreenScene(
                                            callbackAction
                                    )
                    );
                }
        );

        HBox hbRegister =
                new HBox(
                        noAccountText,
                        registerLink
                );

        hbRegister.setAlignment(
                Pos.CENTER
        );

        // =================================================
        // FORM CARD
        // =================================================

        VBox vb2 =
                new VBox(
                        20
                );

        vb2.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 15px;" +
                "-fx-background-radius: 15px;" +
                "-fx-padding: 30px;"
        );

        vb2.setPrefSize(
                300,
                500
        );

        vb2.setMaxSize(
                500,
                700
        );

        vb2.getChildren().addAll(
                hb1,
                hb2,

                emailLabel,
                emailField,

                passLabel,
                passField,

                hbForgot,

                loginBtn,

                hbRegister
        );

        // =================================================
        // RIGHT PANEL
        // =================================================

        StackPane rightPanel =
                new StackPane(
                        vb2
                );

        rightPanel.setStyle(
                "-fx-background-color: #f1efef;"
        );

        // =================================================
        // MAIN
        // =================================================

        HBox main =
                new HBox(
                        leftPanel,
                        rightPanel
                );

        HBox.setHgrow(
                rightPanel,
                Priority.ALWAYS
        );

        rightPanel.prefWidthProperty()
                .bind(
                        main.widthProperty()
                                .multiply(0.8)
                );

        // =================================================
        // SCENE
        // =================================================

        HomePageScene =
                new Scene(
                        main
                );

        HomePageScene.setFill(
                Color.WHITE
        );

        Homestage.setScene(
                HomePageScene
        );

        Homestage.setFullScreen(
                true
        );

        Homestage.setFullScreenExitHint(
                ""
        );

        Homestage.show();
    }

    // =====================================================
    // SWITCH SCENE
    // =====================================================

    public static void switchScene(
            Scene scene) {

        if (Homestage != null) {

            Homestage.setScene(
                    scene
            );
        }
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    public static void logoutToLogin() {

        if (Homestage != null) {

            new LoginScreen()
                    .start(
                            Homestage
                    );
        }
    }

    // =====================================================
    // BACK TO LOGIN
    // =====================================================

    public void backtoLoginScreen() {

        Homestage.setScene(
                HomePageScene
        );
    }

    // =====================================================
    // ALERT
    // =====================================================

    public static void showAlert(
            AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(
                        type
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    // =====================================================
    // GET LOGIN SCENE
    // =====================================================

    public Scene getHomePageScene() {

        return HomePageScene;
    }
}