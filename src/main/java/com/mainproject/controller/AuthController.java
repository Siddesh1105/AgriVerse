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

        String idToken =
                authenticateAndGetToken(
                        email,
                        password
                );

        return idToken != null;
    }

    // =====================================================
    // AUTHENTICATE AND GET ID TOKEN
    // =====================================================

    private String authenticateAndGetToken(
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
                    "Authentication Status: "
                            + response.statusCode()
            );

            System.out.println(
                    "Authentication Response: "
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
                                    + json.getString(
                                            "localId"
                                    )
                    );
                }

                if (json.has("idToken")) {

                    return json.getString(
                            "idToken"
                    );
                }
            }

            return null;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // CHANGE PASSWORD
    // =====================================================
    //
    // 1. Verify current password
    // 2. Get Firebase ID token
    // 3. Update password using Firebase
    //
    // =====================================================

    public boolean changePassword(
            String email,
            String currentPassword,
            String newPassword) {

        try {

            // -------------------------------------------------
            // VALIDATION
            // -------------------------------------------------

            if (email == null
                    || email.trim().isEmpty()
                    || currentPassword == null
                    || currentPassword.isEmpty()
                    || newPassword == null
                    || newPassword.isEmpty()) {

                System.out.println(
                        "Change Password: Missing information."
                );

                return false;
            }

            // -------------------------------------------------
            // VERIFY CURRENT PASSWORD
            // -------------------------------------------------

            System.out.println(
                    "Verifying current password for: "
                            + email
            );

            String idToken =
                    authenticateAndGetToken(
                            email.trim(),
                            currentPassword
                    );

            // Current password is incorrect
            if (idToken == null) {

                System.out.println(
                        "Change Password: Current password is incorrect."
                );

                return false;
            }

            System.out.println(
                    "Current password verified successfully."
            );

            // -------------------------------------------------
            // UPDATE PASSWORD IN FIREBASE
            // -------------------------------------------------

            JSONObject payload =
                    new JSONObject()
                            .put(
                                    "idToken",
                                    idToken
                            )
                            .put(
                                    "password",
                                    newPassword
                            )
                            .put(
                                    "returnSecureToken",
                                    true
                            );

            URI uri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:update?key="
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
                    "Change Password Status: "
                            + response.statusCode()
            );

            System.out.println(
                    "Change Password Response: "
                            + response.body()
            );

            // -------------------------------------------------
            // SUCCESS
            // -------------------------------------------------

            if (response.statusCode() == 200) {

                System.out.println(
                        "Firebase password changed successfully!"
                );

                return true;
            }

            // -------------------------------------------------
            // FAILED
            // -------------------------------------------------

            System.out.println(
                    "Firebase password change failed."
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