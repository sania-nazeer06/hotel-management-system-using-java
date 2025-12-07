package com.hotel.dao;

import com.hotel.model.Booking;
import java.sql.SQLException;
import java.util.List;

public interface BookingDAO {
    int addBooking(Booking booking) throws SQLException;
    Booking getBookingById(int bookingId) throws SQLException;
    List<Booking> getAllBookings() throws SQLException;
    List<Booking> getBookingsByGuest(int guestId) throws SQLException;
    List<Booking> getActiveBookings() throws SQLException;
    void updateBooking(Booking booking) throws SQLException;
    void updateBookingStatus(int bookingId, String status) throws SQLException;
    void deleteBooking(int bookingId) throws SQLException;
}
