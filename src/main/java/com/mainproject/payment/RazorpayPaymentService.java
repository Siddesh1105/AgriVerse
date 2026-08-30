package com.mainproject.payment;

import com.mainproject.config.RazorpayConfig;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.net.InetSocketAddress;
import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * Handles Razorpay payments for the JavaFX desktop application.
 */
public class RazorpayPaymentService {

    private static final HttpClient CLIENT =
            HttpClient.newHttpClient();

    public CompletableFuture<RazorpayPaymentResult> pay(
            double amount,
            String receipt,
            String description) {

        CompletableFuture<RazorpayPaymentResult> future =
                new CompletableFuture<>();

        Thread worker = new Thread(() -> {

            HttpServer server = null;

            try {

                // ============================================
                // CHECK RAZORPAY CONFIGURATION
                // ============================================

                if (!RazorpayConfig.isConfigured()) {

                    future.complete(
                            RazorpayPaymentResult.failure(
                                    "Razorpay is not configured. "
                                            + "Please set RAZORPAY_KEY_ID "
                                            + "and RAZORPAY_KEY_SECRET."
                            )
                    );

                    return;
                }

                // ============================================
                // CREATE RAZORPAY ORDER
                // ============================================

                JSONObject order =
                        createOrder(amount, receipt);

                String razorpayOrderId =
                        order.getString("id");

                String keyId =
                        RazorpayConfig.getKeyId();

                // ============================================
                // START LOCAL CALLBACK SERVER
                // ============================================

                server = HttpServer.create(
                        new InetSocketAddress(
                                "127.0.0.1",
                                0
                        ),
                        0
                );

                int port =
                        server.getAddress().getPort();

                final HttpServer callbackServer = server;

                String callbackUrl =
                        "http://127.0.0.1:"
                                + port
                                + "/payment-success";

                server.createContext("/payment-cancel", exchange -> {
                    try {
                        sendResponse(exchange, 200, "<html><body style='font-family:Arial;text-align:center;padding:60px'><h2>Payment Cancelled</h2><p>You can close this window and return to AgriLink.</p></body></html>");
                    } finally {
                        future.complete(RazorpayPaymentResult.failure("Payment was cancelled."));
                        callbackServer.stop(0);
                    }
                });

                server.createContext(
                        "/payment-success",
                        exchange -> {

                            try {

                                String query =
                                        exchange.getRequestURI()
                                                .getQuery();

                                String paymentId =
                                        getQueryParameter(
                                                query,
                                                "razorpay_payment_id"
                                        );

                                String returnedOrderId =
                                        getQueryParameter(
                                                query,
                                                "razorpay_order_id"
                                        );

                                String signature =
                                        getQueryParameter(
                                                query,
                                                "razorpay_signature"
                                        );

                                boolean verified =
                                        razorpayOrderId.equals(
                                                returnedOrderId
                                        )
                                                && verifySignature(
                                                returnedOrderId,
                                                paymentId,
                                                signature
                                        );

                                String html;

                                if (verified) {

                                    html = """
                                            <!DOCTYPE html>
                                            <html>
                                            <head>
                                                <title>Payment Successful</title>
                                            </head>
                                            <body style="
                                                font-family: Arial;
                                                text-align: center;
                                                padding: 60px;
                                            ">
                                                <h1 style="color: green;">
                                                    Payment Successful!
                                                </h1>

                                                <p>
                                                    Your payment has been
                                                    verified successfully.
                                                </p>

                                                <p>
                                                    You can now return to
                                                    AgriLink.
                                                </p>
                                            </body>
                                            </html>
                                            """;

                                    future.complete(
                                            RazorpayPaymentResult.success(
                                                    paymentId,
                                                    returnedOrderId
                                            )
                                    );

                                } else {

                                    html = """
                                            <!DOCTYPE html>
                                            <html>
                                            <head>
                                                <title>Payment Failed</title>
                                            </head>
                                            <body style="
                                                font-family: Arial;
                                                text-align: center;
                                                padding: 60px;
                                            ">
                                                <h1 style="color: red;">
                                                    Payment Verification Failed
                                                </h1>

                                                <p>
                                                    Please return to AgriLink.
                                                </p>
                                            </body>
                                            </html>
                                            """;

                                    future.complete(
                                            RazorpayPaymentResult.failure(
                                                    "Payment signature "
                                                            + "verification failed."
                                            )
                                    );
                                }

                                sendResponse(
                                        exchange,
                                        200,
                                        html
                                );

                            } catch (Exception e) {

                                try {

                                    sendResponse(
                                            exchange,
                                            500,
                                            "Payment callback failed: "
                                                    + e.getMessage()
                                    );

                                } catch (Exception ignored) {
                                }

                                future.complete(
                                        RazorpayPaymentResult.failure(
                                                e.getMessage()
                                        )
                                );

                            } finally {

                                callbackServer.stop(0);
                            }
                        }
                );

                server.start();

                // ============================================
                // CREATE CHECKOUT PAGE
                // ============================================

                String checkoutHtml =
                        createCheckoutHtml(
                                keyId,
                                razorpayOrderId,
                                Math.round(amount * 100.0),
                                description,
                                callbackUrl,
                                "http://127.0.0.1:" + port + "/payment-cancel"
                        );

                File page =
                        File.createTempFile(
                                "agrilink-razorpay-",
                                ".html"
                        );

                page.deleteOnExit();

                try (
                        Writer writer =
                                new OutputStreamWriter(
                                        new FileOutputStream(page),
                                        StandardCharsets.UTF_8
                                )
                ) {

                    writer.write(checkoutHtml);
                }

                // ============================================
                // OPEN BROWSER
                // ============================================

                if (!Desktop.isDesktopSupported()) {

                    throw new IOException(
                            "Desktop browser is not supported "
                                    + "on this device."
                    );
                }

                Desktop.getDesktop()
                        .browse(page.toURI());

            } catch (Exception e) {

                if (server != null) {
                    server.stop(0);
                }

                future.complete(
                        RazorpayPaymentResult.failure(
                                e.getMessage()
                        )
                );
            }

        }, "razorpay-payment");

        worker.setDaemon(true);

        worker.start();

        return future;
    }

    // =====================================================
    // CREATE RAZORPAY ORDER
    // =====================================================

    private JSONObject createOrder(
            double amount,
            String receipt)
            throws Exception {

        JSONObject payload =
                new JSONObject();

        long amountInPaise =
                Math.max(
                        100,
                        Math.round(amount * 100.0)
                );

        payload.put(
                "amount",
                amountInPaise
        );

        payload.put(
                "currency",
                "INR"
        );

        payload.put(
                "receipt",
                receipt == null || receipt.isBlank()
                        ? "agrilink_receipt"
                        : receipt
        );

        String credentials =
                RazorpayConfig.getKeyId()
                        + ":"
                        + RazorpayConfig.getKeySecret();

        String auth =
                Base64.getEncoder()
                        .encodeToString(
                                credentials.getBytes(
                                        StandardCharsets.UTF_8
                                )
                        );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        "https://api.razorpay.com/v1/orders"
                                )
                        )
                        .header(
                                "Authorization",
                                "Basic " + auth
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

        HttpResponse<String> response =
                CLIENT.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (
                response.statusCode() < 200
                        || response.statusCode() >= 300
        ) {

            throw new IOException(
                    "Razorpay order creation failed: "
                            + response.body()
            );
        }

        return new JSONObject(
                response.body()
        );
    }

    // =====================================================
    // VERIFY PAYMENT SIGNATURE
    // =====================================================

    private boolean verifySignature(
            String orderId,
            String paymentId,
            String signature)
            throws Exception {

        if (
                orderId == null
                        || paymentId == null
                        || signature == null
        ) {

            return false;
        }

        String data =
                orderId + "|" + paymentId;

        Mac mac =
                Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey =
                new SecretKeySpec(
                        RazorpayConfig
                                .getKeySecret()
                                .getBytes(
                                        StandardCharsets.UTF_8
                                ),
                        "HmacSHA256"
                );

        mac.init(secretKey);

        byte[] digest =
                mac.doFinal(
                        data.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        String expectedSignature =
                toHex(digest);

        return constantTimeEquals(
                expectedSignature,
                signature
        );
    }

    // =====================================================
    // CREATE RAZORPAY CHECKOUT HTML
    // =====================================================

    private String createCheckoutHtml(
            String key,
            String orderId,
            long amount,
            String description,
            String callbackUrl,
            String cancelUrl) {

        String safeDescription =
                JSONObject.quote(
                        description == null
                                ? "AgriLink Payment"
                                : description
                );

        return """
                <!DOCTYPE html>
                <html>

                <head>

                    <meta charset="UTF-8">

                    <title>
                        AgriLink Payment
                    </title>

                    <script src=
                    "https://checkout.razorpay.com/v1/checkout.js">
                    </script>

                </head>

                <body>

                <script>

                const options = {

                    key: %s,

                    amount: %d,

                    currency: "INR",

                    name: "AgriLink",

                    description: %s,

                    order_id: %s,

                    handler: function(response) {

                        const url =
                            "%s"
                            + "?razorpay_payment_id="
                            + encodeURIComponent(
                                response.razorpay_payment_id
                            )
                            + "&razorpay_order_id="
                            + encodeURIComponent(
                                response.razorpay_order_id
                            )
                            + "&razorpay_signature="
                            + encodeURIComponent(
                                response.razorpay_signature
                            );

                        window.location.href = url;
                    },

                    modal: {
                        ondismiss: function() {
                            fetch("%s", { method: "GET" }).catch(() => {});
                            document.body.innerHTML = "<div style='font-family:Arial;text-align:center;padding:60px'><h2>Payment Cancelled</h2><p>You can close this window and return to AgriLink.</p></div>";
                        }
                    }
                };

                const razorpay =
                    new Razorpay(options);

                razorpay.open();

                </script>

                </body>

                </html>
                """.formatted(
                        JSONObject.quote(key),
                        amount,
                        safeDescription,
                        JSONObject.quote(orderId),
                        callbackUrl,
                        cancelUrl
                );
    }

    // =====================================================
    // GET QUERY PARAMETER
    // =====================================================

    private String getQueryParameter(
            String query,
            String key) {

        if (query == null || query.isBlank()) {
            return null;
        }

        String[] parameters =
                query.split("&");

        for (String parameter : parameters) {

            String[] parts =
                    parameter.split("=", 2);

            if (
                    parts.length == 2
                            && parts[0].equals(key)
            ) {

                try {

                    return java.net.URLDecoder.decode(
                            parts[1],
                            StandardCharsets.UTF_8
                    );

                } catch (Exception e) {

                    return parts[1];
                }
            }
        }

        return null;
    }

    // =====================================================
    // SEND RESPONSE
    // =====================================================

    private void sendResponse(
            HttpExchange exchange,
            int status,
            String body)
            throws IOException {

        byte[] bytes =
                body.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/html; charset=UTF-8"
        );

        exchange.sendResponseHeaders(
                status,
                bytes.length
        );

        try (
                OutputStream output =
                        exchange.getResponseBody()
        ) {

            output.write(bytes);
        }
    }

    // =====================================================
    // HEX CONVERSION
    // =====================================================

    private String toHex(byte[] bytes) {

        StringBuilder builder =
                new StringBuilder();

        for (byte b : bytes) {

            builder.append(
                    String.format(
                            "%02x",
                            b
                    )
            );
        }

        return builder.toString();
    }

    // =====================================================
    // CONSTANT TIME COMPARISON
    // =====================================================

    private boolean constantTimeEquals(
            String first,
            String second) {

        if (first == null || second == null) {
            return false;
        }

        int result =
                first.length()
                        ^ second.length();

        int length =
                Math.min(
                        first.length(),
                        second.length()
                );

        for (int i = 0; i < length; i++) {

            result |=
                    first.charAt(i)
                            ^ second.charAt(i);
        }

        return result == 0;
    }
}