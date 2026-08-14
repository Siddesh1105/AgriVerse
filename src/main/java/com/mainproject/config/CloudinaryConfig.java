package com.mainproject.config;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryConfig {

    private static final String CLOUD_NAME =
            "nwuttp3z";

    private static final String API_KEY =
            "877153129183626";

    private static final String API_SECRET =
            "cP_YITZUhrIa_uM7jXuYWliThUM";

    private static Cloudinary cloudinary;

    private CloudinaryConfig() {
    }

    public static Cloudinary getCloudinary() {

        if (cloudinary == null) {

            Map<String, String> config =
                    new HashMap<>();

            config.put(
                    "cloud_name",
                    CLOUD_NAME
            );

            config.put(
                    "api_key",
                    API_KEY
            );

            config.put(
                    "api_secret",
                    API_SECRET
            );

            cloudinary =
                    new Cloudinary(config);
        }

        return cloudinary;
    }
}