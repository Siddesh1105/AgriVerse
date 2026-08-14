package com.mainproject.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AddProduct {

    private final FarmerDashboard navigator;

    public AddProduct(FarmerDashboard navigator) {
        this.navigator = navigator;
    }

    public Node getView() {
        VBox root = new VBox(18);
        root.setPadding(new Insets(10));

        VBox titles = new VBox(2);
        Label title = new Label("Add Product");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label sub = new Label("List a new product on the marketplace.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        titles.getChildren().addAll(title, sub);

        HBox layout = new HBox(20);

        // 1. Left Stepper Navigation
        VBox stepper = new VBox(12);
        stepper.setPrefWidth(180);
        stepper.getChildren().addAll(
            createStep("1", "Product Details", true),
            createStep("2", "Pricing & Stock", false),
            createStep("3", "Images", false),
            createStep("4", "Review", false)
        );

        // 2. Middle Form Fields
        VBox form = new VBox(14);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14px; -fx-border-color: #A2D9CE;");
        HBox.setHgrow(form, Priority.ALWAYS);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);

        TextField nameField = new TextField("Tomato");
        nameField.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE;");
        grid.add(createInputBox("Product Name", nameField), 0, 0);

        TextField unitField = new TextField("kg");
        unitField.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE;");
        grid.add(createInputBox("Unit", unitField), 1, 0);

        ComboBox<String> catCb = new ComboBox<>();
        catCb.getItems().addAll("Vegetables", "Fruits", "Grains", "Pulses");
        catCb.setValue("Vegetables");
        catCb.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE;");
        grid.add(createInputBox("Category", catCb), 0, 1);

        TextField varietyField = new TextField("Hybrid");
        varietyField.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE;");
        grid.add(createInputBox("Variety (Optional)", varietyField), 1, 1);

        TextArea descField = new TextArea("Fresh and farm grown tomatoes.");
        descField.setPrefRowCount(3);
        descField.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE;");

        TextField harvestField = new TextField("12/05/2025");
        harvestField.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE;");

        // Action Buttons Row (Cancel, Next)
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #1B2631; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> navigator.navigateTo("Marketplace"));

        Button nextBtn = new Button("Next");
        nextBtn.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 7 24; -fx-cursor: hand;");
        nextBtn.setOnAction(e -> navigator.navigateTo("Marketplace"));

        actions.getChildren().addAll(cancelBtn, nextBtn);

        form.getChildren().addAll(grid, createInputBox("Description", descField), createInputBox("Harvest Date", harvestField), actions);

        // 3. Right Tips Box
        VBox tips = new VBox(10);
        tips.setPadding(new Insets(16));
        tips.setPrefWidth(220);
        tips.setStyle("-fx-background-color: #D4EFDF; -fx-background-radius: 14px; -fx-border-color: #A2D9CE;");

        Label tipTitle = new Label("Tips for better sales");
        tipTitle.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #117864;");

        tips.getChildren().addAll(
            tipTitle,
            createTipItem("📷 Add clear images"),
            createTipItem("💲 Provide accurate pricing"),
            createTipItem("📦 Keep stock updated"),
            createTipItem("✍️ Write detailed description")
        );

        layout.getChildren().addAll(stepper, form, tips);
        root.getChildren().addAll(titles, layout);
        return new ScrollPane(root);
    }

    private HBox createStep(String num, String name, boolean active) {
        HBox box = new HBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        Label n = new Label(num);
        n.setStyle(active ? "-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 12px;"
                          : "-fx-background-color: #FFFFFF; -fx-text-fill: #566573; -fx-border-color: #A2D9CE; -fx-padding: 3 8; -fx-background-radius: 12px;");

        Label t = new Label(name);
        t.setStyle(active ? "-fx-font-weight: bold; -fx-text-fill: #117864;" : "-fx-text-fill: #566573;");

        box.getChildren().addAll(n, t);
        return box;
    }

    private VBox createInputBox(String label, Node inputNode) {
        VBox box = new VBox(4);
        Label l = new Label(label);
        l.setStyle("-fx-font-size: 12px; -fx-text-fill: #566573;");
        box.getChildren().addAll(l, inputNode);
        return box;
    }

    private Label createTipItem(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 12px; -fx-text-fill: #1B2631;");
        return l;
    }
}