package com.mainproject.controller;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
public class AuthController {
    private String API_KEY="AIzaSyA3HO6Q9q5H6CT2LFrazZL28nmfLo8Vd1M";
    public boolean signUp(String email,String password){
        JSONObject payload=new JSONObject()
        .put("email",email)
        .put("password",password)
        .put("returnSecureToken",true);
        try{
            HttpClient client=HttpClient.newHttpClient();
            URI uri=URI.create(
                "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="+ API_KEY
            );
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type","application/json")
                .POST(
                    HttpRequest.BodyPublishers.ofString(
                        payload.toString()
                    )
                ).build();
                System.out.println(request);
                HttpResponse<String> response=
                   client.send(
                    request,HttpResponse.BodyHandlers.ofString()
                   );
                System.out.println(response);
                System.out.println(response.statusCode());
                System.out.println(response.body());
                if (response.statusCode()==200)
                    return true;
                else
                    return false;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
         public boolean signIn(String email, String password) {

        JSONObject payload = new JSONObject()
                .put("email", email)
                .put("password", password)
                .put("returnSecureToken", true);

        try {

            HttpClient client = HttpClient.newHttpClient();

            URI uri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="+ API_KEY
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
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

            System.out.println(response);
            System.out.println(response.statusCode());
            System.out.println(response.body());

            if (response.statusCode() == 200)
                return true;
            else
                return false;

        } catch (Exception e) {

            e.printStackTrace();
            
        }
        return false;
    }
    public String signUpAndGetUid(String email, String password) {

    JSONObject payload = new JSONObject()
            .put("email", email)
            .put("password", password)
            .put("returnSecureToken", true);

    try {

        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(
                "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="
                        + API_KEY
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(
                        HttpRequest.BodyPublishers.ofString(
                                payload.toString()
                        )
                )
                .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        System.out.println(response.body());

        if (response.statusCode() == 200) {

            JSONObject responseJson =
                    new JSONObject(response.body());

            return responseJson.getString("localId");
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    return null;
}
public boolean resetPassword(String email) {

    JSONObject payload = new JSONObject()
            .put("requestType", "PASSWORD_RESET")
            .put("email", email);

    try {

        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(
                "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key="
                        + API_KEY
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(
                        HttpRequest.BodyPublishers.ofString(
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
