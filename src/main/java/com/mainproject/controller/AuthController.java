package com.mainproject.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class AuthController {

    private final String API_KEY =
            "AIzaSyA3HO6Q9q5H6CT2LFrazZL28nmfLo8Vd1M";

    private final HttpClient client =
            HttpClient.newHttpClient();

    // =====================================================
    // SIGN UP
    // =====================================================

    public boolean signUp(
            String email,
            String password) {

        JSONObject payload =
                new JSONObject()
                        .put("email", email)
                        .put("password", password)
                        .put("returnSecureToken", true);

        try {

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
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    payload.toString()
                                            )
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Sign Up Status: "
                            + response.statusCode()
            );

            System.out.println(
                    "Sign Up Response: "
                            + response.body()
            );

            return response.statusCode() == 200;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // SIGN UP AND GET FIREBASE UID
    // =====================================================

    public String signUpAndGetUid(
            String email,
            String password) {

        JSONObject payload =
                new JSONObject()
                        .put("email", email)
                        .put("password", password)
                        .put("returnSecureToken", true);

        try {

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
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    payload.toString()
                                            )
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Sign Up Status: "
                            + response.statusCode()
            );

            System.out.println(
                    "Sign Up Response: "
                            + response.body()
            );

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
                        "Firebase UID: " + uid
                );

                return uid;
            }

            return null;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // SIGN IN
    // =====================================================

    public boolean signIn(
            String email,
            String password) {

        JSONObject payload =
                new JSONObject()
                        .put("email", email)
                        .put("password", password)
                        .put("returnSecureToken", true);

        try {

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
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    payload.toString()
                                            )
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Login Status: "
                            + response.statusCode()
            );

            System.out.println(
                    "Login Response: "
                            + response.body()
            );

            if (response.statusCode() == 200) {

                JSONObject json =
                        new JSONObject(
                                response.body()
                        );

                if (json.has("localId")) {

                    System.out.println(
                            "Firebase UID: "
                                    + json.getString("localId")
                    );
                }

                System.out.println(
                        "Firebase Login Successful!"
                );

                return true;
            }

            System.out.println(
                    "Firebase Login Failed!"
            );

            return false;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // RESET PASSWORD
    // =====================================================

    public boolean resetPassword(
            String email) {

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
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    payload.toString()
                                            )
                            )
                            .build();

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