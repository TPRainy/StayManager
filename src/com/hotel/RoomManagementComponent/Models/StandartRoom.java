package com.hotel.RoomManagementComponent.Models;

public class StandartRoom extends Room {
    public StandartRoom (int id, String roomNumber,double pricePerNight,boolean isAvailable){
        super(id,roomNumber,"Standart",pricePerNight,isAvailable);
    }
}
