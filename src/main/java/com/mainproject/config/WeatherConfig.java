package com.mainproject.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherConfig {

    private static final String API_KEY = "ef3882a6d55fef1368a493a94306d8b4";

    public static JsonObject getCurrentWeather()
            throws IOException, InterruptedException {

        String url =
                "https://api.openweathermap.org/data/2.5/weather"
                        + "?lat=18.5204"
                        + "&lon=73.8567"
                        + "&appid=" + API_KEY
                        + "&units=metric";

        return sendRequest(url);
    }

    public static JsonObject getForecast()
            throws IOException, InterruptedException {

        String url =
                "https://api.openweathermap.org/data/2.5/forecast"
                        + "?lat=18.5204"
                        + "&lon=73.8567"
                        + "&appid=" + API_KEY
                        + "&units=metric";

        return sendRequest(url);
    }

    private static JsonObject sendRequest(String url)
            throws IOException, InterruptedException {

        HttpClient client =
                HttpClient.newHttpClient();

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Weather API Error: "
                            + response.statusCode()
            );
        }

        return JsonParser
                .parseString(response.body())
                .getAsJsonObject();
    }
}