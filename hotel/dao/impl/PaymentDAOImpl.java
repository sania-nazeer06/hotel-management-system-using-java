package com.hotel.dao.impl;

import com.hotel.dao.DBConnection;
import com.hotel.dao.PaymentDAO;
import com.hotel.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public int addPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (booking_id, amount_received, tip, status, payment_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, payment.getBookingId());
            pstmt.setDouble(2, payment.getAmountReceived());
            pstmt.setDouble(3, payment.getTip());
            pstmt.setString(4, payment.getStatus());
            pstmt.setDate(5, Date.valueOf(payment.getPaymentDate()));
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public Payment getPaymentById(int paymentId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, paymentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractPaymentFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public Payment getPaymentByBooking(int bookingId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractPaymentFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public List<Payment> getAllPayments() throws SQLException {
        String sql = "SELECT * FROM payments ORDER BY payment_id DESC";
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        }
        return payments;
    }

    @Override
    public void updatePayment(Payment payment) throws SQLException {
        String sql = "UPDATE payments SET booking_id = ?, amount_received = ?, tip = ?, status = ?, payment_date = ? WHERE payment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, payment.getBookingId());
            pstmt.setDouble(2, payment.getAmountReceived());
            pstmt.setDouble(3, payment.getTip());
            pstmt.setString(4, payment.getStatus());
            pstmt.setDate(5, Date.valueOf(payment.getPaymentDate()));
            pstmt.setInt(6, payment.getPaymentId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, paymentId);
            pstmt.executeUpdate();
        }
    }

    private Payment extractPaymentFromResultSet(ResultSet rs) throws SQLException {
        Payment payment = new Payment(
            rs.getInt("booking_id"),
            rs.getDouble("amount_received"),
            rs.getDouble("tip"),
            rs.getString("status"),
            rs.getDate("payment_date").toLocalDate()
        );
        payment.setPaymentId(rs.getInt("payment_id"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            payment.setCreatedAt(createdAt.toLocalDateTime());
        }
        return payment;
    }
}
