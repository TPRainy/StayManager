package com.hotel.util;
import com.hotel.model.Payment;

public interface PaymentRepository {
    void savePayment(Payment payment);
}