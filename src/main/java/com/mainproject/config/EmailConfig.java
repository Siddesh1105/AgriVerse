package com.mainproject.config;

/** SMTP settings for sending AgriVerse emails.
 * Configure these as environment variables instead of hard-coding secrets.
 */
public final class EmailConfig {
    private EmailConfig() {}

    public static String getSmtpHost() {
        return value("SMTP_HOST", "smtp.gmail.com");
    }

    public static int getSmtpPort() {
        try {
            return Integer.parseInt(value("SMTP_PORT", "587"));
        } catch (NumberFormatException e) {
            return 587;
        }
    }

    public static String getEmail() {
        return System.getenv("SMTP_EMAIL");
    }

    public static String getAppPassword() {
        return System.getenv("SMTP_APP_PASSWORD");
    }

    public static boolean isConfigured() {
        return notBlank(getEmail()) && notBlank(getAppPassword());
    }

    private static String value(String key, String fallback) {
        String value = System.getenv(key);
        return notBlank(value) ? value.trim() : fallback;
    }

    private static boolean notBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
