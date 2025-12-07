package com.hotel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Payment {
    private int paymentId;
    private int bookingId;
    private double amountReceived;
    private double tip;
    private String status;  // Paid, Pending, Refunded
    private LocalDate paymentDate;
    private LocalDateTime createdAt;

    // Constructors
    public Payment() {}

    public Payment(int bookingId, double amountReceived, double tip, String status, LocalDate paymentDate) {
        this.bookingId = bookingId;
        this.amountReceived = amountReceived;
        this.tip = tip;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Payment[ID=%d, Booking=%d, Received=%.2f, Tip=%.2f, Status=%s, Date=%s]",
                paymentId, bookingId, amountReceived, tip, status, paymentDate);
    }
}
