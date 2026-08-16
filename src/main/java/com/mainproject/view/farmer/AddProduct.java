package com.mainproject.view.farmer;

import java.io.File;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mainproject.config.CloudinaryConfig;
import com.mainproject.dao.ProductDAO;
import com.mainproject.model.Product;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class AddProduct {

    // =====================================================
    // VARIABLES
    // =====================================================

    private final FarmerDashboard navigator;

    private final String farmerEmail;

    private final ProductDAO productDAO;

    // Cloudinary directly from Config
    private final Cloudinary cloudinary = CloudinaryConfig.getCloudinary();

    private File selectedImageFile;

    private ImageView imagePreview;

    private Label imageStatus;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AddProduct(
            FarmerDashboard navigator,
            String farmerEmail) {

        this.navigator = navigator;
        this.farmerEmail = farmerEmail;

        this.productDAO = new ProductDAO();
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
                "Add Product");

        title.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: 800;" +
                        "-fx-text-fill: #1B2631;");

        Label subtitle = new Label(
                "List your farm produce on AgriLink.");

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
        // PRODUCT NAME
        // =================================================

        TextField nameField = new TextField();

        nameField.setPromptText(
                "Enter product name");

        styleInput(
                nameField);

        grid.add(
                createInputBox(
                        "Product Name",
                        nameField),
                0,
                0);

        // =================================================
        // UNIT
        // =================================================

        ComboBox<String> unitBox = new ComboBox<>();

        unitBox.getItems().addAll(
                "kg",
                "quintal",
                "ton",
                "piece",
                "dozen");

        unitBox.setValue(
                "kg");

        styleComboBox(
                unitBox);

        grid.add(
                createInputBox(
                        "Unit",
                        unitBox),
                1,
                0);

        // =================================================
        // CATEGORY
        // =================================================

        ComboBox<String> categoryBox = new ComboBox<>();

        categoryBox.getItems().addAll(
                "Vegetables",
                "Fruits",
                "Grains",
                "Pulses");

        categoryBox.setValue(
                "Vegetables");

        styleComboBox(
                categoryBox);

        grid.add(
                createInputBox(
                        "Category",
                        categoryBox),
                0,
                1);

        // =================================================
        // VARIETY
        // =================================================

        TextField varietyField = new TextField();

        varietyField.setPromptText(
                "Optional");

        styleInput(
                varietyField);

        grid.add(
                createInputBox(
                        "Variety",
                        varietyField),
                1,
                1);

        // =================================================
        // PRICE
        // =================================================

        TextField priceField = new TextField();

        priceField.setPromptText(
                "Enter price");

        styleInput(
                priceField);

        grid.add(
                createInputBox(
                        "Price",
                        priceField),
                0,
                2);

        // =================================================
        // STOCK
        // =================================================

        TextField stockField = new TextField();

        stockField.setPromptText(
                "Enter available stock");

        styleInput(
                stockField);

        grid.add(
                createInputBox(
                        "Available Stock",
                        stockField),
                1,
                2);

        // =================================================
        // HARVEST DATE
        // =================================================

        DatePicker harvestDate = new DatePicker();

        harvestDate.setMaxWidth(
                Double.MAX_VALUE);

        grid.add(
                createInputBox(
                        "Harvest Date",
                        harvestDate),
                0,
                3);

        // =================================================
        // DESCRIPTION
        // =================================================

        TextArea descriptionField = new TextArea();

        descriptionField.setPromptText(
                "Describe your product...");

        descriptionField.setPrefRowCount(
                4);

        descriptionField.setWrapText(
                true);

        descriptionField.setStyle(
                "-fx-background-radius: 8px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 8px;");

        // =================================================
        // IMAGE SECTION
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

        cancelBtn.setOnAction(
                e -> navigator.navigateTo(
                        "Products"));

        Button saveBtn = new Button(
                "Add Product");

        saveBtn.setStyle(
                "-fx-background-color: #117864;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 9 22;" +
                        "-fx-cursor: hand;");

        saveBtn.setOnAction(
                e -> {

                    saveBtn.setDisable(
                            true);

                    saveProduct(
                            nameField,
                            unitBox,
                            categoryBox,
                            varietyField,
                            priceField,
                            stockField,
                            harvestDate,
                            descriptionField,
                            saveBtn);
                });

        actions.getChildren().addAll(
                cancelBtn,
                saveBtn);

        form.getChildren().addAll(
                grid,

                createInputBox(
                        "Description",
                        descriptionField),

                imageSection,

                actions);

        root.getChildren().addAll(
                titles,
                form);

        // =================================================
        // SCROLL PANE
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
                "Product Image");

        title.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #566573;");

        HBox imageArea = new HBox(15);

        imageArea.setAlignment(
                Pos.CENTER_LEFT);

        // =================================================
        // IMAGE PREVIEW
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
                "🌱\nSelect Image");

        placeholder.setAlignment(
                Pos.CENTER);

        placeholder.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #566573;");

        previewBox.getChildren().add(
                placeholder);

        // =================================================
        // CHOOSE IMAGE BUTTON
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
                e -> chooseImage(
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
                "Select Product Image");

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
                file.toURI()
                        .toString());

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
    // SAVE PRODUCT
    // =====================================================

    private void saveProduct(
            TextField nameField,
            ComboBox<String> unitBox,
            ComboBox<String> categoryBox,
            TextField varietyField,
            TextField priceField,
            TextField stockField,
            DatePicker harvestDate,
            TextArea descriptionField,
            Button saveBtn) {

        try {

            // =================================================
            // GET VALUES
            // =================================================

            String name = nameField.getText()
                    .trim();

            String variety = varietyField.getText()
                    .trim();

            String priceText = priceField.getText()
                    .trim();

            String stockText = stockField.getText()
                    .trim();

            String description = descriptionField.getText()
                    .trim();

            // =================================================
            // VALIDATION
            // =================================================

            if (name.isEmpty()) {

                showError(
                        "Product name is required.");

                saveBtn.setDisable(
                        false);

                return;
            }

            if (priceText.isEmpty()) {

                showError(
                        "Price is required.");

                saveBtn.setDisable(
                        false);

                return;
            }

            if (stockText.isEmpty()) {

                showError(
                        "Stock is required.");

                saveBtn.setDisable(
                        false);

                return;
            }

            if (selectedImageFile == null) {

                showError(
                        "Please select a product image.");

                saveBtn.setDisable(
                        false);

                return;
            }

            double price = Double.parseDouble(
                    priceText);

            double stock = Double.parseDouble(
                    stockText);

            if (price < 0) {

                showError(
                        "Price cannot be negative.");

                saveBtn.setDisable(
                        false);

                return;
            }

            if (stock < 0) {

                showError(
                        "Stock cannot be negative.");

                saveBtn.setDisable(
                        false);

                return;
            }

            // =================================================
            // UPLOAD IMAGE TO CLOUDINARY
            // =================================================

            imageStatus.setText(
                    "Uploading image...");

            System.out.println(
                    "Uploading image to Cloudinary...");

            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(
                            selectedImageFile,
                            ObjectUtils.asMap(
                                    "folder",
                                    "agrilink/products"));

            // =================================================
            // GET CLOUDINARY URL
            // =================================================

            String imageUrl = (String) uploadResult.get(
                    "secure_url");

            if (imageUrl == null
                    ||
                    imageUrl.isEmpty()) {

                showError(
                        "Cloudinary did not return an image URL.");

                saveBtn.setDisable(
                        false);

                return;
            }

            System.out.println(
                    "Cloudinary upload successful.");

            System.out.println(
                    "Image URL: "
                            + imageUrl);

            imageStatus.setText(
                    "Image uploaded successfully");

            // =================================================
            // CREATE PRODUCT
            // =================================================

            Product product = new Product();

            product.setFarmerEmail(
                    farmerEmail);

            product.setName(
                    name);

            product.setUnit(
                    unitBox.getValue());

            product.setCategory(
                    categoryBox.getValue());

            product.setVariety(
                    variety);

            product.setDescription(
                    description);

            product.setHarvestDate(
                    harvestDate.getValue() == null
                            ? ""
                            : harvestDate
                                    .getValue()
                                    .toString());

            product.setPrice(
                    price);

            product.setStock(
                    stock);

            // =================================================
            // STATUS
            // =================================================

            if (stock <= 0) {

                product.setStatus(
                        "Sold Out");

            } else {

                product.setStatus(
                        "Active");
            }

            // =================================================
            // IMPORTANT
            // CLOUDINARY URL → PRODUCT
            // =================================================

            product.setImageUrl(
                    imageUrl);

            // =================================================
            // SAVE PRODUCT TO FIRESTORE
            // =================================================

            System.out.println(
                    "Saving product to Firestore...");

            boolean saved = productDAO.addProduct(
                    product);

            if (saved) {

                System.out.println(
                        "Product saved successfully!");

                showSuccess();

                navigator.navigateTo(
                        "Products");

            } else {

                showError(
                        "Product could not be saved to Firestore.");

                saveBtn.setDisable(
                        false);
            }

        } catch (NumberFormatException e) {

            showError(
                    "Price and stock must be valid numbers.");

            saveBtn.setDisable(
                    false);

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Something went wrong:\n"
                            + e.getMessage());

            saveBtn.setDisable(
                    false);
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
                "Add Product");

        alert.setHeaderText(
                "Unable to add product");

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
                "Product Added");

        alert.setHeaderText(
                "Product added successfully!");

        alert.setContentText(
                "The product image was uploaded to Cloudinary and its URL was saved in Firestore.");

        alert.showAndWait();
    }
}