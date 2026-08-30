package com.mainproject.config;

public final class RazorpayConfig {

    private RazorpayConfig() {
    }

    public static String getKeyId() {
        return value(
                "RAZORPAY_KEY_ID",
                "razorpay.key.id"
        );
    }

    public static String getKeySecret() {
        return value(
                "RAZORPAY_KEY_SECRET",
                "razorpay.key.secret"
        );
    }

    public static boolean isConfigured() {

        String id = getKeyId();
        String secret = getKeySecret();

        return id != null
                && !id.isBlank()
                && secret != null
                && !secret.isBlank();
    }

    private static String value(
            String environmentVariable,
            String systemProperty) {

        String value =
                System.getenv(environmentVariable);

        if (value == null || value.isBlank()) {

            value =
                    System.getProperty(systemProperty);
        }

        return value == null
                ? ""
                : value.trim();
    }
}