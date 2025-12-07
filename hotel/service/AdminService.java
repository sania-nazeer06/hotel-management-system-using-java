package com.hotel.service;

import com.hotel.dao.GuestDAO;
import com.hotel.dao.RoomDAO;
import com.hotel.dao.BookingDAO;
import com.hotel.dao.PaymentDAO;
import com.hotel.dao.impl.GuestDAOImpl;
import com.hotel.dao.impl.RoomDAOImpl;
import com.hotel.dao.impl.BookingDAOImpl;
import com.hotel.dao.impl.PaymentDAOImpl;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.model.Booking;
import com.hotel.model.Payment;

import java.sql.SQLException;
import java.util.List;

/**
 * Admin Panel Service - Provides administrative functions
 * Use this in your console UI to display admin views
 */
public class AdminService {
    
    private final GuestDAO guestDAO;
    private final RoomDAO roomDAO;
    private final BookingDAO bookingDAO;
    private final PaymentDAO paymentDAO;

    public AdminService() {
        this.guestDAO = new GuestDAOImpl();
        this.roomDAO = new RoomDAOImpl();
        this.bookingDAO = new BookingDAOImpl();
        this.paymentDAO = new PaymentDAOImpl();
    }

    /**
     * Display all users with their passwords (ADMIN ONLY)
     * ⚠️ WARNING: Displaying passwords is a SECURITY RISK!
     * This should ONLY be used in development/testing.
     * In production, NEVER show passwords - use password reset instead.
     */
    public void displayAllUsersWithPasswords() throws SQLException {
        List<Guest> guests = guestDAO.getAllGuests();
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          ALL USERS - ADMIN PANEL                               ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ ⚠️  WARNING: Passwords are visible - Admin access only!                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Table header
        System.out.println("┌──────┬─────────────────────┬───────────────┬──────────────────────┬──────────────────────┐");
        System.out.printf("│ %-4s │ %-19s │ %-13s │ %-20s │ %-20s │%n", "ID", "Name", "Phone", "Email/Address", "Password");
        System.out.println("├──────┼─────────────────────┼───────────────┼──────────────────────┼──────────────────────┤");
        
        if (guests.isEmpty()) {
            System.out.println("│                                    No users found                                          │");
        } else {
            for (Guest guest : guests) {
                System.out.printf("│ %-4d │ %-19s │ %-13s │ %-20s │ %-20s │%n",
                    guest.getGuestId(),
                    truncate(guest.getName(), 19),
                    truncate(guest.getPhone(), 13),
                    truncate(guest.getAddress(), 20),
                    truncate(guest.getPassword(), 20)  // ⚠️ PASSWORD VISIBLE
                );
            }
        }
        
        System.out.println("└──────┴─────────────────────┴───────────────┴──────────────────────┴──────────────────────┘");
        System.out.println("Total Users: " + guests.size());
        System.out.println();
    }

    /**
     * Display all users WITHOUT passwords (RECOMMENDED for security)
     */
    public void displayAllUsersSafe() throws SQLException {
        List<Guest> guests = guestDAO.getAllGuests();
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ALL USERS - ADMIN PANEL                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Table header
        System.out.println("┌──────┬─────────────────────┬───────────────┬────────────────────────────┐");
        System.out.printf("│ %-4s │ %-19s │ %-13s │ %-26s │%n", "ID", "Name", "Phone", "Address");
        System.out.println("├──────┼─────────────────────┼───────────────┼────────────────────────────┤");
        
        if (guests.isEmpty()) {
            System.out.println("│                         No users found                                 │");
        } else {
            for (Guest guest : guests) {
                System.out.printf("│ %-4d │ %-19s │ %-13s │ %-26s │%n",
                    guest.getGuestId(),
                    truncate(guest.getName(), 19),
                    truncate(guest.getPhone(), 13),
                    truncate(guest.getAddress(), 26)
                );
            }
        }
        
        System.out.println("└──────┴─────────────────────┴───────────────┴────────────────────────────┘");
        System.out.println("Total Users: " + guests.size());
        System.out.println();
    }

    /**
     * Display all rooms
     */
    public void displayAllRooms() throws SQLException {
        List<Room> rooms = roomDAO.getAllRooms();
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                  ALL ROOMS - ADMIN PANEL                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        System.out.println("┌──────┬─────────────┬──────────────┬─────────────────┐");
        System.out.printf("│ %-4s │ %-11s │ %-12s │ %-15s │%n", "ID", "Type", "Price", "Status");
        System.out.println("├──────┼─────────────┼──────────────┼─────────────────┤");
        
        for (Room room : rooms) {
            System.out.printf("│ %-4d │ %-11s │ ₹%-11.2f │ %-15s │%n",
                room.getRoomId(),
                room.getRoomType(),
                room.getPrice(),
                room.getStatus()
            );
        }
        
        System.out.println("└──────┴─────────────┴──────────────┴─────────────────┘");
        System.out.println("Total Rooms: " + rooms.size());
        System.out.println();
    }

    /**
     * Display all bookings
     */
    public void displayAllBookings() throws SQLException {
        List<Booking> bookings = bookingDAO.getAllBookings();
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                   ALL BOOKINGS - ADMIN PANEL                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        System.out.println("┌──────┬─────────┬────────┬────────────┬────────────┬──────────────┬──────────────┐");
        System.out.printf("│ %-4s │ %-7s │ %-6s │ %-10s │ %-10s │ %-12s │ %-12s │%n", 
            "ID", "Guest", "Room", "Check-In", "Check-Out", "Total", "Status");
        System.out.println("├──────┼─────────┼────────┼────────────┼────────────┼──────────────┼──────────────┤");
        
        for (Booking booking : bookings) {
            System.out.printf("│ %-4d │ %-7d │ %-6d │ %-10s │ %-10s │ ₹%-11.2f │ %-12s │%n",
                booking.getBookingId(),
                booking.getGuestId(),
                booking.getRoomId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getTotalAmount(),
                booking.getBookingStatus()
            );
        }
        
        System.out.println("└──────┴─────────┴────────┴────────────┴────────────┴──────────────┴──────────────┘");
        System.out.println("Total Bookings: " + bookings.size());
        System.out.println();
    }

    /**
     * Display all payments
     */
    public void displayAllPayments() throws SQLException {
        List<Payment> payments = paymentDAO.getAllPayments();
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                   ALL PAYMENTS - ADMIN PANEL                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        System.out.println("┌──────┬──────────┬──────────────┬────────────┬──────────┬────────────┐");
        System.out.printf("│ %-4s │ %-8s │ %-12s │ %-10s │ %-8s │ %-10s │%n", 
            "ID", "Booking", "Amount", "Tip", "Status", "Date");
        System.out.println("├──────┼──────────┼──────────────┼────────────┼──────────┼────────────┤");
        
        for (Payment payment : payments) {
            System.out.printf("│ %-4d │ %-8d │ ₹%-11.2f │ ₹%-9.2f │ %-8s │ %-10s │%n",
                payment.getPaymentId(),
                payment.getBookingId(),
                payment.getAmountReceived(),
                payment.getTip(),
                payment.getStatus(),
                payment.getPaymentDate()
            );
        }
        
        System.out.println("└──────┴──────────┴──────────────┴────────────┴──────────┴────────────┘");
        System.out.println("Total Payments: " + payments.size());
        System.out.println();
    }

    /**
     * Add a new room (Admin function)
     */
    public void addRoom(String roomType, double price) throws SQLException {
        Room room = new Room(roomType, price, "Available");
        roomDAO.addRoom(room);
        System.out.println("✓ Room added successfully!");
    }

    /**
     * Delete a room (Admin function)
     */
    public void deleteRoom(int roomId) throws SQLException {
        roomDAO.deleteRoom(roomId);
        System.out.println("✓ Room deleted successfully!");
    }

    /**
     * Remove/Delete a guest (Admin function)
     */
    public void removeGuest(int guestId) throws SQLException {
        guestDAO.deleteGuest(guestId);
        System.out.println("✓ Guest removed successfully!");
    }

    // Utility method to truncate long strings for table display
    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}
