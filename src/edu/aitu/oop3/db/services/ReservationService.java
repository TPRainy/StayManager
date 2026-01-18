package edu.aitu.oop3.db.services;

import edu.aitu.oop3.db.exceptions.InvalidDateException;
import java.time.LocalDate;

public class ReservationService {

    public void createReservation(LocalDate checkIn, LocalDate checkOut) {
        if (checkOut.isBefore(checkIn)) {
            throw new InvalidDateException("Check-out date cannot be before check-in date!");
        }

        System.out.println("Reservation created from " + checkIn + " to " + checkOut);
    }
}