package com.hotel.repositories;
import com.hotel.model.Payment;

public interface PaymentRepository {
    void savePayment(Payment payment);
}