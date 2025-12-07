package com.hotel.dao.impl;

import com.hotel.dao.DBConnection;
import com.hotel.dao.RoomDAO;
import com.hotel.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    @Override
    public int addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (room_type, price, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, room.getRoomType());
            pstmt.setDouble(2, room.getPrice());
            pstmt.setString(3, room.getStatus());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public Room getRoomById(int roomId) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractRoomFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public List<Room> getAllRooms() throws SQLException {
        String sql = "SELECT * FROM rooms ORDER BY room_id";
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(extractRoomFromResultSet(rs));
            }
        }
        return rooms;
    }

    @Override
    public List<Room> getAvailableRooms() throws SQLException {
        String sql = "SELECT * FROM rooms WHERE status = 'Available' ORDER BY room_type, price";
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(extractRoomFromResultSet(rs));
            }
        }
        return rooms;
    }

    @Override
    public List<Room> getRoomsByType(String roomType) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE room_type = ? ORDER BY room_id";
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomType);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rooms.add(extractRoomFromResultSet(rs));
            }
        }
        return rooms;
    }

    @Override
    public void updateRoom(Room room) throws SQLException {
        String sql = "UPDATE rooms SET room_type = ?, price = ?, status = ? WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getRoomType());
            pstmt.setDouble(2, room.getPrice());
            pstmt.setString(3, room.getStatus());
            pstmt.setInt(4, room.getRoomId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateRoomStatus(int roomId, String status) throws SQLException {
        String sql = "UPDATE rooms SET status = ? WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, roomId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteRoom(int roomId) throws SQLException {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            pstmt.executeUpdate();
        }
    }

    private Room extractRoomFromResultSet(ResultSet rs) throws SQLException {
        return new Room(
            rs.getInt("room_id"),
            rs.getString("room_type"),
            rs.getDouble("price"),
            rs.getString("status")
        );
    }
}
