package com.mainproject.view.farmer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.mainproject.config.WeatherConfig;
import com.mainproject.controller.UserController;
import com.mainproject.model.User;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Weather {

    private final String userEmail;

    private String userCity = "";
    private String userState = "";

    private Label temperature;
    private Label condition;
    private Label humidity;
    private Label wind;

    private Label subtitle;
    private Label cityLabel;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Weather(String userEmail) {

        this.userEmail = userEmail;
    }

    // =====================================================
    // LOAD FARMER LOCATION FROM FIRESTORE
    // =====================================================

    private void loadUserLocation() {

        try {

            if (userEmail == null ||
                    userEmail.trim().isEmpty()) {

                System.out.println(
                        "Weather: User email is missing."
                );

                return;
            }

            UserController userController =
                    new UserController();

            User user =
                    userController.getUserByEmail(
                            userEmail.trim()
                    );

            if (user == null) {

                System.out.println(
                        "Weather: User not found."
                );

                return;
            }

            userCity = "";
            userState = "";

            if (user.getCity() != null &&
                    !user.getCity().trim().isEmpty()) {

                userCity =
                        user.getCity().trim();
            }

            if (user.getState() != null &&
                    !user.getState().trim().isEmpty()) {

                userState =
                        user.getState().trim();
            }

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "LATEST WEATHER LOCATION LOADED"
            );

            System.out.println(
                    "Email: " + userEmail
            );

            System.out.println(
                    "City: " + userCity
            );

            System.out.println(
                    "State: " + userState
            );

            System.out.println(
                    "===================================="
            );

        } catch (Exception e) {

            System.out.println(
                    "Error loading weather location:"
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // MAIN VIEW
    // =====================================================

    public Node getView() {

        // Always load the latest location from Firestore
        loadUserLocation();

        VBox root = new VBox(20);

        root.setPadding(
                new Insets(20)
        );

        root.setStyle(
                "-fx-background-color: #F1FAF6;"
        );

        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label("Weather");

        title.setStyle(
                "-fx-font-size: 25px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17202A;"
        );

        // =================================================
        // SUBTITLE
        // =================================================

        subtitle =
                new Label(
                        userCity.isEmpty()
                                ? "Update your profile location to view weather."
                                : "Live weather conditions and forecast for "
                                + userCity
                );

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #607D8B;"
        );

        // =================================================
        // WEATHER CARD
        // =================================================

        VBox weatherCard =
                new VBox(18);

        weatherCard.setPadding(
                new Insets(25)
        );

        weatherCard.setStyle(
                "-fx-background-color: linear-gradient(to right, #4285E5, #48D8BD);" +
                        "-fx-background-radius: 20px;"
        );

        // =================================================
        // CITY LABEL
        // =================================================

        String locationText;

        if (userCity.isEmpty()) {

            locationText =
                    "📍 Location not set";

        } else if (userState.isEmpty()) {

            locationText =
                    "📍 " + userCity;

        } else {

            locationText =
                    "📍 " + userCity
                            + ", " + userState;
        }

        cityLabel =
                new Label(locationText);

        cityLabel.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        // =================================================
        // WEATHER INFORMATION
        // =================================================

        HBox weatherInfo =
                new HBox(25);

        weatherInfo.setAlignment(
                Pos.CENTER_LEFT
        );

        Label weatherIcon =
                new Label("☁");

        weatherIcon.setStyle(
                "-fx-font-size: 55px;"
        );

        temperature =
                new Label("--°C");

        temperature.setStyle(
                "-fx-font-size: 52px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        VBox conditionBox =
                new VBox(7);

        condition =
                new Label(
                        userCity.isEmpty()
                                ? "Location required"
                                : "Loading..."
                );

        condition.setStyle(
                "-fx-font-size: 19px;" +
                        "-fx-text-fill: white;"
        );

        Label feels =
                new Label("Live weather");

        feels.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: rgba(255,255,255,0.85);"
        );

        conditionBox.getChildren().addAll(
                condition,
                feels
        );

        weatherInfo.getChildren().addAll(
                weatherIcon,
                temperature,
                conditionBox
        );

        // =================================================
        // STATS
        // =================================================

        HBox stats =
                new HBox(35);

        humidity =
                createStat(
                        "Humidity",
                        "--"
                );

        wind =
                createStat(
                        "Wind",
                        "--"
                );

        stats.getChildren().addAll(
                humidity,
                wind
        );

        weatherCard.getChildren().addAll(
                cityLabel,
                weatherInfo,
                stats
        );

        // =================================================
        // FORECAST TITLE
        // =================================================

        Label forecastTitle =
                new Label("5-Day Forecast");

        forecastTitle.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17202A;"
        );

        // =================================================
        // FORECAST ROW
        // =================================================

        HBox forecastRow =
                new HBox(15);

        forecastRow.setAlignment(
                Pos.CENTER_LEFT
        );

        for (int i = 0; i < 5; i++) {

            forecastRow.getChildren().add(
                    createForecastCard(
                            "--",
                            "☁",
                            "-- / --",
                            "Loading..."
                    )
            );
        }

        root.getChildren().addAll(
                title,
                subtitle,
                weatherCard,
                forecastTitle,
                forecastRow
        );

        // =================================================
        // LOAD WEATHER ONLY IF LOCATION EXISTS
        // =================================================

        if (userCity != null &&
                !userCity.trim().isEmpty()) {

            loadWeather(
                    forecastRow,
                    weatherIcon
            );

        } else {

            condition.setText(
                    "Please update your city"
            );

            forecastRow.getChildren().clear();

            forecastRow.getChildren().add(
                    createForecastCard(
                            "Location",
                            "📍",
                            "--",
                            "Update profile"
                    )
            );
        }

        ScrollPane scrollPane =
                new ScrollPane(root);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color: transparent;"
        );

        return scrollPane;
    }

    // =====================================================
    // CREATE STAT
    // =====================================================

    private Label createStat(
            String name,
            String value) {

        Label label =
                new Label(
                        name + "\n" + value
                );

        label.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: white;"
        );

        return label;
    }

    // =====================================================
    // LOAD CURRENT WEATHER
    // =====================================================

    private void loadWeather(
            HBox forecastRow,
            Label weatherIcon) {

        Task<JsonObject> task =
                new Task<>() {

                    @Override
                    protected JsonObject call()
                            throws Exception {

                        return WeatherConfig.getCurrentWeather(
                                userCity,
                                userState
                        );
                    }
                };

        task.setOnSucceeded(event -> {

            JsonObject data =
                    task.getValue();

            JsonObject main =
                    data.getAsJsonObject("main");

            JsonObject windData =
                    data.getAsJsonObject("wind");

            JsonObject weatherData =
                    data.getAsJsonArray("weather")
                            .get(0)
                            .getAsJsonObject();

            double temp =
                    main.get("temp")
                            .getAsDouble();

            int humidityValue =
                    main.get("humidity")
                            .getAsInt();

            double windValue =
                    windData != null &&
                            windData.has("speed")
                            ? windData.get("speed")
                            .getAsDouble()
                            : 0;

            String description =
                    weatherData.get("description")
                            .getAsString();

            String icon =
                    weatherData.get("icon")
                            .getAsString();

            String actualCity =
                    data.has("name")
                            ? data.get("name").getAsString()
                            : userCity;

            subtitle.setText(
                    "Live weather conditions and forecast for "
                            + actualCity
            );

            if (userState == null ||
                    userState.isEmpty()) {

                cityLabel.setText(
                        "📍 " + actualCity
                );

            } else {

                cityLabel.setText(
                        "📍 " + actualCity
                                + ", " + userState
                );
            }

            temperature.setText(
                    String.format(
                            "%.0f°C",
                            temp
                    )
            );

            condition.setText(
                    capitalize(description)
            );

            weatherIcon.setText(
                    getIcon(icon)
            );

            humidity.setText(
                    "Humidity\n"
                            + humidityValue
                            + "%"
            );

            wind.setText(
                    String.format(
                            "Wind\n%.1f m/s",
                            windValue
                    )
            );

            loadForecast(forecastRow);
        });

        task.setOnFailed(event -> {

            temperature.setText("--°C");

            condition.setText(
                    "Weather unavailable"
            );

            System.out.println(
                    "Weather loading failed:"
            );

            if (task.getException() != null) {

                task.getException()
                        .printStackTrace();
            }
        });

        Thread thread =
                new Thread(task);

        thread.setDaemon(true);

        thread.start();
    }

    // =====================================================
    // LOAD FORECAST
    // =====================================================

    private void loadForecast(
            HBox forecastRow) {

        Task<JsonObject> task =
                new Task<>() {

                    @Override
                    protected JsonObject call()
                            throws Exception {

                        return WeatherConfig.getForecast(
                                userCity,
                                userState
                        );
                    }
                };

        task.setOnSucceeded(event -> {

            JsonObject data =
                    task.getValue();

            JsonArray list =
                    data.getAsJsonArray("list");

            forecastRow.getChildren().clear();

            for (int i = 0; i < 5; i++) {

                int index = i * 8;

                if (index >= list.size()) {
                    break;
                }

                JsonObject item =
                        list.get(index)
                                .getAsJsonObject();

                JsonObject main =
                        item.getAsJsonObject("main");

                JsonObject weather =
                        item.getAsJsonArray("weather")
                                .get(0)
                                .getAsJsonObject();

                double maxTemp =
                        main.get("temp_max")
                                .getAsDouble();

                double minTemp =
                        main.get("temp_min")
                                .getAsDouble();

                String icon =
                        weather.get("icon")
                                .getAsString();

                String description =
                        weather.get("description")
                                .getAsString();

                String day;

                if (i == 0) {

                    day = "Today";

                } else if (i == 1) {

                    day = "Tomorrow";

                } else {

                    String dateTime =
                            item.get("dt_txt")
                                    .getAsString();

                    day =
                            dateTime.length() >= 10
                                    ? dateTime.substring(5, 10)
                                    : "Day " + (i + 1);
                }

                forecastRow.getChildren().add(
                        createForecastCard(
                                day,
                                getIcon(icon),
                                String.format(
                                        "%.0f° / %.0f°",
                                        maxTemp,
                                        minTemp
                                ),
                                capitalize(description)
                        )
                );
            }
        });

        task.setOnFailed(event -> {

            System.out.println(
                    "Forecast loading failed:"
            );

            if (task.getException() != null) {

                task.getException()
                        .printStackTrace();
            }
        });

        Thread thread =
                new Thread(task);

        thread.setDaemon(true);

        thread.start();
    }

    // =====================================================
    // FORECAST CARD
    // =====================================================

    private VBox createForecastCard(
            String day,
            String icon,
            String temp,
            String description) {

        VBox card =
                new VBox(12);

        card.setAlignment(
                Pos.CENTER
        );

        card.setPadding(
                new Insets(18)
        );

        card.setPrefWidth(180);

        card.setPrefHeight(175);

        HBox.setHgrow(
                card,
                Priority.ALWAYS
        );

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-border-color: #D5E8E0;" +
                        "-fx-border-radius: 15px;"
        );

        Label dayLabel =
                new Label(day);

        dayLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17202A;"
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setStyle(
                "-fx-font-size: 38px;"
        );

        Label tempLabel =
                new Label(temp);

        tempLabel.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #263238;"
        );

        Label descriptionLabel =
                new Label(description);

        descriptionLabel.setWrapText(true);

        descriptionLabel.setMaxWidth(150);

        descriptionLabel.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #117864;"
        );

        card.getChildren().addAll(
                dayLabel,
                iconLabel,
                tempLabel,
                descriptionLabel
        );

        return card;
    }

    // =====================================================
    // WEATHER ICON
    // =====================================================

    private String getIcon(
            String code) {

        if (code == null) {
            return "☁";
        }

        if (code.equals("01d") ||
                code.equals("01n")) {

            return "☀";
        }

        if (code.equals("02d") ||
                code.equals("02n")) {

            return "⛅";
        }

        if (code.equals("03d") ||
                code.equals("03n") ||
                code.equals("04d") ||
                code.equals("04n")) {

            return "☁";
        }

        if (code.equals("09d") ||
                code.equals("09n")) {

            return "🌧";
        }

        if (code.equals("10d") ||
                code.equals("10n")) {

            return "🌦";
        }

        if (code.equals("11d") ||
                code.equals("11n")) {

            return "⛈";
        }

        return "☁";
    }

    // =====================================================
    // CAPITALIZE
    // =====================================================

    private String capitalize(
            String text) {

        if (text == null ||
                text.isEmpty()) {

            return "";
        }

        return text.substring(0, 1)
                .toUpperCase()
                + text.substring(1);
    }
}