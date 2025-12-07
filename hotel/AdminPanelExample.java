package com.hotel;

import com.hotel.service.AdminService;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Example: How to use the Admin Panel in your Main.java
 * This demonstrates the password display feature
 */
public class AdminPanelExample {
    
    public static void main(String[] args) {
        AdminService adminService = new AdminService();
        
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   HOTEL ADMIN PANEL - EXAMPLE DEMO     ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
            System.out.println("\n┌─────────────────────────────────────┐");
            System.out.println("│         ADMIN MENU                  │");
            System.out.println("├─────────────────────────────────────┤");
            System.out.println("│ 1. View All Users (WITH passwords)  │");
            System.out.println("│ 2. View All Users (Safe - no pwd)   │");
            System.out.println("│ 3. View All Rooms                   │");
            System.out.println("│ 4. View All Bookings                │");
            System.out.println("│ 5. View All Payments                │");
            System.out.println("│ 6. Add Room                         │");
            System.out.println("│ 7. Delete Room                      │");
            System.out.println("│ 8. Remove Guest                     │");
            System.out.println("│ 0. Exit                             │");
            System.out.println("└─────────────────────────────────────┘");
            System.out.print("\nEnter choice: ");
            
            String choice = scanner.nextLine().trim();
            
            try {
                switch (choice) {
                    case "1":
                        // ⚠️ Shows passwords - Use with caution!
                        adminService.displayAllUsersWithPasswords();
                        break;
                        
                    case "2":
                        // Recommended: Doesn't show passwords
                        adminService.displayAllUsersSafe();
                        break;
                        
                    case "3":
                        adminService.displayAllRooms();
                        break;
                        
                    case "4":
                        adminService.displayAllBookings();
                        break;
                        
                    case "5":
                        adminService.displayAllPayments();
                        break;
                        
                    case "6":
                        System.out.print("Enter room type (Standard/Premium/Suite): ");
                        String type = scanner.nextLine();
                        System.out.print("Enter price: ");
                        double price = Double.parseDouble(scanner.nextLine());
                        adminService.addRoom(type, price);
                        break;
                        
                    case "7":
                        System.out.print("Enter room ID to delete: ");
                        int roomId = Integer.parseInt(scanner.nextLine());
                        adminService.deleteRoom(roomId);
                        break;
                        
                    case "8":
                        System.out.print("Enter guest ID to remove: ");
                        int guestId = Integer.parseInt(scanner.nextLine());
                        adminService.removeGuest(guestId);
                        break;
                        
                    case "0":
                        System.out.println("\nExiting Admin Panel. Goodbye!");
                        return;
                        
                    default:
                        System.out.println("❌ Invalid choice. Try again.");
                }
                } catch (SQLException e) {
                System.out.println("❌ Database error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
            }
        }
        } // End try-with-resources
    }
}
