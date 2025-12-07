package com.hotel;

import com.hotel.dao.impl.BookingDAOImpl;
import com.hotel.dao.DBConnection;
import com.hotel.dao.RoomDAO;
import com.hotel.dao.GuestDAO;
import com.hotel.dao.impl.RoomDAOImpl;
import com.hotel.dao.impl.GuestDAOImpl;
import com.hotel.model.Room;
import com.hotel.model.Guest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String ADMIN_PASSWORD = "blahblah123";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\t===========================================");
            System.out.println("\t   Hotel Management System - Console");
            System.out.println("\t===========================================");
            System.out.println("\t1. Admin Login");
            System.out.println("\t2. User Login/Register");
            System.out.println("\t3. Exit");
            System.out.print("\nSelect option: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    adminLogin(sc);
                    break;
                case "2":
                    userLoginOrRegister(sc);
                    break;
                case "3":
                    System.out.println("\n\tThank you for using the Hotel Management System.");
                    System.out.println("\t\tExiting. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void adminLogin(Scanner sc) {
        System.out.print("Enter admin password: ");
        String pass = sc.nextLine();
        if (ADMIN_PASSWORD.equals(pass)) {
            System.out.println("Admin login successful.");
            adminPanel(sc);
        } else {
            System.out.println("Incorrect password. Returning to main menu.");
        }
    }

    private static void userLoginOrRegister(Scanner sc) {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Select option: ");
        String choice = sc.nextLine().trim();
        switch (choice) {
            case "1":
                userRegister(sc);
                break;
            case "2":
                userLogin(sc);
                break;
            default:
                System.out.println("Invalid option. Returning to main menu.");
        }
    }

    private static void userRegister(Scanner sc) {
        try {
            GuestDAOImpl guestDAO = new GuestDAOImpl();
            System.out.print("Enter your name: ");
            String name = sc.nextLine().trim();
            System.out.print("Enter your phone number: ");
            String phone = sc.nextLine().trim();
            if (guestDAO.getGuestByPhone(phone) != null) {
                System.out.println("A user with this phone already exists. Please login.");
                return;
            }
            System.out.print("Enter your address: ");
            String address = sc.nextLine().trim();
            System.out.print("Create a password: ");
            String password = sc.nextLine();
            Guest guest = new Guest(name, phone, address, password);
            int guestId = guestDAO.addGuest(guest);
            if (guestId > 0) {
                System.out.println("Registration successful! Please login.");
            } else {
                System.out.println("Registration failed. Try again.");
            }
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    private static void userLogin(Scanner sc) {
        try {
            GuestDAOImpl guestDAO = new GuestDAOImpl();
            System.out.print("Enter your phone number: ");
            String phone = sc.nextLine().trim();
            System.out.print("Enter your password: ");
            String password = sc.nextLine();
            Guest guest = guestDAO.getGuestByPhone(phone);
            if (guest != null && guest.getPassword().equals(password)) {
                releaseExpiredRooms();
                System.out.println("Login successful. Welcome, " + guest.getName() + "!");
                userPanel(sc, guest);
            } else {
                System.out.println("Invalid credentials. Returning to main menu.");
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }

    private static void userPanel(Scanner sc, Guest guest) {
        while (true) {
            System.out.println("\n--- User Panel (Guest) ---");
            System.out.println("Welcome, " + guest.getName() + "!");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Logout");
            System.out.print("Select option: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    do {
                        viewAvailableRooms();
                    } while (!askGoBack(sc));
                    break;
                case "2":
                    do {
                        bookRoom(sc, guest);
                    } while (!askGoBack(sc));
                    break;
                case "3":
                    System.out.println("Logging out. Returning to main menu.");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Prompt user to go back to panel
    private static boolean askGoBack(Scanner sc) {
        while (true) {
            System.out.print("Would you like to go back? (y/n): ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("y")) return true;
            if (input.equalsIgnoreCase("n")) return false;
            System.out.println("Please choose the correct option (y/n).\n");
        }
    }

    // Confirm before proceeding with admin action
    private static boolean confirmAction(Scanner sc, String message) {
        System.out.print(message + " (y/n): ");
        String input = sc.nextLine().trim();
        return input.equalsIgnoreCase("y");
    }

    private static void viewAvailableRooms() {
        try {
            RoomDAOImpl roomDAO = new RoomDAOImpl();
            List<Room> rooms = roomDAO.getAvailableRooms();
            System.out.println("\nAvailable Rooms:");
            System.out.println("ID | Type      | Price   | Status");
            for (Room room : rooms) {
                System.out.printf("%-3d| %-9s| %-8.2f| %-10s\n", room.getRoomId(), room.getRoomType(), room.getPrice(), room.getStatus());
            }
            if (rooms.isEmpty()) {
                System.out.println("No rooms available.");
            }
        } catch (Exception e) {
            System.out.println("Error fetching rooms: " + e.getMessage());
        }
    }

    private static void bookRoom(Scanner sc, Guest guest) {
        try {
            releaseExpiredRooms();
            RoomDAOImpl roomDAO = new RoomDAOImpl();
            List<Room> availableRooms = roomDAO.getAvailableRooms();
            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for booking.");
                return;
            }
            System.out.println("\nAvailable Rooms:");
            System.out.println("ID | Type      | Price   | Status");
            for (Room room : availableRooms) {
                System.out.printf("%-3d| %-9s| %-8.2f| %-10s\n", room.getRoomId(), room.getRoomType(), room.getPrice(), room.getStatus());
            }
            System.out.print("Enter Room ID to book: ");
            int roomId = Integer.parseInt(sc.nextLine().trim());
            Room selectedRoom = null;
            for (Room r : availableRooms) {
                if (r.getRoomId() == roomId) {
                    selectedRoom = r;
                    break;
                }
            }
            if (selectedRoom == null) {
                System.out.println("Invalid Room ID. Returning to user panel.");
                return;
            }
            System.out.print("Enter duration of stay (in days, e.g., 1.5): ");
            double duration = Double.parseDouble(sc.nextLine().trim());
            int days = (int)Math.ceil(duration); // store as int for DB

            // Ask for room service
            boolean roomService = false;
            while (true) {
                System.out.print("Add Room Service? (y/n): ");
                String roomServiceInput = sc.nextLine().trim();
                if (roomServiceInput.equalsIgnoreCase("y")) {
                    roomService = true;
                    break;
                } else if (roomServiceInput.equalsIgnoreCase("n")) {
                    roomService = false;
                    break;
                } else {
                    System.out.println("Please enter 'y' or 'n'.");
                }
            }
            double roomServiceCost = roomService ? 500.0 : 0.0;

            // Ask for special service
            boolean specialService = false;
            while (true) {
                System.out.print("Add Special Service? (y/n): ");
                String specialServiceInput = sc.nextLine().trim();
                if (specialServiceInput.equalsIgnoreCase("y")) {
                    specialService = true;
                    break;
                } else if (specialServiceInput.equalsIgnoreCase("n")) {
                    specialService = false;
                    break;
                } else {
                    System.out.println("Please enter 'y' or 'n'.");
                }
            }
            double specialServiceCost = specialService ? 1000.0 : 0.0;

            double baseRoomTotal = selectedRoom.getPrice() * duration;
            double totalAmount = baseRoomTotal + roomServiceCost + specialServiceCost;
            System.out.println("\n--- Bill Summary ---");
            System.out.printf("Room Price per Day: %.2f\n", selectedRoom.getPrice());
            System.out.printf("Duration: %.2f days\n", duration);
            System.out.printf("Base Room Total: %.2f\n", baseRoomTotal);
            System.out.printf("Room Service: %.2f\n", roomServiceCost);
            System.out.printf("Special Service: %.2f\n", specialServiceCost);
            System.out.printf("Total Amount: %.2f\n", totalAmount);

            // Payment
            double paid = 0.0;
            double tip = 0.0;
            while (paid < totalAmount) {
                System.out.printf("Enter payment amount (remaining: %.2f): ", totalAmount - paid);
                double pay = Double.parseDouble(sc.nextLine().trim());
                paid += pay;
                if (paid > totalAmount) {
                    double excess = paid - totalAmount;
                    System.out.printf("Overpaid by %.2f. Take as tip? (y/n): ", excess);
                    if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                        tip = excess;
                        paid = totalAmount;
                    } else {
                        paid = totalAmount;
                        System.out.printf("Returning %.2f as change.\n", excess);
                    }
                } else if (paid < totalAmount) {
                    System.out.printf("Pending amount: %.2f\n", totalAmount - paid);
                }
            }

            // Store booking and payment
            com.hotel.dao.impl.BookingDAOImpl bookingDAO = new com.hotel.dao.impl.BookingDAOImpl();
            com.hotel.model.Booking booking = new com.hotel.model.Booking(
                guest.getGuestId(),
                selectedRoom.getRoomId(),
                java.time.LocalDate.now(),
                java.time.LocalDate.now().plusDays(days),
                days,
                selectedRoom.getPrice(),
                baseRoomTotal,
                roomServiceCost,
                specialServiceCost,
                totalAmount
            );
            int bookingId = bookingDAO.addBooking(booking);
            if (bookingId > 0) {
                com.hotel.dao.impl.PaymentDAOImpl paymentDAO = new com.hotel.dao.impl.PaymentDAOImpl();
                com.hotel.model.Payment payment = new com.hotel.model.Payment(
                    bookingId,
                    paid,
                    tip,
                    "Paid",
                    java.time.LocalDate.now()
                );
                paymentDAO.addPayment(payment);
                // Mark room as booked
                selectedRoom.setStatus("Booked");
                roomDAO.updateRoom(selectedRoom);
                System.out.println("Booking and payment successful! Enjoy your stay.");
            } else {
                System.out.println("Booking failed. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error during booking: " + e.getMessage());
        }
    }

    // Release rooms whose bookings have ended
    private static void releaseExpiredRooms() {
        try {
            BookingDAOImpl bookingDAO = new BookingDAOImpl();
            RoomDAOImpl roomDAO = new RoomDAOImpl();
            java.time.LocalDate today = java.time.LocalDate.now();
            List<com.hotel.model.Booking> allBookings = bookingDAO.getAllBookings();
            
            // First, release rooms with expired bookings
            for (com.hotel.model.Booking booking : allBookings) {
                // Only release rooms if checkout date is BEFORE today (not including today)
                if (booking.getCheckOut().isBefore(today)) {
                    // Mark booking as Completed if it's still Active
                    if ("Active".equals(booking.getBookingStatus())) {
                        bookingDAO.updateBookingStatus(booking.getBookingId(), "Completed");
                    }
                    
                    // Release room if it's still marked as Booked
                    Room room = roomDAO.getRoomById(booking.getRoomId());
                    if (room != null && "Booked".equals(room.getStatus())) {
                        room.setStatus("Available");
                        roomDAO.updateRoom(room);
                    }
                }
            }
            
            // Then, sync room statuses with active bookings
            // Get all active bookings (check-in <= today <= check-out)
            List<Integer> activeRoomIds = new java.util.ArrayList<>();
            for (com.hotel.model.Booking booking : allBookings) {
                if ("Active".equals(booking.getBookingStatus()) && 
                    !booking.getCheckIn().isAfter(today) && 
                    !booking.getCheckOut().isBefore(today)) {
                    activeRoomIds.add(booking.getRoomId());
                }
            }
            
            // Update all rooms to ensure correct status
            List<Room> allRooms = roomDAO.getAllRooms();
            for (Room room : allRooms) {
                if (activeRoomIds.contains(room.getRoomId())) {
                    // Room should be Booked
                    if (!"Booked".equals(room.getStatus()) && !"Maintenance".equals(room.getStatus())) {
                        room.setStatus("Booked");
                        roomDAO.updateRoom(room);
                    }
                } else {
                    // Room should be Available (unless in Maintenance)
                    if ("Booked".equals(room.getStatus())) {
                        room.setStatus("Available");
                        roomDAO.updateRoom(room);
                    }
                }
            }
        } catch (Exception e) {
            // Silent fail, not critical
        }
    }

    // Admin function: Insert Room
    private static void insertRoom(Scanner sc) {
        try {
            RoomDAOImpl roomDAO = new RoomDAOImpl();
            System.out.println("\n--- Insert New Room ---");
            System.out.print("Enter room type (Standard/Premium/Suite): ");
            String roomType = sc.nextLine().trim();
            System.out.print("Enter price per day: ");
            double price = Double.parseDouble(sc.nextLine().trim());
            
            // New rooms are always available initially
            Room newRoom = new Room(roomType, price, "Available");
            int roomId = roomDAO.addRoom(newRoom);
            
            if (roomId > 0) {
                System.out.println("Room added successfully! Room ID: " + roomId);
            } else {
                System.out.println("Failed to add room. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error inserting room: " + e.getMessage());
        }
    }

    // Admin function: Delete Room
    private static void deleteRoom(Scanner sc) {
        try {
            RoomDAOImpl roomDAO = new RoomDAOImpl();
            List<Room> allRooms = roomDAO.getAllRooms();
            
            if (allRooms.isEmpty()) {
                System.out.println("No rooms available to delete.");
                return;
            }
            
            System.out.println("\n--- Delete Room ---");
            System.out.println("ID | Type      | Price   | Status");
            for (Room room : allRooms) {
                System.out.printf("%-3d| %-9s| %-8.2f| %-10s\n", room.getRoomId(), room.getRoomType(), room.getPrice(), room.getStatus());
            }
            
            System.out.print("Enter Room ID to delete: ");
            int roomId = Integer.parseInt(sc.nextLine().trim());
            
            Room roomToDelete = roomDAO.getRoomById(roomId);
            if (roomToDelete == null) {
                System.out.println("Room not found.");
                return;
            }
            
            System.out.print("Are you sure you want to delete Room ID " + roomId + "? (y/n): ");
            String confirm = sc.nextLine().trim();
            if (confirm.equalsIgnoreCase("y")) {
                roomDAO.deleteRoom(roomId);
                System.out.println("Room deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting room: " + e.getMessage());
        }
    }

    // Admin function: Modify Room Status
    private static void modifyRoomStatus(Scanner sc) {
        try {
            RoomDAOImpl roomDAO = new RoomDAOImpl();
            List<Room> allRooms = roomDAO.getAllRooms();
            
            if (allRooms.isEmpty()) {
                System.out.println("No rooms available to modify.");
                return;
            }
            
            System.out.println("\n--- Modify Room Status ---");
            System.out.println("ID | Type      | Price   | Status");
            for (Room room : allRooms) {
                System.out.printf("%-3d| %-9s| %-8.2f| %-10s\n", room.getRoomId(), room.getRoomType(), room.getPrice(), room.getStatus());
            }
            
            System.out.print("Enter Room ID to modify: ");
            int roomId = Integer.parseInt(sc.nextLine().trim());
            
            Room roomToModify = roomDAO.getRoomById(roomId);
            if (roomToModify == null) {
                System.out.println("Room not found.");
                return;
            }
            
            System.out.println("Current status: " + roomToModify.getStatus());
            System.out.println("Available status options:");
            System.out.println("  1. Available");
            System.out.println("  2. Booked");
            System.out.println("  3. Maintenance");
            System.out.print("Enter new status (Available/Booked/Maintenance): ");
            String newStatus = sc.nextLine().trim();
            
            if (newStatus.isEmpty()) {
                System.out.println("Status cannot be empty.");
                return;
            }
            
            // Validate status
            if (!newStatus.equalsIgnoreCase("Available") && 
                !newStatus.equalsIgnoreCase("Booked") && 
                !newStatus.equalsIgnoreCase("Maintenance")) {
                System.out.println("Invalid status. Please enter: Available, Booked, or Maintenance");
                return;
            }
            
            roomDAO.updateRoomStatus(roomId, newStatus);
            System.out.println("Room status updated successfully!");
        } catch (Exception e) {
            System.out.println("Error modifying room status: " + e.getMessage());
        }
    }

    // Admin function: View All Rooms
    private static void viewAllRooms() {
        try {
            RoomDAOImpl roomDAO = new RoomDAOImpl();
            List<Room> allRooms = roomDAO.getAllRooms();
            
            if (allRooms.isEmpty()) {
                System.out.println("No rooms in the system.");
                return;
            }
            
            System.out.println("\n--- All Rooms ---");
            System.out.println("ID | Type      | Price   | Status");
            System.out.println("------------------------------------------");
            for (Room room : allRooms) {
                System.out.printf("%-3d| %-9s| %-8.2f| %-10s\n", room.getRoomId(), room.getRoomType(), room.getPrice(), room.getStatus());
            }
            System.out.println("\nTotal rooms: " + allRooms.size());
        } catch (Exception e) {
            System.out.println("Error viewing rooms: " + e.getMessage());
        }
    }

    // Admin function: View All Guests & Bookings
    private static void viewGuestsAndBookings() {
        try {
            GuestDAOImpl guestDAO = new GuestDAOImpl();
            BookingDAOImpl bookingDAO = new BookingDAOImpl();
            
            List<Guest> allGuests = guestDAO.getAllGuests();
            
            if (allGuests.isEmpty()) {
                System.out.println("No guests in the system.");
                return;
            }
            
            System.out.println("\n--- All Guests & Bookings ---");
            
            for (Guest guest : allGuests) {
                System.out.println("\n========================================");
                System.out.println("Guest ID: " + guest.getGuestId());
                System.out.println("Name: " + guest.getName());
                System.out.println("Phone: " + guest.getPhone());
                System.out.println("Address: " + guest.getAddress());
                
                // Get bookings for this guest
                List<com.hotel.model.Booking> guestBookings = bookingDAO.getBookingsByGuest(guest.getGuestId());
                
                if (guestBookings.isEmpty()) {
                    System.out.println("Bookings: None");
                } else {
                    System.out.println("\nBookings:");
                    System.out.println("  BookingID | RoomID | Check-In   | Check-Out  | Days | Status    | Total");
                    System.out.println("  ----------------------------------------------------------------------------");
                    for (com.hotel.model.Booking booking : guestBookings) {
                        System.out.printf("  %-9d | %-6d | %-10s | %-10s | %-4d | %-9s | %.2f\n",
                            booking.getBookingId(),
                            booking.getRoomId(),
                            booking.getCheckIn(),
                            booking.getCheckOut(),
                            booking.getDays(),
                            booking.getBookingStatus(),
                            booking.getTotalAmount());
                    }
                }
            }
            
            System.out.println("\n========================================");
            System.out.println("Total guests: " + allGuests.size());
        } catch (Exception e) {
            System.out.println("Error viewing guests and bookings: " + e.getMessage());
        }
    }

    // Admin function: Delete User Account
    private static void deleteUserAccount(Scanner sc) {
        try {
            GuestDAOImpl guestDAO = new GuestDAOImpl();
            List<Guest> allGuests = guestDAO.getAllGuests();
            
            if (allGuests.isEmpty()) {
                System.out.println("No guest accounts in the system.");
                return;
            }
            
            System.out.println("\n--- Delete User Account ---");
            System.out.println("GuestID | Name           | Phone       | Address");
            System.out.println("--------------------------------------------------------");
            for (Guest guest : allGuests) {
                System.out.printf("%-7d | %-14s | %-11s | %s\n",
                    guest.getGuestId(),
                    guest.getName(),
                    guest.getPhone(),
                    guest.getAddress());
            }
            
            System.out.print("\nEnter Guest ID to delete: ");
            int guestId = Integer.parseInt(sc.nextLine().trim());
            
            Guest guestToDelete = guestDAO.getGuestById(guestId);
            if (guestToDelete == null) {
                System.out.println("Guest not found.");
                return;
            }
            
            System.out.println("\nGuest Details:");
            System.out.println("Name: " + guestToDelete.getName());
            System.out.println("Phone: " + guestToDelete.getPhone());
            System.out.println("Address: " + guestToDelete.getAddress());
            
            System.out.print("\nAre you sure you want to delete this guest account? (y/n): ");
            String confirm = sc.nextLine().trim();
            if (confirm.equalsIgnoreCase("y")) {
                guestDAO.deleteGuest(guestId);
                System.out.println("Guest account deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting user account: " + e.getMessage());
        }
    }

    // Admin function: View Payment Records
    private static void viewPaymentRecords() {
        try {
            com.hotel.dao.impl.PaymentDAOImpl paymentDAO = new com.hotel.dao.impl.PaymentDAOImpl();
            GuestDAOImpl guestDAO = new GuestDAOImpl();
            BookingDAOImpl bookingDAO = new BookingDAOImpl();
            
            List<com.hotel.model.Payment> allPayments = paymentDAO.getAllPayments();
            
            if (allPayments.isEmpty()) {
                System.out.println("No payment records in the system.");
                return;
            }
            
            System.out.println("\n--- Payment Records ---");
            System.out.println("PaymentID | BookingID | Guest Name     | Amount    | Tip    | Status  | Date");
            System.out.println("------------------------------------------------------------------------------------");
            
            double totalAmount = 0.0;
            double totalTips = 0.0;
            
            for (com.hotel.model.Payment payment : allPayments) {
                // Get booking details
                com.hotel.model.Booking booking = bookingDAO.getBookingById(payment.getBookingId());
                String guestName = "Unknown";
                if (booking != null) {
                    Guest guest = guestDAO.getGuestById(booking.getGuestId());
                    if (guest != null) {
                        guestName = guest.getName();
                    }
                }
                
                System.out.printf("%-9d | %-9d | %-14s | %-9.2f | %-6.2f | %-7s | %s\n",
                    payment.getPaymentId(),
                    payment.getBookingId(),
                    guestName,
                    payment.getAmountReceived(),
                    payment.getTip(),
                    payment.getStatus(),
                    payment.getPaymentDate());
                
                totalAmount += payment.getAmountReceived();
                totalTips += payment.getTip();
            }
            
            System.out.println("------------------------------------------------------------------------------------");
            System.out.printf("Total Payments: %d | Total Amount: %.2f | Total Tips: %.2f\n",
                allPayments.size(), totalAmount, totalTips);
        } catch (Exception e) {
            System.out.println("Error viewing payment records: " + e.getMessage());
        }
    }

    private static void adminPanel(Scanner sc) {
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. Insert Room");
            System.out.println("2. Delete Room");
            System.out.println("3. Modify Room Status");
            System.out.println("4. View All Rooms");
            System.out.println("5. View All Guests & Bookings");
            System.out.println("6. Delete User Account");
            System.out.println("7. View Payment Records");
            System.out.println("8. Logout");
            System.out.print("Select option: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    if (confirmAction(sc, "Do you want to add rooms?")) {
                        do {
                            insertRoom(sc);
                        } while (!askGoBack(sc));
                    }
                    break;
                case "2":
                    if (confirmAction(sc, "Do you want to delete rooms?")) {
                        do {
                            deleteRoom(sc);
                        } while (!askGoBack(sc));
                    }
                    break;
                case "3":
                    if (confirmAction(sc, "Do you want to modify room status?")) {
                        do {
                            modifyRoomStatus(sc);
                        } while (!askGoBack(sc));
                    }
                    break;
                case "4":
                    if (confirmAction(sc, "Do you want to view all rooms?")) {
                        do {
                            viewAllRooms();
                        } while (!askGoBack(sc));
                    }
                    break;
                case "5":
                    if (confirmAction(sc, "Do you want to view all guests and bookings?")) {
                        do {
                            viewGuestsAndBookings();
                        } while (!askGoBack(sc));
                    }
                    break;
                case "6":
                    if (confirmAction(sc, "Do you want to delete user accounts?")) {
                        do {
                            deleteUserAccount(sc);
                        } while (!askGoBack(sc));
                    }
                    break;
                case "7":
                    if (confirmAction(sc, "Do you want to view payment records?")) {
                        do {
                            viewPaymentRecords();
                        } while (!askGoBack(sc));
                    }
                    break;
                case "8":
                    System.out.println("Logging out. Returning to main menu.");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
