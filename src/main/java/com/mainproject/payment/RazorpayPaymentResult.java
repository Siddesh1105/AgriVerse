package com.mainproject.payment;

public class RazorpayPaymentResult {

    private final boolean success;
    private final String paymentId;
    private final String razorpayOrderId;
    private final String error;

    private RazorpayPaymentResult(
            boolean success,
            String paymentId,
            String razorpayOrderId,
            String error) {

        this.success = success;
        this.paymentId = paymentId;
        this.razorpayOrderId = razorpayOrderId;
        this.error = error;
    }

    public static RazorpayPaymentResult success(
            String paymentId,
            String orderId) {

        return new RazorpayPaymentResult(
                true,
                paymentId,
                orderId,
                null
        );
    }

    public static RazorpayPaymentResult failure(
            String error) {

        return new RazorpayPaymentResult(
                false,
                null,
                null,
                error
        );
    }

    public boolean isSuccess() {
        return success;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public String getError() {
        return error;
    }
}