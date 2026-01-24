package com.hotel.services;

import com.hotel.model.*;
import com.hotel.util.*;
import com.hotel.exceptions.*;
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

    public int createReservation(int guestId, int roomId, LocalDate checkIn, LocalDate checkOut) {
        validateDates(checkIn, checkOut);

        if (!availabilityService.isRoomAvailable(roomId)) {
            throw new RoomNotAvailableException("Room " + roomId + " is occupied!");
        }

        Guest guest = guestRepo.getGuestById(guestId);
        Room room = roomRepo.getRoomById(roomId);

        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (days <= 0) days = 1;
        double totalPrice = days * room.getPricePerNight();

        room.setAvailable(false);
        roomRepo.updateRoom(room);

        Reservation reservation = new Reservation(0, guest, room, checkIn, checkOut, totalPrice);
        reservationRepo.saveReservation(reservation);

        return reservation.getId();
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter(checkOut)) throw new InvalidDateException("Invalid dates!");
        if (checkIn.isBefore(LocalDate.now())) throw new InvalidDateException("Past date!");
    }
}