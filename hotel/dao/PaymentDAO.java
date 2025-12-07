package com.hotel.dao;

import com.hotel.model.Payment;
import java.sql.SQLException;
import java.util.List;

public interface PaymentDAO {
    int addPayment(Payment payment) throws SQLException;
    Payment getPaymentById(int paymentId) throws SQLException;
    Payment getPaymentByBooking(int bookingId) throws SQLException;
    List<Payment> getAllPayments() throws SQLException;
    void updatePayment(Payment payment) throws SQLException;
    void deletePayment(int paymentId) throws SQLException;
}
