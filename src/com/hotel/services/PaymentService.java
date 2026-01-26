package com.hotel.services;

import com.hotel.model.Payment;
import com.hotel.model.Reservation;
import com.hotel.repositories.PaymentRepository;
import com.hotel.repositories.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaymentService {
    private final ReservationRepository reservationRepo;
    private final PaymentRepository paymentRepo;

    public PaymentService(ReservationRepository reservationRepo, PaymentRepository paymentRepo){
        this.reservationRepo=reservationRepo;
        this.paymentRepo = paymentRepo;
    }

    public boolean payReservation(int reservationId) {
        Reservation res = reservationRepo.getReservationById(reservationId);
        if (res == null) throw new RuntimeException("Reservation not found");

        if (res.isPaid()) {
            return false;
        }
        res.setPaid(true);
        reservationRepo.updateReservation(res);

        Payment transaction=new Payment(res.getId(),res.getTotal(), LocalDateTime.now(),"Paid");
        paymentRepo.savePayment(transaction);
        return true;
    }
}
