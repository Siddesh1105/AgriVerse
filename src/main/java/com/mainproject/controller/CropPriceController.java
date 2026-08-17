package com.mainproject.controller;

import com.mainproject.config.DataGovConfig;
import com.mainproject.model.CropPrice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CropPriceController {

        private final HttpClient client = HttpClient.newHttpClient();

        // =========================================================
        // GET DATA FROM API
        // =========================================================

        private JSONArray getRecords(String state,String district,String market) {
                try {
                        StringBuilder url = new StringBuilder(DataGovConfig.BASE_URL);
                        url.append("?api-key=")
                                .append(
                                URLEncoder.encode(DataGovConfig.API_KEY,StandardCharsets.UTF_8));
                        url.append("&format=json");
                        /*
                         * Use a reasonable limit.
                         * You can increase it up to the API's allowed limit.
                         */
                        url.append("&limit=20000");

                        // =================================================
                        // STATE FILTER
                        // =================================================

                        if (state != null
                                        && !state.trim().isEmpty()) {

                                url.append(
                                                "&filters[state]=");

                                url.append(
                                                URLEncoder.encode(
                                                                state.trim(),
                                                                StandardCharsets.UTF_8));
                        }

                        // =================================================
                        // DISTRICT FILTER
                        // =================================================

                        if (district != null
                                        && !district.trim().isEmpty()) {

                                url.append(
                                                "&filters[district]=");

                                url.append(
                                                URLEncoder.encode(
                                                                district.trim(),
                                                                StandardCharsets.UTF_8));
                        }

                        // =================================================
                        // MARKET FILTER
                        // =================================================

                        if (market != null
                                        && !market.trim().isEmpty()) {

                                url.append(
                                                "&filters[market]=");

                                url.append(
                                                URLEncoder.encode(
                                                                market.trim(),
                                                                StandardCharsets.UTF_8));
                        }

                        System.out.println(
                                        "=================================");

                        System.out.println(
                                        "Crop Price API Request:");

                        System.out.println(
                                        url);

                        System.out.println(
                                        "=================================");

                        HttpRequest request = HttpRequest.newBuilder()
                                        .uri(URI.create(url.toString()))
                                        .GET()
                                        .build();

                        HttpResponse<String> response = client.send(
                                        request,
                                        HttpResponse.BodyHandlers.ofString());

                        System.out.println(
                                        "API Status: "
                                                        + response.statusCode());

                        if (response.statusCode() != 200) {

                                System.out.println(
                                                "API Error:");

                                System.out.println(
                                                response.body());

                                return new JSONArray();
                        }

                        JSONObject json = new JSONObject(
                                        response.body());

                        return json.optJSONArray(
                                        "records");

                } catch (Exception e) {

                        System.out.println(
                                        "Error while calling Crop Price API");

                        e.printStackTrace();

                        return new JSONArray();
                }
        }

        // =========================================================
        // GET STATES
        // =========================================================

        public List<String> getStates() {

                Set<String> uniqueStates = new LinkedHashSet<>();

                JSONArray records = getRecords(
                                null,
                                null,
                                null);

                for (int i = 0; i < records.length(); i++) {

                        JSONObject record = records.getJSONObject(i);

                        String state = record.optString(
                                        "state",
                                        "").trim();

                        if (!state.isEmpty()) {

                                uniqueStates.add(
                                                state);
                        }
                }

                return new ArrayList<>(
                                uniqueStates);
        }

        // =========================================================
        // GET DISTRICTS
        // =========================================================

        public List<String> getDistricts(
                        String state) {

                Set<String> uniqueDistricts = new LinkedHashSet<>();

                JSONArray records = getRecords(
                                state,
                                null,
                                null);

                for (int i = 0; i < records.length(); i++) {

                        JSONObject record = records.getJSONObject(i);

                        String district = record.optString(
                                        "district",
                                        "").trim();

                        if (!district.isEmpty()) {

                                uniqueDistricts.add(
                                                district);
                        }
                }

                return new ArrayList<>(
                                uniqueDistricts);
        }

        // =========================================================
        // GET MARKETS
        // =========================================================

        public List<String> getMarkets(
                        String state,
                        String district) {

                Set<String> uniqueMarkets = new LinkedHashSet<>();

                JSONArray records = getRecords(
                                state,
                                district,
                                null);

                for (int i = 0; i < records.length(); i++) {

                        JSONObject record = records.getJSONObject(i);

                        String market = record.optString(
                                        "market",
                                        "").trim();

                        if (!market.isEmpty()) {

                                uniqueMarkets.add(
                                                market);
                        }
                }

                return new ArrayList<>(
                                uniqueMarkets);
        }

        // =========================================================
        // GET CROP PRICES
        // =========================================================

        public List<CropPrice> getCropPrices(
                        String state,
                        String district,
                        String market) {

                List<CropPrice> prices = new ArrayList<>();

                JSONArray records = getRecords(
                                state,
                                district,
                                market);

                for (int i = 0; i < records.length(); i++) {

                        JSONObject record = records.getJSONObject(i);

                        CropPrice cropPrice = new CropPrice();

                        cropPrice.setState(
                                        record.optString(
                                                        "state",
                                                        ""));

                        cropPrice.setDistrict(
                                        record.optString(
                                                        "district",
                                                        ""));

                        cropPrice.setMarket(
                                        record.optString(
                                                        "market",
                                                        ""));

                        cropPrice.setCommodity(
                                        record.optString(
                                                        "commodity",
                                                        ""));

                        cropPrice.setVariety(
                                        record.optString(
                                                        "variety",
                                                        ""));

                        cropPrice.setArrivalDate(
                                        record.optString(
                                                        "arrival_date",
                                                        ""));

                        cropPrice.setMinPrice(
                                        record.optString(
                                                        "min_price",
                                                        "0"));

                        cropPrice.setMaxPrice(
                                        record.optString(
                                                        "max_price",
                                                        "0"));

                        cropPrice.setModalPrice(
                                        record.optString(
                                                        "modal_price",
                                                        "0"));

                        prices.add(
                                        cropPrice);
                }

                return prices;
        }
}