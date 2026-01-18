package com.hotel.model;

import java.time.LocalDateTime;

public class Payment {
    private int id;
    private int reservationId;
    private double amount;
    private LocalDateTime paymentDate;
    private String status;

    public Payment(int id,int reservationId,double amount,LocalDateTime paymentDate,String status){
        this.id=id;
        this.reservationId=reservationId;
        this.amount=amount;
        this.paymentDate=paymentDate;
        this.status=status;
    }

    public Payment(int reservationId,int amount,LocalDateTime paymentDate,String status){
        this.reservationId=reservationId;
        this.amount=amount;
        this.paymentDate=paymentDate;
        this.status=status;
    }

    public int getId() {return id;}
    public int getReservationId() {return reservationId;}
    public double getAmount() {return amount;}
    public LocalDateTime getPaymentDate() {return paymentDate;}
    public String getStatus(){return status;}
}
