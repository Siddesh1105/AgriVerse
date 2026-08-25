package com.mainproject.view.farmer;

import com.mainproject.util.LanguageManager;

import java.io.File;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mainproject.config.CloudinaryConfig;
import com.mainproject.controller.EquipmentController;
import com.mainproject.model.Equipment;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * Add Equipment
 *
 * Uses the same Cloudinary implementation as AddProduct:
 * CloudinaryConfig.getCloudinary()
 * -> upload(..., ObjectUtils.asMap("folder", "agrilink/equipment"))
 * -> secure_url
 * -> EquipmentDAO
 *
 * This class provides two constructors:
 *
 * 1. AddEquipment(FarmerDashboard navigator, String farmerEmail)
 * Recommended when opened from FarmerDashboard.
 *
 * 2. AddEquipment(String farmerEmail, String farmerName)
 * Useful when the page is opened directly.
 */
public class AddEquipment {

    // =====================================================
    // VARIABLES
    // =====================================================

    private FarmerDashboard navigator;

    private final String farmerEmail;
    private final String farmerName;

    private final EquipmentController equipmentController;

    // SAME CLOUDINARY CONFIG AS AddProduct
    private final Cloudinary cloudinary = CloudinaryConfig.getCloudinary();

    private File selectedImageFile;

    private ImageView imagePreview;

    private Label imageStatus;
    private final Runnable backToEquipmentRental;

    // =====================================================
    // CONSTRUCTOR - RECOMMENDED FROM FARMER DASHBOARD
    // =====================================================
    // =====================================================
    // CONSTRUCTOR - DIRECT USE
    // =====================================================

    public AddEquipment(
        String farmerEmail,
        String farmerName,
        Runnable backToEquipmentRental) {

    this.farmerEmail = farmerEmail;
    this.farmerName = farmerName;
    this.backToEquipmentRental = backToEquipmentRental;

    this.equipmentController = new EquipmentController();
}

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root = new VBox(18);

        root.setPadding(
                new Insets(10));

        // =================================================
        // HEADER
        // =================================================

        VBox titles = new VBox(3);

        Label title = new Label(
                "Add Equipment");

        title.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: 800;" +
                        "-fx-text-fill: #1B2631;");

        Label subtitle = new Label(
                "List your agricultural equipment for other farmers to rent.");

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #566573;");

        titles.getChildren().addAll(
                title,
                subtitle);

        // =================================================
        // FORM
        // =================================================

        VBox form = new VBox(16);

        form.setPadding(
                new Insets(22));

        form.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 14px;");

        // =================================================
        // GRID
        // =================================================

        GridPane grid = new GridPane();

        grid.setHgap(18);
        grid.setVgap(15);

        // =================================================
        // EQUIPMENT NAME
        // =================================================

        TextField nameField = new TextField();

        nameField.setPromptText(
                "e.g. Tractor");

        styleInput(nameField);

        grid.add(
                createInputBox(
                        "Equipment Name",
                        nameField),
                0,
                0);

        // =================================================
        // CATEGORY
        // =================================================

        ComboBox<String> categoryBox = new ComboBox<>();

        categoryBox.getItems().addAll(
                "Tractor",
                "Tillage",
                "Harvesting",
                "Sprayer",
                "Seeding",
                "Cultivator",
                "Other");

        categoryBox.setValue(
                "Tractor");

        styleComboBox(
                categoryBox);

        grid.add(
                createInputBox(
                        "Category",
                        categoryBox),
                1,
                0);

        // =================================================
        // PRICE
        // =================================================

        TextField priceField = new TextField();

        priceField.setPromptText(
                "e.g. 1500");

        styleInput(
                priceField);

        grid.add(
                createInputBox(
                        "Rental Price / Day",
                        priceField),
                0,
                1);

        // =================================================
        // LOCATION
        // =================================================

        TextField locationField = new TextField();

        locationField.setPromptText(
                "e.g. Nashik");

        styleInput(
                locationField);

        grid.add(
                createInputBox(
                        "Location",
                        locationField),
                1,
                1);

        // =================================================
        // DESCRIPTION
        // =================================================

        TextArea descriptionField = new TextArea();

        descriptionField.setPromptText(
                "Describe the equipment, condition, usage, etc.");

        descriptionField.setPrefRowCount(
                4);

        descriptionField.setWrapText(
                true);

        descriptionField.setStyle(
                "-fx-background-radius: 8px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 8px;");

        // =================================================
        // AVAILABILITY
        // =================================================

        CheckBox availableBox = new CheckBox(
                "Equipment is available for rental");

        availableBox.setSelected(
                true);

        availableBox.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #1B2631;");

        // =================================================
        // IMAGE
        // =================================================

        VBox imageSection = createImageSection();

        // =================================================
        // ACTION BUTTONS
        // =================================================

        HBox actions = new HBox(10);

        actions.setAlignment(
                Pos.CENTER_RIGHT);

        Button cancelBtn = new Button(
                "Cancel");

        cancelBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #1B2631;" +
                        "-fx-padding: 8 18;" +
                        "-fx-cursor: hand;");

        cancelBtn.setOnAction( event -> {

            if (backToEquipmentRental != null) {

                backToEquipmentRental.run();
            }
        }
       );

        Button saveBtn = new Button(
                "Add Equipment");

        saveBtn.setStyle(
                "-fx-background-color: #117864;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 9 22;" +
                        "-fx-cursor: hand;");

        saveBtn.setOnAction(
                event -> {

                    saveBtn.setDisable(
                            true);

                    saveEquipment(
                            nameField,
                            categoryBox,
                            priceField,
                            locationField,
                            descriptionField,
                            availableBox,
                            saveBtn);
                });

        actions.getChildren().addAll(
                cancelBtn,
                saveBtn);

        // =================================================
        // FORM CONTENT
        // =================================================

        form.getChildren().addAll(
                grid,

                createInputBox(
                        "Description",
                        descriptionField),

                availableBox,

                imageSection,

                actions);

        root.getChildren().addAll(
                titles,
                form);

        // =================================================
        // SCROLL
        // =================================================

        ScrollPane scroll = new ScrollPane(
                root);

        scroll.setFitToWidth(
                true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;");

        return scroll;
    }

    // =====================================================
    // IMAGE SECTION
    // =====================================================

    private VBox createImageSection() {

        VBox section = new VBox(10);

        Label title = new Label(
                "Equipment Image");

        title.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #566573;");

        HBox imageArea = new HBox(15);

        imageArea.setAlignment(
                Pos.CENTER_LEFT);

        // =================================================
        // PREVIEW
        // =================================================

        StackPane previewBox = new StackPane();

        previewBox.setPrefSize(
                180,
                130);

        previewBox.setStyle(
                "-fx-background-color: #E9F7EF;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;");

        imagePreview = new ImageView();

        imagePreview.setFitWidth(
                165);

        imagePreview.setFitHeight(
                115);

        imagePreview.setPreserveRatio(
                true);

        Label placeholder = new Label(
                "🚜\nSelect Image");

        placeholder.setAlignment(
                Pos.CENTER);

        placeholder.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #566573;");

        previewBox.getChildren().add(
                placeholder);

        // =================================================
        // CHOOSE IMAGE
        // =================================================

        Button chooseButton = new Button(
                "📷 Choose Image");

        chooseButton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 8 16;" +
                        "-fx-cursor: hand;");

        chooseButton.setOnAction(
                event -> chooseImage(
                        previewBox));

        imageStatus = new Label(
                "No image selected");

        imageStatus.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #566573;");

        VBox controls = new VBox(10);

        controls.setPrefWidth(
                280);

        controls.getChildren().addAll(
                chooseButton,
                imageStatus);

        imageArea.getChildren().addAll(
                previewBox,
                controls);

        section.getChildren().addAll(
                title,
                imageArea);

        return section;
    }

    // =====================================================
    // SELECT IMAGE
    // =====================================================

    private void chooseImage(
            StackPane previewBox) {

        FileChooser chooser = new FileChooser();

        chooser.setTitle(
                "Select Equipment Image");

        chooser.getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(
                                "Image Files",
                                "*.png",
                                "*.jpg",
                                "*.jpeg",
                                "*.webp"));

        File file = chooser.showOpenDialog(
                previewBox
                        .getScene()
                        .getWindow());

        if (file == null) {
            return;
        }

        selectedImageFile = file;

        Image image = new Image(
                file.toURI().toString());

        imagePreview.setImage(
                image);

        previewBox.getChildren()
                .setAll(
                        imagePreview);

        imageStatus.setText(
                "Selected: "
                        + file.getName());

        imageStatus.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #117864;");
    }

    // =====================================================
    // SAVE EQUIPMENT
    // =====================================================

    private void saveEquipment(
            TextField nameField,
            ComboBox<String> categoryBox,
            TextField priceField,
            TextField locationField,
            TextArea descriptionField,
            CheckBox availableBox,
            Button saveBtn) {

        try {

            // =================================================
            // GET VALUES
            // =================================================

            String name = nameField.getText()
                    .trim();

            String category = categoryBox.getValue();

            String priceText = priceField.getText()
                    .trim();

            String location = locationField.getText()
                    .trim();

            String description = descriptionField.getText()
                    .trim();

            // =================================================
            // VALIDATION
            // =================================================

            if (name.isEmpty()) {

                showError(
                        "Equipment name is required.");

                saveBtn.setDisable(false);

                return;
            }

            if (priceText.isEmpty()) {

                showError(
                        "Rental price is required.");

                saveBtn.setDisable(false);

                return;
            }

            if (location.isEmpty()) {

                showError(
                        "Location is required.");

                saveBtn.setDisable(false);

                return;
            }

            if (selectedImageFile == null) {

                showError(
                        "Please select an equipment image.");

                saveBtn.setDisable(false);

                return;
            }

            double price = Double.parseDouble(
                    priceText);

            if (price < 0) {

                showError(
                        "Rental price cannot be negative.");

                saveBtn.setDisable(false);

                return;
            }

            // =================================================
            // CLOUDINARY UPLOAD
            // SAME METHOD AS ADD PRODUCT
            // =================================================

            imageStatus.setText(
                    "Uploading image...");

            System.out.println(
                    "Uploading equipment image to Cloudinary...");

            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(
                            selectedImageFile,
                            ObjectUtils.asMap(
                                    "folder",
                                    "agrilink/equipment"));

            // =================================================
            // GET SECURE URL
            // =================================================

            String imageUrl = (String) uploadResult.get(
                    "secure_url");

            if (imageUrl == null
                    || imageUrl.isEmpty()) {

                showError(
                        "Cloudinary did not return an image URL.");

                saveBtn.setDisable(false);

                return;
            }

            System.out.println(
                    "Cloudinary upload successful.");

            System.out.println(
                    "Equipment Image URL: "
                            + imageUrl);

            imageStatus.setText(
                    "Image uploaded successfully");

            // =================================================
            // CREATE EQUIPMENT
            // =================================================

            Equipment equipment = new Equipment();

            equipment.setName(
                    name);

            equipment.setCategory(
                    category);

            equipment.setPrice(
                    price);

            equipment.setLocation(
                    location);

            equipment.setDescription(
                    description);

            equipment.setAvailable(
                    availableBox.isSelected());

            equipment.setImageUrl(
                    imageUrl);

            equipment.setOwnerEmail(
                    farmerEmail);

            equipment.setOwnerName(
                    farmerName);

            // =================================================
            // SAVE TO FIRESTORE
            // =================================================

            System.out.println(
                    "Saving equipment to Firestore...");

            boolean saved = equipmentController.addEquipment(
                    equipment);

            if (saved) {

                System.out.println(
                        "Equipment saved successfully!");

                showSuccess();

                // Return to Equipment Rental
               if (backToEquipmentRental != null) {

                        backToEquipmentRental.run(); 
                }

            } else {

                showError(
                        "Equipment could not be saved to Firestore.");

                saveBtn.setDisable(false);
            }

        } catch (NumberFormatException e) {

            showError(
                    "Rental price must be a valid number.");

            saveBtn.setDisable(false);

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Something went wrong:\n"
                            + e.getMessage());

            saveBtn.setDisable(false);
        }
    }

    // =====================================================
    // INPUT STYLE
    // =====================================================

    private void styleInput(
            TextField field) {

        field.setMaxWidth(
                Double.MAX_VALUE);

        field.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 8px;");
    }

    // =====================================================
    // COMBOBOX STYLE
    // =====================================================

    private void styleComboBox(
            ComboBox<String> combo) {

        combo.setMaxWidth(
                Double.MAX_VALUE);

        combo.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 8px;");
    }

    // =====================================================
    // INPUT BOX
    // =====================================================

    private VBox createInputBox(
            String label,
            Node input) {

        VBox box = new VBox(5);

        Label labelNode = new Label(
                label);

        labelNode.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #566573;");

        box.getChildren().addAll(
                labelNode,
                input);

        return box;
    }

    // =====================================================
    // ERROR ALERT
    // =====================================================

    private void showError(
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR);

        alert.setTitle(
                "Add Equipment");

        alert.setHeaderText(
                "Unable to add equipment");

        alert.setContentText(
                message);

        alert.showAndWait();
    }

    // =====================================================
    // SUCCESS ALERT
    // =====================================================

    private void showSuccess() {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(
                "Equipment Added");

        alert.setHeaderText(
                "Equipment added successfully!");

        alert.setContentText(
                "The equipment image was uploaded to Cloudinary and the equipment details were saved in Firestore.");

        alert.showAndWait();
    }
}