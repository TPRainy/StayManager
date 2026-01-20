package com.hotel.model;
import java.time.LocalDate;

public class Reservation {
    private int id;
    private Guest guest;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double total;
    private boolean isPaid;


    public Reservation(int id, Guest guest, Room room, LocalDate checkIn, LocalDate checkOut, double total) {
        this.id = id;
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.total = total;
        this.isPaid = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public double getTotal() { return total; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
}