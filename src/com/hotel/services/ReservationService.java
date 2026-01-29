package com.hotel.services;

import com.hotel.model.*;
import com.hotel.repositories.GuestRepository;
import com.hotel.repositories.ReservationRepository;
import com.hotel.repositories.RoomRepository;
import com.hotel.exceptions.*;
import com.hotel.util.SeasonCalendar;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationService {

    private final GuestRepository guestRepo;
    private final RoomRepository roomRepo;
    private final ReservationRepository reservationRepo;
    private final RoomAvailabilityService availabilityService;

    public ReservationService(GuestRepository guestRepo, RoomRepository roomRepo, ReservationRepository reservationRepo, RoomAvailabilityService availabilityService) {
        this.guestRepo = guestRepo;
        this.roomRepo = roomRepo;
        this.reservationRepo = reservationRepo;
        this.availabilityService=availabilityService;
    }

    public void cancelReservation(int reservationId) {
        Reservation res = reservationRepo.getReservationById(reservationId);
        if (res == null) throw new RuntimeException("Reservation not found");

        Room room = res.getRoom();
        room.setAvailable(true);
        roomRepo.updateRoom(room);

        reservationRepo.deleteReservation(reservationId);
    }

    public int createReservation(int guestId, int roomId, LocalDate checkIn, LocalDate checkOut,String option) {
        validateDates(checkIn, checkOut);
        Guest guest = guestRepo.getGuestById(guestId);
        Room room = roomRepo.getRoomById(roomId);
        SeasonCalendar calendar=SeasonCalendar.getInstance();

        if (!availabilityService.isRoomAvailable(roomId)) {
            throw new RoomNotAvailableException("Room " + roomId + " is occupied!");
        }

        double basePrice=room.getPricePerNight();
        if (calendar.isHighSeason(checkIn)){
            basePrice*=1.5;
        }
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (days <= 0) days = 1;

        double optionCharge=0;

        if ("All inclusive".equals(option)){
            optionCharge=4000;
        } else if ("Breakfast in room".equals(option)){
            optionCharge=3000;
        } else if ("WiFi connection".equals(option)){
            optionCharge=1000;
        }
        double pricePerNightWithOption=basePrice+optionCharge;
        double totalPrice=days*pricePerNightWithOption;
        room.setAvailable(false);
        roomRepo.updateRoom(room);

        Reservation reservation = new Reservation(0, guest, room, checkIn, checkOut, totalPrice,option);
        reservationRepo.saveReservation(reservation);

        return reservation.getId();
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter(checkOut)) throw new InvalidDateException("Invalid dates!");
        if (checkIn.isBefore(LocalDate.now())) throw new InvalidDateException("Past date!");
    }

    public ReservationDetails getFullReservationDetails(int reservationId){
        Reservation res=reservationRepo.getReservationById(reservationId);
        return new ReservationDetails.Builder()
                .setRoom(res.getRoom())
                .setPaymentinfo(res.isPaid()? "Paid":"Pending",res.getTotal())
                .addOption(res.getOptions())
                .build();
    }
}