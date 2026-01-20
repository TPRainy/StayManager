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

    public ReservationService(GuestRepository guestRepo, RoomRepository roomRepo, ReservationRepository reservationRepo) {
        this.guestRepo = guestRepo;
        this.roomRepo = roomRepo;
        this.reservationRepo = reservationRepo;
    }


    public boolean checkAvailability(int roomId) {
        Room room = roomRepo.getRoomById(roomId);
        if (room == null) throw new RuntimeException("Room not found");

        System.out.println("[INFO] Checking room " + roomId + "... Available: " + room.isAvailable());
        return room.isAvailable();
    }

    public int createReservation(int guestId, int roomId, LocalDate checkIn, LocalDate checkOut) {
        validateDates(checkIn, checkOut);

        if (!checkAvailability(roomId)) {
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

        System.out.println("[SUCCESS] Room booked for " + guest.getFirstName() + ". To pay: " + totalPrice);

        return reservation.getId();
    }

    public void payReservation(int reservationId) {
        Reservation res = reservationRepo.getReservationById(reservationId);
        if (res == null) throw new RuntimeException("Reservation not found");

        if (res.isPaid()) {
            System.out.println("[INFO] Already paid!");
            return;
        }


        res.setPaid(true);
        reservationRepo.updateReservation(res);

        System.out.println("[PAYMENT] Payment accepted ($" + res.getTotal() + ") for Reservation #" + reservationId);
    }


    public void cancelReservation(int reservationId) {
        Reservation res = reservationRepo.getReservationById(reservationId);
        if (res == null) throw new RuntimeException("Reservation not found");

        Room room = res.getRoom();
        room.setAvailable(true);
        roomRepo.updateRoom(room);

        reservationRepo.deleteReservation(reservationId);
        System.out.println("[INFO] Reservation #" + reservationId + " cancelled. Room " + room.getRoomNumber() + " is free.");
    }


    public void printReservationDetails(int reservationId) {
        Reservation res = reservationRepo.getReservationById(reservationId);
        if (res != null) {
            System.out.println("\n--- Ticket #" + reservationId + " ---");
            System.out.println("Guest: " + res.getGuest().getFirstName());
            System.out.println("Status: " + (res.isPaid() ? "PAID [V]" : "NOT PAID [X]"));
            System.out.println("Total: " + res.getTotal());
        }
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter(checkOut)) throw new InvalidDateException("Invalid dates!");
        if (checkIn.isBefore(LocalDate.now())) throw new InvalidDateException("Past date!");
    }
}