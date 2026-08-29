package com.mainproject.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.json.JSONObject;

public class AuthController {

    private static final String API_KEY =
            "AIzaSyA3HO6Q9q5H6CT2LFrazZL28nmfLo8Vd1M";

    private final HttpClient client =
            HttpClient.newBuilder()
                    .connectTimeout(
                            Duration.ofSeconds(15)
                    )
                    .build();

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

            String url =
                    "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="
                            + API_KEY;

            HttpResponse<String> response =
                    sendPostRequest(
                            url,
                            payload
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

            printNetworkError(e);

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

            String url =
                    "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="
                            + API_KEY;

            HttpResponse<String> response =
                    sendPostRequest(
                            url,
                            payload
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
                        "Firebase UID: "
                                + uid
                );

                return uid;
            }

            return null;

        } catch (Exception e) {

            printNetworkError(e);

            return null;
        }
    }

    // =====================================================
    // SIGN IN
    // =====================================================

    public boolean signIn(
            String email,
            String password) {

        System.out.println(
                "Login attempt for: "
                        + email
        );

        String idToken =
                authenticateAndGetToken(
                        email,
                        password
                );

        if (idToken != null) {

            System.out.println(
                    "Firebase Login Successful!"
            );

            return true;
        }

        System.out.println(
                "Firebase Login Failed!"
        );

        return false;
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

            String url =
                    "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                            + API_KEY;

            HttpResponse<String> response =
                    sendPostRequest(
                            url,
                            payload
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

            printNetworkError(e);

            return null;
        }
    }

    // =====================================================
    // CHANGE PASSWORD
    // =====================================================

    public boolean changePassword(
            String email,
            String currentPassword,
            String newPassword) {

        try {

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

            System.out.println(
                    "Verifying current password for: "
                            + email
            );

            String idToken =
                    authenticateAndGetToken(
                            email.trim(),
                            currentPassword
                    );

            if (idToken == null) {

                System.out.println(
                        "Change Password: Current password is incorrect."
                );

                return false;
            }

            System.out.println(
                    "Current password verified successfully."
            );

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

            String url =
                    "https://identitytoolkit.googleapis.com/v1/accounts:update?key="
                            + API_KEY;

            HttpResponse<String> response =
                    sendPostRequest(
                            url,
                            payload
                    );

            System.out.println(
                    "Change Password Status: "
                            + response.statusCode()
            );

            System.out.println(
                    "Change Password Response: "
                            + response.body()
            );

            if (response.statusCode() == 200) {

                System.out.println(
                        "Firebase password changed successfully!"
                );

                return true;
            }

            System.out.println(
                    "Firebase password change failed."
            );

            return false;

        } catch (Exception e) {

            printNetworkError(e);

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

            String url =
                    "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key="
                            + API_KEY;

            HttpResponse<String> response =
                    sendPostRequest(
                            url,
                            payload
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

            printNetworkError(e);

            return false;
        }
    }

    // =====================================================
    // COMMON POST REQUEST
    // =====================================================

    private HttpResponse<String> sendPostRequest(
            String url,
            JSONObject payload)
            throws Exception {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(url)
                        )
                        .timeout(
                                Duration.ofSeconds(20)
                        )
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

        return client.send(
                request,
                HttpResponse.BodyHandlers
                        .ofString()
        );
    }

    // =====================================================
    // NETWORK ERROR
    // =====================================================

    private void printNetworkError(
            Exception e) {

        System.out.println(
                "========================================"
        );

        System.out.println(
                "Firebase Network Error"
        );

        System.out.println(
                "========================================"
        );

        System.out.println(
                "Message: "
                        + e.getMessage()
        );

        System.out.println(
                "Please check:"
        );

        System.out.println(
                "1. Internet connection"
        );

        System.out.println(
                "2. DNS connection"
        );

        System.out.println(
                "3. Firewall / antivirus"
        );

        System.out.println(
                "4. VPN / proxy settings"
        );

        System.out.println(
                "5. Firebase API availability"
        );

        System.out.println(
                "========================================"
        );

        e.printStackTrace();
    }
}