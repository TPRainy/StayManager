package com.hotel;

import com.hotel.services.ReservationService;
import com.hotel.util.PostgresRepository;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println(">>> HOTEL SYSTEM ONLINE (Supabase) <<<");

        PostgresRepository repo = new PostgresRepository();

        ReservationService service = new ReservationService(repo, repo, repo);

        try {
            System.out.println("Checking Room 101...");
            if (service.checkAvailability(101)) {
                System.out.println("Room is free! Booking now...");

                int bookingId = service.createReservation(1, 101, LocalDate.now(), LocalDate.now().plusDays(3));

                System.out.println("SUCCESS! Check your Supabase Table Editor.");
                System.out.println("Created Reservation ID: " + bookingId);

                System.out.println("\n--- 3. Processing Payment ---");
                service.payReservation(bookingId);

                service.printReservationDetails(bookingId);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}