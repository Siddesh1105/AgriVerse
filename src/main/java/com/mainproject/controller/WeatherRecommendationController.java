package com.mainproject.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.mainproject.config.WeatherConfig;
import com.mainproject.model.User;
import com.mainproject.model.WeatherRecommendation;

import java.util.ArrayList;
import java.util.List;

public class WeatherRecommendationController {

    private final String userEmail;

    private String userCity = "";
    private String userState = "";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public WeatherRecommendationController(String userEmail) {

        this.userEmail = userEmail;

        loadUserLocation();
    }

    // =====================================================
    // LOAD USER LOCATION FROM FIRESTORE
    // =====================================================

    private void loadUserLocation() {

        try {

            if (userEmail == null ||
                    userEmail.trim().isEmpty()) {

                System.out.println(
                        "Weather Recommendation: User email is missing."
                );

                return;
            }

            UserController userController =
                    new UserController();

            User user =
                    userController.getUserByEmail(userEmail);

            if (user != null) {

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
                        "Weather Recommendation Location Loaded"
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
            }

        } catch (Exception e) {

            System.out.println(
                    "Error loading weather recommendation location:"
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // GET WEATHER RECOMMENDATIONS
    // =====================================================

    public List<WeatherRecommendation>
    getRecommendations() {

        List<WeatherRecommendation> recommendations =
                new ArrayList<>();

        try {

            if (userCity == null ||
                    userCity.trim().isEmpty()) {

                recommendations.add(
                        new WeatherRecommendation(
                                "📍 Location Required",
                                "Update Your Location",
                                "Please update your city and state in your "
                                        + "profile to receive weather-based "
                                        + "farming recommendations.",
                                "Update Profile"
                        )
                );

                return recommendations;
            }

            JsonObject currentWeather =
                    WeatherConfig.getCurrentWeather(
                            userCity,
                            userState
                    );

            JsonObject forecast =
                    WeatherConfig.getForecast(
                            userCity,
                            userState
                    );

            double temperature =
                    getTemperature(currentWeather);

            int humidity =
                    getHumidity(currentWeather);

            double windSpeed =
                    getWindSpeed(currentWeather);

            String condition =
                    getWeatherCondition(currentWeather);

            boolean rainExpected =
                    isRainExpected(forecast);

            // =============================================
            // TEMPERATURE RECOMMENDATION
            // =============================================

            if (temperature >= 35) {

                recommendations.add(
                        new WeatherRecommendation(
                                "🌡 High Temperature",
                                "Protect Your Crops",
                                "The temperature in "
                                        + userCity
                                        + " is currently "
                                        + Math.round(temperature)
                                        + "°C. High temperatures can "
                                        + "increase water loss. Irrigate "
                                        + "during early morning or evening.",
                                "Weather Advice"
                        )
                );

            } else if (temperature >= 30) {

                recommendations.add(
                        new WeatherRecommendation(
                                "🌡 Warm Weather",
                                "Monitor Irrigation",
                                "The current temperature in "
                                        + userCity
                                        + " is "
                                        + Math.round(temperature)
                                        + "°C. Monitor soil moisture and "
                                        + "provide water when required.",
                                "View Advice"
                        )
                );

            } else if (temperature <= 15) {

                recommendations.add(
                        new WeatherRecommendation(
                                "❄ Cool Weather",
                                "Protect Sensitive Crops",
                                "The temperature in "
                                        + userCity
                                        + " is currently "
                                        + Math.round(temperature)
                                        + "°C. Protect sensitive crops "
                                        + "from cold conditions.",
                                "View Advice"
                        )
                );

            } else {

                recommendations.add(
                        new WeatherRecommendation(
                                "🌡 Temperature",
                                "Good Farming Conditions",
                                "The current temperature in "
                                        + userCity
                                        + " is "
                                        + Math.round(temperature)
                                        + "°C, which provides generally "
                                        + "comfortable farming conditions.",
                                "View Details"
                        )
                );
            }

            // =============================================
            // RAIN RECOMMENDATION
            // =============================================

            if (rainExpected ||
                    containsRain(condition)) {

                recommendations.add(
                        new WeatherRecommendation(
                                "🌧 Rain Advisory",
                                "Rain Expected",
                                "Rain is expected soon in "
                                        + userCity
                                        + ". Avoid unnecessary irrigation "
                                        + "and postpone pesticide or "
                                        + "fertilizer spraying.",
                                "View Forecast"
                        )
                );

            } else {

                recommendations.add(
                        new WeatherRecommendation(
                                "☀ Weather Forecast",
                                "No Immediate Rain",
                                "No significant rainfall is expected soon "
                                        + "in "
                                        + userCity
                                        + ". Continue monitoring soil "
                                        + "moisture and irrigation needs.",
                                "View Forecast"
                        )
                );
            }

            // =============================================
            // WIND RECOMMENDATION
            // =============================================

            if (windSpeed >= 8) {

                recommendations.add(
                        new WeatherRecommendation(
                                "💨 Wind Advisory",
                                "Strong Winds Detected",
                                "Wind speed is currently high in "
                                        + userCity
                                        + ". Avoid spraying pesticides "
                                        + "because chemicals may drift away.",
                                "View Advice"
                        )
                );

            } else if (windSpeed >= 5) {

                recommendations.add(
                        new WeatherRecommendation(
                                "💨 Wind Conditions",
                                "Moderate Wind",
                                "Moderate wind is present in "
                                        + userCity
                                        + ". Be careful while spraying "
                                        + "pesticides or fertilizers.",
                                "View Advice"
                        )
                );

            } else {

                recommendations.add(
                        new WeatherRecommendation(
                                "💨 Wind Conditions",
                                "Suitable Conditions",
                                "Wind speed is low, providing generally "
                                        + "suitable conditions for outdoor "
                                        + "farm activities.",
                                "View Details"
                        )
                );
            }

            // =============================================
            // HUMIDITY RECOMMENDATION
            // =============================================

            if (humidity <= 35) {

                recommendations.add(
                        new WeatherRecommendation(
                                "💧 Low Humidity",
                                "Check Soil Moisture",
                                "Humidity is currently low in "
                                        + userCity
                                        + ". Crops may lose water faster, "
                                        + "so monitor soil moisture carefully.",
                                "View Advice"
                        )
                );

            } else if (humidity >= 85) {

                recommendations.add(
                        new WeatherRecommendation(
                                "💧 High Humidity",
                                "Monitor Crop Diseases",
                                "High humidity can increase the risk of "
                                        + "fungal diseases. Regularly inspect "
                                        + "your crops and improve airflow.",
                                "View Advice"
                        )
                );

            } else {

                recommendations.add(
                        new WeatherRecommendation(
                                "💧 Humidity",
                                "Normal Humidity",
                                "Current humidity in "
                                        + userCity
                                        + " is "
                                        + humidity
                                        + "%. Continue regular monitoring "
                                        + "of your crops and soil.",
                                "View Details"
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            recommendations.clear();

            recommendations.add(
                    new WeatherRecommendation(
                            "⚠ Weather Service",
                            "Weather Data Unavailable",
                            "Unable to load weather information for "
                                    + (userCity == null ||
                                    userCity.isEmpty()
                                    ? "your location"
                                    : userCity)
                                    + ". Please check your internet "
                                    + "connection and OpenWeather API "
                                    + "configuration.",
                            "Retry"
                    )
            );
        }

        return recommendations;
    }

    // =====================================================
    // GET CURRENT WEATHER SUMMARY
    // =====================================================

    public String getCurrentWeatherSummary() {

        try {

            if (userCity == null ||
                    userCity.trim().isEmpty()) {

                return "Please update your location.";
            }

            JsonObject weather =
                    WeatherConfig.getCurrentWeather(
                            userCity,
                            userState
                    );

            double temperature =
                    getTemperature(weather);

            int humidity =
                    getHumidity(weather);

            double windSpeed =
                    getWindSpeed(weather);

            String condition =
                    getWeatherCondition(weather);

            return userCity
                    + " • "
                    + condition
                    + " • "
                    + Math.round(temperature)
                    + "°C"
                    + " • Humidity "
                    + humidity
                    + "%"
                    + " • Wind "
                    + Math.round(windSpeed * 10.0) / 10.0
                    + " m/s";

        } catch (Exception e) {

            return "Unable to load current weather.";
        }
    }

    // =====================================================
    // GET TEMPERATURE
    // =====================================================

    private double getTemperature(
            JsonObject weather) {

        return weather
                .getAsJsonObject("main")
                .get("temp")
                .getAsDouble();
    }

    // =====================================================
    // GET HUMIDITY
    // =====================================================

    private int getHumidity(
            JsonObject weather) {

        return weather
                .getAsJsonObject("main")
                .get("humidity")
                .getAsInt();
    }

    // =====================================================
    // GET WIND SPEED
    // =====================================================

    private double getWindSpeed(
            JsonObject weather) {

        JsonObject wind =
                weather.getAsJsonObject("wind");

        if (wind == null ||
                wind.get("speed") == null) {

            return 0;
        }

        return wind
                .get("speed")
                .getAsDouble();
    }

    // =====================================================
    // GET WEATHER CONDITION
    // =====================================================

    private String getWeatherCondition(
            JsonObject weather) {

        JsonArray weatherArray =
                weather.getAsJsonArray("weather");

        if (weatherArray == null ||
                weatherArray.isEmpty()) {

            return "Unknown";
        }

        JsonObject firstWeather =
                weatherArray
                        .get(0)
                        .getAsJsonObject();

        String description =
                firstWeather
                        .get("description")
                        .getAsString();

        if (description == null ||
                description.trim().isEmpty()) {

            return "Unknown";
        }

        return Character.toUpperCase(
                description.charAt(0)
        ) + description.substring(1);
    }

    // =====================================================
    // CHECK FORECAST FOR RAIN
    // =====================================================

    private boolean isRainExpected(
            JsonObject forecast) {

        if (forecast == null ||
                forecast.getAsJsonArray("list") == null) {

            return false;
        }

        JsonArray forecastList =
                forecast.getAsJsonArray("list");

        for (int i = 0;
             i < forecastList.size() && i < 8;
             i++) {

            JsonObject item =
                    forecastList
                            .get(i)
                            .getAsJsonObject();

            JsonArray weatherArray =
                    item.getAsJsonArray("weather");

            if (weatherArray == null ||
                    weatherArray.isEmpty()) {

                continue;
            }

            String main =
                    weatherArray
                            .get(0)
                            .getAsJsonObject()
                            .get("main")
                            .getAsString()
                            .toLowerCase();

            if (main.contains("rain") ||
                    main.contains("drizzle") ||
                    main.contains("thunderstorm")) {

                return true;
            }
        }

        return false;
    }

    // =====================================================
    // CHECK CURRENT CONDITION FOR RAIN
    // =====================================================

    private boolean containsRain(
            String condition) {

        if (condition == null) {

            return false;
        }

        String lower =
                condition.toLowerCase();

        return lower.contains("rain")
                || lower.contains("drizzle")
                || lower.contains("thunderstorm");
    }
}