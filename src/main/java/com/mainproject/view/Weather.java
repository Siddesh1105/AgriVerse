package com.mainproject.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Weather {

    public Node getView() {
        VBox root = new VBox(18);
        root.setPadding(new Insets(10));

        VBox titles = new VBox(2);
        Label title = new Label("Weather");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label sub = new Label("Current weather conditions and forecast.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        titles.getChildren().addAll(title, sub);

        // Big Primary Weather Card
        VBox weatherCard = new VBox(15);
        weatherCard.setPadding(new Insets(24));
        weatherCard.setStyle("-fx-background-color: linear-gradient(to right, #4A90E2, #50E3C2); -fx-background-radius: 16px;");

        Label city = new Label("Nashik, Maharashtra");
        city.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

        HBox centerInfo = new HBox(20);
        centerInfo.setAlignment(Pos.CENTER_LEFT);
        Label temp = new Label("23°C");
        temp.setStyle("-fx-font-size: 48px; -fx-font-weight: 900; -fx-text-fill: #FFFFFF;");
        Label condition = new Label("Partly Cloudy ⛅");
        condition.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
        centerInfo.getChildren().addAll(temp, condition);

        HBox stats = new HBox(30);
        stats.getChildren().addAll(
            createWeatherStat("Humidity", "68%"),
            createWeatherStat("Wind", "12 km/h"),
            createWeatherStat("Rain Chance", "20%")
        );

        weatherCard.getChildren().addAll(city, centerInfo, stats);

        // 5-Day Forecast Row
        HBox forecastRow = new HBox(12);
        forecastRow.getChildren().addAll(
            createForecastDay("Today", "⛅", "23° / 16°"),
            createForecastDay("Tue", "☀️", "24° / 15°"),
            createForecastDay("Wed", "🌦️", "25° / 17°"),
            createForecastDay("Thu", "🌧️", "26° / 16°"),
            createForecastDay("Fri", "⛅", "24° / 15°")
        );

        // Weather Alert Banner
        HBox alert = new HBox(10);
        alert.setAlignment(Pos.CENTER_LEFT);
        alert.setPadding(new Insets(14));
        alert.setStyle("-fx-background-color: #D4EFDF; -fx-background-radius: 12px; -fx-border-color: #A2D9CE;");
        Label checkIcon = new Label("🛡️");
        Label alertText = new Label("No severe weather alerts for the next 5 days.");
        alertText.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #117864;");
        alert.getChildren().addAll(checkIcon, alertText);

        root.getChildren().addAll(titles, weatherCard, forecastRow, alert);
        return new ScrollPane(root);
    }

    private VBox createWeatherStat(String key, String val) {
        VBox box = new VBox(2);
        Label k = new Label(key);
        k.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.8);");
        Label v = new Label(val);
        v.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        box.getChildren().addAll(k, v);
        return box;
    }

    private VBox createForecastDay(String day, String icon, String tempRange) {
        VBox box = new VBox(6);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(12));
        box.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #A2D9CE;");
        HBox.setHgrow(box, Priority.ALWAYS);

        Label d = new Label(day);
        d.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B2631;");
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 22px;");
        Label tr = new Label(tempRange);
        tr.setStyle("-fx-font-size: 12px; -fx-text-fill: #566573;");

        box.getChildren().addAll(d, ic, tr);
        return box;
    }
}