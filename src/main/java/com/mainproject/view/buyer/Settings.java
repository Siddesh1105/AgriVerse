package com.mainproject.view.buyer;

import com.mainproject.controller.UserController;
import com.mainproject.model.User;


import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Settings {

    private final BuyerDashboard mainController;
    private final Runnable languageChanged;
    private final UserController userController;

    private final List<SettingsSection> sections = new ArrayList<>();

    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public Settings(BuyerDashboard controller) {
        this(controller, null);
    }

    public Settings(
            BuyerDashboard controller,
            Runnable languageChanged) {

        this.mainController = controller;
        this.languageChanged = languageChanged;
        this.userController = new UserController();
    }

    // =====================================================
    // MAIN VIEW
    // =====================================================

    public Node getView() {

        VBox content = new VBox(18);

        content.setPadding(new Insets(30, 40, 60, 40));

        content.setFillWidth(true);

        content.setStyle(
                "-fx-background-color: #F8FAFC;"
        );


        // =====================================================
        // HEADER
        // =====================================================

        Label title = new Label("Settings & Preferences");

        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1E293B;"
        );


        Label subtitle = new Label(
                "Manage your account and application preferences."
        );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #64748B;"
        );


        VBox headerBox = new VBox(6);

        headerBox.getChildren().addAll(
                title,
                subtitle
        );


        // =====================================================
        // SETTINGS SECTIONS
        // =====================================================

        SettingsSection accountSection = createSection(
                "👤",
                "Account Settings",
                "Manage your personal information and profile details.",
                createAccountSettings()
        );


        SettingsSection addressSection = createSection(
                "📍",
                "Saved Delivery Addresses",
                "Add and manage your delivery addresses.",
                createAddressSettings()
        );


        SettingsSection paymentSection = createSection(
                "💳",
                "Payment Methods & UPI",
                "Manage your preferred payment method.",
                createPaymentSettings()
        );


        SettingsSection notificationSection = createSection(
                "🔔",
                "Notification Preferences",
                "Choose which notifications you want to receive.",
                createNotificationSettings()
        );


        SettingsSection privacySection = createSection(
                "🔒",
                "Privacy & Security",
                "Manage your privacy and security preferences.",
                createPrivacySettings()
        );


        SettingsSection languageSection = createSection(
                "🌐",
                "App Language",
                "Choose between English and Marathi.",
                createLanguageSettings()
        );


        SettingsSection helpSection = createSection(
                "❓",
                "Help & Support",
                "Get help and contact the AgriVerse support team.",
                createHelpSettings()
        );


        VBox sectionsBox = new VBox(12);

        sectionsBox.getChildren().addAll(
                accountSection.getView(),
                addressSection.getView(),
                paymentSection.getView(),
                notificationSection.getView(),
                privacySection.getView(),
                languageSection.getView(),
                helpSection.getView()
        );


        // =====================================================
        // MAIN CONTENT
        // =====================================================

        content.getChildren().addAll(
                headerBox,
                sectionsBox
        );


        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane = new ScrollPane(content);

        scrollPane.setFitToWidth(true);

        scrollPane.setFitToHeight(false);

        scrollPane.setPannable(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );


        scrollPane.setStyle(
                "-fx-background-color: #F8FAFC;" +
                "-fx-background: #F8FAFC;" +
                "-fx-border-color: transparent;"
        );


        // Content uses available width

        content.prefWidthProperty().bind(
                scrollPane.widthProperty().subtract(20)
        );


     

        return scrollPane;
    }


    // =====================================================
    // CREATE MODERN EXPANDABLE SECTION
    // =====================================================

    private SettingsSection createSection(
            String icon,
            String title,
            String description,
            Node sectionContent) {

        SettingsSection section =
                new SettingsSection(
                        icon,
                        title,
                        description,
                        sectionContent
                );

        sections.add(section);

        return section;
    }


    // =====================================================
    // CLOSE OTHER SECTIONS
    // =====================================================

    private void openOnly(SettingsSection selectedSection) {

        for (SettingsSection section : sections) {

            if (section != selectedSection) {

                section.close();
            }
        }

        selectedSection.open();
    }


    // =====================================================
    // ACCOUNT SETTINGS
    // =====================================================

    private Node createAccountSettings() {

        VBox box = createSectionBox();


        User currentUser = mainController != null
                ? mainController.getCurrentUser()
                : null;


        Label sectionTitle = createSectionTitle(
                "Account Settings"
        );


        Label description = createDescription(
                "Update your personal information."
        );


        TextField fullName = new TextField(
                currentUser != null
                        && currentUser.getFullName() != null
                        ? currentUser.getFullName()
                        : ""
        );


        TextField email = new TextField(
                currentUser != null
                        && currentUser.getEmail() != null
                        ? currentUser.getEmail()
                        : ""
        );

        email.setEditable(false);


        TextField mobile = new TextField(
                currentUser != null
                        && currentUser.getMobileNumber() != null
                        ? currentUser.getMobileNumber()
                        : ""
        );


        ComboBox<String> gender = new ComboBox<>();

        gender.getItems().addAll(
                "Male",
                "Female",
                "Other",
                "Prefer not to say"
        );


        if (currentUser != null
                && currentUser.getGender() != null) {

            gender.setValue(
                    currentUser.getGender()
            );
        }


        Button saveButton =
                createPrimaryButton(
                        "Save Profile Changes"
                );


        saveButton.setOnAction(e -> {

            if (currentUser == null) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Current user information is not available."
                );

                return;
            }


            String name =
                    fullName.getText().trim();


            if (name.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Required",
                        "Please enter your full name."
                );

                return;
            }


            currentUser.setFullName(name);

            currentUser.setMobileNumber(
                    mobile.getText().trim()
            );

            currentUser.setGender(
                    gender.getValue()
            );


            boolean updated =
                    userController.updateProfile(
                            currentUser
                    );


            if (updated) {

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Profile updated successfully."
                );

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Unable to update profile."
                );
            }
        });


        box.getChildren().addAll(
                sectionTitle,
                description,
                createField("Full Name", fullName),
                createField("Email", email),
                createField("Mobile Number", mobile),
                createField("Gender", gender),
                saveButton
        );


        return box;
    }


    // =====================================================
    // SAVED DELIVERY ADDRESSES
    // =====================================================

    private Node createAddressSettings() {

        VBox box = createSectionBox();


        Label sectionTitle =
                createSectionTitle(
                        "Saved Delivery Addresses"
                );


        Label description =
                createDescription(
                        "Add and manage your delivery addresses."
                );


        TextArea addressInput =
                new TextArea();

        addressInput.setPromptText(
                "Enter complete delivery address"
        );

        addressInput.setPrefRowCount(3);

        addressInput.setWrapText(true);

        addressInput.setStyle(
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;"
        );


        VBox addressList = new VBox(10);


        Button addButton =
                createPrimaryButton(
                        "+ Add Address"
                );


        addButton.setOnAction(e -> {

            String address =
                    addressInput.getText().trim();


            if (address.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Address Required",
                        "Please enter an address."
                );

                return;
            }


            HBox row = new HBox(12);

            row.setAlignment(
                    Pos.CENTER_LEFT
            );

            row.setPadding(
                    new Insets(12)
            );

            row.setStyle(
                    "-fx-background-color: #F8FAFC;" +
                    "-fx-border-color: #E2E8F0;" +
                    "-fx-border-radius: 10;" +
                    "-fx-background-radius: 10;"
            );


            Label addressLabel =
                    new Label("📍 " + address);

            addressLabel.setWrapText(true);

            addressLabel.setStyle(
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: #334155;"
            );


            HBox.setHgrow(
                    addressLabel,
                    Priority.ALWAYS
            );


            addressLabel.setMaxWidth(
                    Double.MAX_VALUE
            );


            Button removeButton =
                    new Button("Remove");


            removeButton.setStyle(
                    "-fx-background-color: #FEE2E2;" +
                    "-fx-text-fill: #DC2626;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 7;" +
                    "-fx-cursor: hand;"
            );


            removeButton.setOnAction(x ->
                    addressList.getChildren().remove(row)
            );


            row.getChildren().addAll(
                    addressLabel,
                    removeButton
            );


            addressList.getChildren().add(
                    row
            );


            addressInput.clear();
        });


        box.getChildren().addAll(
                sectionTitle,
                description,
                addressInput,
                addButton,
                addressList
        );


        return box;
    }


    // =====================================================
    // PAYMENT METHODS
    // =====================================================

    private Node createPaymentSettings() {

        VBox box = createSectionBox();


        Label sectionTitle =
                createSectionTitle(
                        "Payment Methods & UPI"
                );


        Label description =
                createDescription(
                        "Choose your preferred payment method."
                );


        ComboBox<String> method =
                new ComboBox<>();


        method.getItems().addAll(
                "UPI",
                "Cash on Delivery",
                "Debit / Credit Card"
        );


        method.setValue("UPI");


        TextField upiId =
                new TextField();

        upiId.setPromptText(
                "example@upi"
        );


        Button saveButton =
                createPrimaryButton(
                        "Save Payment Preference"
                );


        saveButton.setOnAction(e ->

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Saved",
                        "Your payment preference has been saved."
                )
        );


        box.getChildren().addAll(
                sectionTitle,
                description,
                createField(
                        "Payment Method",
                        method
                ),
                createField(
                        "UPI ID",
                        upiId
                ),
                saveButton
        );


        return box;
    }


    // =====================================================
    // NOTIFICATION PREFERENCES
    // =====================================================

    private Node createNotificationSettings() {

        VBox box = createSectionBox();


        Label sectionTitle =
                createSectionTitle(
                        "Notification Preferences"
                );


        Label description =
                createDescription(
                        "Choose which notifications you want to receive."
                );


        CheckBox orderUpdates =
                createCheckBox(
                        "Order updates"
                );

        orderUpdates.setSelected(true);


        CheckBox priceAlerts =
                createCheckBox(
                        "Crop price alerts"
                );

        priceAlerts.setSelected(true);


        CheckBox liveAlerts =
                createCheckBox(
                        "Farmer live streaming alerts"
                );

        liveAlerts.setSelected(true);


        CheckBox offers =
                createCheckBox(
                        "Offers and promotions"
                );

        offers.setSelected(true);


        Button saveButton =
                createPrimaryButton(
                        "Save Notification Preferences"
                );


        saveButton.setOnAction(e ->

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Saved",
                        "Notification preferences saved successfully."
                )
        );


        box.getChildren().addAll(
                sectionTitle,
                description,
                orderUpdates,
                priceAlerts,
                liveAlerts,
                offers,
                saveButton
        );


        return box;
    }


    // =====================================================
    // PRIVACY & SECURITY
    // =====================================================

    private Node createPrivacySettings() {

        VBox box = createSectionBox();


        Label sectionTitle =
                createSectionTitle(
                        "Privacy & Security"
                );


        Label description =
                createDescription(
                        "Manage your privacy and security preferences."
                );


        CheckBox profileVisible =
                createCheckBox(
                        "Allow farmers to view my public profile"
                );

        profileVisible.setSelected(true);


        CheckBox rememberSession =
                createCheckBox(
                        "Keep me signed in on this device"
                );

        rememberSession.setSelected(true);


        PasswordField currentPassword =
                new PasswordField();

        currentPassword.setPromptText(
                "Enter current password"
        );


        PasswordField newPassword =
                new PasswordField();

        newPassword.setPromptText(
                "Enter new password"
        );


        Button saveButton =
                createPrimaryButton(
                        "Save Privacy Preferences"
                );


        saveButton.setOnAction(e ->

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Saved",
                        "Privacy preferences saved successfully."
                )
        );


        box.getChildren().addAll(
                sectionTitle,
                description,
                profileVisible,
                rememberSession,
                createField(
                        "Current Password",
                        currentPassword
                ),
                createField(
                        "New Password",
                        newPassword
                ),
                saveButton
        );


        return box;
    }


    // =====================================================
    // LANGUAGE SETTINGS
    // =====================================================

    private Node createLanguageSettings() {

        VBox box = createSectionBox();


        Label sectionTitle =
                createSectionTitle(
                        "App Language"
                );


        Label description =
                createDescription(
                        "Choose your preferred application language."
                );


        ComboBox<String> language =
                new ComboBox<>();


        language.getItems().addAll(
                "English",
                "Marathi"
        );




        box.getChildren().addAll(
                sectionTitle,
                description,
                createField(
                        "Select Language",
                        language
                )
        );


        return box;
    }


    // =====================================================
    // HELP & SUPPORT
    // =====================================================

    private Node createHelpSettings() {

        VBox box = createSectionBox();


        Label sectionTitle =
                createSectionTitle(
                        "Help & Support"
                );


        Label description =
                createDescription(
                        "Need help? Contact the AgriVerse support team."
                );


        Label support =
                new Label(
                        "📧  Support: support@agriverse.com"
                );


        support.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #2E7D32;" +
                "-fx-font-weight: bold;"
        );


        box.getChildren().addAll(
                sectionTitle,
                description,
                support
        );


        return box;
    }


    // =====================================================
    // MODERN SETTINGS SECTION CLASS
    // =====================================================

    private class SettingsSection {

        private final VBox root;

        private final VBox contentBox;

        private final Label arrow;

        private boolean expanded = false;


        public SettingsSection(
                String icon,
                String title,
                String description,
                Node content) {


            root = new VBox();


            root.setMaxWidth(
                    Double.MAX_VALUE
            );


            root.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                    "-fx-border-color: #E2E8F0;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;"
            );


            // =============================================
            // HEADER
            // =============================================

            HBox header =
                    new HBox(14);


            header.setAlignment(
                    Pos.CENTER_LEFT
            );


            header.setPadding(
                    new Insets(16, 20, 16, 20)
            );


            header.setCursor(
                    Cursor.HAND
            );


            Label iconLabel =
                    new Label(icon);


            iconLabel.setStyle(
                    "-fx-font-size: 18px;"
            );


            VBox textBox =
                    new VBox(3);


            Label titleLabel =
                    new Label(title);


            titleLabel.setStyle(
                    "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1E293B;"
            );


            Label descriptionLabel =
                    new Label(description);


            descriptionLabel.setStyle(
                    "-fx-font-size: 12px;" +
                    "-fx-text-fill: #94A3B8;"
            );


            textBox.getChildren().addAll(
                    titleLabel,
                    descriptionLabel
            );


            HBox.setHgrow(
                    textBox,
                    Priority.ALWAYS
            );


            arrow =
                    new Label("⌄");


            arrow.setStyle(
                    "-fx-font-size: 20px;" +
                    "-fx-text-fill: #64748B;"
            );


            header.getChildren().addAll(
                    iconLabel,
                    textBox,
                    arrow
            );


            // =============================================
            // CONTENT
            // =============================================

            Separator separator =
                    new Separator();


            contentBox =
                    new VBox();


            contentBox.setVisible(false);

            contentBox.setManaged(false);


            contentBox.getChildren().add(
                    content
            );


            root.getChildren().addAll(
                    header,
                    separator,
                    contentBox
            );


            header.setOnMouseClicked(e -> {

                if (expanded) {

                    close();

                } else {

                    openOnly(this);
                }
            });
        }


        public Node getView() {

            return root;
        }


        public void open() {

            expanded = true;


            contentBox.setVisible(true);

            contentBox.setManaged(true);


            arrow.setText("⌃");


            root.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                    "-fx-border-color: #B7DFC0;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;"
            );
        }


        public void close() {

            expanded = false;


            contentBox.setVisible(false);

            contentBox.setManaged(false);


            arrow.setText("⌄");


            root.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                    "-fx-border-color: #E2E8F0;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;"
            );
        }
    }


    // =====================================================
    // UI HELPERS
    // =====================================================

    private VBox createSectionBox() {

        VBox box = new VBox(16);


        box.setPadding(
                new Insets(22)
        );


        box.setFillWidth(true);


        box.setStyle(
                "-fx-background-color: #FFFFFF;"
        );


        return box;
    }


    private Label createSectionTitle(
            String text) {

        Label label =
                new Label(text);


        label.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1E293B;"
        );


        return label;
    }


    private Label createDescription(
            String text) {

        Label label =
                new Label(text);


        label.setWrapText(true);


        label.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #64748B;"
        );


        return label;
    }


    private CheckBox createCheckBox(
            String text) {

        CheckBox checkBox =
                new CheckBox(text);


        checkBox.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #334155;"
        );


        return checkBox;
    }


    private VBox createField(
            String labelText,
            Node field) {

        VBox box =
                new VBox(7);


        Label label =
                new Label(labelText);


        label.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #334155;"
        );


        if (field instanceof Region) {

            Region region =
                    (Region) field;


            region.setMaxWidth(
                    Double.MAX_VALUE
            );


            region.setStyle(
                    "-fx-background-radius: 8;" +
                    "-fx-border-radius: 8;"
            );
        }


        box.getChildren().addAll(
                label,
                field
        );


        return box;
    }


    private Button createPrimaryButton(
            String text) {

        Button button =
                new Button(text);


        button.setStyle(
                "-fx-background-color: #2E7D32;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 20;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );


        return button;
    }


    // =====================================================
    // ALERT
    // =====================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(type);


        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}