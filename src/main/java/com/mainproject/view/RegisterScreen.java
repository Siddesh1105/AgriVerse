package com.mainproject.view;

import com.mainproject.controller.AuthController;
import com.mainproject.dao.UserDAO;
import com.mainproject.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.stage.Screen;

public class RegisterScreen {

        private Scene registerScreenScene;

        private final AuthController authController;
        private final UserDAO userDAO;

        // =====================================================
        // CONSTRUCTOR
        // =====================================================

        public RegisterScreen() {

                authController = new AuthController();

                userDAO = new UserDAO();
        }

        // =====================================================
        // GET REGISTER SCREEN
        // =====================================================

        public Scene getRegisterScreenScene(
                        Runnable callbackAction) {

                // =================================================
                // LEFT IMAGE
                // =================================================

                ImageView farmerImage = new ImageView(
                                "file:src/main/resources/assets/icons/registerscreen.png");

                farmerImage.setFitWidth(800);
                farmerImage.setFitHeight(1000);

                VBox leftPanel = new VBox(
                                farmerImage);

                leftPanel.setStyle(
                                "-fx-background-color: white;");

                leftPanel.setAlignment(
                                Pos.CENTER);

                // =================================================
                // HEADER
                // =================================================

                Text title = new Text(
                                "Create Your Account");

                title.setStyle(
                                "-fx-font-size: 32px;" +
                                                "-fx-font-weight: bold;");

                Text subtitle = new Text(
                                "Join AgriLink by AgriVerse");

                subtitle.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-fill: #777777;");

                VBox headerBox = new VBox(
                                6,
                                title,
                                subtitle);

                // =================================================
                // FULL NAME
                // =================================================

                Label fullNameLabel = new Label(
                                "Full Name");

                fullNameLabel.setStyle(
                                "-fx-font-weight: bold;");

                TextField fullNameField = new TextField();

                fullNameField.setPromptText(
                                "Enter your full name");

                fullNameField.setPrefHeight(
                                42);

                // =================================================
                // EMAIL
                // =================================================

                Label emailLabel = new Label(
                                "Email");

                emailLabel.setStyle(
                                "-fx-font-weight: bold;");

                TextField emailField = new TextField();

                emailField.setPromptText(
                                "Enter your email");

                emailField.setPrefHeight(
                                42);

                // =================================================
                // MOBILE NUMBER
                // =================================================

                Label mobileLabel = new Label(
                                "Mobile Number");

                mobileLabel.setStyle(
                                "-fx-font-weight: bold;");

                TextField mobileField = new TextField();

                mobileField.setPromptText(
                                "Enter your mobile number");

                mobileField.setPrefHeight(
                                42);

                // =================================================
                // GENDER
                // =================================================

                Label genderLabel = new Label(
                                "Gender");

                genderLabel.setStyle(
                                "-fx-font-weight: bold;");

                ComboBox<String> genderBox = new ComboBox<>();

                genderBox.getItems().addAll(
                                "Male",
                                "Female",
                                "Other");

                genderBox.setPromptText(
                                "Select your gender");

                genderBox.setPrefHeight(
                                42);

                genderBox.setMaxWidth(
                                Double.MAX_VALUE);

                // =================================================
                // PASSWORD
                // =================================================

                Label passLabel = new Label(
                                "Password");

                passLabel.setStyle(
                                "-fx-font-weight: bold;");

                PasswordField passField = new PasswordField();

                passField.setPromptText(
                                "Create a password");

                passField.setPrefHeight(
                                42);

                // =================================================
                // CONFIRM PASSWORD
                // =================================================

                Label confirmPassLabel = new Label(
                                "Confirm Password");

                confirmPassLabel.setStyle(
                                "-fx-font-weight: bold;");

                PasswordField confirmPassField = new PasswordField();

                confirmPassField.setPromptText(
                                "Confirm your password");

                confirmPassField.setPrefHeight(
                                42);

                // =================================================
                // ROLE
                // =================================================

                Label roleLabel = new Label(
                                "Select Role");

                roleLabel.setStyle(
                                "-fx-font-weight: bold;");

                ToggleGroup roleGroup = new ToggleGroup();

                ToggleButton farmerBtn = createRoleButton(
                                "Farmer",
                                roleGroup);

                ToggleButton buyerBtn = createRoleButton(
                                "Buyer",
                                roleGroup);

                // Farmer selected by default
                farmerBtn.setSelected(
                                true);

                HBox roleBox = new HBox(
                                15,
                                farmerBtn,
                                buyerBtn);

                HBox.setHgrow(
                                farmerBtn,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                buyerBtn,
                                Priority.ALWAYS);

                farmerBtn.setMaxWidth(
                                Double.MAX_VALUE);

                buyerBtn.setMaxWidth(
                                Double.MAX_VALUE);

                // =================================================
                // REGISTER BUTTON
                // =================================================

                Button registerBtn = new Button(
                                "Register");

                registerBtn.setPrefHeight(
                                48);

                registerBtn.setMaxWidth(
                                Double.MAX_VALUE);

                registerBtn.setStyle(
                                "-fx-background-color: #2E7D32;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8px;");

                // =================================================
                // REGISTER ACTION
                // =================================================

                registerBtn.setOnAction(
                                event -> {

                                        String fullName = fullNameField
                                                        .getText()
                                                        .trim();

                                        String email = emailField
                                                        .getText()
                                                        .trim();

                                        String mobileNumber = mobileField
                                                        .getText()
                                                        .trim();

                                        String gender = genderBox
                                                        .getValue();

                                        String password = passField.getText();

                                        String confirmPassword = confirmPassField
                                                        .getText();

                                        // =====================================
                                        // VALIDATION
                                        // =====================================

                                        if (fullName.isEmpty()
                                                        ||
                                                        email.isEmpty()
                                                        ||
                                                        mobileNumber.isEmpty()
                                                        ||
                                                        gender == null
                                                        ||
                                                        password.isEmpty()
                                                        ||
                                                        confirmPassword.isEmpty()) {

                                                LoginScreen.showAlert(
                                                                AlertType.WARNING,
                                                                "Missing Information",
                                                                "Please fill in all fields before registering.");

                                                return;
                                        }

                                        // =====================================
                                        // PASSWORD MATCH
                                        // =====================================

                                        if (!password.equals(
                                                        confirmPassword)) {

                                                LoginScreen.showAlert(
                                                                AlertType.WARNING,
                                                                "Password Mismatch",
                                                                "Password and Confirm Password do not match.");

                                                return;
                                        }

                                        // =====================================
                                        // ROLE
                                        // =====================================

                                        Toggle selected = roleGroup
                                                        .getSelectedToggle();

                                        if (selected == null) {

                                                LoginScreen.showAlert(
                                                                AlertType.WARNING,
                                                                "Role Required",
                                                                "Please select Farmer or Buyer.");

                                                return;
                                        }

                                        String role = ((ToggleButton) selected)
                                                        .getText();

                                        System.out.println(
                                                        "Registering User:");

                                        System.out.println(
                                                        "Name: " + fullName);

                                        System.out.println(
                                                        "Email: " + email);

                                        System.out.println(
                                                        "Mobile: " + mobileNumber);

                                        System.out.println(
                                                        "Gender: " + gender);

                                        System.out.println(
                                                        "Role: " + role);

                                        // =====================================
                                        // FIREBASE AUTHENTICATION
                                        // =====================================

                                        String uid = authController
                                                        .signUpAndGetUid(
                                                                        email,
                                                                        password);

                                        if (uid == null) {

                                                LoginScreen.showAlert(
                                                                AlertType.ERROR,
                                                                "Registration Failed",
                                                                "Unable to create Firebase account.\n"
                                                                                + "The email may already exist.");

                                                return;
                                        }

                                        System.out.println(
                                                        "Firebase UID: " + uid);

                                        // =====================================
                                        // CREATE USER MODEL
                                        // =====================================

                                        // Note: Ensure your User model constructor is updated to accept mobileNumber
                                        // and gender
                                        User user = new User(
                                                        uid,
                                                        fullName,
                                                        email,
                                                        mobileNumber,
                                                        gender,
                                                        role);

                                        // =====================================
                                        // SAVE USER TO FIRESTORE
                                        // =====================================

                                        boolean saved = userDAO.saveUser(
                                                        user);

                                        if (!saved) {

                                                LoginScreen.showAlert(
                                                                AlertType.ERROR,
                                                                "Firestore Error",
                                                                "Firebase account was created, "
                                                                                + "but user information could not "
                                                                                + "be saved.");

                                                return;
                                        }

                                        System.out.println(
                                                        "User information saved in Firestore!");

                                        // =====================================
                                        // SUCCESS
                                        // =====================================

                                        LoginScreen.showAlert(
                                                        AlertType.INFORMATION,
                                                        "Registration Successful",
                                                        "Your account has been created successfully.\n"
                                                                        + "Please login to continue.");

                                        // =====================================
                                        // RUN CALLBACK
                                        // =====================================

                                        if (callbackAction != null) {

                                                callbackAction.run();
                                        }
                                });

                // =================================================
                // LOGIN LINK
                // =================================================

                Text alreadyText = new Text(
                                "Already have an account?");

                alreadyText.setStyle(
                                "-fx-fill: #555555;");

                Hyperlink backToLogin = new Hyperlink(
                                "Login here");

                backToLogin.setStyle(
                                "-fx-text-fill: #2E7D32;" +
                                                "-fx-font-weight: bold;");

                backToLogin.setOnAction(
                                event -> {

                                        if (callbackAction != null) {

                                                callbackAction.run();
                                        }
                                });

                HBox loginBox = new HBox(
                                5,
                                alreadyText,
                                backToLogin);

                loginBox.setAlignment(
                                Pos.CENTER);

                // =================================================
                // FORM CARD
                // =================================================

                VBox formCard = new VBox(
                                18,
                                headerBox,

                                fullNameLabel,
                                fullNameField,

                                emailLabel,
                                emailField,

                                mobileLabel,
                                mobileField,

                                genderLabel,
                                genderBox,

                                passLabel,
                                passField,

                                confirmPassLabel,
                                confirmPassField,

                                roleLabel,
                                roleBox,

                                registerBtn,

                                loginBox);

                formCard.setPadding(
                                new Insets(40));

                formCard.setStyle(
                                "-fx-background-color: #ffffff;" +
                                                "-fx-background-radius: 15px;" +
                                                "-fx-border-color: #dcdcdc;" +
                                                "-fx-border-radius: 15px;" +
                                                "-fx-border-width: 1px;");

                formCard.setMaxWidth(
                                560);

                // =================================================
                // RIGHT PANEL
                // =================================================

                StackPane rightPanel = new StackPane(
                                formCard);

                rightPanel.setStyle(
                                "-fx-background-color: #f1efef;");

                rightPanel.setPadding(
                                new Insets(40));

                // =================================================
                // MAIN
                // =================================================

                HBox main = new HBox(
                                leftPanel,
                                rightPanel);

                HBox.setHgrow(
                                rightPanel,
                                Priority.ALWAYS);

                rightPanel.prefWidthProperty()
                                .bind(
                                                main.widthProperty()
                                                                .multiply(0.55));

                leftPanel.prefWidthProperty()
                                .bind(
                                                main.widthProperty()
                                                                .multiply(0.45));

                main.setStyle(
                                "-fx-background-color: #9e9d98;");

                // =================================================
                // SCENE
                // =================================================

                registerScreenScene = new Scene(
                                main);

                Rectangle2D screen = Screen.getPrimary()
                                .getVisualBounds();
                registerScreenScene.setFill(
                                Color.WHITESMOKE);

                return registerScreenScene;
        }

        // =====================================================
        // CREATE ROLE BUTTON
        // =====================================================

        private ToggleButton createRoleButton(
                        String text,
                        ToggleGroup group) {

                ToggleButton btn = new ToggleButton(
                                text);

                btn.setToggleGroup(
                                group);

                btn.setPrefHeight(
                                50);

                btn.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #dcdcdc;" +
                                                "-fx-border-radius: 10px;" +
                                                "-fx-background-radius: 10px;" +
                                                "-fx-font-weight: bold;");

                btn.selectedProperty()
                                .addListener(
                                                (
                                                                obs,
                                                                wasSelected,
                                                                isSelected) -> {

                                                        if (isSelected) {

                                                                btn.setStyle(
                                                                                "-fx-background-color: #eaf4ea;" +
                                                                                                "-fx-border-color: #2E7D32;"
                                                                                                +
                                                                                                "-fx-border-radius: 10px;"
                                                                                                +
                                                                                                "-fx-background-radius: 10px;"
                                                                                                +
                                                                                                "-fx-text-fill: #2E7D32;"
                                                                                                +
                                                                                                "-fx-font-weight: bold;");

                                                        } else {

                                                                btn.setStyle(
                                                                                "-fx-background-color: white;" +
                                                                                                "-fx-border-color: #dcdcdc;"
                                                                                                +
                                                                                                "-fx-border-radius: 10px;"
                                                                                                +
                                                                                                "-fx-background-radius: 10px;"
                                                                                                +
                                                                                                "-fx-font-weight: bold;");
                                                        }
                                                });

                return btn;
        }
}