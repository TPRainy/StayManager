package com.hotel.RoomManagementComponent.Utils;
import com.hotel.RoomManagementComponent.Models.Room;
import com.hotel.RoomManagementComponent.Models.StandartRoom;
import com.hotel.RoomManagementComponent.Models.VIProom;

public class RoomFactory {
    public static Room createRoom(int id, String roomNumber, String type, double pricePerNight, boolean isAvailable){
        if (type==null) return new StandartRoom(id,roomNumber,pricePerNight,isAvailable);
        return switch (type.trim().toUpperCase()){
            case "VIP" -> new VIProom(id,roomNumber,pricePerNight,isAvailable);
            default -> new StandartRoom(id,roomNumber,pricePerNight,isAvailable);
        };
    }
}
