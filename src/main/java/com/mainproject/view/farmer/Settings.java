package com.mainproject.view.farmer;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Settings {

    public Node getView() {
        VBox root = new VBox(18);
        root.setPadding(new Insets(10));

        VBox titles = new VBox(2);
        Label title = new Label("Settings");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label sub = new Label("Manage your account and preferences.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        titles.getChildren().addAll(title, sub);

        HBox layout = new HBox(20);

        // Left Submenu
        VBox menu = new VBox(6);
        menu.setPrefWidth(180);
        menu.getChildren().addAll(
            createMenuItem("Account", true),
            createMenuItem("Password", false),
            createMenuItem("Notifications", false),
            createMenuItem("Payment Methods", false),
            createMenuItem("Privacy", false),
            createMenuItem("Language", false)
        );

        // Right Settings Form
        VBox form = new VBox(16);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14px; -fx-border-color: #A2D9CE;");
        HBox.setHgrow(form, Priority.ALWAYS);

        Label secTitle = new Label("Account Settings");
        secTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1B2631;");

        form.getChildren().addAll(
            secTitle,
            createSettingRow("Email", "rajeshpatil@email.com", true),
            createSettingRow("Phone Number", "+91 98765 43210", true),
            createDropdownRow("Language", "English", "Marathi", "Hindi"),
            createDropdownRow("Theme", "Light", "Dark")
        );

        layout.getChildren().addAll(menu, form);
        root.getChildren().addAll(titles, layout);
        return new ScrollPane(root);
    }

    private Button createMenuItem(String name, boolean active) {
        Button b = new Button(name);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(8, 12, 8, 12));
        if (active) {
            b.setStyle("-fx-background-color: #D4EFDF; -fx-text-fill: #117864; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;");
        } else {
            b.setStyle("-fx-background-color: transparent; -fx-text-fill: #1B2631; -fx-font-weight: 600; -fx-cursor: hand;");
        }
        return b;
    }

    private HBox createSettingRow(String key, String val, boolean editable) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(2);
        Label k = new Label(key);
        k.setStyle("-fx-font-size: 12px; -fx-text-fill: #566573;");
        Label v = new Label(val);
        v.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1B2631;");
        box.getChildren().addAll(k, v);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        row.getChildren().addAll(box, sp);
        if (editable) {
            Button edit = new Button("Edit");
            edit.setStyle("-fx-background-color: transparent; -fx-border-color: #A2D9CE; -fx-border-radius: 6px; -fx-cursor: hand;");
            row.getChildren().add(edit);
        }
        return row;
    }

    private VBox createDropdownRow(String key, String... options) {
        VBox box = new VBox(4);
        Label k = new Label(key);
        k.setStyle("-fx-font-size: 12px; -fx-text-fill: #566573;");
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll(options);
        cb.setValue(options[0]);
        cb.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE;");
        box.getChildren().addAll(k, cb);
        return box;
    }
}