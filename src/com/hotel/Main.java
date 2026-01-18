package com.hotel;

import com.hotel.util.DBConnector;
import com.hotel.util.IDB;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.services.ReservationService;
import java.time.LocalDate;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        System.out.println("--- Testing DB Connection ---");
        IDB db = new DBConnector();

        try (Connection conn = db.getConnection()) {
            if (conn != null) {
                System.out.println("SUCCESS: Connected to Supabase (AWS Tokyo)!");
            }
        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage());
        }

        System.out.println("\n--- Testing Reservation Service ---");
        ReservationService service = new ReservationService();

        Guest guest = new Guest(1, "Ali", "Batyr", "ali@mail.kz", "87011112233");
        Room room = new Room(101, "101", "Standard", 15000.0, true);

        try {
            service.createReservation(guest, room, LocalDate.now(), LocalDate.now().plusDays(2));

            System.out.println("Room 101 status (Available?): " + room.isAvailable());

        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}