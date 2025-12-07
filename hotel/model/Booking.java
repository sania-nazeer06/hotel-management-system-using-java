package com.hotel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {
    private int bookingId;
    private int guestId;
    private int roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int days;
    private double roomPricePerDay;
    private double baseRoomTotal;
    private double roomService;
    private double specialService;
    private double totalAmount;
    private String bookingStatus;  // Active, Completed, Cancelled
    private LocalDateTime createdAt;

    // Constructors
    public Booking() {}

    public Booking(int guestId, int roomId, LocalDate checkIn, LocalDate checkOut,
                   int days, double roomPricePerDay, double baseRoomTotal, double roomService, double specialService, double totalAmount) {
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.days = days;
        this.roomPricePerDay = roomPricePerDay;
        this.baseRoomTotal = baseRoomTotal;
        this.roomService = roomService;
        this.specialService = specialService;
        this.totalAmount = totalAmount;
        this.bookingStatus = "Active";
    }
    public double getRoomPricePerDay() {
        return roomPricePerDay;
    }

    public void setRoomPricePerDay(double roomPricePerDay) {
        this.roomPricePerDay = roomPricePerDay;
    }

    public double getBaseRoomTotal() {
        return baseRoomTotal;
    }

    public void setBaseRoomTotal(double baseRoomTotal) {
        this.baseRoomTotal = baseRoomTotal;
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public double getRoomService() {
        return roomService;
    }

    public void setRoomService(double roomService) {
        this.roomService = roomService;
    }

    public double getSpecialService() {
        return specialService;
    }

    public void setSpecialService(double specialService) {
        this.specialService = specialService;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Booking[ID=%d, Guest=%d, Room=%d, CheckIn=%s, CheckOut=%s, Days=%d, Price/Day=%.2f, BaseTotal=%.2f, RoomService=%.2f, SpecialService=%.2f, Total=%.2f, Status=%s]",
                bookingId, guestId, roomId, checkIn, checkOut, days, roomPricePerDay, baseRoomTotal, roomService, specialService, totalAmount, bookingStatus);
    }
}
