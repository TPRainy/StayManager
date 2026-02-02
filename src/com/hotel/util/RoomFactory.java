package com.hotel.util;
import com.hotel.model.*;
public class RoomFactory {
    public static Room createRoom(int id,String roomNumber,String type,double pricePerNight,boolean isAvailable){
        if (type==null) return new StandartRoom(id,roomNumber,pricePerNight,isAvailable);
        return switch (type.trim().toUpperCase()){
            case "VIP" -> new VIProom(id,roomNumber,pricePerNight,isAvailable);
            default -> new StandartRoom(id,roomNumber,pricePerNight,isAvailable);
        };
    }
}
