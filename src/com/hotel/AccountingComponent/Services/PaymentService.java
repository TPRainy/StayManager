package com.hotel.AccountingComponent.Services;

import com.hotel.AccountingComponent.Models.Payment;
import com.hotel.ReservationComponent.Models.Reservation;
import com.hotel.AccountingComponent.Repositories.PaymentRepository;
import com.hotel.ReservationComponent.Repositories.ReservationRepository;

import java.time.LocalDateTime;

public class PaymentService {
    private final ReservationRepository reservationRepo;
    private final PaymentRepository paymentRepo;

    public PaymentService(ReservationRepository reservationRepo, PaymentRepository paymentRepo){
        this.reservationRepo=reservationRepo;
        this.paymentRepo = paymentRepo;
    }

    public boolean payReservation(int reservationId) {
        Reservation res = reservationRepo.getById(reservationId);
        if (res == null) throw new RuntimeException("Reservation not found");

        if (res.isPaid()) {
            return false;
        }
        res.setPaid(true);
        reservationRepo.update(res);

        Payment transaction=new Payment(res.getId(),res.getTotal(), LocalDateTime.now(),"Paid");
        paymentRepo.save(transaction);
        return true;
    }
}
