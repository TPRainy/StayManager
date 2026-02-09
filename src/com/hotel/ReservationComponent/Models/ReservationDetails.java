package com.hotel.ReservationComponent.Models;

import com.hotel.RoomManagementComponent.Models.Room;

import java.util.ArrayList;
import java.util.List;

public class ReservationDetails {
    private final Room room;
    private final List<String> options;
    private final String paymentStatus;
    private final double finalPrice;

    private ReservationDetails(Builder builder){
        this.room=builder.room;
        this.options=builder.options;
        this.paymentStatus=builder.paymentStatus;
        this.finalPrice=builder.finalPrice;
    }

    public static class Builder{
        private Room room;
        private List<String> options=new ArrayList<>();
        private String paymentStatus="Pending";
        private double finalPrice;

        public Builder setRoom(Room room){
            this.room=room;
            return this;
        }

        public Builder addOption(String option){
            if (option != null && !option.equals("None")) {
                this.options.add(option);
            }
            return this;
        }

        public Builder setPaymentinfo(String status, double price){
            this.paymentStatus=status;
            this.finalPrice=price;
            return this;
        }

        public ReservationDetails build(){
            if (room==null) throw new IllegalArgumentException("Set a room!");
            return new ReservationDetails(this);
        }

    }

    public Room getRoom(){return room;}
    public List<String> getOptions(){return options;}
    public String getPaymentStatus(){return paymentStatus;}
    public double getFinalPrice(){return finalPrice;}
}
