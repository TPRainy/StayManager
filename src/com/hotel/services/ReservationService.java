package com.hotel.services;

import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.model.Reservation;
import com.hotel.exceptions.InvalidDateException;
import com.hotel.exceptions.RoomNotAvailableException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationService {

    public Reservation createReservation(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {

        validateDates(checkInDate, checkOutDate);

        if (!room.isAvailable()) {
            throw new RoomNotAvailableException("Room " + room.getRoomNumber() + " is already occupied!");
        }

        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (days == 0) days = 1;

        double totalPrice = days * room.getPricePerNight();

        room.setAvailable(false);

        Reservation reservation = new Reservation(guest, room, checkInDate, checkOutDate, totalPrice);

        System.out.println("Reservation created successfully for " + guest.getFirstName());
        System.out.println("Total Price: " + totalPrice);

        return reservation;
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter(checkOut)) {
            throw new InvalidDateException("Check-in date cannot be after check-out date!");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Cannot book dates in the past!");
        }
    }
}