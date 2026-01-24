package com.hotel;

import com.hotel.model.Reservation;
import com.hotel.services.ReservationService;
import com.hotel.util.PostgresRepository;
import java.time.LocalDate;
import com.hotel.services.PaymentService;
import com.hotel.services.RoomAvailabilityService;

public class Main {
    public static void main(String[] args) {
        System.out.println(">>> HOTEL SYSTEM ONLINE (Supabase) <<<");

        PostgresRepository repo = new PostgresRepository();
        PaymentService payService = new PaymentService(repo);
        RoomAvailabilityService availabilityService = new RoomAvailabilityService(repo);
        ReservationService resService = new ReservationService(repo,repo,repo,availabilityService);

        int testRoomId=101;
        int testGuestId=1;
        LocalDate checkIn=LocalDate.now();
        LocalDate checkOut=LocalDate.now().plusDays(3);

        try {
            System.out.println("\n[1] Checking availability");
            if (availabilityService.isRoomAvailable(testRoomId)){
                System.out.println("Room"+testRoomId+"is free. Booking now");
                //----------------------------------------------------
                int newBookingId= resService.createReservation(testGuestId,testRoomId,checkIn,checkOut);
                System.out.println("Success. Reservation created with ID"+newBookingId);

                Reservation res=repo.getReservationById(newBookingId);
                System.out.println("[SUCCESS] Ticket #"+res.getId()+"----");
                System.out.println("Guest: " + res.getGuest().getFirstName());
                System.out.println("Status: " + (res.isPaid() ? "PAID [V]" : "NOT PAID [X]"));
                System.out.println("Total: " + res.getTotal());
                //----------------------------------------------------
                System.out.println("\n--- 3. Processing Payment ---");
                boolean paymentSuccess=payService.payReservation(newBookingId);
                if (paymentSuccess){
                    System.out.println("Payment accepted");
                } else {
                    System.out.println("Already paid");
                }
            } else {
                System.out.println("Room "+testRoomId+" is occupied.");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}