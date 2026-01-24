package com.hotel.services;

import com.hotel.model.Room;
import com.hotel.repositories.RoomRepository;
import java.time.LocalDate;
import java.util.List;

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
    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut){
        if (checkIn.isBefore(LocalDate.now())){
            throw new RuntimeException("Choose proper date(not past)");
        }
        if (checkOut.isBefore(checkIn)){
            throw new RuntimeException("Date of check out can't be before check in");
        }
        return roomRepo.findAvailableByDates(checkIn,checkOut);
    }
}
