package com.hotel.ReservationComponent.Utils;

public class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("[EMAIL SENT]: " + message);
    }
}