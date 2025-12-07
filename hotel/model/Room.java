package com.hotel.model;

public class Room {
    private int roomId;
    private String roomType;  // Standard, Premium, Suite
    private double price;
    private String status;    // Available, Booked, Maintenance

    // Constructors
    public Room() {}

    public Room(int roomId, String roomType, double price, String status) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.price = price;
        this.status = status;
    }

    public Room(String roomType, double price, String status) {
        this.roomType = roomType;
        this.price = price;
        this.status = status;
    }

    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Room[ID=%d, Type=%s, Price=%.2f, Status=%s]",
                roomId, roomType, price, status);
    }
}
