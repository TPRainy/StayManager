package com.hotel.repositories;
import com.hotel.model.Reservation;

public interface ReservationRepository {
    void saveReservation(Reservation reservation);
    Reservation getReservationById(int id);
    void deleteReservation(int id);
    void updateReservation(Reservation reservation);
}