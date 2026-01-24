package com.hotel;

import com.hotel.services.*;
import com.hotel.repositories.PostgresRepository;
import com.hotel.util.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        System.out.println(">>> HOTEL SYSTEM ONLINE (Supabase) <<<");

        PostgresRepository repo = new PostgresRepository();
        PaymentService payService = new PaymentService(repo);
        RoomAvailabilityService availabilityService = new RoomAvailabilityService(repo);
        ReservationService resService = new ReservationService(repo,repo,repo,availabilityService);
        ConsoleUI ui=new ConsoleUI(resService, payService, availabilityService, repo);

        ui.start();
    }
}