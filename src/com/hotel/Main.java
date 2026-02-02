package com.hotel;

import com.hotel.repositories.*;
import com.hotel.services.*;
import com.hotel.util.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        System.out.println(">>> HOTEL SYSTEM ONLINE (Supabase) <<<");

        GuestRepository guestRepo=new PostgresGuestRepository();
        RoomRepository roomRepo=new PostgresRoomRepository();
        ReservationRepository resRepo=new PostgresReservationRepository();
        PaymentRepository payRepo=new PostgresPaymentRepository();

        PaymentService payService = new PaymentService(resRepo,payRepo);
        RoomAvailabilityService availabilityService = new RoomAvailabilityService(roomRepo);
        ReservationService resService = new ReservationService(guestRepo, roomRepo,resRepo,availabilityService);
        ConsoleUI ui=new ConsoleUI(resService, payService, availabilityService);

        ui.start();
    }
}