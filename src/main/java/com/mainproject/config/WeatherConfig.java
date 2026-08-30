package com.mainproject.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class WeatherConfig {

    private static final String API_KEY =
            "ef3882a6d55fef1368a493a94306d8b4";

    private static final String BASE_URL =
            "https://api.openweathermap.org/data/2.5/";

    private WeatherConfig() {
    }

    // =====================================================
    // GET CURRENT WEATHER USING CITY
    // =====================================================

    public static JsonObject getCurrentWeather(
            String city,
            String state)
            throws IOException, InterruptedException {

        String location =
                buildLocation(city, state);

        String url =
                BASE_URL
                        + "weather"
                        + "?q="
                        + URLEncoder.encode(
                                location,
                                StandardCharsets.UTF_8
                        )
                        + "&appid="
                        + API_KEY
                        + "&units=metric";

        return sendRequest(url);
    }

    // =====================================================
    // GET FORECAST USING CITY
    // =====================================================

    public static JsonObject getForecast(
            String city,
            String state)
            throws IOException, InterruptedException {

        String location =
                buildLocation(city, state);

        String url =
                BASE_URL
                        + "forecast"
                        + "?q="
                        + URLEncoder.encode(
                                location,
                                StandardCharsets.UTF_8
                        )
                        + "&appid="
                        + API_KEY
                        + "&units=metric";

        return sendRequest(url);
    }

    // =====================================================
    // OVERLOAD
    // ONLY CITY
    // =====================================================

    public static JsonObject getCurrentWeather(
            String city)
            throws IOException, InterruptedException {

        return getCurrentWeather(
                city,
                ""
        );
    }

    public static JsonObject getForecast(
            String city)
            throws IOException, InterruptedException {

        return getForecast(
                city,
                ""
        );
    }

    // =====================================================
    // BUILD LOCATION
    // =====================================================

    private static String buildLocation(
            String city,
            String state) {

        String safeCity =
                city == null
                        ? ""
                        : city.trim();

        String safeState =
                state == null
                        ? ""
                        : state.trim();

        // Default fallback if location is not available
        if (safeCity.isEmpty()) {

            System.out.println(
                    "City not found. Using Pune as fallback."
            );

            return "Pune,IN";
        }

        // City + State + India
        if (!safeState.isEmpty()) {

            return safeCity
                    + ","
                    + safeState
                    + ",IN";
        }

        // City + India
        return safeCity + ",IN";
    }

    // =====================================================
    // SEND API REQUEST
    // =====================================================

    private static JsonObject sendRequest(
            String url)
            throws IOException, InterruptedException {

        HttpClient client =
                HttpClient.newHttpClient();

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(url)
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() != 200) {

            System.out.println(
                    "Weather API Response: "
                            + response.body()
            );

            throw new IOException(
                    "Weather API Error: "
                            + response.statusCode()
            );
        }

        return JsonParser
                .parseString(
                        response.body()
                )
                .getAsJsonObject();
    }
}