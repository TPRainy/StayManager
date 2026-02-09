package com.hotel.ReservationComponent.Utils;

public class NotificationFactory {
    public static Notification createNotification(String type) {
        if (type == null) return null;
        if (type.equalsIgnoreCase("EMAIL")) {
            return new EmailNotification();
        } else if (type.equalsIgnoreCase("SMS")) {
            return new SmsNotification();
        }
        return new EmailNotification();
    }
}