package com.mainproject.controller;

import com.mainproject.email.InvoiceEmailService;
import com.mainproject.model.Order;
import com.mainproject.model.ProductOrder;
import com.mainproject.model.EquipmentRental;

import java.util.concurrent.CompletableFuture;

/** Controller for buyer invoice email delivery. */
public class InvoiceController {
    private final InvoiceEmailService invoiceEmailService = new InvoiceEmailService();

    public void sendOrderInvoiceAsync(Order order) {
        CompletableFuture.runAsync(() -> invoiceEmailService.sendOrderInvoice(order));
    }

    public void sendProductOrderInvoiceAsync(ProductOrder order) {
        CompletableFuture.runAsync(() -> invoiceEmailService.sendProductOrderInvoice(order));
    }
    public void sendRentalInvoiceAsync(EquipmentRental rental) {
        CompletableFuture.runAsync(() -> invoiceEmailService.sendRentalInvoice(rental));
    }
}

