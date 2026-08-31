package com.mainproject.email;

import com.mainproject.config.EmailConfig;
import com.mainproject.model.Order;
import com.mainproject.model.OrderItem;
import com.mainproject.model.ProductOrder;
import com.mainproject.model.EquipmentRental;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/** Sends payment invoices to buyers. */
public class InvoiceEmailService {

    public boolean sendOrderInvoice(Order order) {
        if (order == null || blank(order.getBuyerEmail())) return false;
        return send(order.getBuyerEmail(), "AgriVerse Invoice - Order " + safe(order.getOrderId()),
                buildOrderHtml(order));
    }

    public boolean sendProductOrderInvoice(ProductOrder order) {
        if (order == null || blank(order.getBuyerEmail())) return false;
        return send(order.getBuyerEmail(), "AgriVerse Invoice - Order " + safe(order.getOrderId()),
                buildProductOrderHtml(order));
    }

    public boolean sendRentalInvoice(EquipmentRental rental) {
        if (rental == null || blank(rental.getBuyerEmail())) return false;
        String rows = "<tr><td>" + escape(safe(rental.getEquipmentName())) + "</td><td>"
                + rental.getNumberOfDays() + " day(s)</td><td>₹" + money(rental.getPricePerDay())
                + " / day</td><td>₹" + money(rental.getTotalAmount()) + "</td></tr>";
        String html = shell(rental.getBuyerName(), rental.getRentalId(),
                formatDate(rental.getPaymentDate() != null ? rental.getPaymentDate() : new Date()),
                rows, rental.getTotalAmount(), 0, rental.getTotalAmount(),
                rental.getPaymentMethod(), rental.getPaymentId())
                .replace("Product</th>", "Equipment</th>");
        return send(rental.getBuyerEmail(), "AgriVerse Equipment Rental Invoice - " + safe(rental.getRentalId()), html);
    }

    private boolean send(String recipient, String subject, String html) {
        if (!EmailConfig.isConfigured()) {
            System.out.println("Invoice email not sent: SMTP_EMAIL or SMTP_APP_PASSWORD is not configured.");
            return false;
        }
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", EmailConfig.getSmtpHost());
            properties.put("mail.smtp.port", String.valueOf(EmailConfig.getSmtpPort()));

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EmailConfig.getEmail(), EmailConfig.getAppPassword());
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EmailConfig.getEmail(), "AgriVerse"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient.trim()));
            message.setSubject(subject, "UTF-8");
            message.setContent(html, "text/html; charset=UTF-8");
            Transport.send(message);
            System.out.println("Invoice email sent successfully to: " + recipient);
            return true;
        } catch (Exception e) {
            System.out.println("Error sending invoice email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private String buildOrderHtml(Order order) {
        StringBuilder rows = new StringBuilder();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                if (item == null) continue;
                rows.append("<tr><td>").append(escape(safe(item.getProductName())))
                    .append("</td><td>").append(item.getQuantity()).append(" ")
                    .append(escape(safe(item.getUnit())))
                    .append("</td><td>₹").append(money(item.getPrice()))
                    .append("</td><td>₹").append(money(item.getTotalPrice()))
                    .append("</td></tr>");
            }
        }
        return shell(order.getBuyerName(), order.getOrderId(), formatDate(order.getPaymentDate() != null ? order.getPaymentDate() : new Date()),
                rows.toString(), order.getSubtotal(), order.getDeliveryCharge(), order.getTotalAmount(), order.getPaymentMethod(), order.getPaymentId());
    }

    private String buildProductOrderHtml(ProductOrder order) {
        String rows = "<tr><td>" + escape(safe(order.getProductName())) + "</td><td>" + order.getQuantity() + " "
                + escape(safe(order.getUnit())) + "</td><td>₹" + money(order.getPricePerUnit()) + "</td><td>₹"
                + money(order.getTotalAmount()) + "</td></tr>";
        return shell(order.getBuyerName(), order.getOrderId(), formatDate(order.getPaymentDate() != null ? order.getPaymentDate() : new Date()),
                rows, order.getTotalAmount(), 0, order.getTotalAmount(), order.getPaymentMethod(), order.getPaymentId());
    }

    private String shell(String buyerName, String orderId, String date, String rows, double subtotal, double delivery, double total, String method, String paymentId) {
        return "<html><body style='font-family:Arial,sans-serif;background:#f5f7f6;padding:24px;'>"
                + "<div style='max-width:760px;margin:auto;background:white;padding:28px;border-radius:12px;'>"
                + "<h1 style='color:#117864;'>AgriLink</h1><h2>Payment Invoice</h2>"
                + "<p>Hello " + escape(safe(buyerName)) + ",</p><p>Thank you for your payment. Your order invoice is below.</p>"
                + "<p><b>Order ID:</b> " + escape(safe(orderId)) + "<br><b>Payment Date:</b> " + escape(date) + "<br><b>Payment Method:</b> " + escape(safe(method))
                + "<br><b>Payment ID:</b> " + escape(safe(paymentId)) + "</p>"
                + "<table style='width:100%;border-collapse:collapse;' border='1' cellpadding='10'><tr><th>Product</th><th>Quantity</th><th>Price</th><th>Total</th></tr>"
                + rows + "</table>"
                + "<div style='margin-top:18px;text-align:right;'><p>Subtotal: ₹" + money(subtotal) + "</p><p>Delivery: ₹" + money(delivery)
                + "</p><h2 style='color:#117864;'>Grand Total: ₹" + money(total) + "</h2></div>"
                + "<p style='color:#666;'>This is a system-generated invoice from AgriVerse.</p></div></body></html>";
    }

    private String formatDate(Date date) { return new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(date); }
    private String money(double value) { return String.format("%.2f", value); }
    private String safe(String value) { return blank(value) ? "-" : value.trim(); }
    private boolean blank(String value) { return value == null || value.trim().isEmpty(); }
    private String escape(String value) { return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"); }
}
