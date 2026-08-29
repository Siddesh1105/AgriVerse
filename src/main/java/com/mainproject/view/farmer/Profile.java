package com.mainproject.view.farmer;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mainproject.config.CloudinaryConfig;
import com.mainproject.controller.UserController;
import com.mainproject.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Map;

public class Profile {

    // =====================================================
    // USER EMAIL
    // =====================================================

    private final String userEmail;

    // =====================================================
    // DAO
    // =====================================================

    private final UserController userController;

    // =====================================================
    // CLOUDINARY
    // =====================================================

    private final Cloudinary cloudinary;

    // =====================================================
    // USER
    // =====================================================

    private User currentUser;

    // =====================================================
    // FIELDS
    // =====================================================

    private TextField nameField;

    private TextField phoneField;

    private TextField emailField;

    private TextField genderField;

    private TextField roleField;

    // =====================================================
    // IMAGE
    // =====================================================

    private ImageView profileImageView;

    // =====================================================
    // BUTTONS
    // =====================================================

    private Button updateBtn;

    private Button cancelBtn;

    // =====================================================
    // ORIGINAL VALUES
    // =====================================================

    private String originalName;

    private String originalPhone;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Profile(String userEmail) {

        System.out.println(
                "===================================="
        );

        System.out.println(
                "PROFILE CONSTRUCTOR"
        );

        System.out.println(
                "Received email: ["
                        + userEmail
                        + "]"
        );

        this.userEmail =
                userEmail;

        this.userController =
                new UserController();

        this.cloudinary =
                CloudinaryConfig.getCloudinary();

        loadUser();

        System.out.println(
                "===================================="
        );
    }

    // =====================================================
    // LOAD USER
    // =====================================================

    private void loadUser() {

        try {

            if (userEmail == null
                    || userEmail.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Profile received empty email."
                );

                return;
            }

            System.out.println(
                    "Fetching user from Firestore..."
            );

            System.out.println(
                    "Email: ["
                            + userEmail
                            + "]"
            );

            currentUser =
                    userController.getUserByEmail(
                            userEmail.trim()
                    );

            if (currentUser == null) {

                System.out.println(
                        "Profile user not found: "
                                + userEmail
                );

                return;
            }

            originalName =
                    safe(
                            currentUser.getFullName()
                    );

            originalPhone =
                    safe(
                            currentUser.getMobileNumber()
                    );

            System.out.println(
                    "PROFILE USER LOADED"
            );

            System.out.println(
                    "Name: "
                            + currentUser.getFullName()
            );

            System.out.println(
                    "Email: "
                            + currentUser.getEmail()
            );

            System.out.println(
                    "Mobile: "
                            + currentUser.getMobileNumber()
            );

            System.out.println(
                    "Gender: "
                            + currentUser.getGender()
            );

            System.out.println(
                    "Role: "
                            + currentUser.getRole()
            );

            System.out.println(
                    "Profile Image: "
                            + currentUser.getProfileImageUrl()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error loading profile:"
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root =
                new VBox(18);

        root.setPadding(
                new Insets(10)
        );

        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label("Profile");

        title.setStyle(
                "-fx-font-size: 22px;"
                        + "-fx-font-weight: 800;"
                        + "-fx-text-fill: #1B2631;"
        );

        Label subtitle =
                new Label(
                        "Manage your personal details."
                );

        subtitle.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-text-fill: #566573;"
        );

        VBox titleBox =
                new VBox(
                        3,
                        title,
                        subtitle
                );

        // =================================================
        // CARD
        // =================================================

        HBox card =
                new HBox(35);

        card.setPadding(
                new Insets(28)
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 14px;"
                        + "-fx-border-color: #A2D9CE;"
                        + "-fx-border-radius: 14px;"
        );

        // =================================================
        // PHOTO SECTION
        // =================================================

        VBox photoSection =
                new VBox(14);

        photoSection.setAlignment(
                Pos.CENTER
        );

        photoSection.setPrefWidth(
                180
        );

        profileImageView =
                createProfileImage();

        StackPane imageContainer =
                new StackPane();

        imageContainer.setPrefSize(
                120,
                120
        );

        Circle background =
                new Circle(
                        55,
                        Color.web("#D4EFDF")
                );

        imageContainer.getChildren()
                .add(
                        background
                );

        imageContainer.getChildren()
                .add(
                        profileImageView
                );

        Button changePhoto =
                new Button(
                        "Change Photo"
                );

        changePhoto.setStyle(
                "-fx-background-color: white;"
                        + "-fx-text-fill: #117864;"
                        + "-fx-border-color: #A2D9CE;"
                        + "-fx-border-radius: 7px;"
                        + "-fx-background-radius: 7px;"
                        + "-fx-padding: 8 16;"
                        + "-fx-cursor: hand;"
        );

        changePhoto.setOnAction(
                e -> choosePhoto()
        );

        Label role =
                new Label(
                        safe(
                                currentUser == null
                                        ? ""
                                        : currentUser.getRole()
                        )
                );

        role.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-text-fill: #566573;"
        );

        photoSection.getChildren()
                .addAll(
                        imageContainer,
                        changePhoto,
                        role
                );

        // =================================================
        // INFORMATION
        // =================================================

        VBox information =
                new VBox(18);

        HBox.setHgrow(
                information,
                Priority.ALWAYS
        );

        GridPane grid =
                new GridPane();

        grid.setHgap(30);

        grid.setVgap(16);

        // =================================================
        // FIELDS
        // =================================================

        nameField =
                createTextField(
                        currentUser == null
                                ? null
                                : currentUser.getFullName(),
                        "Full Name"
                );

        phoneField =
                createTextField(
                        currentUser == null
                                ? null
                                : currentUser.getMobileNumber(),
                        "Phone Number"
                );

        emailField =
                createTextField(
                        currentUser == null
                                ? userEmail
                                : currentUser.getEmail(),
                        "Email"
                );

        genderField =
                createTextField(
                        currentUser == null
                                ? null
                                : currentUser.getGender(),
                        "Gender"
                );

        roleField =
                createTextField(
                        currentUser == null
                                ? null
                                : currentUser.getRole(),
                        "Role"
                );

        // =================================================
        // READ ONLY
        // =================================================

        nameField.setEditable(false);

        phoneField.setEditable(false);

        emailField.setEditable(false);

        genderField.setEditable(false);

        roleField.setEditable(false);

        // =================================================
        // GRID
        // =================================================

        grid.add(
                createFieldBox(
                        "Full Name",
                        nameField
                ),
                0,
                0
        );

        grid.add(
                createFieldBox(
                        "Phone Number",
                        phoneField
                ),
                1,
                0
        );

        grid.add(
                createFieldBox(
                        "Email",
                        emailField
                ),
                0,
                1
        );

        grid.add(
                createFieldBox(
                        "Gender",
                        genderField
                ),
                1,
                1
        );

        grid.add(
                createFieldBox(
                        "Role",
                        roleField
                ),
                0,
                2
        );

        // =================================================
        // BUTTONS
        // =================================================

        updateBtn =
                new Button(
                        "Update Profile"
                );

        updateBtn.setStyle(
                "-fx-background-color: #117864;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-font-size: 14px;"
                        + "-fx-background-radius: 8px;"
                        + "-fx-padding: 10 24;"
                        + "-fx-cursor: hand;"
        );

        cancelBtn =
                new Button(
                        "Cancel"
                );

        cancelBtn.setStyle(
                "-fx-background-color: white;"
                        + "-fx-text-fill: #117864;"
                        + "-fx-font-weight: bold;"
                        + "-fx-border-color: #A2D9CE;"
                        + "-fx-border-radius: 8px;"
                        + "-fx-background-radius: 8px;"
                        + "-fx-padding: 10 24;"
                        + "-fx-cursor: hand;"
        );

        cancelBtn.setVisible(false);

        cancelBtn.setManaged(false);

        HBox buttons =
                new HBox(
                        10,
                        updateBtn,
                        cancelBtn
                );

        updateBtn.setOnAction(
                e -> {

                    if (updateBtn
                            .getText()
                            .equals(
                                    "Update Profile"
                            )) {

                        enableEditing();

                    } else {

                        saveProfile();
                    }
                }
        );

        cancelBtn.setOnAction(
                e -> cancelEditing()
        );

        information.getChildren()
                .addAll(
                        grid,
                        buttons
                );

        card.getChildren()
                .addAll(
                        photoSection,
                        information
                );

        root.getChildren()
                .addAll(
                        titleBox,
                        card
                );

        // =================================================
        // SCROLL
        // =================================================

        ScrollPane scroll =
                new ScrollPane(
                        root
                );

        scroll.setFitToWidth(
                true
        );

        scroll.setFitToHeight(
                true
        );

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scroll.setStyle(
                "-fx-background-color: transparent;"
        );

        return scroll;
    }

    // =====================================================
    // CREATE TEXT FIELD
    // =====================================================

    private TextField createTextField(
            String value,
            String prompt) {

        TextField field =
                new TextField();

        if (value != null
                && !value.trim().isEmpty()) {

            field.setText(
                    value
            );

        } else {

            field.setPromptText(
                    prompt
            );
        }

        field.setPrefWidth(
                270
        );

        field.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #1B2631;"
                        + "-fx-prompt-text-fill: #7F8C8D;"
                        + "-fx-background-color: #F4FAF7;"
                        + "-fx-background-radius: 8px;"
                        + "-fx-border-color: #A2D9CE;"
                        + "-fx-border-radius: 8px;"
                        + "-fx-padding: 9 12;"
        );

        return field;
    }

    // =====================================================
    // FIELD BOX
    // =====================================================

    private VBox createFieldBox(
            String label,
            TextField field) {

        Label labelText =
                new Label(
                        label
                );

        labelText.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-text-fill: #566573;"
        );

        return new VBox(
                5,
                labelText,
                field
        );
    }

    // =====================================================
    // PROFILE IMAGE
    // =====================================================

    private ImageView createProfileImage() {

        ImageView imageView =
                new ImageView();

        imageView.setFitWidth(
                110
        );

        imageView.setFitHeight(
                110
        );

        imageView.setPreserveRatio(
                true
        );

        imageView.setSmooth(
                true
        );

        if (currentUser != null) {

            String imageUrl =
                    currentUser.getProfileImageUrl();

            if (imageUrl != null
                    && !imageUrl.trim().isEmpty()) {

                try {

                    Image image =
                            new Image(
                                    imageUrl,
                                    110,
                                    110,
                                    true,
                                    true,
                                    true
                            );

                    imageView.setImage(
                            image
                    );

                } catch (Exception e) {

                    System.out.println(
                            "Unable to load profile image."
                    );
                }
            }
        }

        Circle clip =
                new Circle(
                        55,
                        55,
                        55
                );

        imageView.setClip(
                clip
        );

        return imageView;
    }

    // =====================================================
    // ENABLE EDITING
    // =====================================================

    private void enableEditing() {

        nameField.setEditable(
                true
        );

        phoneField.setEditable(
                true
        );

        updateBtn.setText(
                "Save Changes"
        );

        cancelBtn.setVisible(
                true
        );

        cancelBtn.setManaged(
                true
        );
    }

    // =====================================================
    // SAVE PROFILE
    // =====================================================

    private void saveProfile() {

        String name =
                nameField
                        .getText()
                        .trim();

        String phone =
                phoneField
                        .getText()
                        .trim();

        if (name.isEmpty()) {

            showAlert(
                    "Validation",
                    "Please enter your full name."
            );

            return;
        }

        if (phone.isEmpty()) {

            showAlert(
                    "Validation",
                    "Please enter your phone number."
            );

            return;
        }

        currentUser.setFullName(
                name
        );

        currentUser.setMobileNumber(
                phone
        );

        boolean saved =
                userController.updateProfile(
                        currentUser
                );

        if (!saved) {

            showAlert(
                    "Error",
                    "Unable to update profile in Firestore."
            );

            return;
        }

        originalName =
                name;

        originalPhone =
                phone;

        nameField.setEditable(
                false
        );

        phoneField.setEditable(
                false
        );

        updateBtn.setText(
                "Update Profile"
        );

        cancelBtn.setVisible(
                false
        );

        cancelBtn.setManaged(
                false
        );

        showAlert(
                "Success",
                "Profile updated successfully."
        );
    }

    // =====================================================
    // CANCEL
    // =====================================================

    private void cancelEditing() {

        nameField.setText(
                originalName
        );

        phoneField.setText(
                originalPhone
        );

        nameField.setEditable(
                false
        );

        phoneField.setEditable(
                false
        );

        updateBtn.setText(
                "Update Profile"
        );

        cancelBtn.setVisible(
                false
        );

        cancelBtn.setManaged(
                false
        );
    }

    // =====================================================
    // CHOOSE PHOTO
    // =====================================================

    private void choosePhoto() {

        FileChooser chooser =
                new FileChooser();

        chooser.setTitle(
                "Choose Profile Photo"
        );

        chooser.getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(
                                "Image Files",
                                "*.png",
                                "*.jpg",
                                "*.jpeg",
                                "*.webp"
                        )
                );

        File file =
                chooser.showOpenDialog(
                        null
                );

        if (file == null) {

            return;
        }

        try {

            System.out.println(
                    "Uploading profile image..."
            );

            Map<?, ?> result =
                    cloudinary
                            .uploader()
                            .upload(
                                    file,
                                    ObjectUtils.asMap(
                                            "folder",
                                            "agrilink/profile"
                                    )
                            );

            String imageUrl =
                    (String)
                            result.get(
                                    "secure_url"
                            );

            if (imageUrl == null
                    || imageUrl.trim().isEmpty()) {

                showAlert(
                        "Error",
                        "Cloudinary did not return an image URL."
                );

                return;
            }

            System.out.println(
                    "Cloudinary upload successful."
            );

            System.out.println(
                    "Image URL: "
                            + imageUrl
            );

            boolean saved =
                    userController.updateProfileImage(
                            currentUser.getEmail(),
                            imageUrl
                    );

            if (!saved) {

                showAlert(
                        "Error",
                        "Image uploaded but could not be saved to Firestore."
                );

                return;
            }

            currentUser.setProfileImageUrl(
                    imageUrl
            );

            Image image =
                    new Image(
                            imageUrl,
                            110,
                            110,
                            true,
                            true,
                            true
                    );

            profileImageView.setImage(
                    image
            );

            showAlert(
                    "Success",
                    "Profile photo updated successfully."
            );

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    "Error",
                    "Unable to upload profile photo."
            );
        }
    }

    // =====================================================
    // SAFE
    // =====================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }

    // =====================================================
    // ALERT
    // =====================================================

    private void showAlert(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
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
 
    
}
