package com.mainproject.util;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;

/**
 * Global English/Marathi language manager.
 * UI text is translated when a screen is built; user-entered/database data is not translated.
 */
public final class LanguageManager {

    private static final Preferences PREFS =
            Preferences.userNodeForPackage(LanguageManager.class);

    private static final String LANGUAGE_KEY = "language";
    private static String language = PREFS.get(LANGUAGE_KEY, "English");

    private static final Map<String, String> MR = new HashMap<>();

    static {
        put("Dashboard", "डॅशबोर्ड");
        put("Products", "उत्पादने");
        put("My Products", "माझी उत्पादने");
        put("Add Product", "उत्पादन जोडा");
        put("Orders", "ऑर्डर्स");
        put("My Orders", "माझे ऑर्डर्स");
        put("Marketplace", "मार्केटप्लेस");
        put("Crop Prices", "पिकांचे दर");
        put("Equipment Rental", "उपकरणे भाड्याने");
        put("My Equipment", "माझी उपकरणे");
        put("Add Equipment", "उपकरण जोडा");
        put("My Cart", "माझी कार्ट");
        put("Cart", "कार्ट");
        put("Weather", "हवामान");
        put("AI Recommendations", "AI शिफारसी");
        put("Notifications", "सूचना");
        put("Profile", "प्रोफाइल");
        put("Settings", "सेटिंग्ज");
        put("Logout", "लॉगआउट");
        put("Login", "लॉगिन");
        put("Register", "नोंदणी");
        put("Email", "ईमेल");
        put("Password", "पासवर्ड");
        put("Phone Number", "फोन नंबर");
        put("Language", "भाषा");
        put("English", "इंग्रजी");
        put("Marathi", "मराठी");
        put("Hindi", "हिंदी");
        put("Save", "जतन करा");
        put("Cancel", "रद्द करा");
        put("Delete", "हटवा");
        put("Edit", "संपादित करा");
        put("Update", "अपडेट करा");
        put("Search", "शोधा");
        put("Refresh", "रिफ्रेश");
        put("Back", "मागे");
        put("View All", "सर्व पहा");
        put("Continue Shopping", "खरेदी सुरू ठेवा");
        put("Checkout", "चेकआउट");
        put("Account", "खाते");
        put("Password", "पासवर्ड");
        put("Payment Methods", "पेमेंट पद्धती");
        put("Privacy", "गोपनीयता");
        put("Theme", "थीम");
        put("Good Morning", "शुभ सकाळ");
        put("Good Afternoon", "शुभ दुपार");
        put("Good Evening", "शुभ संध्याकाळ");
        put("Welcome back to AgriLink", "AgriLink मध्ये पुन्हा स्वागत आहे");
        put("Total Products", "एकूण उत्पादने");
        put("Active Products", "सक्रिय उत्पादने");
        put("Pending Orders", "प्रलंबित ऑर्डर्स");
        put("Completed Orders", "पूर्ण ऑर्डर्स");
        put("Total Orders", "एकूण ऑर्डर्स");
        put("Cart Items", "कार्टमधील वस्तू");
        put("Recent Orders", "अलीकडील ऑर्डर्स");
        put("Recommended for You", "तुमच्यासाठी शिफारस");
        put("Live Farmers", "लाइव्ह शेतकरी");
        put("Live Now", "आत्ता लाइव्ह");
        put("Wishlist", "विशलिस्ट");
        put("Farmers", "शेतकरी");
        put("Search & Rent", "शोधा आणि भाड्याने घ्या");
        put("Messages", "संदेश");
        put("Voice Assistant", "व्हॉइस असिस्टंट");
        put("AI Smart Recommendations", "AI स्मार्ट शिफारसी");
        put("Try Smart Assistant", "स्मार्ट असिस्टंट वापरा");
        put("Settings & Preferences ⚙️", "सेटिंग्ज आणि प्राधान्ये ⚙️");
        put("👤 Account Settings (Name, Email, Password)", "👤 खाते सेटिंग्ज (नाव, ईमेल, पासवर्ड)");
        put("📍 Saved Delivery Addresses", "📍 जतन केलेले डिलिव्हरी पत्ते");
        put("💳 Payment Methods & UPI", "💳 पेमेंट पद्धती आणि UPI");
        put("🔔 Notification Preferences", "🔔 सूचना प्राधान्ये");
        put("🔒 Privacy & Security Settings", "🔒 गोपनीयता आणि सुरक्षा सेटिंग्ज");
        put("🌐 App Language (English / मराठी)", "🌐 अॅप भाषा (इंग्रजी / मराठी)");
        put("❓ Help & Support", "❓ मदत आणि समर्थन");
        put("Account Settings", "खाते सेटिंग्ज");
        put("Manage your account and preferences.", "तुमचे खाते आणि प्राधान्ये व्यवस्थापित करा.");
        put("Choose your preferred application language.", "तुमची आवडती अॅप भाषा निवडा.");
        put("Application Language", "अॅप भाषा");
        put("Good Morning, Buyer 👋", "शुभ सकाळ, खरेदीदार 👋");
        put("Buyer Dashboard", "खरेदीदार डॅशबोर्ड");
        put("Farmer Dashboard", "शेतकरी डॅशबोर्ड");
        put("Sell your products", "तुमची उत्पादने विक्री करा");
        put("Rent or list equipment", "उपकरणे भाड्याने द्या किंवा सूचीबद्ध करा");
        put("Check latest prices", "नवीनतम दर तपासा");
        put("Current weather info", "सध्याची हवामान माहिती");
        put("Smart farm insights", "स्मार्ट शेती माहिती");
        put("View all alerts", "सर्व सूचना पहा");
        put("All", "सर्व");
        put("Vegetables", "भाज्या");
        put("Fruits", "फळे");
        put("Grains", "धान्य");
        put("Others", "इतर");
        put("Status", "स्थिती");
        put("Price", "किंमत");
        put("Stock", "साठा");
        put("Category", "श्रेणी");
        put("Description", "वर्णन");
        put("Harvest Date", "कापणीची तारीख");
        put("Unit", "एकक");
        put("Variety", "वाण");
        put("Name", "नाव");
        put("Phone", "फोन");
        put("Gender", "लिंग");
        put("Notifications", "सूचना");
        put("Mark All as Read", "सर्व वाचलेले म्हणून चिन्हांकित करा");
        put("No notifications found.", "कोणत्याही सूचना आढळल्या नाहीत.");
        put("No products found.", "कोणतीही उत्पादने आढळली नाहीत.");
        put("Add New Product", "नवीन उत्पादन जोडा");
        put("Add New Equipment", "नवीन उपकरण जोडा");
        put("Save Product", "उत्पादन जतन करा");
        put("Save Equipment", "उपकरण जतन करा");
        put("Are you sure?", "तुम्हाला खात्री आहे का?");
        put("Yes", "होय");
        put("No", "नाही");
        put("Confirm", "पुष्टी करा");
    }

    private LanguageManager() { }

    private static void put(String english, String marathi) {
        MR.put(english, marathi);
    }

    public static String getLanguage() {
        return language;
    }

    public static boolean isMarathi() {
        return "Marathi".equalsIgnoreCase(language);
    }

    public static void setLanguage(String selected) {
        if (selected == null) return;
        language = selected.equalsIgnoreCase("Marathi") || selected.equals("मराठी")
                ? "Marathi" : "English";
        PREFS.put(LANGUAGE_KEY, language);
    }

    public static String t(String english) {
        if (english == null || !isMarathi()) return english;
        return MR.getOrDefault(english, english);
    }

    /** Translate static UI text recursively. Database/user data remains untouched. */
    public static void apply(Node node) {
        if (node == null) return;

        if (node instanceof Label label) {
            label.setText(t(label.getText()));
        }
        if (node instanceof ButtonBase button) {
            button.setText(t(button.getText()));
        }
        if (node instanceof TitledPane titledPane) {
            titledPane.setText(t(titledPane.getText()));
        }
        if (node instanceof TextInputControl input) {
            input.setPromptText(t(input.getPromptText()));
        }
        if (node instanceof Control control && control.getTooltip() != null) {
            Tooltip tooltip = control.getTooltip();
            tooltip.setText(t(tooltip.getText()));
        }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                apply(child);
            }
        }
    }
}
