package com.hotel.services;

import com.hotel.model.Room;
import com.hotel.util.RoomRepository;

public class RoomAvailabilityService {

    private final RoomRepository roomRepo;

    public RoomAvailabilityService(RoomRepository roomRepo){
        this.roomRepo=roomRepo;
    }

    public boolean isRoomAvailable(int roomId) {
        Room room = roomRepo.getRoomById(roomId);
        if (room == null) throw new RuntimeException("Room not found");
        return room.isAvailable();
    }
}
