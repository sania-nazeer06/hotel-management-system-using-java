package com.hotel.dao.impl;

import com.hotel.dao.BookingDAO;
import com.hotel.dao.DBConnection;
import com.hotel.model.Booking;
import com.hotel.dao.impl.RoomDAOImpl;
import com.hotel.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public int addBooking(Booking booking) throws SQLException {
        // Auto-populate room_price_per_day and base_room_total if not set
        double roomPricePerDay = booking.getRoomPricePerDay();
        double baseRoomTotal = booking.getBaseRoomTotal();
        if (roomPricePerDay == 0.0 || baseRoomTotal == 0.0) {
            RoomDAOImpl roomDAO = new RoomDAOImpl();
            Room room = roomDAO.getRoomById(booking.getRoomId());
            if (room != null) {
                roomPricePerDay = room.getPrice();
                baseRoomTotal = roomPricePerDay * booking.getDays();
                booking.setRoomPricePerDay(roomPricePerDay);
                booking.setBaseRoomTotal(baseRoomTotal);
            }
        }
        String sql = "INSERT INTO bookings (guest_id, room_id, check_in, check_out, days, room_price_per_day, base_room_total, room_service, special_service, total_amount, booking_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, booking.getGuestId());
            pstmt.setInt(2, booking.getRoomId());
            pstmt.setDate(3, Date.valueOf(booking.getCheckIn()));
            pstmt.setDate(4, Date.valueOf(booking.getCheckOut()));
            pstmt.setInt(5, booking.getDays());
            pstmt.setDouble(6, booking.getRoomPricePerDay());
            pstmt.setDouble(7, booking.getBaseRoomTotal());
            pstmt.setDouble(8, booking.getRoomService());
            pstmt.setDouble(9, booking.getSpecialService());
            pstmt.setDouble(10, booking.getTotalAmount());
            pstmt.setString(11, booking.getBookingStatus());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public Booking getBookingById(int bookingId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractBookingFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public List<Booking> getAllBookings() throws SQLException {
        String sql = "SELECT * FROM bookings ORDER BY booking_id DESC";
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByGuest(int guestId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE guest_id = ? ORDER BY booking_id DESC";
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, guestId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        }
        return bookings;
    }

    @Override
    public List<Booking> getActiveBookings() throws SQLException {
        String sql = "SELECT * FROM bookings WHERE booking_status = 'Active' ORDER BY check_in";
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        }
        return bookings;
    }

    @Override
    public void updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE bookings SET guest_id = ?, room_id = ?, check_in = ?, check_out = ?, days = ?, room_price_per_day = ?, base_room_total = ?, room_service = ?, special_service = ?, total_amount = ?, booking_status = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, booking.getGuestId());
            pstmt.setInt(2, booking.getRoomId());
            pstmt.setDate(3, Date.valueOf(booking.getCheckIn()));
            pstmt.setDate(4, Date.valueOf(booking.getCheckOut()));
            pstmt.setInt(5, booking.getDays());
            pstmt.setDouble(6, booking.getRoomPricePerDay());
            pstmt.setDouble(7, booking.getBaseRoomTotal());
            pstmt.setDouble(8, booking.getRoomService());
            pstmt.setDouble(9, booking.getSpecialService());
            pstmt.setDouble(10, booking.getTotalAmount());
            pstmt.setString(11, booking.getBookingStatus());
            pstmt.setInt(12, booking.getBookingId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateBookingStatus(int bookingId, String status) throws SQLException {
        String sql = "UPDATE bookings SET booking_status = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            pstmt.executeUpdate();
        }
    }

    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking(
            rs.getInt("guest_id"),
            rs.getInt("room_id"),
            rs.getDate("check_in").toLocalDate(),
            rs.getDate("check_out").toLocalDate(),
            rs.getInt("days"),
            rs.getDouble("room_price_per_day"),
            rs.getDouble("base_room_total"),
            rs.getDouble("room_service"),
            rs.getDouble("special_service"),
            rs.getDouble("total_amount")
        );
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setBookingStatus(rs.getString("booking_status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            booking.setCreatedAt(createdAt.toLocalDateTime());
        }
        return booking;
    }
}
