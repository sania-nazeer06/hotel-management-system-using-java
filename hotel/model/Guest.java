package com.hotel.model;

import java.time.LocalDateTime;

public class Guest {
    private int guestId;
    private String name;
    private String phone;
    private String address;
    private String password;
    private LocalDateTime createdAt;

    // Constructors
    public Guest() {}

    public Guest(int guestId, String name, String phone, String address, String password) {
        this.guestId = guestId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    public Guest(String name, String phone, String address, String password) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    // Getters and Setters
    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Guest[ID=%d, Name=%s, Phone=%s, Address=%s]",
                guestId, name, phone, address);
    }
}
