package com.hotel.exceptions;
//оплата под вопросом
public class PaymentDeclinedException extends RuntimeException {
    public PaymentDeclinedException(String message) {
        super(message);
    }
}