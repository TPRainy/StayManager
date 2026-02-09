package com.hotel.UI;

import com.hotel.AccountingComponent.Repositories.PaymentRepository;
import com.hotel.AccountingComponent.Repositories.PostgresPaymentRepository;
import com.hotel.AccountingComponent.Services.PaymentService;
import com.hotel.ReservationComponent.Repositories.GuestRepository;
import com.hotel.ReservationComponent.Repositories.PostgresGuestRepository;
import com.hotel.ReservationComponent.Repositories.PostgresReservationRepository;
import com.hotel.ReservationComponent.Repositories.ReservationRepository;
import com.hotel.ReservationComponent.Services.ReservationService;
import com.hotel.ReservationComponent.Services.RoomAvailabilityService;
import com.hotel.RoomManagementComponent.Repositories.PostgresRoomRepository;
import com.hotel.RoomManagementComponent.Repositories.RoomRepository;

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