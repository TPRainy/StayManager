package com.hotel.services;

import com.hotel.model.Reservation;
import com.hotel.util.ReservationRepository;

public class PaymentService {
    private final ReservationRepository reservationRepo;

    public PaymentService(ReservationRepository reservationRepo){
        this.reservationRepo=reservationRepo;
    }

    public boolean payReservation(int reservationId) {
        Reservation res = reservationRepo.getReservationById(reservationId);
        if (res == null) throw new RuntimeException("Reservation not found");

        if (res.isPaid()) {
            return false;
        }
        res.setPaid(true);
        reservationRepo.updateReservation(res);
        return true;
    }
}
