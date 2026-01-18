package com.hotel.model;

import java.time.LocalDate;

public class Reservation {
    private int id;
    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double total;

    public Reservation(int id,Guest guest,Room room,LocalDate checkInDate,LocalDate checkOutDate,double total){
        this.id=id;
        this.guest=guest;
        this.room=room;
        this.checkInDate=checkInDate;
        this.checkOutDate=checkOutDate;
        this.total=total;
    }
    public Reservation(Guest guest,Room room,LocalDate checkInDate,LocalDate checkOutDate,double total){
        this.guest=guest;
        this.room=room;
        this.checkInDate=checkInDate;
        this.checkOutDate=checkOutDate;
        this.total=total;
    }

    public int getId(){return id;}
    public Guest getGuest(){return guest;}
    public Room getRoom(){return room;}
    public LocalDate getCheckInDate(){return checkInDate;}
    public LocalDate getCheckOutDate(){return checkOutDate;}
    public double getTotal(){return total;}
}
