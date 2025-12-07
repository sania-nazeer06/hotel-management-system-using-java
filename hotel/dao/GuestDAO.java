package com.hotel.dao;

import com.hotel.model.Guest;
import java.sql.SQLException;
import java.util.List;

public interface GuestDAO {
    int addGuest(Guest guest) throws SQLException;
    Guest getGuestById(int guestId) throws SQLException;
    Guest getGuestByPhone(String phone) throws SQLException;
    List<Guest> getAllGuests() throws SQLException;
    void updateGuest(Guest guest) throws SQLException;
    void deleteGuest(int guestId) throws SQLException;
    boolean validateLogin(String phone, String password) throws SQLException;
}
