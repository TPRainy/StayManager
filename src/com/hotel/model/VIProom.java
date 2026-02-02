package com.hotel.model;

public class VIProom extends Room{
    public VIProom(int id,String roomNumber,double pricePerNight,boolean isAvailable){
        super(id,roomNumber,"VIP",pricePerNight,isAvailable);
    }
}
