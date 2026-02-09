package com.hotel.ReservationComponent.Utils;

public class SmsNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("[SMS SENT]: " + message);
    }
}