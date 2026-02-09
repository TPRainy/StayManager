package com.hotel.ReservationComponent.Exceptions;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException(String message) {
        super(message);}
}