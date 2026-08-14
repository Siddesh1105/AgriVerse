package com.mainproject.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class AuthController {

    private String API_KEY =
            "AIzaSyA3HO6Q9q5H6CT2LFrazZL28nmfLo8Vd1M";

    // =========================================================
    // SIGN UP
    // =========================================================

    public boolean signUp(
            String email,
            String password) {

        JSONObject payload =
                new JSONObject()
                        .put("email", email)
                        .put("password", password)
                        .put("returnSecureToken", true);

        try {

            HttpClient client =
                    HttpClient.newHttpClient();

            URI uri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="
                            + API_KEY
            );

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            payload.toString()
                                    )
                            )
                            .build();

            System.out.println(request);

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Status Code: "
                            + response.statusCode()
            );

            System.out.println(
                    "Response: "
                            + response.body()
            );

            return response.statusCode() == 200;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // SIGN IN
    // =========================================================
    //
    // Returns Firebase UID if login is successful.
    // Returns null if login fails.
    //
    // =========================================================

    public String signIn(
            String email,
            String password) {

        JSONObject payload =
                new JSONObject()
                        .put("email", email)
                        .put("password", password)
                        .put("returnSecureToken", true);

        try {

            HttpClient client =
                    HttpClient.newHttpClient();

            URI uri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                            + API_KEY
            );

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            payload.toString()
                                    )
                            )
                            .build();

            System.out.println(
                    "Login Request: "
                            + request
            );

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Login Status Code: "
                            + response.statusCode()
            );

            System.out.println(
                    "Login Response: "
                            + response.body()
            );

            // =================================================
            // LOGIN SUCCESS
            // =================================================

            if (response.statusCode() == 200) {

                JSONObject responseJson =
                        new JSONObject(
                                response.body()
                        );

                String uid =
                        responseJson.getString(
                                "localId"
                        );

                System.out.println(
                        "Firebase UID: "
                                + uid
                );

                return uid;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        // Login failed
        return null;
    }

    // =========================================================
    // SIGN UP AND GET UID
    // =========================================================

    public String signUpAndGetUid(
            String email,
            String password) {

        JSONObject payload =
                new JSONObject()
                        .put("email", email)
                        .put("password", password)
                        .put("returnSecureToken", true);

        try {

            HttpClient client =
                    HttpClient.newHttpClient();

            URI uri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="
                            + API_KEY
            );

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            payload.toString()
                                    )
                            )
                            .build();

            System.out.println(
                    "Registration Request: "
                            + request
            );

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Registration Status Code: "
                            + response.statusCode()
            );

            System.out.println(
                    "Registration Response: "
                            + response.body()
            );

            // =================================================
            // REGISTRATION SUCCESS
            // =================================================

            if (response.statusCode() == 200) {

                JSONObject responseJson =
                        new JSONObject(
                                response.body()
                        );

                String uid =
                        responseJson.getString(
                                "localId"
                        );

                System.out.println(
                        "Firebase UID: "
                                + uid
                );

                return uid;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // RESET PASSWORD
    // =========================================================

    public boolean resetPassword( String email) {

        JSONObject payload =
                new JSONObject()
                        .put(
                                "requestType",
                                "PASSWORD_RESET"
                        )
                        .put(
                                "email",
                                email
                        );

        try {

            HttpClient client =
                    HttpClient.newHttpClient();

            URI uri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key="
                            + API_KEY
            );

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            payload.toString()
                                    )
                            )
                            .build();

            System.out.println(
                    "Password Reset Request: "
                            + request
            );

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Password Reset Status: "
                            + response.statusCode()
            );

            System.out.println(
                    "Password Reset Response: "
                            + response.body()
            );

            return response.statusCode() == 200;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}