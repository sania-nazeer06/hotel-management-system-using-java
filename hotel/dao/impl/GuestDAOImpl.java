package com.hotel.dao.impl;

import com.hotel.dao.DBConnection;
import com.hotel.dao.GuestDAO;
import com.hotel.model.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAOImpl implements GuestDAO {

    @Override
    public int addGuest(Guest guest) throws SQLException {
        String sql = "INSERT INTO guests (name, phone, address, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, guest.getName());
            pstmt.setString(2, guest.getPhone());
            pstmt.setString(3, guest.getAddress());
            pstmt.setString(4, guest.getPassword());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public Guest getGuestById(int guestId) throws SQLException {
        String sql = "SELECT * FROM guests WHERE guest_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, guestId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractGuestFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public Guest getGuestByPhone(String phone) throws SQLException {
        String sql = "SELECT * FROM guests WHERE phone = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractGuestFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public List<Guest> getAllGuests() throws SQLException {
        String sql = "SELECT * FROM guests ORDER BY guest_id";
        List<Guest> guests = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                guests.add(extractGuestFromResultSet(rs));
            }
        }
        return guests;
    }

    @Override
    public void updateGuest(Guest guest) throws SQLException {
        String sql = "UPDATE guests SET name = ?, phone = ?, address = ?, password = ? WHERE guest_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, guest.getName());
            pstmt.setString(2, guest.getPhone());
            pstmt.setString(3, guest.getAddress());
            pstmt.setString(4, guest.getPassword());
            pstmt.setInt(5, guest.getGuestId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteGuest(int guestId) throws SQLException {
        String sql = "DELETE FROM guests WHERE guest_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, guestId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public boolean validateLogin(String phone, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM guests WHERE phone = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private Guest extractGuestFromResultSet(ResultSet rs) throws SQLException {
        Guest guest = new Guest(
            rs.getInt("guest_id"),
            rs.getString("name"),
            rs.getString("phone"),
            rs.getString("address"),
            rs.getString("password")
        );
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            guest.setCreatedAt(createdAt.toLocalDateTime());
        }
        return guest;
    }
}
