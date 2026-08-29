package com.mainproject.view.farmer;


import com.mainproject.controller.AuthController;
import com.mainproject.controller.UserController;
import com.mainproject.model.User;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Settings {

        // =====================================================
        // USER
        // =====================================================

        private final String farmerEmail;

        private final Runnable languageChanged;

        private User user;

        private final UserController userController = new UserController();

        // =====================================================
        // AUTH CONTROLLER
        // =====================================================

        private final AuthController authController = new AuthController();

        // =====================================================
        // COLORS
        // =====================================================

        private static final String MAIN_BG = "#E9F7EF";

        private static final String PRIMARY_GREEN = "#117864";

        private static final String PRIMARY_TEXT = "#1B2631";

        private static final String SECONDARY_TEXT = "#566573";

        private static final String BORDER_COLOR = "#A2D9CE";

        // =====================================================
        // CONTENT PANEL
        // =====================================================

        private VBox contentPanel;

        private Button activeButton;

        // =====================================================
        // CONSTRUCTOR
        // =====================================================

        public Settings(String farmerEmail) {

                this(farmerEmail, null);
        }

        public Settings(
                        String farmerEmail,
                        Runnable languageChanged) {

                this.farmerEmail = farmerEmail;
                this.languageChanged = languageChanged;

                loadUser();
        }

        // =====================================================
        // LOAD USER FROM FIRESTORE
        // =====================================================

        private void loadUser() {

                if (farmerEmail == null
                                || farmerEmail.trim().isEmpty()) {

                        System.out.println(
                                        "Settings: Farmer email is empty.");

                        return;
                }

                try {

                        user = userController.getUserByEmail(
                                        farmerEmail);

                        if (user != null) {

                                System.out.println(
                                                "====================================");

                                System.out.println(
                                                "Settings user loaded successfully");

                                System.out.println(
                                                "User Name: "
                                                                + user.getFullName());

                                System.out.println(
                                                "User Email: "
                                                                + user.getEmail());

                                System.out.println(
                                                "User Mobile: "
                                                                + user.getMobileNumber());

                                System.out.println(
                                                "User Gender: "
                                                                + user.getGender());

                                System.out.println(
                                                "User Role: "
                                                                + user.getRole());

                                System.out.println(
                                                "====================================");

                        } else {

                                System.out.println(
                                                "Settings user not found: "
                                                                + farmerEmail);
                        }

                } catch (Exception e) {

                        System.out.println(
                                        "Error loading settings user:");

                        e.printStackTrace();
                }
        }

        // =====================================================
        // MAIN VIEW
        // =====================================================

        public Node getView() {

                VBox root = new VBox(18);

                root.setPadding(
                                new Insets(
                                                10,
                                                10,
                                                20,
                                                10));

                root.setStyle(
                                "-fx-background-color:"
                                                + MAIN_BG
                                                + ";");

                // =================================================
                // PAGE HEADER
                // =================================================

                VBox titles = new VBox(4);

                Label title = new Label("Settings");

                title.setStyle(
                                "-fx-font-size:30px;"
                                                + "-fx-font-weight:800;"
                                                + "-fx-text-fill:"
                                                + PRIMARY_TEXT
                                                + ";");

                Label sub = new Label(
                                "Manage your account and preferences.");

                sub.setStyle(
                                "-fx-font-size:15px;"
                                                + "-fx-text-fill:"
                                                + SECONDARY_TEXT
                                                + ";");

                titles.getChildren()
                                .addAll(
                                                title,
                                                sub);

                // =================================================
                // MAIN SETTINGS LAYOUT
                // =================================================

                HBox layout = new HBox(26);

                layout.setFillHeight(true);

                // =================================================
                // LEFT MENU
                // =================================================

                VBox menu = new VBox(6);

                menu.setPrefWidth(220);
                menu.setMinWidth(200);
                menu.setMaxWidth(230);

                Button accountButton = createMenuItem(
                                "Account",
                                true);

                Button passwordButton = createMenuItem(
                                "Password",
                                false);

                Button notificationButton = createMenuItem(
                                "Notifications",
                                false);

                Button paymentButton = createMenuItem(
                                "Payment Methods",
                                false);

                Button privacyButton = createMenuItem(
                                "Privacy",
                                false);

                Button languageButton = createMenuItem(
                                "Language",
                                false);

                menu.getChildren()
                                .addAll(
                                                accountButton,
                                                passwordButton,
                                                notificationButton,
                                                paymentButton,
                                                privacyButton,
                                                languageButton);

                // =================================================
                // CONTENT PANEL
                // =================================================

                contentPanel = new VBox(18);

                contentPanel.setPadding(
                                new Insets(28));

                contentPanel.setMinHeight(520);

                contentPanel.setMaxWidth(
                                Double.MAX_VALUE);

                HBox.setHgrow(
                                contentPanel,
                                Priority.ALWAYS);

                contentPanel.setStyle(
                                "-fx-background-color:#FFFFFF;"
                                                + "-fx-background-radius:14px;"
                                                + "-fx-border-color:"
                                                + BORDER_COLOR
                                                + ";"
                                                + "-fx-border-radius:14px;");

                // =================================================
                // BUTTON EVENTS
                // =================================================

                accountButton.setOnAction(e -> {

                        setActiveButton(
                                        accountButton);

                        showAccount();
                });

                passwordButton.setOnAction(e -> {

                        setActiveButton(
                                        passwordButton);

                        showPassword();
                });

                notificationButton.setOnAction(e -> {

                        setActiveButton(
                                        notificationButton);

                        showNotifications();
                });

                paymentButton.setOnAction(e -> {

                        setActiveButton(
                                        paymentButton);

                        showPaymentMethods();
                });

                privacyButton.setOnAction(e -> {

                        setActiveButton(
                                        privacyButton);

                        showPrivacy();
                });

                languageButton.setOnAction(e -> {

                        setActiveButton(
                                        languageButton);

                        showLanguage();
                });

                // =================================================
                // DEFAULT PAGE
                // =================================================

                activeButton = accountButton;

                showAccount();

                // =================================================
                // LAYOUT
                // =================================================

                layout.getChildren()
                                .addAll(
                                                menu,
                                                contentPanel);

                VBox.setVgrow(
                                layout,
                                Priority.ALWAYS);

                root.getChildren()
                                .addAll(
                                                titles,
                                                layout);

                // =================================================
                // SCROLL PANE
                // =================================================

                ScrollPane scrollPane = new ScrollPane(root);

                scrollPane.setFitToWidth(true);

                scrollPane.setFitToHeight(true);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setStyle(
                                "-fx-background-color:"
                                                + MAIN_BG
                                                + ";"
                                                + "-fx-background:"
                                                + MAIN_BG
                                                + ";");
        return scrollPane;
        }

        // =====================================================
        // ACTIVE MENU BUTTON
        // =====================================================

        private void setActiveButton(
                        Button button) {

                if (activeButton != null) {

                        activeButton.setStyle(
                                        "-fx-background-color:transparent;"
                                                        + "-fx-text-fill:"
                                                        + PRIMARY_TEXT
                                                        + ";"
                                                        + "-fx-font-size:14px;"
                                                        + "-fx-font-weight:600;"
                                                        + "-fx-background-radius:9px;"
                                                        + "-fx-cursor:hand;");
                }

                button.setStyle(
                                "-fx-background-color:#D4EFDF;"
                                                + "-fx-text-fill:"
                                                + PRIMARY_GREEN
                                                + ";"
                                                + "-fx-font-weight:bold;"
                                                + "-fx-font-size:14px;"
                                                + "-fx-background-radius:9px;"
                                                + "-fx-cursor:hand;");

                activeButton = button;
        }

        // =====================================================
        // ACCOUNT PAGE
        // =====================================================

        private void showAccount() {

                contentPanel.getChildren().clear();

                contentPanel.setSpacing(18);

                // =================================================
                // TITLE
                // =================================================

                Label title = createSectionTitle(
                                "Account Settings");

                Label subtitle = createSubtitle(
                                "Manage your personal account information.");

                // =================================================
                // PERSONAL INFORMATION
                // =================================================

                Label personalTitle = new Label(
                                "Personal Information");

                personalTitle.setStyle(
                                "-fx-font-size:16px;"
                                                + "-fx-font-weight:bold;"
                                                + "-fx-text-fill:"
                                                + PRIMARY_TEXT
                                                + ";");

                // =================================================
                // FETCH FIRESTORE DATA
                // =================================================

                String fullName = user != null
                                ? safe(user.getFullName())
                                : "Not available";

                String email = user != null
                                ? safe(user.getEmail())
                                : safe(farmerEmail);

                String mobile = user != null
                                ? safe(user.getMobileNumber())
                                : "Not provided";

                String gender = user != null
                                ? safe(user.getGender())
                                : "Not provided";

                String role = user != null
                                ? safe(user.getRole())
                                : "Farmer";

                // =================================================
                // FIRST ROW
                // =================================================

                HBox row1 = new HBox(18);

                row1.setFillHeight(true);

                VBox nameBox = createAccountCard(
                                "Full Name",
                                fullName);

                VBox emailBox = createAccountCard(
                                "Email",
                                email);

                HBox.setHgrow(
                                nameBox,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                emailBox,
                                Priority.ALWAYS);

                row1.getChildren()
                                .addAll(
                                                nameBox,
                                                emailBox);

                // =================================================
                // SECOND ROW
                // =================================================

                HBox row2 = new HBox(18);

                row2.setFillHeight(true);

                VBox phoneBox = createAccountCard(
                                "Phone Number",
                                mobile);

                VBox genderBox = createAccountCard(
                                "Gender",
                                gender);

                HBox.setHgrow(
                                phoneBox,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                genderBox,
                                Priority.ALWAYS);

                row2.getChildren()
                                .addAll(
                                                phoneBox,
                                                genderBox);

                // =================================================
                // ACCOUNT TYPE TITLE
                // =================================================

                Label accountTypeTitle = new Label(
                                "Account Type");

                accountTypeTitle.setStyle(
                                "-fx-font-size:16px;"
                                                + "-fx-font-weight:bold;"
                                                + "-fx-text-fill:"
                                                + PRIMARY_TEXT
                                                + ";");

                // =================================================
                // ROLE CARD
                // =================================================

                VBox roleCard = new VBox(5);

                roleCard.setPadding(
                                new Insets(
                                                15,
                                                18,
                                                15,
                                                18));

                roleCard.setMinHeight(78);

                roleCard.setMaxWidth(400);

                roleCard.setStyle(
                                "-fx-background-color:#F4FAF6;"
                                                + "-fx-background-radius:10px;"
                                                + "-fx-border-color:"
                                                + BORDER_COLOR
                                                + ";"
                                                + "-fx-border-radius:10px;");

                Label roleLabel = new Label("Role");

                roleLabel.setStyle(
                                "-fx-font-size:13px;"
                                                + "-fx-text-fill:"
                                                + SECONDARY_TEXT
                                                + ";");

                Label roleValue = new Label(role);

                roleValue.setStyle(
                                "-fx-font-size:16px;"
                                                + "-fx-font-weight:bold;"
                                                + "-fx-text-fill:"
                                                + PRIMARY_GREEN
                                                + ";");

                roleCard.getChildren()
                                .addAll(
                                                roleLabel,
                                                roleValue);

                // =================================================
                // BOTTOM SPACE
                // =================================================

                Region bottomSpace = new Region();

                VBox.setVgrow(
                                bottomSpace,
                                Priority.ALWAYS);

                // =================================================
                // ADD CONTENT
                // =================================================

                contentPanel.getChildren()
                                .addAll(
                                                title,
                                                subtitle,
                                                personalTitle,
                                                row1,
                                                row2,
                                                accountTypeTitle,
                                                roleCard,
                                                bottomSpace);
        }

        // =====================================================
        // ACCOUNT CARD
        // =====================================================

        private VBox createAccountCard(
                        String label,
                        String value) {

                VBox card = new VBox(7);

                card.setPadding(
                                new Insets(
                                                15,
                                                18,
                                                15,
                                                18));

                card.setMinHeight(82);

                card.setMaxWidth(
                                Double.MAX_VALUE);

                HBox.setHgrow(
                                card,
                                Priority.ALWAYS);

                card.setStyle(
                                "-fx-background-color:#F4FAF6;"
                                                + "-fx-background-radius:10px;"
                                                + "-fx-border-color:"
                                                + BORDER_COLOR
                                                + ";"
                                                + "-fx-border-radius:10px;");

                Label labelText = new Label(label);

                labelText.setStyle(
                                "-fx-font-size:13px;"
                                                + "-fx-text-fill:"
                                                + SECONDARY_TEXT
                                                + ";");

                Label valueText = new Label(value);

                valueText.setWrapText(true);

                valueText.setStyle(
                                "-fx-font-size:16px;"
                                                + "-fx-font-weight:bold;"
                                                + "-fx-text-fill:"
                                                + PRIMARY_TEXT
                                                + ";");

                card.getChildren()
                                .addAll(
                                                labelText,
                                                valueText);

                return card;
        }

        // =====================================================
        // PASSWORD PAGE
        // =====================================================

        private void showPassword() {

                contentPanel.getChildren().clear();

                Label title = createSectionTitle(
                                "Password");

                Label subtitle = createSubtitle(
                                "Manage your account password.");

                // =================================================
                // CURRENT PASSWORD
                // =================================================

                PasswordField currentPassword = new PasswordField();

                currentPassword.setPromptText(
                                "Current Password");

                // =================================================
                // NEW PASSWORD
                // =================================================

                PasswordField newPassword = new PasswordField();

                newPassword.setPromptText(
                                "New Password");

                // =================================================
                // CONFIRM PASSWORD
                // =================================================

                PasswordField confirmPassword = new PasswordField();

                confirmPassword.setPromptText(
                                "Confirm New Password");

                styleField(
                                currentPassword);

                styleField(
                                newPassword);

                styleField(
                                confirmPassword);

                // =================================================
                // SAVE BUTTON
                // =================================================

                Button save = new Button(
                                "Save Password");

                stylePrimaryButton(
                                save);

                // =================================================
                // SAVE PASSWORD ACTION
                // =================================================

                save.setOnAction(e -> {

                        String current = currentPassword.getText();

                        String newPass = newPassword.getText();

                        String confirm = confirmPassword.getText();

                        // =============================================
                        // EMPTY FIELD CHECK
                        // =============================================

                        if (current == null
                                        || current.trim().isEmpty()
                                        || newPass == null
                                        || newPass.trim().isEmpty()
                                        || confirm == null
                                        || confirm.trim().isEmpty()) {

                                showAlert(
                                                Alert.AlertType.WARNING,
                                                "Missing Information",
                                                "Please fill in all password fields.");

                                return;
                        }

                        // =============================================
                        // PASSWORD MATCH
                        // =============================================

                        if (!newPass.equals(confirm)) {

                                showAlert(
                                                Alert.AlertType.WARNING,
                                                "Password Mismatch",
                                                "New password and confirm password do not match.");

                                return;
                        }

                        // =============================================
                        // PASSWORD LENGTH
                        // =============================================

                        if (newPass.length() < 6) {

                                showAlert(
                                                Alert.AlertType.WARNING,
                                                "Invalid Password",
                                                "New password must contain at least 6 characters.");

                                return;
                        }

                        // =============================================
                        // SAME PASSWORD
                        // =============================================

                        if (current.equals(newPass)) {

                                showAlert(
                                                Alert.AlertType.WARNING,
                                                "Invalid Password",
                                                "New password must be different from your current password.");

                                return;
                        }

                        // =============================================
                        // EMAIL CHECK
                        // =============================================

                        String email = farmerEmail;

                        if (email == null
                                        || email.trim().isEmpty()) {

                                showAlert(
                                                Alert.AlertType.ERROR,
                                                "Account Error",
                                                "Unable to determine your account email.");

                                return;
                        }

                        // =============================================
                        // DISABLE BUTTON
                        // =============================================

                        save.setDisable(true);

                        save.setText(
                                        "Changing Password...");

                        // =============================================
                        // BACKGROUND TASK
                        // =============================================

                        Task<Boolean> changePasswordTask = new Task<>() {

                                @Override
                                protected Boolean call() {

                                        System.out.println(
                                                        "====================================");

                                        System.out.println(
                                                        "Password change requested");

                                        System.out.println(
                                                        "Email: " + email);

                                        System.out.println(
                                                        "Verifying current password...");

                                        boolean result = authController.changePassword(
                                                        email,
                                                        current,
                                                        newPass);

                                        System.out.println(
                                                        "Password change result: "
                                                                        + result);

                                        System.out.println(
                                                        "====================================");

                                        return result;
                                }
                        };

                        // =============================================
                        // SUCCESS / FAILURE
                        // =============================================

                        changePasswordTask.setOnSucceeded(event -> {

                                Boolean result = changePasswordTask.getValue();

                                save.setDisable(false);

                                save.setText(
                                                "Save Password");

                                if (Boolean.TRUE.equals(result)) {

                                        currentPassword.clear();
                                        newPassword.clear();
                                        confirmPassword.clear();

                                        showAlert(
                                                        Alert.AlertType.INFORMATION,
                                                        "Password Changed",
                                                        "Your password has been changed successfully.");

                                } else {

                                        showAlert(
                                                        Alert.AlertType.ERROR,
                                                        "Password Change Failed",
                                                        "Current password is incorrect or the password could not be changed.");
                                }
                        });

                        // =============================================
                        // EXCEPTION
                        // =============================================

                        changePasswordTask.setOnFailed(event -> {

                                save.setDisable(false);

                                save.setText(
                                                "Save Password");

                                Throwable exception = changePasswordTask.getException();

                                if (exception != null) {

                                        exception.printStackTrace();
                                }

                                showAlert(
                                                Alert.AlertType.ERROR,
                                                "Error",
                                                "An error occurred while changing your password.");
                        });

                        Thread passwordThread = new Thread(
                                        changePasswordTask);

                        passwordThread.setDaemon(true);

                        passwordThread.start();
                });

                // =================================================
                // ADD CONTENT
                // =================================================

                contentPanel.getChildren()
                                .addAll(
                                                title,
                                                subtitle,
                                                currentPassword,
                                                newPassword,
                                                confirmPassword,
                                                save);
        }

        // =====================================================
        // ALERT
        // =====================================================

        private void showAlert(
                        Alert.AlertType type,
                        String title,
                        String message) {

                Platform.runLater(() -> {

                        Alert alert = new Alert(type);

                        alert.setTitle(title);

                        alert.setHeaderText(null);

                        alert.setContentText(message);

                        alert.showAndWait();
                });
        }

        // =====================================================
        // NOTIFICATIONS PAGE
        // =====================================================

        private void showNotifications() {

                contentPanel.getChildren().clear();

                Label title = createSectionTitle(
                                "Notifications");

                Label subtitle = createSubtitle(
                                "Choose which notifications you want to receive.");

                CheckBox orders = createCheckBox(
                                "Order and rental updates");

                CheckBox equipment = createCheckBox(
                                "Equipment availability updates");

                CheckBox market = createCheckBox(
                                "Market and crop price updates");

                CheckBox weather = createCheckBox(
                                "Weather alerts");

                CheckBox recommendations = createCheckBox(
                                "AI recommendations");

                contentPanel.getChildren()
                                .addAll(
                                                title,
                                                subtitle,
                                                orders,
                                                equipment,
                                                market,
                                                weather,
                                                recommendations);
        }

        // =====================================================
        // PAYMENT METHODS
        // =====================================================

        private void showPaymentMethods() {

                contentPanel.getChildren().clear();

                Label title = createSectionTitle(
                                "Payment Methods");

                Label subtitle = createSubtitle(
                                "Manage your payment methods.");

                VBox paymentCard = new VBox(8);

                paymentCard.setPadding(
                                new Insets(20));

                paymentCard.setStyle(
                                "-fx-background-color:#F4FAF6;"
                                                + "-fx-background-radius:10px;"
                                                + "-fx-border-color:"
                                                + BORDER_COLOR
                                                + ";"
                                                + "-fx-border-radius:10px;");

                Label razorpay = new Label(
                                "Razorpay");

                razorpay.setStyle(
                                "-fx-font-size:17px;"
                                                + "-fx-font-weight:bold;"
                                                + "-fx-text-fill:"
                                                + PRIMARY_TEXT
                                                + ";");

                Label status = new Label(
                                "Payment gateway will be available during checkout.");

                status.setWrapText(true);

                status.setStyle(
                                "-fx-font-size:14px;"
                                                + "-fx-text-fill:"
                                                + SECONDARY_TEXT
                                                + ";");

                paymentCard.getChildren()
                                .addAll(
                                                razorpay,
                                                status);

                contentPanel.getChildren()
                                .addAll(
                                                title,
                                                subtitle,
                                                paymentCard);
        }

        // =====================================================
        // PRIVACY
        // =====================================================

        private void showPrivacy() {

                contentPanel.getChildren().clear();

                Label title = createSectionTitle(
                                "Privacy");

                Label subtitle = createSubtitle(
                                "Manage your privacy preferences.");

                CheckBox profileVisibility = createCheckBox(
                                "Allow my profile to be visible to other users");

                CheckBox equipmentVisibility = createCheckBox(
                                "Allow my listed equipment to appear in marketplace");

                CheckBox recommendations = createCheckBox(
                                "Allow personalized recommendations");

                contentPanel.getChildren()
                                .addAll(
                                                title,
                                                subtitle,
                                                profileVisibility,
                                                equipmentVisibility,
                                                recommendations);
        }

        // =====================================================
        // LANGUAGE
        // =====================================================

        private void showLanguage() {

                contentPanel.getChildren().clear();

                Label title = createSectionTitle(
                                "Language");

                Label subtitle = createSubtitle(
                                "Choose your preferred application language.");

                Label languageLabel = new Label(
                                "Application Language");

                languageLabel.setStyle(
                                "-fx-font-size:14px;"
                                                + "-fx-text-fill:"
                                                + SECONDARY_TEXT
                                                + ";");

                ComboBox<String> language = new ComboBox<>();

                language.getItems()
                                .addAll(
                                                "English",
                                                "Marathi");

                language.setPrefWidth(300);

                language.setPrefHeight(42);

                language.setStyle(
                                "-fx-background-color:white;"
                                                + "-fx-border-color:"
                                                + BORDER_COLOR
                                                + ";"
                                                + "-fx-border-radius:8px;"
                                                + "-fx-background-radius:8px;"
                                                + "-fx-font-size:14px;");

                contentPanel.getChildren()
                                .addAll(
                                                title,
                                                subtitle,
                                                languageLabel,
                                                language);
        }

        // =====================================================
        // MENU BUTTON
        // =====================================================

        private Button createMenuItem(
                        String name,
                        boolean active) {

                Button button = new Button(name);

                button.setMaxWidth(
                                Double.MAX_VALUE);

                button.setPrefHeight(44);

                button.setAlignment(
                                Pos.CENTER_LEFT);

                button.setPadding(
                                new Insets(
                                                8,
                                                15,
                                                8,
                                                25));

                if (active) {

                        button.setStyle(
                                        "-fx-background-color:#D4EFDF;"
                                                        + "-fx-text-fill:"
                                                        + PRIMARY_GREEN
                                                        + ";"
                                                        + "-fx-font-weight:bold;"
                                                        + "-fx-font-size:14px;"
                                                        + "-fx-background-radius:9px;"
                                                        + "-fx-cursor:hand;");

                } else {

                        button.setStyle(
                                        "-fx-background-color:transparent;"
                                                        + "-fx-text-fill:"
                                                        + PRIMARY_TEXT
                                                        + ";"
                                                        + "-fx-font-size:14px;"
                                                        + "-fx-font-weight:600;"
                                                        + "-fx-background-radius:9px;"
                                                        + "-fx-cursor:hand;");
                }

                return button;
        }

        // =====================================================
        // CHECKBOX
        // =====================================================

        private CheckBox createCheckBox(
                        String text) {

                CheckBox checkBox = new CheckBox(text);

                checkBox.setSelected(true);

                checkBox.setPadding(
                                new Insets(
                                                8,
                                                0,
                                                8,
                                                0));

                checkBox.setStyle(
                                "-fx-font-size:14px;"
                                                + "-fx-text-fill:"
                                                + PRIMARY_TEXT
                                                + ";");

                return checkBox;
        }

        // =====================================================
        // SECTION TITLE
        // =====================================================

        private Label createSectionTitle(
                        String text) {

                Label label = new Label(text);

                label.setStyle(
                                "-fx-font-size:26px;"
                                                + "-fx-font-weight:800;"
                                                + "-fx-text-fill:"
                                                + PRIMARY_TEXT
                                                + ";");

                return label;
        }

        // =====================================================
        // SUBTITLE
        // =====================================================

        private Label createSubtitle(
                        String text) {

                Label label = new Label(text);

                label.setStyle(
                                "-fx-font-size:14px;"
                                                + "-fx-text-fill:"
                                                + SECONDARY_TEXT
                                                + ";");

                return label;
        }

        // =====================================================
        // PASSWORD FIELD STYLE
        // =====================================================

        private void styleField(
                        PasswordField field) {

                field.setPrefHeight(42);

                field.setMaxWidth(450);

                field.setStyle(
                                "-fx-background-color:#F4FAF6;"
                                                + "-fx-border-color:"
                                                + BORDER_COLOR
                                                + ";"
                                                + "-fx-border-radius:8px;"
                                                + "-fx-background-radius:8px;"
                                                + "-fx-font-size:14px;");
        }

        // =====================================================
        // PRIMARY BUTTON
        // =====================================================

        private void stylePrimaryButton(
                        Button button) {

                button.setPrefHeight(42);

                button.setPadding(
                                new Insets(
                                                8,
                                                22,
                                                8,
                                                22));

                button.setStyle(
                                "-fx-background-color:"
                                                + PRIMARY_GREEN
                                                + ";"
                                                + "-fx-text-fill:white;"
                                                + "-fx-font-weight:bold;"
                                                + "-fx-font-size:14px;"
                                                + "-fx-background-radius:8px;"
                                                + "-fx-cursor:hand;");
        }

        // =====================================================
        // SAFE VALUE
        // =====================================================

        private String safe(
                        String value) {

                if (value == null
                                || value.trim().isEmpty()) {

                        return "Not provided";
                }

                return value;
        }
}