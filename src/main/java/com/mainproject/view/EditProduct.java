package com.mainproject.view;

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

public class EditProduct {

    // =====================================================
    // VARIABLES
    // =====================================================

    private final FarmerDashboard navigator;

    private final Product product;

    private final ProductDAO productDAO;

    private final Cloudinary cloudinary =
            CloudinaryConfig.getCloudinary();

    private File selectedImageFile;

    private ImageView imagePreview;

    private Label imageStatus;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EditProduct(
            FarmerDashboard navigator,
            Product product) {

        this.navigator =
                navigator;

        this.product =
                product;

        this.productDAO =
                new ProductDAO();
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
        // HEADER
        // =================================================

        VBox titles =
                new VBox(3);

        Label title =
                new Label(
                        "Edit Product"
                );

        title.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: 800;" +
                "-fx-text-fill: #1B2631;"
        );

        Label subtitle =
                new Label(
                        "Update your product information, price, stock or image."
                );

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #566573;"
        );

        titles.getChildren().addAll(
                title,
                subtitle
        );

        // =================================================
        // FORM
        // =================================================

        VBox form =
                new VBox(16);

        form.setPadding(
                new Insets(22)
        );

        form.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: #A2D9CE;" +
                "-fx-border-radius: 14px;"
        );

        // =================================================
        // GRID
        // =================================================

        GridPane grid =
                new GridPane();

        grid.setHgap(18);
        grid.setVgap(15);

        // =================================================
        // PRODUCT NAME
        // =================================================

        TextField nameField =
                new TextField(
                        safe(
                                product.getName()
                        )
                );

        styleInput(
                nameField
        );

        grid.add(
                createInputBox(
                        "Product Name",
                        nameField
                ),
                0,
                0
        );

        // =================================================
        // UNIT
        // =================================================

        ComboBox<String> unitBox =
                new ComboBox<>();

        unitBox.getItems().addAll(
                "kg",
                "quintal",
                "ton",
                "piece",
                "dozen"
        );

        String existingUnit =
                safe(
                        product.getUnit()
                );

        if (
                !existingUnit.isEmpty()
                        &&
                !unitBox.getItems()
                        .contains(
                                existingUnit
                        )
        ) {

            unitBox.getItems()
                    .add(
                            existingUnit
                    );
        }

        unitBox.setValue(
                existingUnit.isEmpty()
                        ? "kg"
                        : existingUnit
        );

        styleComboBox(
                unitBox
        );

        grid.add(
                createInputBox(
                        "Unit",
                        unitBox
                ),
                1,
                0
        );

        // =================================================
        // CATEGORY
        // =================================================

        ComboBox<String> categoryBox =
                new ComboBox<>();

        categoryBox.getItems().addAll(
                "Vegetables",
                "Fruits",
                "Grains",
                "Pulses"
        );

        String existingCategory =
                safe(
                        product.getCategory()
                );

        if (
                !existingCategory.isEmpty()
                        &&
                !categoryBox.getItems()
                        .contains(
                                existingCategory
                        )
        ) {

            categoryBox.getItems()
                    .add(
                            existingCategory
                    );
        }

        categoryBox.setValue(
                existingCategory.isEmpty()
                        ? "Vegetables"
                        : existingCategory
        );

        styleComboBox(
                categoryBox
        );

        grid.add(
                createInputBox(
                        "Category",
                        categoryBox
                ),
                0,
                1
        );

        // =================================================
        // VARIETY
        // =================================================

        TextField varietyField =
                new TextField(
                        safe(
                                product.getVariety()
                        )
                );

        varietyField.setPromptText(
                "Optional"
        );

        styleInput(
                varietyField
        );

        grid.add(
                createInputBox(
                        "Variety",
                        varietyField
                ),
                1,
                1
        );

        // =================================================
        // PRICE
        // =================================================

        TextField priceField =
                new TextField(
                        formatNumber(
                                product.getPrice()
                        )
                );

        styleInput(
                priceField
        );

        grid.add(
                createInputBox(
                        "Price",
                        priceField
                ),
                0,
                2
        );

        // =================================================
        // STOCK
        // =================================================

        TextField stockField =
                new TextField(
                        formatNumber(
                                product.getStock()
                        )
                );

        styleInput(
                stockField
        );

        grid.add(
                createInputBox(
                        "Available Stock",
                        stockField
                ),
                1,
                2
        );

        // =================================================
        // HARVEST DATE
        // =================================================

        DatePicker harvestDate =
                new DatePicker();

        String harvestDateString =
                safe(
                        product.getHarvestDate()
                );

        if (
                !harvestDateString.isEmpty()
        ) {

            try {

                harvestDate.setValue(
                        java.time.LocalDate
                                .parse(
                                        harvestDateString
                                )
                );

            } catch (Exception e) {

                System.out.println(
                        "Invalid harvest date: "
                                + harvestDateString
                );
            }
        }

        harvestDate.setMaxWidth(
                Double.MAX_VALUE
        );

        grid.add(
                createInputBox(
                        "Harvest Date",
                        harvestDate
                ),
                0,
                3
        );

        // =================================================
        // DESCRIPTION
        // =================================================

        TextArea descriptionField =
                new TextArea(
                        safe(
                                product.getDescription()
                        )
                );

        descriptionField.setPromptText(
                "Describe your product..."
        );

        descriptionField.setPrefRowCount(
                4
        );

        descriptionField.setWrapText(
                true
        );

        descriptionField.setStyle(
                "-fx-background-radius: 8px;" +
                "-fx-border-color: #A2D9CE;" +
                "-fx-border-radius: 8px;"
        );

        // =================================================
        // IMAGE SECTION
        // =================================================

        VBox imageSection =
                createImageSection();

        // =================================================
        // ACTION BUTTONS
        // =================================================

        HBox actions =
                new HBox(10);

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        Button cancelBtn =
                new Button(
                        "Cancel"
                );

        cancelBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #1B2631;" +
                "-fx-padding: 8 18;" +
                "-fx-cursor: hand;"
        );

        cancelBtn.setOnAction(
                e -> navigator.navigateTo(
                        "Products"
                )
        );

        Button updateBtn =
                new Button(
                        "Update Product"
                );

        updateBtn.setStyle(
                "-fx-background-color: #117864;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-padding: 9 22;" +
                "-fx-cursor: hand;"
        );

        updateBtn.setOnAction(
                e -> {

                    updateBtn.setDisable(
                            true
                    );

                    updateProduct(
                            nameField,
                            unitBox,
                            categoryBox,
                            varietyField,
                            priceField,
                            stockField,
                            harvestDate,
                            descriptionField,
                            updateBtn
                    );
                }
        );

        actions.getChildren().addAll(
                cancelBtn,
                updateBtn
        );

        form.getChildren().addAll(
                grid,

                createInputBox(
                        "Description",
                        descriptionField
                ),

                imageSection,

                actions
        );

        root.getChildren().addAll(
                titles,
                form
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

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scroll.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;"
        );

        return scroll;
    }

    // =====================================================
    // IMAGE SECTION
    // =====================================================

    private VBox createImageSection() {

        VBox section =
                new VBox(10);

        Label title =
                new Label(
                        "Product Image"
                );

        title.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #566573;"
        );

        HBox imageArea =
                new HBox(15);

        imageArea.setAlignment(
                Pos.CENTER_LEFT
        );

        // =================================================
        // PREVIEW
        // =================================================

        StackPane previewBox =
                new StackPane();

        previewBox.setPrefSize(
                180,
                130
        );

        previewBox.setStyle(
                "-fx-background-color: #E9F7EF;" +
                "-fx-border-color: #A2D9CE;" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 10px;"
        );

        imagePreview =
                new ImageView();

        imagePreview.setFitWidth(
                165
        );

        imagePreview.setFitHeight(
                115
        );

        imagePreview.setPreserveRatio(
                true
        );

        String existingImageUrl =
                safe(
                        product.getImageUrl()
                );

        if (
                !existingImageUrl.isEmpty()
        ) {

            try {

                Image image =
                        new Image(
                                existingImageUrl,
                                165,
                                115,
                                true,
                                true,
                                true
                        );

                imagePreview.setImage(
                        image
                );

                previewBox.getChildren()
                        .add(
                                imagePreview
                        );

            } catch (Exception e) {

                addPlaceholder(
                        previewBox
                );
            }

        } else {

            addPlaceholder(
                    previewBox
            );
        }

        // =================================================
        // CHOOSE NEW IMAGE
        // =================================================

        Button chooseButton =
                new Button(
                        "📷 Change Image"
                );

        chooseButton.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #A2D9CE;" +
                "-fx-border-radius: 8px;" +
                "-fx-background-radius: 8px;" +
                "-fx-padding: 8 16;" +
                "-fx-cursor: hand;"
        );

        chooseButton.setOnAction(
                e -> chooseImage(
                        previewBox
                )
        );

        imageStatus =
                new Label();

        if (
                !existingImageUrl.isEmpty()
        ) {

            imageStatus.setText(
                    "Current image is being used"
            );

        } else {

            imageStatus.setText(
                    "No image available"
            );
        }

        imageStatus.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #566573;"
        );

        VBox controls =
                new VBox(10);

        controls.setPrefWidth(
                280
        );

        controls.getChildren().addAll(
                chooseButton,
                imageStatus
        );

        imageArea.getChildren().addAll(
                previewBox,
                controls
        );

        section.getChildren().addAll(
                title,
                imageArea
        );

        return section;
    }

    // =====================================================
    // CHOOSE NEW IMAGE
    // =====================================================

    private void chooseImage(
            StackPane previewBox) {

        FileChooser chooser =
                new FileChooser();

        chooser.setTitle(
                "Select New Product Image"
        );

        chooser.getExtensionFilters()
                .add(
                        new FileChooser
                                .ExtensionFilter(
                                        "Image Files",
                                        "*.png",
                                        "*.jpg",
                                        "*.jpeg",
                                        "*.webp"
                                )
                );

        File file =
                chooser.showOpenDialog(
                        previewBox
                                .getScene()
                                .getWindow()
                );

        if (file == null) {
            return;
        }

        selectedImageFile =
                file;

        Image image =
                new Image(
                        file.toURI()
                                .toString()
                );

        imagePreview.setImage(
                image
        );

        previewBox.getChildren()
                .setAll(
                        imagePreview
                );

        imageStatus.setText(
                "New image selected: "
                        + file.getName()
        );

        imageStatus.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #117864;"
        );
    }

    // =====================================================
    // UPDATE PRODUCT
    // =====================================================

    private void updateProduct(
            TextField nameField,
            ComboBox<String> unitBox,
            ComboBox<String> categoryBox,
            TextField varietyField,
            TextField priceField,
            TextField stockField,
            DatePicker harvestDate,
            TextArea descriptionField,
            Button updateBtn) {

        try {

            // =================================================
            // VALIDATION
            // =================================================

            String name =
                    nameField.getText()
                            .trim();

            String variety =
                    varietyField.getText()
                            .trim();

            String priceText =
                    priceField.getText()
                            .trim();

            String stockText =
                    stockField.getText()
                            .trim();

            String description =
                    descriptionField.getText()
                            .trim();

            if (name.isEmpty()) {

                showError(
                        "Product name is required."
                );

                updateBtn.setDisable(
                        false
                );

                return;
            }

            if (priceText.isEmpty()) {

                showError(
                        "Price is required."
                );

                updateBtn.setDisable(
                        false
                );

                return;
            }

            if (stockText.isEmpty()) {

                showError(
                        "Stock is required."
                );

                updateBtn.setDisable(
                        false
                );

                return;
            }

            double price =
                    Double.parseDouble(
                            priceText
                    );

            double stock =
                    Double.parseDouble(
                            stockText
                    );

            if (price < 0) {

                showError(
                        "Price cannot be negative."
                );

                updateBtn.setDisable(
                        false
                );

                return;
            }

            if (stock < 0) {

                showError(
                        "Stock cannot be negative."
                );

                updateBtn.setDisable(
                        false
                );

                return;
            }

            // =================================================
            // IMAGE
            // =================================================

            String imageUrl =
                    safe(
                            product.getImageUrl()
                    );

            /*
             * Only upload to Cloudinary if the farmer
             * selected a NEW image.
             */

            if (
                    selectedImageFile != null
            ) {

                imageStatus.setText(
                        "Uploading new image..."
                );

                System.out.println(
                        "Uploading new image to Cloudinary..."
                );

                Map<?, ?> uploadResult =
                        cloudinary.uploader()
                                .upload(
                                        selectedImageFile,
                                        ObjectUtils.asMap(
                                                "folder",
                                                "agrilink/products"
                                        )
                                );

                String newImageUrl =
                        (String) uploadResult
                                .get(
                                        "secure_url"
                                );

                if (
                        newImageUrl == null
                                ||
                        newImageUrl.isEmpty()
                ) {

                    showError(
                            "Cloudinary did not return an image URL."
                    );

                    updateBtn.setDisable(
                            false
                    );

                    return;
                }

                imageUrl =
                        newImageUrl;

                System.out.println(
                        "New Cloudinary URL:"
                );

                System.out.println(
                        imageUrl
                );
            }

            // =================================================
            // UPDATE EXISTING PRODUCT OBJECT
            // =================================================

            /*
             * IMPORTANT:
             * We modify the existing product.
             * We DO NOT create a new Product().
             *
             * Therefore productId remains unchanged.
             */

            product.setName(
                    name
            );

            product.setUnit(
                    unitBox.getValue()
            );

            product.setCategory(
                    categoryBox.getValue()
            );

            product.setVariety(
                    variety
            );

            product.setDescription(
                    description
            );

            product.setHarvestDate(
                    harvestDate.getValue() == null
                            ? ""
                            : harvestDate
                                    .getValue()
                                    .toString()
            );

            product.setPrice(
                    price
            );

            product.setStock(
                    stock
            );

            // =================================================
            // STATUS
            // =================================================

            if (stock <= 0) {

                product.setStatus(
                        "Sold Out"
                );

            } else {

                /*
                 * If the old status was Inactive,
                 * keep it Inactive.
                 *
                 * Otherwise make it Active.
                 */

                if (
                        "Inactive".equalsIgnoreCase(
                                product.getStatus()
                        )
                ) {

                    product.setStatus(
                            "Inactive"
                    );

                } else {

                    product.setStatus(
                            "Active"
                    );
                }
            }

            // =================================================
            // IMAGE URL
            // =================================================

            product.setImageUrl(
                    imageUrl
            );

            // =================================================
            // FIRESTORE UPDATE
            // =================================================

            System.out.println(
                    "Updating product in Firestore..."
            );

            boolean updated =
                    productDAO.updateProduct(
                            product
                    );

            if (updated) {

                System.out.println(
                        "Product updated successfully!"
                );

                showSuccess();

                navigator.navigateTo(
                        "Products"
                );

            } else {

                showError(
                        "Product could not be updated."
                );

                updateBtn.setDisable(
                        false
                );
            }

        } catch (NumberFormatException e) {

            showError(
                    "Price and stock must be valid numbers."
            );

            updateBtn.setDisable(
                    false
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Something went wrong:\n"
                            + e.getMessage()
            );

            updateBtn.setDisable(
                    false
            );
        }
    }

    // =====================================================
    // PLACEHOLDER
    // =====================================================

    private void addPlaceholder(
            StackPane previewBox) {

        Label placeholder =
                new Label(
                        "🌱\nNo Image"
                );

        placeholder.setAlignment(
                Pos.CENTER
        );

        placeholder.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #566573;"
        );

        previewBox.getChildren()
                .add(
                        placeholder
                );
    }

    // =====================================================
    // INPUT STYLE
    // =====================================================

    private void styleInput(
            TextField field) {

        field.setMaxWidth(
                Double.MAX_VALUE
        );

        field.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-color: #A2D9CE;" +
                "-fx-border-radius: 8px;" +
                "-fx-padding: 8px;"
        );
    }

    // =====================================================
    // COMBOBOX STYLE
    // =====================================================

    private void styleComboBox(
            ComboBox<String> combo) {

        combo.setMaxWidth(
                Double.MAX_VALUE
        );

        combo.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-color: #A2D9CE;" +
                "-fx-border-radius: 8px;"
        );
    }

    // =====================================================
    // INPUT BOX
    // =====================================================

    private VBox createInputBox(
            String label,Node input) {

        VBox box =
                new VBox(5);

        Label labelNode =
                new Label(
                        label
                );

        labelNode.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #566573;"
        );

        box.getChildren().addAll(
                labelNode,
                input
        );

        return box;
    }

    // =====================================================
    // FORMAT NUMBER
    // =====================================================

    private String formatNumber(
            double value) {

        if (
                value == Math.floor(value)
        ) {

            return String.format(
                    "%.0f",
                    value
            );
        }

        return String.format(
                "%.2f",
                value
        );
    }

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }

    // =====================================================
    // SUCCESS
    // =====================================================

    private void showSuccess() {

    Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Edit Product"
        );

        alert.setHeaderText(
                "Product updated successfully!"
        );

        alert.setContentText(
                "Your changes have been saved to Firestore."
        );

        alert.showAndWait();
    }

    // =====================================================
    // ERROR
    // =====================================================

    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Edit Product"
        );

        alert.setHeaderText(
                "Unable to update product"
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}