package com.mainproject.view.farmer;

import com.mainproject.dao.UserDAO;
import com.mainproject.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

    private final String farmerEmail;

    private User user;

    private final UserDAO userDAO =
            new UserDAO();

    private static final String MAIN_BG = "#E9F7EF";
    private static final String PRIMARY_GREEN = "#117864";
    private static final String PRIMARY_TEXT = "#1B2631";
    private static final String SECONDARY_TEXT = "#566573";
    private static final String BORDER_COLOR = "#A2D9CE";

    private VBox contentPanel;
    private Button activeButton;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Settings(String farmerEmail) {

        this.farmerEmail = farmerEmail;

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

            user =
                    userDAO.getUserByEmail(
                            farmerEmail);

            if (user != null) {

                System.out.println(
                        "Settings user loaded successfully.");

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

        VBox root =
                new VBox(18);

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
        // TITLE
        // =================================================

        VBox titles =
                new VBox(4);

        Label title =
                new Label("Settings");

        title.setStyle(
                "-fx-font-size:30px;"
                        + "-fx-font-weight:800;"
                        + "-fx-text-fill:"
                        + PRIMARY_TEXT
                        + ";");

        Label sub =
                new Label(
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

        HBox layout =
                new HBox(20);

        // =================================================
        // LEFT MENU
        // =================================================

        VBox menu =
                new VBox(6);

        menu.setPrefWidth(220);
        menu.setMinWidth(200);
        menu.setMaxWidth(230);

        // ACCOUNT
        Button accountButton =
                createMenuItem(
                        "Account",
                        true);

        // PASSWORD
        Button passwordButton =
                createMenuItem(
                        "Password",
                        false);

        // NOTIFICATIONS
        Button notificationButton =
                createMenuItem(
                        "Notifications",
                        false);

        // PAYMENT METHODS
        Button paymentButton =
                createMenuItem(
                        "Payment Methods",
                        false);

        // PRIVACY
        Button privacyButton =
                createMenuItem(
                        "Privacy",
                        false);

        // LANGUAGE
        Button languageButton =
                createMenuItem(
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

        contentPanel =
                new VBox();

        contentPanel.setPadding(
                new Insets(28));

        contentPanel.setMaxWidth(
                Double.MAX_VALUE);

        HBox.setHgrow(
                contentPanel,
                Priority.ALWAYS);

        contentPanel.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:14px;"
                        + "-fx-border-color:"
                        + BORDER_COLOR
                        + ";"
                        + "-fx-border-radius:14px;");

        // =================================================
        // BUTTON ACTIONS
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
        // DEFAULT
        // =================================================

        activeButton =
                accountButton;

        showAccount();

        // =================================================
        // ADD TO LAYOUT
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
        // SCROLL
        // =================================================

        ScrollPane scrollPane =
                new ScrollPane(root);

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
    // ACTIVE MENU
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
    // ACCOUNT
    // =====================================================

    private void showAccount() {

        contentPanel.getChildren().clear();

        Label title =
                createSectionTitle(
                        "Account Settings");

        Label subtitle =
                createSubtitle(
                        "Manage your personal account information.");

        String fullName =
                user != null
                        ? safe(user.getFullName())
                        : "Not available";

        String email =
                user != null
                        ? safe(user.getEmail())
                        : farmerEmail;

        String mobile =
                user != null
                        ? safe(user.getMobileNumber())
                        : "Not provided";

        String gender =
                user != null
                        ? safe(user.getGender())
                        : "Not provided";

        String role =
                user != null
                        ? safe(user.getRole())
                        : "Farmer";

        HBox row1 =
                new HBox(35);

        VBox nameBox =
                createInfoBox(
                        "Full Name",
                        fullName);

        VBox emailBox =
                createInfoBox(
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

        HBox row2 =
                new HBox(35);

        VBox phoneBox =
                createInfoBox(
                        "Phone Number",
                        mobile);

        VBox genderBox =
                createInfoBox(
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

        VBox roleBox =
                createInfoBox(
                        "Role",
                        role);

        contentPanel.getChildren()
                .addAll(
                        title,
                        subtitle,
                        new Region(),
                        row1,
                        row2,
                        roleBox);
    }

    // =====================================================
    // PASSWORD
    // =====================================================

    private void showPassword() {

        contentPanel.getChildren().clear();

        Label title =
                createSectionTitle(
                        "Password");

        Label subtitle =
                createSubtitle(
                        "Manage your account password.");

        PasswordField currentPassword =
                new PasswordField();

        currentPassword.setPromptText(
                "Current Password");

        PasswordField newPassword =
                new PasswordField();

        newPassword.setPromptText(
                "New Password");

        PasswordField confirmPassword =
                new PasswordField();

        confirmPassword.setPromptText(
                "Confirm New Password");

        styleField(
                currentPassword);

        styleField(
                newPassword);

        styleField(
                confirmPassword);

        Button save =
                new Button(
                        "Save Password");

        stylePrimaryButton(
                save);

        save.setOnAction(e -> {

            System.out.println(
                    "Password update requested.");

            /*
             * Firebase password update can be
             * connected here later using your
             * existing authentication logic.
             */
        });

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
    // NOTIFICATIONS
    // =====================================================

    private void showNotifications() {

        contentPanel.getChildren().clear();

        Label title =
                createSectionTitle(
                        "Notifications");

        Label subtitle =
                createSubtitle(
                        "Choose which notifications you want to receive.");

        CheckBox orders =
                new CheckBox(
                        "Order and rental updates");

        CheckBox equipment =
                new CheckBox(
                        "Equipment availability updates");

        CheckBox market =
                new CheckBox(
                        "Market and crop price updates");

        CheckBox weather =
                new CheckBox(
                        "Weather alerts");

        CheckBox recommendations =
                new CheckBox(
                        "AI recommendations");

        orders.setSelected(true);
        equipment.setSelected(true);
        market.setSelected(true);
        weather.setSelected(true);
        recommendations.setSelected(true);

        styleCheckBox(orders);
        styleCheckBox(equipment);
        styleCheckBox(market);
        styleCheckBox(weather);
        styleCheckBox(recommendations);

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

        Label title =
                createSectionTitle(
                        "Payment Methods");

        Label subtitle =
                createSubtitle(
                        "Manage your payment methods.");

        VBox paymentCard =
                new VBox(8);

        paymentCard.setPadding(
                new Insets(20));

        paymentCard.setStyle(
                "-fx-background-color:#F8FBF9;"
                        + "-fx-border-color:"
                        + BORDER_COLOR
                        + ";"
                        + "-fx-border-radius:10px;"
                        + "-fx-background-radius:10px;");

        Label razorpay =
                new Label(
                        "Razorpay");

        razorpay.setStyle(
                "-fx-font-size:16px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:"
                        + PRIMARY_TEXT
                        + ";");

        Label status =
                new Label(
                        "Payment gateway will be available during checkout.");

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

        Label title =
                createSectionTitle(
                        "Privacy");

        Label subtitle =
                createSubtitle(
                        "Manage your privacy preferences.");

        CheckBox profileVisibility =
                new CheckBox(
                        "Allow my profile to be visible to other users");

        CheckBox equipmentVisibility =
                new CheckBox(
                        "Allow my listed equipment to appear in marketplace");

        CheckBox recommendations =
                new CheckBox(
                        "Allow personalized recommendations");

        profileVisibility.setSelected(true);
        equipmentVisibility.setSelected(true);
        recommendations.setSelected(true);

        styleCheckBox(
                profileVisibility);

        styleCheckBox(
                equipmentVisibility);

        styleCheckBox(
                recommendations);

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

        Label title =
                createSectionTitle(
                        "Language");

        Label subtitle =
                createSubtitle(
                        "Choose your preferred application language.");

        Label languageLabel =
                new Label(
                        "Application Language");

        languageLabel.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-text-fill:"
                        + SECONDARY_TEXT
                        + ";");

        ComboBox<String> language =
                new ComboBox<>();

        language.getItems()
                .addAll(
                        "English",
                        "Marathi",
                        "Hindi");

        language.setValue(
                "English");

        language.setPrefWidth(
                300);

        language.setPrefHeight(
                42);

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

        Button button =
                new Button(name);

        button.setMaxWidth(
                Double.MAX_VALUE);

        button.setPrefHeight(
                44);

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
    // INFO BOX
    // =====================================================

    private VBox createInfoBox(
            String key,
            String value) {

        VBox box =
                new VBox(6);

        HBox.setHgrow(
                box,
                Priority.ALWAYS);

        Label keyLabel =
                new Label(key);

        keyLabel.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-text-fill:"
                        + SECONDARY_TEXT
                        + ";");

        Label valueLabel =
                new Label(value);

        valueLabel.setWrapText(true);

        valueLabel.setStyle(
                "-fx-font-size:16px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:"
                        + PRIMARY_TEXT
                        + ";");

        box.getChildren()
                .addAll(
                        keyLabel,
                        valueLabel);

        return box;
    }

    // =====================================================
    // SECTION TITLE
    // =====================================================

    private Label createSectionTitle(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size:21px;"
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

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-text-fill:"
                        + SECONDARY_TEXT
                        + ";");

        return label;
    }

    // =====================================================
    // TEXT FIELD STYLE
    // =====================================================

    private void styleField(
            PasswordField field) {

        field.setPrefHeight(42);
        field.setMaxWidth(450);

        field.setStyle(
                "-fx-background-color:#F8FBF9;"
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
    // CHECKBOX STYLE
    // =====================================================

    private void styleCheckBox(
            CheckBox checkBox) {

        checkBox.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-text-fill:"
                        + PRIMARY_TEXT
                        + ";");

        checkBox.setPadding(
                new Insets(
                        8,
                        0,
                        8,
                        0));
    }

    // =====================================================
    // SAFE STRING
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