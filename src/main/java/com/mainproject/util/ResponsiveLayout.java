package com.mainproject.util;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/** Shared responsive helpers. UI only; does not change application logic. */
public final class ResponsiveLayout {
    private ResponsiveLayout() {}

    public static Scene createScene(Parent root) {
        javafx.geometry.Rectangle2D b = Screen.getPrimary().getVisualBounds();
        double w = Math.max(1000, Math.min(1440, b.getWidth() * 0.92));
        double h = Math.max(700, Math.min(900, b.getHeight() * 0.90));
        return new Scene(root, w, h);
    }

    public static void prepareStage(Stage stage) {
        if (stage == null) return;
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        javafx.geometry.Rectangle2D b = Screen.getPrimary().getVisualBounds();
        stage.setWidth(Math.min(1440, b.getWidth() * 0.95));
        stage.setHeight(Math.min(920, b.getHeight() * 0.95));
        stage.centerOnScreen();
    }

    /** Makes a page safely scroll vertically inside a dashboard. */
    public static ScrollPane scrollPage(Node content) {
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setPannable(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        if (content instanceof Region r) {
            r.setMaxWidth(Double.MAX_VALUE);
            VBox.setVgrow(r, Priority.ALWAYS);
        }
        return scroll;
    }
}
