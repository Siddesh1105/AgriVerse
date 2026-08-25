package com.mainproject.view.buyer;

import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class Wishlist {

    private final BuyerDashboard mainController;

    public Wishlist(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25, 30, 25, 30));

        Label title = new Label("My Wishlist ❤️ (4 Items)");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        grid.add(createWishlistCard("Alphonso Mango", "Suresh Farm", "₹120 / kg"), 0, 0);
        grid.add(createWishlistCard("Organic Wheat", "Green Valley Farm", "₹26 / kg"), 1, 0);
        grid.add(createWishlistCard("Fresh Tomato", "Ramesh Patil", "₹28 / kg"), 0, 1);
        grid.add(createWishlistCard("Onion", "Mahesh Farm", "₹22 / kg"), 1, 1);

        root.getChildren().addAll(title, grid);

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        LanguageManager.apply(sp);
        return sp;
    }

    private VBox createWishlistCard(String title, String farm, String price) {
        VBox c = new VBox(6);
        c.setPrefWidth(280);
        c.setPadding(new Insets(15));
        c.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");
        c.getChildren().addAll(
            new Label(title),
            new Label(farm),
            new Label(price)
        );
        return c;
    }
}