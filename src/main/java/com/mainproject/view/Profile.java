package com.mainproject.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;

public class Profile {

    // =========================================================
    // COLORS
    // =========================================================

    private static final String GREEN = "#117864";
    private static final String LIGHT_GREEN = "#D4EFDF";
    private static final String BORDER = "#A2D9CE";
    private static final String TEXT = "#1B2631";
    private static final String SUB_TEXT = "#566573";
    private static final String FIELD_BG = "#F4FAF7";

    // =========================================================
    // PROFILE DATA
    // =========================================================

    private String fullName = "Rajesh Patil";
    private String phone = "+91 98765 43210";
    private String email = "rajeshpatil@email.com";
    private String farmName = "Patil Farms";
    private String location = "Nashik, Maharashtra";

    // =========================================================
    // UI FIELDS
    // =========================================================

    private TextField nameField;
    private TextField phoneField;
    private TextField emailField;
    private TextField farmField;
    private TextField locationField;

    private Button updateBtn;
    private Button cancelBtn;

    // =========================================================
    // GET VIEW
    // =========================================================

    public Node getView() {

        VBox root = new VBox(18);
        root.setPadding(new Insets(10));
        root.setFillWidth(true);

        // =====================================================
        // PAGE TITLE
        // =====================================================

        VBox titles = new VBox(3);

        Label title = new Label("Profile");

        title.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: 800;" +
                        "-fx-text-fill: " + TEXT + ";");

        Label sub = new Label(
                "Manage your personal and farm details.");

        sub.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: " + SUB_TEXT + ";");

        titles.getChildren().addAll(title, sub);

        // =====================================================
        // MAIN PROFILE CARD
        // =====================================================

        HBox mainCard = new HBox(35);

        mainCard.setPadding(new Insets(28));

        mainCard.setMaxWidth(Double.MAX_VALUE);

        mainCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 14px;");

        // =====================================================
        // AVATAR SECTION
        // =====================================================

        VBox avatarCol = new VBox(14);

        avatarCol.setAlignment(Pos.CENTER);

        avatarCol.setPrefWidth(150);

        Circle pic = new Circle(
                55,
                Color.web(LIGHT_GREEN));

        Label picIcon = new Label("👨‍🌾");

        picIcon.setStyle(
                "-fx-font-size: 42px;");

        StackPane picStack = new StackPane(
                pic,
                picIcon);

        Button changePhotoBtn = new Button("Change Photo");

        changePhotoBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + GREEN + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 7px;" +
                        "-fx-background-radius: 7px;" +
                        "-fx-padding: 8 14;" +
                        "-fx-cursor: hand;");

        changePhotoBtn.setOnAction(e -> choosePhoto());

        Label farmerLabel = new Label("Farmer");

        farmerLabel.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: " + SUB_TEXT + ";");

        avatarCol.getChildren().addAll(
                picStack,
                changePhotoBtn,
                farmerLabel);

        // =====================================================
        // INFORMATION SECTION
        // =====================================================

        VBox information = new VBox(18);

        HBox.setHgrow(
                information,
                Priority.ALWAYS);

        // =====================================================
        // GRID
        // =====================================================

        GridPane grid = new GridPane();

        grid.setHgap(30);
        grid.setVgap(16);

        // NAME
        nameField = createTextField(fullName);

        // PHONE
        phoneField = createTextField(phone);

        // EMAIL
        emailField = createTextField(email);

        // FARM
        farmField = createTextField(farmName);

        // LOCATION
        locationField = createTextField(location);

        grid.add(
                createEditableField(
                        "Full Name",
                        nameField),
                0,
                0);

        grid.add(
                createEditableField(
                        "Phone Number",
                        phoneField),
                1,
                0);

        grid.add(
                createEditableField(
                        "Email",
                        emailField),
                0,
                1);

        grid.add(
                createEditableField(
                        "Farm Name",
                        farmField),
                1,
                1);

        grid.add(
                createEditableField(
                        "Location",
                        locationField),
                0,
                2);

        // MEMBER SINCE
        grid.add(
                createField(
                        "Member Since",
                        "Jan 10, 2024"),
                1,
                2);

        // =====================================================
        // BUTTONS
        // =====================================================

        updateBtn = new Button("Update Profile");

        updateBtn.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 9 22;" +
                        "-fx-cursor: hand;");

        cancelBtn = new Button("Cancel");

        cancelBtn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: " + GREEN + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 9 22;" +
                        "-fx-cursor: hand;");

        cancelBtn.setVisible(false);
        cancelBtn.setManaged(false);

        HBox buttons = new HBox(10);

        buttons.setAlignment(
                Pos.CENTER_LEFT);

        buttons.getChildren().addAll(
                updateBtn,
                cancelBtn);

        // =====================================================
        // UPDATE BUTTON ACTION
        // =====================================================

        updateBtn.setOnAction(e -> {

            if (updateBtn.getText().equals("Update Profile")) {

                enableEditing();

            } else {

                saveProfile();

            }
        });

        // =====================================================
        // CANCEL BUTTON ACTION
        // =====================================================

        cancelBtn.setOnAction(e -> cancelEditing());

        information.getChildren().addAll(
                grid,
                buttons);

        mainCard.getChildren().addAll(
                avatarCol,
                information);

        // =====================================================
        // ADD TO ROOT
        // =====================================================

        root.getChildren().addAll(
                titles,
                mainCard);

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scroll = new ScrollPane(root);

        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;");

        return scroll;
    }

    // =========================================================
    // CREATE DISPLAY FIELD
    // =========================================================

    private VBox createField(
            String label,
            String value) {

        VBox box = new VBox(5);

        Label l = new Label(label);

        l.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: " + SUB_TEXT + ";");

        Label v = new Label(value);

        v.setMinWidth(190);

        v.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-background-color: " + FIELD_BG + ";" +
                        "-fx-padding: 9 14;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 8px;");

        box.getChildren().addAll(
                l,
                v);

        return box;
    }

    // =========================================================
    // CREATE EDITABLE FIELD
    // =========================================================

    private VBox createEditableField(
            String label,
            TextField field) {

        VBox box = new VBox(5);

        Label l = new Label(label);

        l.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: " + SUB_TEXT + ";");

        box.getChildren().addAll(
                l,
                field);

        return box;
    }

    // =========================================================
    // TEXT FIELD
    // =========================================================

    private TextField createTextField(
            String value) {

        TextField field = new TextField(value);

        field.setPrefWidth(210);

        field.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-background-color: " + FIELD_BG + ";" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 9 12;");

        field.setEditable(false);

        return field;
    }

    // =========================================================
    // ENABLE EDITING
    // =========================================================

    private void enableEditing() {

        nameField.setEditable(true);
        phoneField.setEditable(true);
        emailField.setEditable(true);
        farmField.setEditable(true);
        locationField.setEditable(true);

        updateBtn.setText(
                "Save Changes");

        cancelBtn.setVisible(true);
        cancelBtn.setManaged(true);
    }

    // =========================================================
    // SAVE PROFILE
    // =========================================================

    private void saveProfile() {

        fullName = nameField.getText().trim();

        phone = phoneField.getText().trim();

        email = emailField.getText().trim();

        farmName = farmField.getText().trim();

        location = locationField.getText().trim();

        nameField.setEditable(false);
        phoneField.setEditable(false);
        emailField.setEditable(false);
        farmField.setEditable(false);
        locationField.setEditable(false);

        updateBtn.setText(
                "Update Profile");

        cancelBtn.setVisible(false);
        cancelBtn.setManaged(false);

        System.out.println(
                "Profile updated successfully!");

        System.out.println(
                "Name: " + fullName);

        System.out.println(
                "Phone: " + phone);

        System.out.println(
                "Email: " + email);

        System.out.println(
                "Farm: " + farmName);

        System.out.println(
                "Location: " + location);
    }

    // =========================================================
    // CANCEL EDITING
    // =========================================================

    private void cancelEditing() {

        nameField.setText(fullName);
        phoneField.setText(phone);
        emailField.setText(email);
        farmField.setText(farmName);
        locationField.setText(location);

        nameField.setEditable(false);
        phoneField.setEditable(false);
        emailField.setEditable(false);
        farmField.setEditable(false);
        locationField.setEditable(false);

        updateBtn.setText(
                "Update Profile");

        cancelBtn.setVisible(false);
        cancelBtn.setManaged(false);
    }

    // =========================================================
    // CHANGE PHOTO
    // =========================================================

    private void choosePhoto() {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(
                "Choose Profile Photo");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.png",
                        "*.jpg",
                        "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {

            System.out.println(
                    "Selected profile photo: "
                            + file.getAbsolutePath());

            // Later you can load this image into
            // the profile avatar.
        }
    }
}